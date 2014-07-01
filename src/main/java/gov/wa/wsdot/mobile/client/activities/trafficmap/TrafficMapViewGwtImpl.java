/*
 * Copyright (c) 2014 Washington State Department of Transportation
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

package gov.wa.wsdot.mobile.client.activities.trafficmap;

import gov.wa.wsdot.mobile.client.css.AppBundle;
import gov.wa.wsdot.mobile.client.util.ParserUtils;
import gov.wa.wsdot.mobile.client.widget.buttonbar.ClockButton;
import gov.wa.wsdot.mobile.client.widget.buttonbar.FlashButton;
import gov.wa.wsdot.mobile.client.widget.buttonbar.LocationButton;
import gov.wa.wsdot.mobile.client.widget.buttonbar.NavigationButton;
import gov.wa.wsdot.mobile.client.widget.buttonbar.PhotoButton;
import gov.wa.wsdot.mobile.client.widget.buttonbar.WarningButton;
import gov.wa.wsdot.mobile.shared.CameraItem;
import gov.wa.wsdot.mobile.shared.HighwayAlertItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

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
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.resources.client.ImageResource;
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
import com.googlecode.mgwt.ui.client.widget.HeaderButton;
import com.googlecode.mgwt.ui.client.widget.ProgressBar;
import com.googlecode.mgwt.ui.client.widget.buttonbar.ButtonBar;

public class TrafficMapViewGwtImpl extends Composite implements TrafficMapView {

	/**
	 * The UiBinder interface.
	 */	
	interface TrafficMapViewGwtImplUiBinder extends
			UiBinder<Widget, TrafficMapViewGwtImpl> {
	}
	
	/**
	 * The UiBinder used to generate the view.
	 */
	private static TrafficMapViewGwtImplUiBinder uiBinder = GWT
			.create(TrafficMapViewGwtImplUiBinder.class);

	@UiField
	HeaderButton backButton;
	
	@UiField
	FlowPanel flowPanel;
	
	@UiField
	ProgressBar progressBar;
	
	@UiField
	ButtonBar buttonBar;
	
	@UiField
	PhotoButton cameraButton;
	
	@UiField
	ClockButton travelTimesButton;
	
	@UiField
	LocationButton locationButton;

	@UiField
	WarningButton seattleAlertsButton;
	
	@UiField
	FlashButton expressLanesButton;
	
	@UiField
	NavigationButton navigationButton;
	
	@UiField
	HeaderButton refreshButton;
	
	private Presenter presenter;
	private MyMapWidget mapWidget;
	private Marker cameraMarker;
	private Marker alertMarker;
	private static ArrayList<Marker> cameraMarkers = new ArrayList<Marker>();
	private static ArrayList<Marker> alertMarkers = new ArrayList<Marker>();
	
	private static Storage localStorage = Storage.getLocalStorageIfSupported();
	private static StorageMap storageMap;
	
	public TrafficMapViewGwtImpl() {

		initWidget(uiBinder.createAndBindUi(this));
		
		if (localStorage != null) {
			storageMap = new StorageMap(localStorage);
			if (!storageMap.containsKey("KEY_SHOW_CAMERAS")) {
				localStorage.setItem("KEY_SHOW_CAMERAS", "true"); // Set initial default value
			}
			
			// Set initial default location and zoom to Seattle area.
			if (!storageMap.containsKey("KEY_MAP_LAT")) {
				localStorage.setItem("KEY_MAP_LAT", "47.5990");
			}
			if (!storageMap.containsKey("KEY_MAP_LON")) {
				localStorage.setItem("KEY_MAP_LON", "-122.3350");
			}
			if (!storageMap.containsKey("KEY_MAP_ZOOM")) {
				localStorage.setItem("KEY_MAP_ZOOM", "12");
			}
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
	
	@UiHandler("travelTimesButton")
	protected void onTravelTimesButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onTravelTimesButtonPressed();
		}
	}
	
	@UiHandler("locationButton")
	protected void onGoToLocationButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onGoToLocationButtonPressed();
		}
	}

	@UiHandler("seattleAlertsButton")
	protected void onSeattleAlertsButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onSeattleTrafficAlertsButtonPressed();
		}
	}
	
	@UiHandler("expressLanesButton")
	protected void onExpressLanesButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onSeattleExpressLanesButtonPressed();
		}
	}
	
	@UiHandler("navigationButton")
	protected void onLocateButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onLocateButtonPressed();
		}
	}
	
	@UiHandler("refreshButton")
	protected void onRefreshMapButtonPressed(TapEvent event) {
	    if (presenter != null) {
	        presenter.onRefreshMapButtonPressed();
	    }
	}
	
	@Override
	public void showProgressBar() {
		progressBar.setVisible(true);
	}

	@Override
	public void hideProgressBar() {
		progressBar.setVisible(false);
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
		    
			MarkerImage shadow = MarkerImage.newInstance(AppBundle.INSTANCE
					.cameraShadowPNG().getSafeUri().asString());
		    options.setIcon(icon);
		    options.setShadow(shadow);
		    
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
	public void drawAlerts(List<HighwayAlertItem> alerts) {
		
        // Types of categories which result in one icon or another being displayed. 
        String[] eventClosure = {"closed", "closure"};
        String[] eventConstruction = {"construction", "maintenance", "lane closure"};

        HashMap<String, String[]> eventCategories = new HashMap<String, String[]>();
        eventCategories.put("closure", eventClosure);
        eventCategories.put("construction", eventConstruction);
	    
		deleteAlerts();
		
		for (final HighwayAlertItem alert: alerts) {
			LatLng loc = LatLng.newInstance(alert.getStartLatitude(), alert.getStartLongitude());
		    MarkerOptions options = MarkerOptions.newInstance();
		    options.setPosition(loc);
		    
		    // Determine correct icon for alert type
            ImageResource imageResource = getCategoryIcon(eventCategories,
                    alert.getEventCategory(), alert.getPriority());
		    
		    MarkerImage icon = MarkerImage.newInstance(imageResource.getSafeUri().asString());
		    options.setIcon(icon);
		    
		    alertMarker = Marker.newInstance(options);    
		    alertMarker.addClickHandler(new ClickMapHandler() {

				@Override
				public void onEvent(ClickMapEvent event) {
					presenter.onAlertSelected(alert.getAlertId());
				}
			});
		    
		    alertMarkers.add(alertMarker);
		    
		}
		
	    showAlerts();
	    
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
	public void hideAlerts() {
		for (Marker marker: alertMarkers) {
			marker.setMap((MapWidget)null);
		}
	}

	@Override
	public void showAlerts() {
		for (Marker marker: alertMarkers) {
			marker.setMap(mapWidget);
		}
	}

	@Override
	public void deleteAlerts() {
		for (Marker marker: alertMarkers) {
			marker.setMap((MapWidget)null);
		}
		
		alertMarkers.clear();
	}

	/**
	 * Refresh the map to update the traffic layer.
	 * 
	 * Force the traffic layer to update by zooming in and then out
	 * of the map.
	 */
    @Override
    public void refreshMap() {
        mapWidget.setZoom(mapWidget.getZoom() + 1);
        new Timer() {

            @Override
            public void run() {
                mapWidget.setZoom(mapWidget.getZoom() - 1);                
            }
         }.schedule(1);

    }
	
    /**
     * Get the correct icon given the priority and category of alert.
     * 
     * @param eventCategories
     * @param category
     * @param priority
     * @return
     */
	private ImageResource getCategoryIcon(
			HashMap<String, String[]> eventCategories, String category, String priority) {
		
	    ImageResource alertClosed = AppBundle.INSTANCE.closedPNG();
	    ImageResource alertHighest = AppBundle.INSTANCE.alertHighestPNG();
	    ImageResource alertHigh = AppBundle.INSTANCE.alertHighPNG();
	    ImageResource alertMedium = AppBundle.INSTANCE.alertModeratePNG();
	    ImageResource alertLow = AppBundle.INSTANCE.alertLowPNG();
	    ImageResource constructionHighest = AppBundle.INSTANCE.constructionHighestPNG();
	    ImageResource constructionHigh = AppBundle.INSTANCE.constructionHighPNG();
	    ImageResource constructionMedium = AppBundle.INSTANCE.constructionModeratePNG();
	    ImageResource constructionLow = AppBundle.INSTANCE.constructionLowPNG();
	    ImageResource defaultAlertImage = alertHighest;
	    
		Set<Entry<String, String[]>> set = eventCategories.entrySet();
		Iterator<Entry<String, String[]>> i = set.iterator();

		if (category.equals("")) return defaultAlertImage;

		while(i.hasNext()) {
			Entry<String, String[]> me = i.next();
			for (String phrase: (String[])me.getValue()) {
				String patternStr = phrase;
				RegExp pattern = RegExp.compile(patternStr, "i");
				MatchResult matcher = pattern.exec(category);
				boolean matchFound = matcher != null;
                
				if (matchFound) {
                    String keyWord = me.getKey();
                    
                    if (keyWord.equalsIgnoreCase("closure")) {
                        return alertClosed;
                    } else if (keyWord.equalsIgnoreCase("construction")) {
                        if (priority.equalsIgnoreCase("highest")) {
                            return constructionHighest;
                        } else if (priority.equalsIgnoreCase("high")) {
                            return constructionHigh;
                        } else if (priority.equalsIgnoreCase("medium")) {
                            return constructionMedium;
                       } else if (priority.equalsIgnoreCase("low")
                               || priority.equalsIgnoreCase("lowest")) {
                            return constructionLow;
                        }
                    }
                }
			}
		}

		// If we arrive here, it must be an accident or alert item.
        if (priority.equalsIgnoreCase("highest")) {
            return alertHighest;
        } else if (priority.equalsIgnoreCase("high")) {
            return alertHigh;
        } else if (priority.equalsIgnoreCase("medium")) {
            return alertMedium;
        } else if (priority.equalsIgnoreCase("low")
                || priority.equalsIgnoreCase("lowest")) {
            return alertLow;
        }

		return defaultAlertImage;
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
	
}
