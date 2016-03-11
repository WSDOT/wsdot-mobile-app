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

package gov.wa.wsdot.mobile.client.activities.ferries.schedules;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.sailings.FerriesRouteSailingsPlace;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.client.plugins.analytics.Analytics;
import gov.wa.wsdot.mobile.client.plugins.accessibility.Accessibility;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.CachesColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.FerriesSchedulesColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService.Tables;
import gov.wa.wsdot.mobile.client.util.Consts;
import gov.wa.wsdot.mobile.shared.CacheItem;
import gov.wa.wsdot.mobile.shared.FerriesRouteFeed;
import gov.wa.wsdot.mobile.shared.FerriesRouteItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.code.gwt.database.client.GenericRow;
import com.google.code.gwt.database.client.service.DataServiceException;
import com.google.code.gwt.database.client.service.ListCallback;
import com.google.code.gwt.database.client.service.RowIdListCallback;
import com.google.code.gwt.database.client.service.VoidCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.gwtphonegap.client.notification.AlertCallback;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowStandardHandler;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowStandardHandler.PullActionHandler;

public class FerriesRouteSchedulesActivity extends MGWTAbstractActivity
		implements FerriesRouteSchedulesView.Presenter {

	private final ClientFactory clientFactory;
	private FerriesRouteSchedulesView view;
	private EventBus eventBus;
	private WSDOTDataService dbService;
	private PhoneGap phoneGap;
	private Analytics analytics;
	private Accessibility accessibility;
	private static List<FerriesRouteItem> ferriesRouteItems = new ArrayList<FerriesRouteItem>();
	private static List<Integer> starred = new ArrayList<Integer>();
	private static DateTimeFormat dateFormat = DateTimeFormat.getFormat("MMMM d, yyyy h:mm a");
	private static final String FERRIES_SCHEDULES_URL = Consts.HOST_URL + "/traveler/api/wsfschedule";
	
	public FerriesRouteSchedulesActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getFerriesRouteSchedulesView();
		dbService = clientFactory.getDbService();
		phoneGap = clientFactory.getPhoneGap();
		analytics = clientFactory.getAnalytics();
		accessibility = clientFactory.getAccessibility();
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
		createTopicsList();

		if (Consts.ANALYTICS_ENABLED) {
            analytics.trackScreen("/Ferries/Schedules");
        }

		panel.setWidget(view);

		accessibility.postNotification();
	}

	@Override
	public void onItemSelected(int index) {
		FerriesRouteItem item = ferriesRouteItems.get(index);
		
		if (Consts.ANALYTICS_ENABLED) {
    		analytics.trackEvent("Ferries", "Schedules", item.getDescription());
		}
		
		clientFactory.getPlaceController().goTo(
				new FerriesRouteSailingsPlace(Integer.toString(item
						.getRouteID())));
	}
	
	private void createTopicsList() {
		
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
					shouldUpdate = (Math.abs(now - lastUpdated) > (15 * 60000)); // Refresh every 15 minutes.
				}

				view.showProgressIndicator();
				
				if (shouldUpdate) {
					/**
					 * Check the ferry schedules table for any starred entries. If we find some,
					 * save them to a list so we can re-star those after we flush the database.
					 */
					dbService.getStarredFerriesSchedules(new ListCallback<GenericRow>() {

						@Override
						public void onFailure(DataServiceException error) {
						}

						@Override
						public void onSuccess(List<GenericRow> result) {
							starred.clear();
							
							if (!result.isEmpty()) {
								for (GenericRow row: result) {
									starred.add(row.getInt(FerriesSchedulesColumns.FERRIES_SCHEDULE_ID));
								}
							}

							JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
							// Set timeout for 30 seconds (30000 milliseconds)
							jsonp.setTimeout(30000);
							jsonp.requestObject(
									FERRIES_SCHEDULES_URL,
									new AsyncCallback<FerriesRouteFeed>() {

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
																dbService.getFerriesSchedules(new ListCallback<GenericRow>() {

																	@Override
																	public void onFailure(DataServiceException error) {
																	}

																	@Override
																	public void onSuccess(List<GenericRow> result) {
																		getFerriesSchedules(result);																
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
					dbService.getFerriesSchedules(new ListCallback<GenericRow>() {

						@Override
						public void onFailure(DataServiceException error) {
						}

						@Override
						public void onSuccess(List<GenericRow> result) {
							getFerriesSchedules(result);																
						}
					});
				}
				
			}
		});

	}
	
	/**
	 * Get the lastest ferries route schedules from the database.
	 * 
	 * @param view
	 * @param result
	 */
	private void getFerriesSchedules(List<GenericRow> result) {
		ferriesRouteItems.clear();
		
		FerriesRouteItem item;
		
		int numResults = result.size();
		
		for (int i = 0; i < numResults; i++) {
			item = new FerriesRouteItem();

			item.setRouteID(result.get(i).getInt(FerriesSchedulesColumns.FERRIES_SCHEDULE_ID));
			item.setDescription(result.get(i).getString(FerriesSchedulesColumns.FERRIES_SCHEDULE_TITLE));
			item.setCrossingTime(result.get(i).getString(FerriesSchedulesColumns.FERRIES_SCHEDULE_CROSSING_TIME));
			item.setScheduleDate(result.get(i).getString(FerriesSchedulesColumns.FERRIES_SCHEDULE_DATE));
			item.setRouteAlert(result.get(i).getString(FerriesSchedulesColumns.FERRIES_SCHEDULE_ALERT));
			item.setCacheDate(result.get(i).getString(FerriesSchedulesColumns.FERRIES_SCHEDULE_UPDATED));
			item.setIsStarred(result.get(i).getInt(FerriesSchedulesColumns.FERRIES_SCHEDULE_IS_STARRED));
			
			ferriesRouteItems.add(item);

		}
		
		view.hideProgressIndicator();
		view.render(ferriesRouteItems);
		view.refresh();
		
	}

	@Override
	public void onStop() {
		view.setPresenter(null);
	}
	
	@Override
	public void onBackButtonPressed() {
		ActionEvent.fire(eventBus, ActionNames.BACK);
	}


}
