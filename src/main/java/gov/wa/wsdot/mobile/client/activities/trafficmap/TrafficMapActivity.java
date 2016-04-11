/*
 * Copyright (c) 2016 Washington State Department of Transportation
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
import gov.wa.wsdot.mobile.client.activities.callout.CalloutPlace;
import gov.wa.wsdot.mobile.client.activities.camera.CameraPlace;
import gov.wa.wsdot.mobile.client.activities.home.HomePlace;
import gov.wa.wsdot.mobile.client.activities.trafficmap.menu.TrafficMenuPlace;
import gov.wa.wsdot.mobile.client.activities.trafficmap.menu.expresslanes.SeattleExpressLanesPlace;
import gov.wa.wsdot.mobile.client.activities.trafficmap.trafficincidents.TrafficAlertsPlace;
import gov.wa.wsdot.mobile.client.plugins.analytics.Analytics;
import gov.wa.wsdot.mobile.client.plugins.accessibility.Accessibility;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.CachesColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.CamerasColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.HighwayAlertsColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService.Tables;
import gov.wa.wsdot.mobile.client.util.Consts;
import gov.wa.wsdot.mobile.shared.CacheItem;
import gov.wa.wsdot.mobile.shared.CalloutItem;
import gov.wa.wsdot.mobile.shared.CameraItem;
import gov.wa.wsdot.mobile.shared.CamerasFeed;
import gov.wa.wsdot.mobile.shared.HighwayAlertItem;
import gov.wa.wsdot.mobile.shared.HighwayAlerts;
import gov.wa.wsdot.mobile.shared.LatLonItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.code.gwt.database.client.GenericRow;
import com.google.code.gwt.database.client.service.DataServiceException;
import com.google.code.gwt.database.client.service.ListCallback;
import com.google.code.gwt.database.client.service.RowIdListCallback;
import com.google.code.gwt.database.client.service.VoidCallback;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.base.LatLngBounds;
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
	private Analytics analytics;
	private Accessibility accessibility;
	private static List<Integer> starred = new ArrayList<Integer>();
	private static List<CameraItem> cameraItems = new ArrayList<CameraItem>();
	private static List<HighwayAlertItem> highwayAlertItems = new ArrayList<HighwayAlertItem>();
	private static List<CalloutItem> calloutItems = new ArrayList<CalloutItem>();
	private static final String CAMERAS_URL = Consts.HOST_URL + "/traveler/api/cameras";
	private static final String HIGHWAY_ALERTS_URL = Consts.HOST_URL + "/traveler/api/highwayalerts";
	private static DateTimeFormat dateFormat = DateTimeFormat.getFormat("MMMM d, yyyy h:mm a");
	
	public TrafficMapActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getTrafficMapView();
		dbService = clientFactory.getDbService();
		phoneGap = clientFactory.getPhoneGap();
		analytics = clientFactory.getAnalytics();
		accessibility = clientFactory.getAccessibility();
		this.eventBus = eventBus;
		view.setPresenter(this);
		view.setMapLocation(); // Set initial map location.

		if (Consts.ANALYTICS_ENABLED) {
			analytics.trackScreen("/Traffic Map");
		}

		panel.setWidget(view);

		accessibility.postScreenChangeNotification();

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
																view.hideProgressIndicator();
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
				
				if (!result.isEmpty()) {
				    view.drawCameras(cameras);
				}
			}
		});
		
	}
	
    private void getHighwayAlerts() {
        /** 
         * Check the cache table for the last time data was downloaded. If we are within
         * the allowed time period, don't sync, otherwise get fresh data from the server.
         */
        dbService.getCacheLastUpdated(Tables.HIGHWAY_ALERTS, new ListCallback<GenericRow>() {
            
            @Override
            public void onFailure(DataServiceException error) {
            }

            @Override
            public void onSuccess(List<GenericRow> result) {
                boolean shouldUpdate = true;

                if (!result.isEmpty()) {
                    double now = System.currentTimeMillis();
                    double lastUpdated = result.get(0).getDouble(CachesColumns.CACHE_LAST_UPDATED);
                    shouldUpdate = (Math.abs(now - lastUpdated) > (5 * 60000)); // Refresh every 5 minutes.
                }

                if (shouldUpdate) {
                    try {
                        JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
                        // Set timeout for 30 seconds (30000 milliseconds)
                        jsonp.setTimeout(30000);
                        jsonp.requestObject(HIGHWAY_ALERTS_URL, new AsyncCallback<HighwayAlerts>() {

                            @Override
                            public void onFailure(Throwable caught) {
                            }

                            @Override
                            public void onSuccess(HighwayAlerts result) {
                                highwayAlertItems.clear();
                                
                                if (result.getAlerts() != null) {
                                    HighwayAlertItem item;
                                    int size = result.getAlerts().getItems().length();
                                    
                                    for (int i = 0; i < size; i++) {
                                        item = new HighwayAlertItem();
                                        
                                        item.setAlertId(result.getAlerts().getItems().get(i).getAlertID());
                                        item.setHeadlineDescription(result.getAlerts().getItems().get(i).getHeadlineDescription());
                                        item.setEventCategory(result.getAlerts().getItems().get(i).getEventCategory());
                                        item.setPriority(result.getAlerts().getItems().get(i).getPriority());
                                        item.setStartLatitude(result.getAlerts().getItems().get(i).getStartRoadwayLocation().getLatitude());
                                        item.setStartLongitude(result.getAlerts().getItems().get(i).getStartRoadwayLocation().getLongitude());
                                        item.setStartRoadName(result.getAlerts().getItems().get(i).getStartRoadwayLocation().getRoadName());
                                        item.setLastUpdatedTime(dateFormat.format(new Date(
                                                        Long.parseLong(result
                                                                .getAlerts()
                                                                .getItems()
                                                                .get(i)
                                                                .getLastUpdatedTime()
                                                                .substring(6, 19)))));

                                        highwayAlertItems.add(item);
                                    }
                                    
                                    // Purge existing highway alerts covered by incoming data
                                    dbService.deleteHighwayAlerts(new VoidCallback() {

                                        @Override
                                        public void onFailure(DataServiceException error) {
                                        }

                                        @Override
                                        public void onSuccess() {
                                            // Bulk insert all the new highway alerts
                                            dbService.insertHighwayAlerts(highwayAlertItems, new RowIdListCallback() {

                                                @Override
                                                public void onFailure(DataServiceException error) {
                                                }

                                                @Override
                                                public void onSuccess(List<Integer> rowIds) {
                                                    // Update the cache table with the time we did the update
                                                    List<CacheItem> cacheItems = new ArrayList<CacheItem>();
                                                    cacheItems.add(new CacheItem(Tables.HIGHWAY_ALERTS, System.currentTimeMillis()));
                                                    dbService.updateCachesTable(cacheItems, new VoidCallback() {

                                                        @Override
                                                        public void onFailure(DataServiceException error) {
                                                        }

                                                        @Override
                                                        public void onSuccess() {
                                                            drawHighwayAlertsLayer();
                                                        }
                                                        
                                                    });
                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        });
                        
                    } catch (Exception e) {

                    }
                } else {
                    drawHighwayAlertsLayer();
                }
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
                LatLngBounds bounds = view.getViewportBounds();
                LatLng swPoint = bounds.getSouthWest();
                LatLng nePoint = bounds.getNorthEast();

                ArrayList<LatLonItem> viewableMapArea = new ArrayList<LatLonItem>();
                viewableMapArea.add(new LatLonItem(nePoint.getLatitude(), swPoint.getLongitude()));
                viewableMapArea.add(new LatLonItem(nePoint.getLatitude(), nePoint.getLongitude()));
                viewableMapArea.add(new LatLonItem(swPoint.getLatitude(), nePoint.getLongitude()));
                viewableMapArea.add(new LatLonItem(swPoint.getLatitude(), swPoint.getLongitude()));
			    
			    List<HighwayAlertItem> alerts = new ArrayList<HighwayAlertItem>();

				for (GenericRow alert: result) {
                    if (inPolygon(
                            viewableMapArea,
                            alert.getDouble(HighwayAlertsColumns.HIGHWAY_ALERT_LATITUDE),
                            alert.getDouble(HighwayAlertsColumns.HIGHWAY_ALERT_LONGITUDE))) {
                        
	                    alerts.add(new HighwayAlertItem(
	                            alert.getInt(HighwayAlertsColumns.HIGHWAY_ALERT_ID),
	                            alert.getString(HighwayAlertsColumns.HIGHWAY_ALERT_CATEGORY),
	                            alert.getString(HighwayAlertsColumns.HIGHWAY_ALERT_HEADLINE),
	                            alert.getDouble(HighwayAlertsColumns.HIGHWAY_ALERT_LATITUDE),
	                            alert.getDouble(HighwayAlertsColumns.HIGHWAY_ALERT_LONGITUDE),
	                            alert.getString(HighwayAlertsColumns.HIGHWAY_ALERT_PRIORITY)));				        
				    }
				}
				
				if (!result.isEmpty()) {
					view.drawAlerts(alerts);
				}
			}
		});
	}

	/**
	 * 
	 */
    private void getCallouts() {
        calloutItems.clear();

        CalloutItem item = new CalloutItem();
        item.setTitle("JBLM");
        item.setImageUrl("http://images.wsdot.wa.gov/traffic/flowmaps/jblm.png");
        item.setLatitude(47.103033);
        item.setLongitude(-122.584394);

        calloutItems.add(item);

        drawCalloutsLayer();
    }
	
	/**
	 * 
	 */
    private void drawCalloutsLayer() {
        LatLngBounds bounds = view.getViewportBounds();
        LatLng swPoint = bounds.getSouthWest();
        LatLng nePoint = bounds.getNorthEast();

        ArrayList<LatLonItem> viewableMapArea = new ArrayList<LatLonItem>();
        viewableMapArea.add(new LatLonItem(nePoint.getLatitude(), swPoint.getLongitude()));
        viewableMapArea.add(new LatLonItem(nePoint.getLatitude(), nePoint.getLongitude()));
        viewableMapArea.add(new LatLonItem(swPoint.getLatitude(), nePoint.getLongitude()));
        viewableMapArea.add(new LatLonItem(swPoint.getLatitude(), swPoint.getLongitude()));

        List<CalloutItem> callouts = new ArrayList<CalloutItem>();

        for (CalloutItem item: calloutItems) {
            if (inPolygon(
                    viewableMapArea,
                    item.getLatitude(),
                    item.getLongitude())) {

                callouts.add(new CalloutItem(
                        item.getTitle(),
                        item.getImageUrl(),
                        item.getLatitude(),
                        item.getLongitude()));
            }
        }

        if (!callouts.isEmpty()) {
            view.drawCallouts(callouts);
        }
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
	public void onMenuButtonPressed() {
		if (Consts.ANALYTICS_ENABLED) {
			analytics.trackScreen("/Traffic Map/Menu");
		}
		clientFactory.getPlaceController().goTo(new TrafficMenuPlace());
	}

	@Override
	public void onCameraButtonPressed(boolean showCameras) {
		if (showCameras) {	
			if (Consts.ANALYTICS_ENABLED) {
				analytics.trackEvent("Traffic", "Cameras", "Hide Cameras");
			}	
			view.hideCameras();
		} else {	
			if (Consts.ANALYTICS_ENABLED) {
				analytics.trackEvent("Traffic", "Cameras", "Show Cameras");
			}		
			view.showCameras();
		}
	}

	@Override
	public void onCameraSelected(int cameraId) {
		if (Consts.ANALYTICS_ENABLED) {
			analytics.trackScreen("/Traffic Map/Cameras");
		}
        clientFactory.getPlaceController().goTo(
                new CameraPlace(Integer.toString(cameraId)));
	}

	@Override
	public void onAlertSelected(int alertId) {
		if (Consts.ANALYTICS_ENABLED) {
			analytics.trackScreen("/Traffic Map/Map Alert");
		}
        clientFactory.getPlaceController().goTo(
                new AlertPlace(Integer.toString(alertId)));
	}	

    @Override
    public void onCalloutSelected(String url) {
		if (Consts.ANALYTICS_ENABLED) {
			analytics.trackScreen("/Traffic Map/Callout");
		}
		clientFactory.getPlaceController().goTo(new CalloutPlace(url));
    }

	@Override
	public void onLocateButtonPressed() {
		if (Consts.ANALYTICS_ENABLED) {
			analytics.trackScreen("/Traffic Map/My Location");
		}
		phoneGap.getGeolocation().getCurrentPosition(new GeolocationCallback() {

			@Override
			public void onSuccess(Position position) {
				double latitude = position.getCoordinates().getLatitude();
				double longitude = position.getCoordinates().getLongitude();

				view.addMapMarker(position);

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

	public void onSeattleExpressLanesButtonPressed() {
		if (Consts.ANALYTICS_ENABLED) {
			analytics.trackScreen("/Traffic Map/Seattle Express Lanes");
		}
        clientFactory.getPlaceController().goTo(new SeattleExpressLanesPlace());
	}

	@Override
	public void onSeattleTrafficAlertsButtonPressed(LatLngBounds bounds) {

        // Check if map has loaded
		if (bounds != null) {
			if (Consts.ANALYTICS_ENABLED) {
				analytics.trackScreen("/Traffic Map/Alerts In This Area");
			}
			clientFactory.getPlaceController()
					.goTo(new TrafficAlertsPlace(bounds));
		}
	}
	
	@Override
	public void onMapIsIdle() {
		captureClickEvents();
		getCameras();
        getHighwayAlerts();
        getCallouts();
	}

    @Override
    public void onRefreshMapButtonPressed() {
        view.refreshMap();
        getHighwayAlerts();
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