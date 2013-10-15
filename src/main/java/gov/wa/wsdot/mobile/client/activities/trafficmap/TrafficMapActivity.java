/*
 * Copyright (c) 2013 Washington State Department of Transportation
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

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.activities.alert.AlertPlace;
import gov.wa.wsdot.mobile.client.activities.camera.CameraPlace;
import gov.wa.wsdot.mobile.client.activities.home.HomePlace;
import gov.wa.wsdot.mobile.client.activities.trafficmap.expresslanes.SeattleExpressLanesPlace;
import gov.wa.wsdot.mobile.client.activities.trafficmap.location.GoToLocationPlace;
import gov.wa.wsdot.mobile.client.activities.trafficmap.seattleincidents.SeattleTrafficAlertsPlace;
import gov.wa.wsdot.mobile.client.activities.trafficmap.traveltimes.TravelTimesPlace;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.CachesColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.CamerasColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.HighwayAlertsColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService.Tables;
import gov.wa.wsdot.mobile.client.util.Consts;
import gov.wa.wsdot.mobile.shared.CacheItem;
import gov.wa.wsdot.mobile.shared.CameraItem;
import gov.wa.wsdot.mobile.shared.CamerasFeed;
import gov.wa.wsdot.mobile.shared.HighwayAlertItem;
import gov.wa.wsdot.mobile.shared.LatLonItem;

import java.util.ArrayList;
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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.gwtphonegap.client.geolocation.GeolocationCallback;
import com.googlecode.gwtphonegap.client.geolocation.Position;
import com.googlecode.gwtphonegap.client.geolocation.PositionError;
import com.googlecode.gwtphonegap.client.notification.AlertCallback;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;

public class TrafficMapActivity extends MGWTAbstractActivity implements
		TrafficMapView.Presenter {

	private final ClientFactory clientFactory;
	private TrafficMapView view;
	
	private EventBus eventBus;
	private WSDOTDataService dbService;
	private PhoneGap phoneGap;
	private static List<Integer> starred = new ArrayList<Integer>();
	private static List<CameraItem> cameraItems = new ArrayList<CameraItem>();
	private static final String CAMERAS_URL = Consts.HOST_URL + "/traveler/api/cameras";
	private Timer timer;
	
	public TrafficMapActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getTrafficMapView();
		dbService = clientFactory.getDbService();
		phoneGap = clientFactory.getPhoneGap();
		this.eventBus = eventBus;
		view.setPresenter(this);
		view.setMapLocation(); // Set initial map location.
		
		createTopicsList(view);
		drawHighwayAlertsLayer();

		panel.setWidget(view);

	}

	private void createTopicsList(final TrafficMapView view) {
		
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
				
				view.showProgressBar();
				
				if (shouldUpdate) {
					/**
					 * Check the cameras table for any starred entries. If we find some,
					 * save them to a list so we can re-star those after we flush the database.
					 */
					dbService.getStarredCameras(new ListCallback<GenericRow>() {

						@Override
						public void onFailure(DataServiceException error) {
						}

						@Override
						public void onSuccess(List<GenericRow> result) {
							starred.clear();
							
							if (!result.isEmpty()) {
								for (GenericRow row: result) {
									starred.add(row.getInt(CamerasColumns.CAMERA_ID));
								}
							}

							JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
							// Set timeout for 30 seconds (30000 milliseconds)
							jsonp.setTimeout(30000);
							jsonp.requestObject(CAMERAS_URL, new AsyncCallback<CamerasFeed>() {

								@Override
								public void onFailure(Throwable caught) {
									view.hideProgressBar();
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
								public void onSuccess(CamerasFeed result) {
									
									if (result.getCameras() != null) {
										cameraItems.clear();
										CameraItem item;
												
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
														List<CacheItem> cacheItems = new ArrayList<CacheItem>();
														cacheItems.add(new CacheItem(Tables.CAMERAS, System.currentTimeMillis()));
														
														dbService.updateCachesTable(cacheItems, new VoidCallback() {

															@Override
															public void onFailure(DataServiceException error) {
															}

															@Override
															public void onSuccess() {
																view.hideProgressBar();
																drawCamerasLayer();
															}
														});
													}
												});
											}
										});
									}
								}
							});
						}
					});

				} else {
					view.hideProgressBar();
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

				List<CameraItem> cameras = new ArrayList<CameraItem>();
				int numRows = result.size();
				
				for (int i = 0; i < numRows; i++) {
					if (inPolygon(
							viewableMapArea,
							result.get(i).getDouble(CamerasColumns.CAMERA_LATITUDE),
							result.get(i).getDouble(CamerasColumns.CAMERA_LONGITUDE))) {
						
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
	
	private void drawHighwayAlertsLayer() {
		
		dbService.getHighwayAlerts(new ListCallback<GenericRow>() {

			@Override
			public void onFailure(DataServiceException error) {
			}

			@Override
			public void onSuccess(List<GenericRow> result) {
				List<HighwayAlertItem> alerts = new ArrayList<HighwayAlertItem>();

				for (GenericRow alert: result) {
					alerts.add(new HighwayAlertItem(
							alert.getInt(HighwayAlertsColumns.HIGHWAY_ALERT_ID),
							alert.getString(HighwayAlertsColumns.HIGHWAY_ALERT_CATEGORY),
							alert.getString(HighwayAlertsColumns.HIGHWAY_ALERT_HEADLINE),
							alert.getDouble(HighwayAlertsColumns.HIGHWAY_ALERT_LATITUDE),
							alert.getDouble(HighwayAlertsColumns.HIGHWAY_ALERT_LONGITUDE)));
				}
				
				if (!result.isEmpty()) {
					view.drawAlerts(alerts);
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
	}
	
	@Override
	public void onBackButtonPressed() {
		clientFactory.getPlaceController().goTo(new HomePlace());
	}

	@Override
	public void onTravelTimesButtonPressed() {
		clientFactory.getPlaceController().goTo(new TravelTimesPlace());
	}

	@Override
	public void onGoToLocationButtonPressed() {
		clientFactory.getPlaceController().goTo(new GoToLocationPlace());
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
	}

	@Override
	public void onAlertSelected(int alertId) {
		clientFactory.getPlaceController().goTo(
				new AlertPlace(Integer.toString(alertId)));
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
					case PositionError.POSITION_UNAVAILABLE:
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
	public void onSeattleExpressLanesButtonPressed() {
		clientFactory.getPlaceController()
				.goTo(new SeattleExpressLanesPlace());
	}

	@Override
	public void onSeattleTrafficAlertsButtonPressed() {
		clientFactory.getPlaceController()
				.goTo(new SeattleTrafficAlertsPlace());
	}
	
	@Override
	public void onMapIsIdle() {
		captureClickEvents();
		drawCamerasLayer();
	}

    @Override
    public void onRefreshMapButtonPressed() {
        view.refreshMap();
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
		var anchors = $doc.getElementsByTagName('a');
		for ( var i = 0; i < anchors.length; i++) {
			anchors[i].addEventListener('click', function(e) {
				e.preventDefault();
				$wnd.open(this.href, '_blank',
						'location=yes,enableViewportScale=yes');
			});
		}
	}-*/;

}