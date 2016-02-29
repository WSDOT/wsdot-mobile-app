/*
 * Copyright (c) 2015 Washington State Department of Transportation
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 */

package gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch;

import gov.wa.wsdot.mobile.client.css.AppBundle;
import gov.wa.wsdot.mobile.client.util.ParserUtils;
import gov.wa.wsdot.mobile.client.widget.button.image.BackImageButton;
import gov.wa.wsdot.mobile.client.widget.button.image.CameraImageButton;
import gov.wa.wsdot.mobile.client.widget.button.image.LocationImageButton;
import gov.wa.wsdot.mobile.client.widget.button.image.NavigationImageButton;
import gov.wa.wsdot.mobile.shared.CameraItem;
import gov.wa.wsdot.mobile.shared.VesselWatchItem;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.aria.client.Roles;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.base.LatLngBounds;
import com.google.gwt.maps.client.controls.MapTypeStyle;
import com.google.gwt.maps.client.events.MapEventType;
import com.google.gwt.maps.client.events.MapHandlerRegistration;
import com.google.gwt.maps.client.events.click.ClickMapEvent;
import com.google.gwt.maps.client.events.click.ClickMapHandler;
import com.google.gwt.maps.client.events.idle.IdleMapEvent;
import com.google.gwt.maps.client.events.idle.IdleMapHandler;
import com.google.gwt.maps.client.events.resize.ResizeMapEvent;
import com.google.gwt.maps.client.events.resize.ResizeMapHandler;
import com.google.gwt.maps.client.layers.TrafficLayer;
import com.google.gwt.maps.client.maptypes.MapTypeStyleElementType;
import com.google.gwt.maps.client.maptypes.MapTypeStyleFeatureType;
import com.google.gwt.maps.client.maptypes.MapTypeStyler;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.overlays.MarkerImage;
import com.google.gwt.maps.client.overlays.MarkerOptions;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageMap;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.buttonbar.ButtonBar;
import com.googlecode.mgwt.ui.client.widget.header.HeaderTitle;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexSpacer;
import com.googlecode.mgwt.ui.client.widget.progress.ProgressIndicator;

public class VesselWatchMapViewGwtImpl extends Composite implements
		VesselWatchMapView {

	/**
	 * The UiBinder interface.
	 */	
	interface TrafficMapViewGwtImplUiBinder extends
			UiBinder<Widget, VesselWatchMapViewGwtImpl> {
	}
	
	/**
	 * The UiBinder used to generate the view.
	 */
	private static TrafficMapViewGwtImplUiBinder uiBinder = GWT
			.create(TrafficMapViewGwtImplUiBinder.class);

	@UiField
	HeaderTitle heading;
	
	@UiField
	BackImageButton backButton;
	
	@UiField
	FlexSpacer leftFlexSpacer;
	
	@UiField
	FlowPanel flowPanel;
	
	@UiField
	ProgressIndicator progressIndicator;
	
	@UiField
	ButtonBar buttonBar;
	
	@UiField
	CameraImageButton cameraButton;
	
	@UiField
	LocationImageButton locationButton;

	@UiField
	NavigationImageButton navigationButton;
	
	private Presenter presenter;
	private MyMapWidget mapWidget;
	private Marker cameraMarker;
	private Marker vesselMarker;
	private static ArrayList<Marker> cameraMarkers = new ArrayList<Marker>();
	private static ArrayList<Marker> vesselMarkers = new ArrayList<Marker>();
	
	private static Storage localStorage = Storage.getLocalStorageIfSupported();
	private static StorageMap storageMap;
	
	public VesselWatchMapViewGwtImpl() {

		initWidget(uiBinder.createAndBindUi(this));
        
		accessibilityPrepare();
		
        if (MGWT.getOsDetection().isAndroid()) {
            leftFlexSpacer.setVisible(false);
        }
		
		if (localStorage != null) {
			storageMap = new StorageMap(localStorage);
			if (!storageMap.containsKey("KEY_SHOW_CAMERAS")) {
				localStorage.setItem("KEY_SHOW_CAMERAS", "true"); // Set initial default value
			}
			
			// Set initial default location and zoom to Seattle area.
			localStorage.setItem("KEY_MAP_LAT", "47.565125");
			localStorage.setItem("KEY_MAP_LON", "-122.480508");
			localStorage.setItem("KEY_MAP_ZOOM", "11");
		}
		
		final TrafficLayer trafficLayer = TrafficLayer.newInstance();
		
		LatLng center = LatLng.newInstance(
				Double.valueOf(localStorage.getItem("KEY_MAP_LAT")),
				Double.valueOf(localStorage.getItem("KEY_MAP_LON")));
		
	    MapOptions opts = MapOptions.newInstance();
	    opts.setMapTypeId(MapTypeId.ROADMAP);
	    opts.setCenter(center);
	    opts.setZoom(Integer.valueOf(localStorage.getItem("KEY_MAP_ZOOM"), 10));
	    opts.setPanControl(false);
	    opts.setZoomControl(false);
	    opts.setMapTypeControl(false);
	    opts.setScaleControl(false);
	    opts.setStreetViewControl(false);
	    opts.setOverviewMapControl(false);
	    
	    // Custom map style to remove all "Points of Interest" labels from map
	    MapTypeStyle style1 = MapTypeStyle.newInstance();
	    style1.setFeatureType(MapTypeStyleFeatureType.POI);
	    style1.setElementType(MapTypeStyleElementType.LABELS);
	    style1.setStylers(new MapTypeStyler[] {MapTypeStyler.newVisibilityStyler("off")});
	    MapTypeStyle[] styles = { style1 };
	    
	    opts.setMapTypeStyles(styles);
	    
	    mapWidget = new MyMapWidget(opts);
	    trafficLayer.setMap(mapWidget);
	    flowPanel.add(mapWidget);

        mapWidget.setSize(Window.getClientWidth() + "px",
                (Window.getClientHeight() - ParserUtils.windowUI()) + "px");

	    Window.addResizeHandler(new ResizeHandler() {
	        @Override
	        public void onResize(ResizeEvent event) {
	            MapHandlerRegistration.trigger(mapWidget, MapEventType.RESIZE);
           
	        }
	    });

	    mapWidget.addResizeHandler(new ResizeMapHandler() {
	        @Override
	        public void onEvent(ResizeMapEvent event) {
                mapWidget.setSize(Window.getClientWidth() + "px",
                        (Window.getClientHeight() - ParserUtils.windowUI()) + "px");
	        }
	    });
	    
	    mapWidget.addIdleHandler(new IdleMapHandler() {

			@Override
			public void onEvent(IdleMapEvent event) {			
				localStorage.setItem("KEY_MAP_LAT",
						String.valueOf(mapWidget.getCenter().getLatitude()));
				localStorage.setItem("KEY_MAP_LON",
						String.valueOf(mapWidget.getCenter().getLongitude()));
				localStorage.setItem("KEY_MAP_ZOOM",
						String.valueOf(mapWidget.getZoom()));
				
				if (presenter != null) {
					presenter.onMapIsIdle();
				}
			}
		});
	    
	}

	@UiHandler("backButton")
	protected void onBackButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onBackButtonPressed();
		}
	}

	@UiHandler("cameraButton")
	protected void onCameraButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onCameraButtonPressed(Boolean.valueOf(localStorage
					.getItem("KEY_SHOW_CAMERAS")));
		}
	}
	
	@UiHandler("locationButton")
	protected void onGoToLocationButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onGoToLocationButtonPressed();
		}
	}

	@UiHandler("navigationButton")
	protected void onLocateButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onLocateButtonPressed();
		}
	}
	
	@Override
	public void showProgressIndicator() {
		progressIndicator.setVisible(true);
	}

	@Override
	public void hideProgressIndicator() {
		progressIndicator.setVisible(false);
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public LatLngBounds getViewportBounds() {
		return mapWidget.getBounds();
	}

	@Override
	public void drawCameras(final List<CameraItem> cameras) {
		
		deleteCameras();
		
		for (final CameraItem camera : cameras) {
			final LatLng loc = LatLng.newInstance(camera.getLatitude(), camera.getLongitude());
		    MarkerOptions options = MarkerOptions.newInstance();
		    options.setPosition(loc);
		    
		    MarkerImage icon;
		    
		    if (camera.getHasVideo() == 1) {
				icon = MarkerImage.newInstance(AppBundle.INSTANCE
						.cameraVideoPNG().getSafeUri().asString());		    	
		    } else {
				icon = MarkerImage.newInstance(AppBundle.INSTANCE
						.cameraPNG().getSafeUri().asString());
		    }

		    options.setIcon(icon);
		    
		    cameraMarker = Marker.newInstance(options);    
		    cameraMarker.addClickHandler(new ClickMapHandler() {

				@Override
				public void onEvent(ClickMapEvent event) {
					presenter.onCameraSelected(camera.getCameraId());
				}
			});
		    
		    cameraMarkers.add(cameraMarker);
	    }
		
		if (Boolean.valueOf(localStorage.getItem("KEY_SHOW_CAMERAS"))) {
			showCameras();
		}
		
	}

	@Override
	public void drawFerries(List<VesselWatchItem> vessels) {

		deleteFerries();
		
		for (final VesselWatchItem vessel : vessels) {
			final LatLng loc = LatLng.newInstance(vessel.getLat(), vessel.getLon());
		    MarkerOptions options = MarkerOptions.newInstance();
		    options.setPosition(loc);
		    
			MarkerImage icon = MarkerImage.newInstance(vessel.getIcon());		    	
		    options.setIcon(icon);
		    
			MarkerImage shadow = MarkerImage.newInstance(vessel.getIconShadow());
		    options.setShadow(shadow);
		    
		    vesselMarker = Marker.newInstance(options);    
		    vesselMarker.addClickHandler(new ClickMapHandler() {

				@Override
				public void onEvent(ClickMapEvent event) {
					presenter.onFerrySelected(vessel);
				}
			});
		    
		    vesselMarkers.add(vesselMarker);
	    }
		
		showFerries();
		
	}
	
	@Override
	public void hideCameras() {
		for (Marker marker: cameraMarkers) {
			marker.setMap((MapWidget)null);
		}
		
		localStorage.setItem("KEY_SHOW_CAMERAS", "false");
	}

	@Override
	public void showCameras() {
		for (Marker marker: cameraMarkers) {
			marker.setMap(mapWidget);
		}

		localStorage.setItem("KEY_SHOW_CAMERAS", "true");
	}

	@Override
	public MapWidget getMapWidget() {
		return mapWidget;
	}

	@Override
	public void deleteCameras() {
		for (Marker marker: cameraMarkers) {
			marker.setMap((MapWidget)null);
		}
		
		cameraMarkers.clear();
	}

	@Override
	public void setMapLocation(double latitude, double longitude, int zoom) {
		LatLng latLng = LatLng.newInstance(latitude, longitude);
		
		mapWidget.panTo(latLng);
		mapWidget.setZoom(zoom);
		
	}

	@Override
	public void setMapLocation() {
		LatLng center = LatLng.newInstance(
				Double.valueOf(localStorage.getItem("KEY_MAP_LAT")),
				Double.valueOf(localStorage.getItem("KEY_MAP_LON")));
		
		int zoom = Integer.valueOf(localStorage.getItem("KEY_MAP_ZOOM"), 10);
		
		mapWidget.panTo(center);
		mapWidget.setZoom(zoom);
	}
	
	@Override
	public void hideFerries() {
		for (Marker marker: vesselMarkers) {
			marker.setMap((MapWidget)null);
		}
	}

	@Override
	public void showFerries() {
		for (Marker marker: vesselMarkers) {
			marker.setMap(mapWidget);
		}
	}

	@Override
	public void deleteFerries() {
		for (Marker marker: vesselMarkers) {
			marker.setMap((MapWidget)null);
		}
		
		vesselMarkers.clear();
	}
	
	/**
	 * Custom MapWidget for map not resizing issue.
	 * 
	 * @author Kostas Patakas https://plus.google.com/112436396950239118116
	 *
	 * When the onAttach occurs and animation exists meaning that the div started
	 * smaller and increases in size, but the map has already attached. At the end
	 * of the animation, the map doesn't auto resize.
	 *
	 */
	private class MyMapWidget extends MapWidget {

		public MyMapWidget(MapOptions options) {
			super(options);
		}

		@Override
		protected void onAttach() {
			super.onAttach();
			Timer timer = new Timer() {

				@Override
				public void run() {
					resize();
				}
			};
			timer.schedule(5);
		}

		/**
		 * This method is called to fix the Map loading issue when opening
		 * multiple instances of maps in different tabs
		 * Triggers a resize event to be consumed by google api in order to resize view
		 * after attach.
		 *
		 */
		public void resize() {
			LatLng center = this.getCenter();
			MapHandlerRegistration.trigger(this, MapEventType.RESIZE);        
			this.setCenter(center);
		}

	}
	private void accessibilityPrepare(){
		
		// Add ARIA roles for accessibility
		Roles.getButtonRole().set(backButton.getElement());
		Roles.getButtonRole().setAriaLabelProperty(backButton.getElement(), "navigate back");
		
		Roles.getHeadingRole().set(heading.getElement());
	}
}