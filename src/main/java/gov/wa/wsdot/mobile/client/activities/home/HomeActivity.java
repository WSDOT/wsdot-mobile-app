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

package gov.wa.wsdot.mobile.client.activities.home;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.google.code.gwt.database.client.GenericRow;
import com.google.code.gwt.database.client.service.DataServiceException;
import com.google.code.gwt.database.client.service.ListCallback;
import com.google.code.gwt.database.client.service.RowIdListCallback;
import com.google.code.gwt.database.client.service.VoidCallback;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.gwtphonegap.client.notification.AlertCallback;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowStandardHandler;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowStandardHandler.PullActionHandler;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.activities.about.AboutPlace;
import gov.wa.wsdot.mobile.client.activities.alert.AlertPlace;
import gov.wa.wsdot.mobile.client.activities.amtrakcascades.AmtrakCascadesPlace;
import gov.wa.wsdot.mobile.client.activities.borderwait.BorderWaitPlace;
import gov.wa.wsdot.mobile.client.activities.camera.CameraPlace;
import gov.wa.wsdot.mobile.client.activities.ferries.FerriesPlace;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.sailings.FerriesRouteSailingsPlace;
import gov.wa.wsdot.mobile.client.activities.mountainpasses.MountainPassDetailsPlace;
import gov.wa.wsdot.mobile.client.activities.mountainpasses.MountainPassesPlace;
import gov.wa.wsdot.mobile.client.activities.socialmedia.SocialMediaPlace;
import gov.wa.wsdot.mobile.client.activities.tollrates.TollRatesPlace;
import gov.wa.wsdot.mobile.client.activities.trafficmap.TrafficMapPlace;
import gov.wa.wsdot.mobile.client.activities.trafficmap.traveltimes.TravelTimeDetailsPlace;
import gov.wa.wsdot.mobile.client.css.AppBundle;
import gov.wa.wsdot.mobile.client.plugins.analytics.Analytics;
import gov.wa.wsdot.mobile.client.plugins.accessibility.Accessibility;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.CachesColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.CamerasColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.FerriesSchedulesColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.HighwayAlertsColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.MountainPassesColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.TravelTimesColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService.Tables;
import gov.wa.wsdot.mobile.client.util.Consts;
import gov.wa.wsdot.mobile.shared.CacheItem;
import gov.wa.wsdot.mobile.shared.CameraItem;
import gov.wa.wsdot.mobile.shared.FerriesRouteFeed;
import gov.wa.wsdot.mobile.shared.FerriesRouteItem;
import gov.wa.wsdot.mobile.shared.HighwayAlertItem;
import gov.wa.wsdot.mobile.shared.HighwayAlerts;
import gov.wa.wsdot.mobile.shared.MountainPassConditions;
import gov.wa.wsdot.mobile.shared.MountainPassConditions.Forecast;
import gov.wa.wsdot.mobile.shared.MountainPassItem;
import gov.wa.wsdot.mobile.shared.TravelTimes;
import gov.wa.wsdot.mobile.shared.TravelTimesItem;

public class HomeActivity extends MGWTAbstractActivity implements
		HomeView.Presenter {

	private final ClientFactory clientFactory;
	private final HomeView view;
	private WSDOTDataService dbService;
	private PhoneGap phoneGap;
	private Analytics analytics;
	private static final String HIGHWAY_ALERTS_URL = Consts.HOST_URL + "/traveler/api/highwayalerts";
	private static final String FERRIES_SCHEDULES_URL = Consts.HOST_URL + "/traveler/api/wsfschedule";
	private static final String MOUNTAIN_PASS_URL = Consts.HOST_URL + "/traveler/api/mountainpassconditions";
	private static final String TRAVEL_TIMES_URL = Consts.HOST_URL + "/traveler/api/traveltimes";
	private Accessibility accessibility;
	private static List<CameraItem> cameraItems = new ArrayList<CameraItem>();
	private static List<FerriesRouteItem> ferriesRouteItems = new ArrayList<FerriesRouteItem>();
	private static List<MountainPassItem> mountainPassItems = new ArrayList<MountainPassItem>();
	private static List<TravelTimesItem> travelTimesItems = new ArrayList<TravelTimesItem>();
	private static List<HighwayAlertItem> highwayAlertItems = new ArrayList<HighwayAlertItem>();
	private static Timer timer;
	private static DateTimeFormat dateFormat = DateTimeFormat.getFormat("MMMM d, yyyy h:mm a");
	private static DateTimeFormat parseDateFormat = DateTimeFormat.getFormat("yyyy,M,d,H,m"); //e.g. [2010, 11, 2, 8, 22]
	private static HashMap<String, String[]> weatherPhrases = new HashMap<String, String[]>();
	private static HashMap<String, String[]> weatherPhrasesNight = new HashMap<String, String[]>();
	private static int lastTab = 0;

	
	public HomeActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
		view = clientFactory.getHomeView();
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
        view.setPresenter(this);
        dbService = clientFactory.getDbService();
        phoneGap = clientFactory.getPhoneGap();
        analytics = clientFactory.getAnalytics();
		accessibility = clientFactory.getAccessibility();
		view.getPullHeader().setHTML("pull down");
		
		PullArrowStandardHandler headerHandler = new PullArrowStandardHandler(
				view.getPullHeader(), view.getPullPanel());

		headerHandler.setErrorText("Error");
		headerHandler.setLoadingText("Loading");
		headerHandler.setNormalText("pull down");
		headerHandler.setPulledText("release to load");
		headerHandler.setPullActionHandler(new PullActionHandler() {

			@Override
			public void onPullAction(final AsyncCallback<Void> callback) {
				
				new Timer() {

					@Override
					public void run() {
						createFavoritesList();					
						view.refresh();
						callback.onSuccess(null);
					}
					
				}.schedule(1);
			}
		});

		view.setHeaderPullHandler(headerHandler);

		buildWeatherPhrases();
		createAlertsList();
		createFavoritesList();
		
		timer = new Timer() {
			@Override
			public void run() {
				createAlertsList();
			}
		};
		
		// Schedule alert box to update every 60 seconds (60000 millseconds).
		timer.scheduleRepeating(60000);

		if (Consts.ANALYTICS_ENABLED) {
			analytics.trackScreen("/Home");
		}

		panel.setWidget(view);

		accessibility.postScreenChangeNotification();
	}

	private void createAlertsList() {
		
		/** 
		 * Check the cache table for the last time data was downloaded. If we are within
		 * the allowed time period, don't sync, otherwise get fresh data from the server.
		 */
		dbService.getCacheLastUpdated(Tables.HIGHWAY_ALERTS, new ListCallback<GenericRow>() {
			
			@Override
			public void onFailure(DataServiceException error) {
                // On first install the 'caches' table doesn't exist yet. Why?
			    highwayAlertItems.clear();
                highwayAlertItems.add(new HighwayAlertItem(-1, "No highest impact travel alerts"));
                view.hideProgressIndicator();
                view.render(highwayAlertItems);
                view.refresh();
			}

			@Override
			public void onSuccess(List<GenericRow> result) {
				boolean shouldUpdate = true;

				if (!result.isEmpty()) {
					double now = System.currentTimeMillis();
					double lastUpdated = result.get(0).getDouble(CachesColumns.CACHE_LAST_UPDATED);
					shouldUpdate = (Math.abs(now - lastUpdated) > (5 * 60000)); // Refresh every 5 minutes.
				}

				view.showProgressIndicator();
				view.clear();
				
				if (shouldUpdate) {
					try {
						JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
						// Set timeout for 30 seconds (30000 milliseconds)
						jsonp.setTimeout(30000);
						jsonp.requestObject(HIGHWAY_ALERTS_URL,	new AsyncCallback<HighwayAlerts>() {

							@Override
							public void onFailure(Throwable caught) {
								highwayAlertItems.clear();
								highwayAlertItems.add(new HighwayAlertItem(-1, "Can't load data. Check your connection."));
								view.hideProgressIndicator();
								view.render(highwayAlertItems);
								view.refresh();
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
					                                highwayAlertItems.clear();
					                                highwayAlertItems.add(new HighwayAlertItem(-1, error.getMessage()));
					                                view.hideProgressIndicator();
					                                view.render(highwayAlertItems);
					                                view.refresh();
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
															dbService.getHighwayAlerts(new ListCallback<GenericRow>() {
																
																@Override
																public void onFailure(DataServiceException error) {
																}
											
																@Override
																public void onSuccess(List<GenericRow> result) {
																	getHighestPriorityHighwayAlerts(result);
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
						
					} catch (Exception e) {

					}
				} else {
				
					dbService.getHighwayAlerts(new ListCallback<GenericRow>() {
	
						@Override
						public void onFailure(DataServiceException error) {
						}
	
						@Override
						public void onSuccess(List<GenericRow> result) {
							getHighestPriorityHighwayAlerts(result);
						}
					});
				}
			}
		});

	}

	private void getHighestPriorityHighwayAlerts(List<GenericRow> result) {
	    highwayAlertItems.clear();
		HighwayAlertItem item;
		int numResults = result.size();
		
		for (int i = 0; i < numResults; i++) {
			if (result.get(i).getString(HighwayAlertsColumns.HIGHWAY_ALERT_PRIORITY).equalsIgnoreCase("highest")) {
				item = new HighwayAlertItem();
				item.setAlertId(result.get(i).getInt(HighwayAlertsColumns.HIGHWAY_ALERT_ID));
				item.setHeadlineDescription(result.get(i).getString(HighwayAlertsColumns.HIGHWAY_ALERT_HEADLINE));
				highwayAlertItems.add(item);
			}
		}
		
		if (highwayAlertItems.isEmpty()) {
			highwayAlertItems.add(new HighwayAlertItem(-1, "No highest impact travel alerts"));
		}
		
		view.hideProgressIndicator();
		view.render(highwayAlertItems);
		view.refresh();
	}
	
	@Override
	public void onStop() {
        timer.cancel();
	    view.setPresenter(null);
	}
	
	@Override
	public void onAboutButtonPressed() {

		clientFactory.getPlaceController().goTo(new AboutPlace());
	}

	@Override
	public void onTrafficMapButtonPressed() {
		clientFactory.getPlaceController().goTo(new TrafficMapPlace());
	}

	@Override
	public void onFerriesButtonPressed() {
		clientFactory.getPlaceController().goTo(new FerriesPlace());		
	}
	
	@Override
	public void onMountainPassesButtonPressed() {
		clientFactory.getPlaceController().goTo(new MountainPassesPlace());
	}

	@Override
	public void onSocialMediaButtonPressed() {
		clientFactory.getPlaceController().goTo(new SocialMediaPlace());
	}

	@Override
	public void onTollRatesButtonPressed() {
		clientFactory.getPlaceController().goTo(new TollRatesPlace());
	}

	@Override
	public void onBorderWaitButtonPressed() {
		clientFactory.getPlaceController().goTo(new BorderWaitPlace());
	}

    @Override
    public void onAmtrakButtonPressed() {
        clientFactory.getPlaceController().goTo(new AmtrakCascadesPlace());
    }

	@Override
	public void onHighImpactAlertSelected(int alertId) {
		clientFactory.getPlaceController().goTo(
				new AlertPlace(Integer.toString(alertId)));
	}
	
	private void createFavoritesList() {
		
		dbService.getStarredCameras(new ListCallback<GenericRow>() {

			@Override
			public void onFailure(DataServiceException error) {
				
				
			}
			
			@Override
			public void onSuccess(List<GenericRow> result) {
				
				if (!result.isEmpty()) {
					cameraItems.clear();
					CameraItem c;
					
					for (GenericRow camera: result) {
						c = new CameraItem();
						
						c.setCameraId(camera.getInt(CamerasColumns.CAMERA_ID));
						c.setTitle(camera.getString(CamerasColumns.CAMERA_TITLE));

						cameraItems.add(c);						
					}

					view.hideEmptyFavoritesMessage();
					view.showCamerasHeader();
					view.showCamerasList();
					view.renderCameras(cameraItems);
					view.refresh();
					
				} else {
					view.hideCamerasHeader();
					view.hideCamerasList();					
				}
				
			}
		});
		
		dbService.getStarredFerriesSchedules(new ListCallback<GenericRow>() {

			@Override
			public void onFailure(DataServiceException error) {
			}

			@Override
			public void onSuccess(final List<GenericRow> starredFerriesSchedules) {
				if (!starredFerriesSchedules.isEmpty()) {
					/** 
					 * Check the cache table for the last time data was downloaded. If we are within
					 * the allowed time period, don't sync, otherwise get fresh data from the server.
					 */
					dbService.getCacheLastUpdated(Tables.FERRIES_SCHEDULES, new ListCallback<GenericRow>() {

						@Override
						public void onFailure(DataServiceException error) {
						}

						@Override
						public void onSuccess(List<GenericRow> result) {
							boolean shouldUpdate = true;
							
							if (!result.isEmpty()) {
								double now = System.currentTimeMillis();
								double lastUpdated = result.get(0).getDouble(CachesColumns.CACHE_LAST_UPDATED);
								shouldUpdate = (Math.abs(now - lastUpdated) > (30 * 60000)); // Refresh every 30 minutes.
							}

							//view.showProgressBar();
							
							if (shouldUpdate) {
								final List<Integer> starred = new ArrayList<Integer>();
								
								for (GenericRow row: starredFerriesSchedules) {
									starred.add(row.getInt(FerriesSchedulesColumns.FERRIES_SCHEDULE_ID));
								}

								JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
								// Set timeout for 30 seconds (30000 milliseconds)
								jsonp.setTimeout(30000);
								jsonp.requestObject(
										FERRIES_SCHEDULES_URL,
										new AsyncCallback<FerriesRouteFeed>() {

									@Override
									public void onFailure(Throwable caught) {
										//view.hideProgressBar();
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
									public void onSuccess(FerriesRouteFeed result) {
										
										if (result.getRoutes() != null) {
											ferriesRouteItems.clear();
											FerriesRouteItem item;
											int numRoutes = result.getRoutes().length();
											
											for (int i = 0; i < numRoutes; i++) {
												item = new FerriesRouteItem();

												item.setRouteID(result.getRoutes().get(i).getRouteID());
												item.setDescription(result.getRoutes().get(i).getDescription());
												item.setCrossingTime(result.getRoutes().get(i).getCrossingTime());
												item.setScheduleDate(new JSONArray(result.getRoutes().get(i).getDate()).toString());
												item.setRouteAlert(new JSONArray(result.getRoutes().get(i).getRouteAlert()).toString());
												item.setCacheDate(dateFormat.format(new Date(
																Long.parseLong(result.getRoutes()
																		.get(i).getCacheDate()
																		.substring(6, 19)))));
												
												if (starred.contains(result.getRoutes().get(i).getRouteID())) {
													item.setIsStarred(1);
												}
												
												ferriesRouteItems.add(item);
											}
											
											// Purge existing border wait times covered by incoming data
											dbService.deleteFerriesSchedules(new VoidCallback() {

												@Override
												public void onFailure(DataServiceException error) {
												}

												@Override
												public void onSuccess() {
													// Bulk insert all the new ferries schedules
													dbService.insertFerriesSchedules(ferriesRouteItems, new RowIdListCallback() {

														@Override
														public void onFailure(DataServiceException error) {
														}

														@Override
														public void onSuccess(List<Integer> rowIds) {
															// Update the cache table with the time we did the update
															List<CacheItem> cacheItems = new ArrayList<CacheItem>();
															cacheItems.add(new CacheItem(Tables.FERRIES_SCHEDULES, System.currentTimeMillis()));
															dbService.updateCachesTable(cacheItems, new VoidCallback() {

																@Override
																public void onFailure(DataServiceException error) {
																}

																@Override
																public void onSuccess() {
																	getFerriesSchedules(starredFerriesSchedules);
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
								getFerriesSchedules(starredFerriesSchedules);
							}

						}
					});
					
				} else {
					view.hideFerriesHeader();
					view.hideFerriesList();
				}
			}
		});
		
		dbService.getStarredMountainPasses(new ListCallback<GenericRow>() {

			@Override
			public void onFailure(DataServiceException error) {
			}

			@Override
			public void onSuccess(final List<GenericRow> starredMountainPassRows) {
				if (!starredMountainPassRows.isEmpty()) {
					/** 
					 * Check the cache table for the last time data was downloaded. If we are within
					 * the allowed time period, don't sync, otherwise get fresh data from the server.
					 */
					dbService.getCacheLastUpdated(Tables.MOUNTAIN_PASSES, new ListCallback<GenericRow>() {

						@Override
						public void onFailure(DataServiceException error) {
						}

						@Override
						public void onSuccess(List<GenericRow> result) {
							boolean shouldUpdate = true;
							
							if (!result.isEmpty()) {
								double now = System.currentTimeMillis();
								double lastUpdated = result.get(0).getDouble(CachesColumns.CACHE_LAST_UPDATED);
								shouldUpdate = (Math.abs(now - lastUpdated) > (15 * 60000)); // Refresh every 15 minutes.
							}

							//view.showProgressBar();
							
							if (shouldUpdate) {
								final List<Integer> starred = new ArrayList<Integer>();
	
								for (GenericRow row: starredMountainPassRows) {
									starred.add(row.getInt(MountainPassesColumns.MOUNTAIN_PASS_ID));
								}

								JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
								// Set timeout for 30 seconds (30000 milliseconds)
								jsonp.setTimeout(30000);
								jsonp.requestObject(
										MOUNTAIN_PASS_URL,
										new AsyncCallback<MountainPassConditions>() {

									@Override
									public void onFailure(Throwable caught) {
										//view.hideProgressBar();
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
									public void onSuccess(MountainPassConditions result) {
										mountainPassItems.clear();
										
										if (result.getMountainPassConditionsResult() != null) {
											MountainPassItem item;
											
											String weatherCondition;
											String weatherImage;
											String forecast_weather_image;
											
											String mDateUpdated = "";
											
											int numConditions = result.getMountainPassConditionsResult().getPassCondition().length();
											
											for (int i = 0; i < numConditions; i++) {
												item = new MountainPassItem();
												
												weatherCondition = result.getMountainPassConditionsResult().getPassCondition().get(i).getWeatherCondition();
												weatherImage = getWeatherImage(weatherPhrases, weatherCondition);
											
												JsArrayInteger dateUpdated = result.getMountainPassConditionsResult().getPassCondition().get(i).getDateUpdated();
											    
												try {
													StringBuilder sb = new StringBuilder();
													for (int j=0; j < 5; j++) {
														sb.append(dateUpdated.get(j));
														sb.append(",");
													}
													String tempDate = sb.toString().trim();
													tempDate = tempDate.substring(0, tempDate.length()-1);
													Date date = parseDateFormat.parse(tempDate);
													mDateUpdated = dateFormat.format(date);
												} catch (Exception e) {
													mDateUpdated = "N/A";
												}
												
												JsArray<Forecast> forecasts = result.getMountainPassConditionsResult().getPassCondition().get(i).getForecast();
												int numForecasts = forecasts.length();
												
												for (int k=0; k < numForecasts; k++) {
													Forecast forecast = forecasts.get(k);

													if (isNight(forecast.getDay())) {
														forecast_weather_image = getWeatherImage(weatherPhrasesNight, forecast.getForecastText());
													} else {
														forecast_weather_image = getWeatherImage(weatherPhrases, forecast.getForecastText());
													}
													
													forecast.setWeatherIcon(forecast_weather_image);
													
													if (k == 0) {
														if (weatherCondition.equals("")) {
															weatherCondition = forecast.getForecastText().split("\\.")[0] + ".";
															weatherImage = forecast_weather_image;
														}
													}
													
													forecasts.set(k, forecast);

												}
												
												item.setWeatherCondition(weatherCondition);
												item.setElevationInFeet(result.getMountainPassConditionsResult().getPassCondition().get(i).getElevationInFeet());			
												item.setMountainPassId(result.getMountainPassConditionsResult().getPassCondition().get(i).getMountainPassId());
												item.setRoadCondition(result.getMountainPassConditionsResult().getPassCondition().get(i).getRoadCondition());
												item.setTemperatureInFahrenheit(result.getMountainPassConditionsResult().getPassCondition().get(i).getTemperatureInFahrenheit());
												item.setDateUpdated(mDateUpdated);
												item.setMountainPassName(result.getMountainPassConditionsResult().getPassCondition().get(i).getMountainPassName());
												item.setWeatherIcon(weatherImage);
												item.setRestrictionOneText(result.getMountainPassConditionsResult().getPassCondition().get(i).getRestrictionOne().getRestrictionText());
												item.setRestrictionOneTravelDirection(result.getMountainPassConditionsResult().getPassCondition().get(i).getRestrictionOne().getTravelDirection());
												item.setRestrictionTwoText(result.getMountainPassConditionsResult().getPassCondition().get(i).getRestrictionTwo().getRestrictionText());
												item.setRestrictionTwoTravelDirection(result.getMountainPassConditionsResult().getPassCondition().get(i).getRestrictionTwo().getTravelDirection());
												item.setCamera(new JSONArray(result.getMountainPassConditionsResult().getPassCondition().get(i).getCameras()).toString());
												item.setForecast(new JSONArray(forecasts).toString());

												if (starred.contains(result.getMountainPassConditionsResult().getPassCondition().get(i).getMountainPassId())) {
													item.setIsStarred(1);
												}
												
												mountainPassItems.add(item);
											}
											
											// Purge existing mountain passes covered by incoming data
											dbService.deleteMountainPasses(new VoidCallback() {

												@Override
												public void onFailure(DataServiceException error) {
												}

												@Override
												public void onSuccess() {
													// Bulk insert all the new mountain passes
													dbService.insertMountainPasses(mountainPassItems, new RowIdListCallback() {

														@Override
														public void onFailure(DataServiceException error) {
														}

														@Override
														public void onSuccess(List<Integer> rowIds) {
															// Update the cache table with the time we did the update
															ArrayList<CacheItem> cacheItems = new ArrayList<CacheItem>();
															cacheItems.add(new CacheItem(Tables.MOUNTAIN_PASSES, System.currentTimeMillis()));
															
															dbService.updateCachesTable(cacheItems, new VoidCallback() {

																@Override
																public void onFailure(DataServiceException error) {
																}

																@Override
																public void onSuccess() {
																	getMountainPasses(starredMountainPassRows);
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
								getMountainPasses(starredMountainPassRows);
							}
						}
					});
					
				} else {
					view.hideMountainPassesHeader();
					view.hideMountainPassesList();
				}
				
			}
		});
		
		dbService.getStarredTravelTimes(new ListCallback<GenericRow>() {

			@Override
			public void onFailure(DataServiceException error) {
			}

			@Override
			public void onSuccess(final List<GenericRow> starredTravelTimesRows) {
				if (!starredTravelTimesRows.isEmpty()) {				
					/** 
					 * Check the cache table for the last time data was downloaded. If we are within
					 * the allowed time period, don't sync, otherwise get fresh data from the server.
					 */
					dbService.getCacheLastUpdated(Tables.TRAVEL_TIMES, new ListCallback<GenericRow>() {

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

							//view.showProgressBar();
							
							if (shouldUpdate) {
								final List<Integer> starred = new ArrayList<Integer>();
								
								for (GenericRow row: starredTravelTimesRows) {
									starred.add(row.getInt(TravelTimesColumns.TRAVEL_TIMES_ID));
								}
								
								JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
								// Set timeout for 30 seconds (30000 milliseconds)
								jsonp.setTimeout(30000);
								jsonp.requestObject(TRAVEL_TIMES_URL, new AsyncCallback<TravelTimes>() {
			
									@Override
									public void onFailure(Throwable caught) {
										//view.hideProgressBar();
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
									public void onSuccess(TravelTimes result) {
										
										if (result.getTravelTimes() != null) {
											travelTimesItems.clear();
											TravelTimesItem item ;
											
											int numItems = result.getTravelTimes().getItems().length();
											
											for (int i = 0; i < numItems; i++) {
												item = new TravelTimesItem();
												
												item.setDistance(result.getTravelTimes().getItems().get(i).getDistance());
												item.setUpdated(result.getTravelTimes().getItems().get(i).getUpdated());
												item.setTitle(result.getTravelTimes().getItems().get(i).getTitle());
												item.setAverageTime(result.getTravelTimes().getItems().get(i).getAverage());
												item.setRouteId(Integer.parseInt(result.getTravelTimes().getItems().get(i).getRouteId(), 10));
												item.setCurrentTime(result.getTravelTimes().getItems().get(i).getCurrent());
												
												if (starred.contains(Integer.parseInt(result.getTravelTimes().getItems().get(i).getRouteId(), 10))) {
													item.setIsStarred(1);
												}
												
												travelTimesItems.add(item);
											}
											
											// Purge existing mountain passes covered by incoming data
											dbService.deleteTravelTimes(new VoidCallback() {
			
												@Override
												public void onFailure(DataServiceException error) {
												}
			
												@Override
												public void onSuccess() {
													// Bulk insert all the new travel times
													dbService.insertTravelTimes(travelTimesItems, new RowIdListCallback() {
			
														@Override
														public void onFailure(DataServiceException error) {
														}
			
														@Override
														public void onSuccess(List<Integer> rowIds) {
															// Update the cache table with the time we did the update
															ArrayList<CacheItem> cacheItems = new ArrayList<CacheItem>();
															cacheItems.add(new CacheItem(Tables.TRAVEL_TIMES, System.currentTimeMillis()));
															
															dbService.updateCachesTable(cacheItems, new VoidCallback() {
			
																@Override
																public void onFailure(DataServiceException error) {
																}
			
																@Override
																public void onSuccess() {
																	getTravelTimes(starredTravelTimesRows);
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
								getTravelTimes(starredTravelTimesRows);
							}
							
						}
					});
					
				} else {
					view.hideTravelTimesHeader();
					view.hideTravelTimesList();
				}
			}
		});
		
		view.showEmptyFavoritesMessage();
		
	}

	private void getFerriesSchedules(List<GenericRow> result) {
		
		ferriesRouteItems.clear();
		FerriesRouteItem f;
		
		for (GenericRow route: result) {
			f = new FerriesRouteItem();
			
			f.setRouteID(route.getInt(FerriesSchedulesColumns.FERRIES_SCHEDULE_ID));
			f.setDescription(route.getString(FerriesSchedulesColumns.FERRIES_SCHEDULE_TITLE));
			f.setCrossingTime(route.getString(FerriesSchedulesColumns.FERRIES_SCHEDULE_CROSSING_TIME));
			f.setRouteAlert(route.getString(FerriesSchedulesColumns.FERRIES_SCHEDULE_ALERT));
			f.setCacheDate(route.getString(FerriesSchedulesColumns.FERRIES_SCHEDULE_UPDATED));
			
			ferriesRouteItems.add(f);
		}
		
		view.hideEmptyFavoritesMessage();
		view.showFerriesHeader();
		view.showFerriesList();
		view.renderFerries(ferriesRouteItems);
		view.refresh();
		
	}
	
	private void getMountainPasses(List<GenericRow> result) {
		
		mountainPassItems.clear();
		MountainPassItem m;
		
		for (GenericRow pass: result) {
			m = new MountainPassItem();
			
			m.setMountainPassId(pass.getInt(MountainPassesColumns.MOUNTAIN_PASS_ID));
			m.setMountainPassName(pass.getString(MountainPassesColumns.MOUNTAIN_PASS_NAME));
			m.setWeatherIcon(pass.getString(MountainPassesColumns.MOUNTAIN_PASS_WEATHER_ICON));
			m.setWeatherCondition(pass.getString(MountainPassesColumns.MOUNTAIN_PASS_WEATHER_CONDITION));
			m.setDateUpdated(pass.getString(MountainPassesColumns.MOUNTAIN_PASS_DATE_UPDATED));
			
			mountainPassItems.add(m);						
		}
		
		view.hideEmptyFavoritesMessage();
		view.showMountainPassesHeader();
		view.showMountainPassesList();
		view.renderMountainPasses(mountainPassItems);
		view.refresh();
		
	}
	
	private void getTravelTimes(List<GenericRow> result) {
		
		travelTimesItems.clear();
		TravelTimesItem t;
		
		for (GenericRow time: result) {
			t = new TravelTimesItem();
			
			t.setRouteId(time.getInt(TravelTimesColumns.TRAVEL_TIMES_ID));
			t.setTitle(time.getString(TravelTimesColumns.TRAVEL_TIMES_TITLE));
			t.setUpdated(time.getString(TravelTimesColumns.TRAVEL_TIMES_UPDATED));
			t.setDistance(time.getString(TravelTimesColumns.TRAVEL_TIMES_DISTANCE));
			t.setAverageTime(time.getInt(TravelTimesColumns.TRAVEL_TIMES_AVERAGE));
			t.setCurrentTime(time.getInt(TravelTimesColumns.TRAVEL_TIMES_CURRENT));
			
			travelTimesItems.add(t);
		}
		
		view.hideEmptyFavoritesMessage();
		view.showTravelTimesHeader();
		view.showTravelTimesList();
		view.renderTravelTimes(travelTimesItems);
		view.refresh();
		
	}
	
	@Override
	public void onCameraSelected(int index) {
		if (Consts.ANALYTICS_ENABLED) {
			analytics.trackScreen("/Favorites/Cameras");
		}
		CameraItem item = cameraItems.get(index);
		
		clientFactory.getPlaceController().goTo(
				new CameraPlace(Integer.toString(item.getCameraId())));
	}

	@Override
	public void onFerriesSelected(int index) {
		if (Consts.ANALYTICS_ENABLED) {
			analytics.trackScreen("/Favorites/Ferries");
		}
		FerriesRouteItem item = ferriesRouteItems.get(index);
		
		clientFactory.getPlaceController().goTo(
				new FerriesRouteSailingsPlace(Integer.toString(item
						.getRouteID())));
	}

	@Override
	public void onMountainPassSelected(int index) {
		if (Consts.ANALYTICS_ENABLED) {
			analytics.trackScreen("/Favorites/Mountain Passes");
		}
		MountainPassItem item = mountainPassItems.get(index);

		clientFactory.getPlaceController().goTo(
				new MountainPassDetailsPlace(Integer.toString(item
						.getMountainPassId())));

	}

	@Override
	public void onTravelTimeSelected(int index) {
		if (Consts.ANALYTICS_ENABLED) {
			analytics.trackScreen("/Favorites/Travel Times");
		}
		TravelTimesItem item = travelTimesItems.get(index);
		
		clientFactory.getPlaceController()
				.goTo(new TravelTimeDetailsPlace(Integer.toString(item
						.getRouteId())));

	}

    @Override
    public void onTabSelected(int index) {
        int currentTab = index;

        if ((currentTab != lastTab) && (currentTab == 1)){
            if (Consts.ANALYTICS_ENABLED) {
                analytics.trackScreen("/Favorites");
            }
        }

        lastTab = currentTab;
    }

	@SuppressWarnings("unused")
    private static String makeImage(ImageResource resource) {
		AbstractImagePrototype image = AbstractImagePrototype.create(resource);
		String html = image.getHTML();
		
		return html;
	}
	
	private void buildWeatherPhrases() {
		String[] weather_clear = {"fair", "sunny", "clear"};
		String[] weather_few_clouds = {"few clouds", "scattered clouds", "mostly sunny", "mostly clear"};
		String[] weather_partly_cloudy = {"partly cloudy", "partly sunny"};
		String[] weather_cloudy = {"cloudy"};
		String[] weather_mostly_cloudy = {"broken", "mostly cloudy"};
		String[] weather_overcast = {"overcast"};
		String[] weather_light_rain = {"occasional light rain", "light rain", "showers"};
		String[] weather_rain = {"rain", "heavy rain", "raining"};
		String[] weather_snow = {"snow", "snowing", "light snow", "heavy snow"};
		String[] weather_fog = {"areas of fog", "fog"};
		String[] weather_sleet = {"rain snow", "light rain snow", "heavy rain snow", "rain and snow"};
		String[] weather_hail = {"ice pellets", "light ice pellets", "heavy ice pellets", "hail"};
		String[] weather_thunderstorm = {"thunderstorm", "thunderstorms"};
		
		weatherPhrases.clear();
		weatherPhrasesNight.clear();
		
        weatherPhrases.put(AppBundle.INSTANCE.css().sunnyIcon(), weather_clear);
        weatherPhrases.put(AppBundle.INSTANCE.css().cloudy_1Icon(), weather_few_clouds);
        weatherPhrases.put(AppBundle.INSTANCE.css().cloudy_2Icon(), weather_partly_cloudy);
        weatherPhrases.put(AppBundle.INSTANCE.css().cloudy_3Icon(), weather_cloudy);
        weatherPhrases.put(AppBundle.INSTANCE.css().cloudy_4Icon(), weather_mostly_cloudy);
        weatherPhrases.put(AppBundle.INSTANCE.css().overcastIcon(), weather_overcast);
        weatherPhrases.put(AppBundle.INSTANCE.css().light_rainIcon(), weather_light_rain);
        weatherPhrases.put(AppBundle.INSTANCE.css().shower_3Icon(), weather_rain);
        weatherPhrases.put(AppBundle.INSTANCE.css().snow_4Icon(), weather_snow);
        weatherPhrases.put(AppBundle.INSTANCE.css().fogIcon(), weather_fog);
        weatherPhrases.put(AppBundle.INSTANCE.css().sleetIcon(), weather_sleet);
        weatherPhrases.put(AppBundle.INSTANCE.css().hailIcon(), weather_hail);
        weatherPhrases.put(AppBundle.INSTANCE.css().tstorm_3Icon(), weather_thunderstorm);
        
        weatherPhrasesNight.put(AppBundle.INSTANCE.css().sunny_nightIcon(), weather_clear);
        weatherPhrasesNight.put(AppBundle.INSTANCE.css().cloudy_1_nightIcon(), weather_few_clouds);
        weatherPhrasesNight.put(AppBundle.INSTANCE.css().cloudy_2_nightIcon(), weather_partly_cloudy);
        weatherPhrasesNight.put(AppBundle.INSTANCE.css().cloudy_3_nightIcon(), weather_cloudy);
        weatherPhrasesNight.put(AppBundle.INSTANCE.css().cloudy_4_nightIcon(), weather_mostly_cloudy);
        weatherPhrasesNight.put(AppBundle.INSTANCE.css().overcastIcon(), weather_overcast);
        weatherPhrasesNight.put(AppBundle.INSTANCE.css().light_rainIcon(), weather_light_rain);
        weatherPhrasesNight.put(AppBundle.INSTANCE.css().shower_3Icon(), weather_rain);
        weatherPhrasesNight.put(AppBundle.INSTANCE.css().snow_4Icon(), weather_snow);
        weatherPhrasesNight.put(AppBundle.INSTANCE.css().fog_nightIcon(), weather_fog);
        weatherPhrasesNight.put(AppBundle.INSTANCE.css().sleetIcon(), weather_sleet);
        weatherPhrasesNight.put(AppBundle.INSTANCE.css().hailIcon(), weather_hail);
        weatherPhrasesNight.put(AppBundle.INSTANCE.css().tstorm_3Icon(), weather_thunderstorm);
		
		return;
	}
	
	private static String getWeatherImage(HashMap<String, String[]> weatherPhrases, String weather) {
		String image = AppBundle.INSTANCE.css().weather_naIcon();
		Set<Entry<String, String[]>> set = weatherPhrases.entrySet();
		Iterator<Entry<String, String[]>> i = set.iterator();
		
		if (weather.equals("")) return image;

		String s0 = weather.split("\\.")[0]; // Pattern match on first sentence only.
		
		while(i.hasNext()) {
			Entry<String, String[]> me = i.next();
			for (String phrase: (String[])me.getValue()) {
				if (s0.toLowerCase().startsWith(phrase)) {
					image = (String)me.getKey();
					return image;
				}
			}
		}
		
		return image;
	}
    
	private static boolean isNight(String text) {
		String patternStr = "night|tonight";
		RegExp pattern = RegExp.compile(patternStr, "i");
		MatchResult matcher = pattern.exec(text);
		boolean matchFound = matcher != null;
		
		return matchFound;
	}

}