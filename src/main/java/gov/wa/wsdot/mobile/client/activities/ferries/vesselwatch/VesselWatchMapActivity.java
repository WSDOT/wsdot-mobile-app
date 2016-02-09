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

package gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.activities.camera.CameraPlace;
import gov.wa.wsdot.mobile.client.activities.ferries.FerriesPlace;
import gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.location.GoToFerriesLocationPlace;
import gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.vesseldetails.VesselDetailsPlace;
import gov.wa.wsdot.mobile.client.css.AppBundle;
import gov.wa.wsdot.mobile.client.plugins.analytics.Analytics;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.CachesColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.CamerasColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService.Tables;
import gov.wa.wsdot.mobile.client.util.Consts;
import gov.wa.wsdot.mobile.shared.CacheItem;
import gov.wa.wsdot.mobile.shared.CameraItem;
import gov.wa.wsdot.mobile.shared.CamerasFeed;
import gov.wa.wsdot.mobile.shared.LatLonItem;
import gov.wa.wsdot.mobile.shared.VesselWatchFeed;
import gov.wa.wsdot.mobile.shared.VesselWatchItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.code.gwt.database.client.GenericRow;
import com.google.code.gwt.database.client.service.DataServiceException;
import com.google.code.gwt.database.client.service.ListCallback;
import com.google.code.gwt.database.client.service.RowIdListCallback;
import com.google.code.gwt.database.client.service.VoidCallback;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.base.LatLngBounds;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.gwtphonegap.client.geolocation.GeolocationCallback;
import com.googlecode.gwtphonegap.client.geolocation.Position;
import com.googlecode.gwtphonegap.client.geolocation.PositionError;
import com.googlecode.gwtphonegap.client.notification.AlertCallback;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;

public class VesselWatchMapActivity extends MGWTAbstractActivity implements
        VesselWatchMapView.Presenter {

	private final ClientFactory clientFactory;
	private VesselWatchMapView view;
	private EventBus eventBus;
	private WSDOTDataService dbService;
	private static final String CAMERAS_URL = Consts.HOST_URL + "/traveler/api/cameras";
	private static final String VESSEL_WATCH_URL = Consts.HOST_URL + "/traveler/api/ferries/vessellocations";
	private static ArrayList<VesselWatchItem> vesselWatchItems = new ArrayList<VesselWatchItem>();
	private static HashMap<Integer, String> ferryIcons;
	private Timer timer;
	private PhoneGap phoneGap;
	
	public VesselWatchMapActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getVesselWatchMapView();
		dbService = clientFactory.getDbService();
		phoneGap = clientFactory.getPhoneGap();
		this.eventBus = eventBus;
		view.setPresenter(this);
		view.setMapLocation(); // Set initial map location.
		
		buildFerryIcons();
		drawVesselsLayer();

		timer = new Timer() {
			public void run() {
				drawVesselsLayer();
			}
		};

		// Schedule vessels to update every 30 seconds (30000 millseconds).
		timer.scheduleRepeating(30000);		
		
		panel.setWidget(view);
		
		if (Consts.ANALYTICS_ENABLED) {
			Analytics.trackScreen("/Ferries/Vessel Watch");
		}

	}

	private void buildFerryIcons() {

		ferryIcons = new HashMap<Integer, String>();

		ferryIcons.put(0, AppBundle.INSTANCE.ferry0().getSafeUri().asString());
		ferryIcons.put(30, AppBundle.INSTANCE.ferry30().getSafeUri().asString());
		ferryIcons.put(60, AppBundle.INSTANCE.ferry60().getSafeUri().asString());
		ferryIcons.put(90, AppBundle.INSTANCE.ferry90().getSafeUri().asString());
		ferryIcons.put(120, AppBundle.INSTANCE.ferry120().getSafeUri().asString());
		ferryIcons.put(150, AppBundle.INSTANCE.ferry150().getSafeUri().asString());
		ferryIcons.put(180, AppBundle.INSTANCE.ferry180().getSafeUri().asString());
		ferryIcons.put(210, AppBundle.INSTANCE.ferry210().getSafeUri().asString());
		ferryIcons.put(240, AppBundle.INSTANCE.ferry240().getSafeUri().asString());
		ferryIcons.put(270, AppBundle.INSTANCE.ferry270().getSafeUri().asString());
		ferryIcons.put(300, AppBundle.INSTANCE.ferry300().getSafeUri().asString());
		ferryIcons.put(330, AppBundle.INSTANCE.ferry330().getSafeUri().asString());
		ferryIcons.put(360, AppBundle.INSTANCE.ferry360().getSafeUri().asString());		
	}

	private void getCameras() {
		
		/** 
		 * Check the cache table for the last time data was downloaded. If we are within
		 * the allowed time period, don't sync, otherwise get fresh data from the server.
		 */
		dbService.getCacheLastUpdated(Tables.CAMERAS, new ListCallback<GenericRow>() {

			@Override
			public void onFailure(DataServiceException error) {
			}

			@Override
			public void onSuccess(List<GenericRow> result) {
				boolean shouldUpdate = true;
				
				if (!result.isEmpty()) {
					double now = System.currentTimeMillis();
					double lastUpdated = result.get(0).getDouble(CachesColumns.CACHE_LAST_UPDATED);
					shouldUpdate = (Math.abs(now - lastUpdated) > (7 * 86400000)); // Refresh every 7 days.
				}
				
				view.showProgressIndicator();
				
				if (shouldUpdate) {
					JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
					// Set timeout for 30 seconds (30000 milliseconds)
					jsonp.setTimeout(30000);
					jsonp.requestObject(CAMERAS_URL, new AsyncCallback<CamerasFeed>() {

						@Override
						public void onFailure(Throwable caught) {
							view.hideProgressIndicator();
							phoneGap.getNotification()
							.alert("Can't load data. Check your connection.",
									new AlertCallback() {
										@Override
										public void onOkButtonClicked() {
											// TODO Auto-generated method stub
										}
									}, "Connection Error");
						}

						@Override
						public void onSuccess(final CamerasFeed result) {
							if (result.getCameras() != null) {
								final List<Integer> starred = new ArrayList<Integer>();
								
								// Get any starred camera rows.
								dbService.getStarredCameras(new ListCallback<GenericRow>() {

									@Override
									public void onFailure(DataServiceException error) {
										Window.alert(error.getMessage());
									}

									@Override
									public void onSuccess(List<GenericRow> rows) {
										final ArrayList<CameraItem> cameraItems = new ArrayList<CameraItem>();
										CameraItem item;
										
										if (!rows.isEmpty()) {
											int numResults = rows.size();
											for (int i = 0; i < numResults; i++) {
												starred.add(rows.get(i).getInt(CamerasColumns.CAMERA_ID));
											}
										}
										
										//cameraItems.clear();
										int numCameras = result.getCameras().getItems().length();
										
										for (int i = 0; i < numCameras; i++) {
											item = new CameraItem();
											
											item.setCameraId(result.getCameras().getItems().get(i).getId());
											item.setTitle(result.getCameras().getItems().get(i).getTitle());
											item.setImageUrl(result.getCameras().getItems().get(i).getUrl());
											item.setLatitude(result.getCameras().getItems().get(i).getLat());
											item.setLongitude(result.getCameras().getItems().get(i).getLon());
											item.setHasVideo(result.getCameras().getItems().get(i).getHasVideo());
											item.setRoadName(result.getCameras().getItems().get(i).getRoadName());
											
											if (starred.contains(result.getCameras().getItems().get(i).getId())) {
												item.setIsStarred(1);
											}
											
											cameraItems.add(item);
										}
										
										// Purge existing cameras covered by incoming data
										dbService.deleteCameras(new VoidCallback() {

											@Override
											public void onFailure(DataServiceException error) {
											}

											@Override
											public void onSuccess() {
												// Bulk insert all the new cameras
												dbService.insertCameras(cameraItems, new RowIdListCallback() {

													@Override
													public void onFailure(DataServiceException error) {
													}

													@Override
													public void onSuccess(List<Integer> rowIds) {
														// Update the cache table with the time we did the update
														ArrayList<CacheItem> cacheItems = new ArrayList<CacheItem>();
														cacheItems.add(new CacheItem(Tables.CAMERAS, System.currentTimeMillis()));
														
														dbService.updateCachesTable(cacheItems, new VoidCallback() {

															@Override
															public void onFailure(DataServiceException error) {
															}

															@Override
															public void onSuccess() {
																view.hideProgressIndicator();
																drawCamerasLayer();
															}
														});
													}
												});
											}
										});
									}
								});
							}
						}
					});

				} else {
					view.hideProgressIndicator();
					drawCamerasLayer();
				}
			}
		});
	}
	
	private void drawCamerasLayer() {
		
		dbService.getCameras(new ListCallback<GenericRow>() {

			@Override
			public void onFailure(DataServiceException error) {
			}

			@Override
			public void onSuccess(List<GenericRow> result) {
				LatLngBounds bounds = view.getViewportBounds();
			    LatLng swPoint = bounds.getSouthWest();
			    LatLng nePoint = bounds.getNorthEast();

				ArrayList<LatLonItem> viewableMapArea = new ArrayList<LatLonItem>();
				viewableMapArea.add(new LatLonItem(nePoint.getLatitude(), swPoint.getLongitude()));
				viewableMapArea.add(new LatLonItem(nePoint.getLatitude(), nePoint.getLongitude()));
				viewableMapArea.add(new LatLonItem(swPoint.getLatitude(), nePoint.getLongitude()));
				viewableMapArea.add(new LatLonItem(swPoint.getLatitude(), swPoint.getLongitude()));

				ArrayList<CameraItem> cameras = new ArrayList<CameraItem>();
				int numRows = result.size();
				
				for (int i = 0; i < numRows; i++) {
					if (inPolygon(
							viewableMapArea,
							result.get(i).getDouble(CamerasColumns.CAMERA_LATITUDE),
							result.get(i).getDouble(CamerasColumns.CAMERA_LONGITUDE))
							&& result.get(i).getString(CamerasColumns.CAMERA_ROAD_NAME)
									.equalsIgnoreCase("ferries")) {
						
						cameras.add(new CameraItem(
								result.get(i).getInt(CamerasColumns.CAMERA_ID),
								result.get(i).getString(CamerasColumns.CAMERA_TITLE),
								result.get(i).getString(CamerasColumns.CAMERA_URL),
								result.get(i).getDouble(CamerasColumns.CAMERA_LATITUDE),
								result.get(i).getDouble(CamerasColumns.CAMERA_LONGITUDE),
								result.get(i).getInt(CamerasColumns.CAMERA_HAS_VIDEO)));
					}
				}
				
				view.drawCameras(cameras);
			}
		});
		
	}
	
	private void drawVesselsLayer() {

		JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
		// Set timeout for 25 seconds (25000 milliseconds)
		jsonp.setTimeout(25000);
		jsonp.requestObject(VESSEL_WATCH_URL, new AsyncCallback<VesselWatchFeed>() {

			@Override
			public void onFailure(Throwable caught) {
				view.hideProgressIndicator();
				phoneGap.getNotification()
				.alert("Can't load data. Check your connection.",
						new AlertCallback() {
							@Override
							public void onOkButtonClicked() {
								// TODO Auto-generated method stub
							}
						}, "Connection Error");
			}

			@Override
			public void onSuccess(VesselWatchFeed result) {
				
				vesselWatchItems.clear();
				VesselWatchItem item = null;
				
				if (result != null) {
					int numEntries = result.length();
					for (int i = 0; i < numEntries; i++) {
						item = new VesselWatchItem();
						
						if (!result.get(i).getInService()) {
							continue;
						}
						
						item.setVesselID(result.get(i).getVesselID());
						item.setName(result.get(i).getName());
						item.setRoute(result.get(i).getRoute());
						item.setLastDock(result.get(i).getLastDock());
						item.setArrivingTerminal(result.get(i).getATerm());
						item.setLeftDock(result.get(i).getLeftDock());
						item.setNextDep(result.get(i).getNextDep());
						item.setEta(result.get(i).getEta());
						item.setHead(result.get(i).getHead());
						item.setSpeed(result.get(i).getSpeed());
						
						// round heading to nearest 30 degrees
						int nearest = (result.get(i)
								.getHead() + 30 / 2) / 30 * 30;
						
						item.setIcon(ferryIcons.get(nearest));
						item.setLat(result.get(i).getLat());
						item.setLon(result.get(i).getLon());						
						
						vesselWatchItems.add(item);
						
					}
					
					view.drawFerries(vesselWatchItems);

				}
				
			}
		});
		
	}
	
	/**
	 * Iterate through collection of LatLon objects in ArrayList and see if
	 * passed latitude and longitude point is within the collection.
	 * 
	 * @param points
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	private boolean inPolygon(ArrayList<LatLonItem> points, double latitude, double longitude) {
		
		int j = points.size() - 1;
		double lat = latitude;
		double lon = longitude;
		boolean inPoly = false;

		for (int i = 0; i < points.size(); i++) {
			if ((points.get(i).getLongitude() < lon && points.get(j).getLongitude() >= lon)
					|| (points.get(j).getLongitude() < lon && points.get(i).getLongitude() >= lon)) {
				if (points.get(i).getLatitude()	+ (lon - points.get(i).getLongitude())
						/ (points.get(j).getLongitude() - points.get(i).getLongitude())
						* (points.get(j).getLatitude() - points.get(i).getLatitude()) < lat) {
					inPoly = !inPoly;
				}
			}
			j = i;
		}
		return inPoly;
	}
	
	@Override
	public void onStop() {
		view.setPresenter(null);
		timer.cancel();

	}
	
	@Override
	public void onBackButtonPressed() {
		clientFactory.getPlaceController().goTo(new FerriesPlace());
	}

	@Override
	public void onGoToLocationButtonPressed() {
		clientFactory.getPlaceController().goTo(new GoToFerriesLocationPlace());
		if (Consts.ANALYTICS_ENABLED) {
			Analytics.trackScreen("/Ferries/Vessel Watch/Go To Location");
		}
	}

	@Override
	public void onCameraButtonPressed(boolean showCameras) {
		if (showCameras) {
			view.hideCameras();
		} else {
			view.showCameras();
		}
		
	}

	@Override
	public void onCameraSelected(int cameraId) {
		clientFactory.getPlaceController().goTo(
				new CameraPlace(Integer.toString(cameraId)));
		if (Consts.ANALYTICS_ENABLED) {
			Analytics.trackScreen("/Ferries/Vessel Watch/Cameras");
		}
	}

	@Override
	public void onLocateButtonPressed() {
		
		phoneGap.getGeolocation().getCurrentPosition(new GeolocationCallback() {

			@Override
			public void onSuccess(Position position) {
				double latitude = position.getCoordinates().getLatitude();
				double longitude = position.getCoordinates().getLongitude();

				view.setMapLocation(latitude, longitude, 12);

			}

			@Override
			public void onFailure(PositionError error) {
				switch (error.getCode()) {
					case PositionError.PERMISSION_DENIED:
						phoneGap.getNotification()
							.alert("You can turn Location Services on at Settings > Privacy > Location Services.",
									new AlertCallback() {

										@Override
										public void onOkButtonClicked() {
											// TODO Auto-generated method stub
										}
									}, "Location Services Off");
						
						break;
					default:
						break;
				}

			}
		});
	}

	@Override
	public void onFerrySelected(VesselWatchItem vessel) {
		
		if (Consts.ANALYTICS_ENABLED) {
			Analytics.trackScreen("/Ferries/Vessel Watch/Vessel Details/" + vessel.getName());
		}
		
		clientFactory.getPlaceController().goTo(new VesselDetailsPlace(vessel));
		
	}
	
	@Override
	public void onMapIsIdle() {
		captureClickEvents();
		getCameras();
	}
	
	/**
	 * JSNI method to capture click events and open urls in PhoneGap
	 * InAppBrowser.
	 * 
	 * Tapping external links on the Google map like the Google logo and 'Terms
	 * of Use' will cause those links to open in the same browser window as the
	 * app with no way for the user to return to the app.
	 * 
	 * http://docs.phonegap.com/en/2.4.0/cordova_inappbrowser_inappbrowser.md.html
	 */
	public static native void captureClickEvents() /*-{
		anchors = $doc.getElementsByTagName('a');
		for ( var i = 0; i < anchors.length; i++) {
			anchors[i].addEventListener('click', function(e) {
				e.preventDefault();
				$wnd.open(this.href, '_blank',
						'location=yes,enableViewportScale=yes');
			});
		}
	}-*/;

}