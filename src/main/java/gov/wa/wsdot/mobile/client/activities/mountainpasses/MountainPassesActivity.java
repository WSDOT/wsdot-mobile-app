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

package gov.wa.wsdot.mobile.client.activities.mountainpasses;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.css.AppBundle;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.CachesColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.MountainPassesColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService.Tables;
import gov.wa.wsdot.mobile.client.util.Consts;
import gov.wa.wsdot.mobile.shared.CacheItem;
import gov.wa.wsdot.mobile.shared.MountainPassConditions;
import gov.wa.wsdot.mobile.shared.MountainPassConditions.Forecast;
import gov.wa.wsdot.mobile.shared.MountainPassItem;

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

public class MountainPassesActivity extends MGWTAbstractActivity implements
		MountainPassesView.Presenter {

	private final ClientFactory clientFactory;
	private MountainPassesView view;
	private EventBus eventBus;
	private WSDOTDataService dbService;
	private PhoneGap phoneGap;
	private static HashMap<String, String[]> weatherPhrases = new HashMap<String, String[]>();
	private static HashMap<String, String[]> weatherPhrasesNight = new HashMap<String, String[]>();
	private static DateTimeFormat parseDateFormat = DateTimeFormat.getFormat("yyyy,M,d,H,m"); //e.g. [2010, 11, 2, 8, 22]
	private static DateTimeFormat displayDateFormat = DateTimeFormat.getFormat("MMMM d, yyyy h:mm a");
	private static final String MOUNTAIN_PASS_URL = Consts.HOST_URL + "/traveler/api/mountainpassconditions";
	private static List<MountainPassItem> mountainPassItems = new ArrayList<MountainPassItem>();
	private static List<Integer> starred = new ArrayList<Integer>();
	
	public MountainPassesActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view = clientFactory.getMountainPassesView();
		dbService = clientFactory.getDbService();
		phoneGap = clientFactory.getPhoneGap();
		this.eventBus = eventBus;
		view.setPresenter(this);
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
						createTopicsList();							
						view.refresh();
						callback.onSuccess(null);
					}
					
				}.schedule(1);
			}
			
		});
		
		view.setHeaderPullHandler(headerHandler);
		buildWeatherPhrases();
		createTopicsList();
		panel.setWidget(view);
	}
	
	@Override
	public void onStop() {
		view.setPresenter(null);
	}

	@Override
	public void onItemSelected(int index) {
		MountainPassItem item = mountainPassItems.get(index);

		clientFactory.getPlaceController().goTo(
				new MountainPassDetailsPlace(Integer.toString(item
						.getMountainPassId())));		
	}

	@Override
	public void onBackButtonPressed() {
		ActionEvent.fire(eventBus, ActionNames.BACK);		
	}

	private void createTopicsList() {
		
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

				view.showProgressIndicator();
				
				if (shouldUpdate) {
					/**
					 * Check the mountain passes table for any starred entries. If we find some,
					 * save them to a list so we can re-star those after we flush the database.
					 */
					dbService.getStarredMountainPasses(new ListCallback<GenericRow>() {

						@Override
						public void onFailure(DataServiceException error) {
						}

						@Override
						public void onSuccess(List<GenericRow> result) {
							starred.clear();
							
							if (!result.isEmpty()) {
								for (GenericRow row: result) {
									starred.add(row.getInt(MountainPassesColumns.MOUNTAIN_PASS_ID));
								}
							}

							JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
							// Set timeout for 30 seconds (30000 milliseconds)
							jsonp.setTimeout(30000);
							jsonp.requestObject(
									MOUNTAIN_PASS_URL,
									new AsyncCallback<MountainPassConditions>() {

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
												mDateUpdated = displayDateFormat.format(date);
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
																dbService.getMountainPasses(new ListCallback<GenericRow>() {

																	@Override
																	public void onFailure(DataServiceException error) {
																	}

																	@Override
																	public void onSuccess(List<GenericRow> result) {
																		getMountainPasses(result);
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
						}
					});

				} else {
					dbService.getMountainPasses(new ListCallback<GenericRow>() {

						@Override
						public void onFailure(DataServiceException error) {
						}

						@Override
						public void onSuccess(List<GenericRow> result) {
							getMountainPasses(result);
						}

					});
				}
			}
		});

	}
	
	/**
	 * Get the lastest mountain pass conditions from the database.
	 * 
	 * @param view
	 * @param result
	 */
	private void getMountainPasses(List<GenericRow> result) {
		mountainPassItems.clear();
		MountainPassItem item;
		
		int numResults = result.size();
		
		for (int i = 0; i < numResults; i++) {
			item = new MountainPassItem();
			
			item.setMountainPassId(result.get(i).getInt(MountainPassesColumns.MOUNTAIN_PASS_ID));
			item.setMountainPassName(result.get(i).getString(MountainPassesColumns.MOUNTAIN_PASS_NAME));
			item.setWeatherIcon(result.get(i).getString(MountainPassesColumns.MOUNTAIN_PASS_WEATHER_ICON));
			item.setWeatherCondition(result.get(i).getString(MountainPassesColumns.MOUNTAIN_PASS_WEATHER_CONDITION));
			item.setDateUpdated(result.get(i).getString(MountainPassesColumns.MOUNTAIN_PASS_DATE_UPDATED));
			
			mountainPassItems.add(item);
			
		}
		
		view.hideProgressIndicator();
		view.render(mountainPassItems);
		view.refresh();		
	}	

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
