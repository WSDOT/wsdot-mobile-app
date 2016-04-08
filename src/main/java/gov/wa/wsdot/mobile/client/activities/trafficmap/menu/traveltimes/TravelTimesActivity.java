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

package gov.wa.wsdot.mobile.client.activities.trafficmap.menu.traveltimes;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.client.plugins.accessibility.Accessibility;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.CachesColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.TravelTimesColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService.Tables;
import gov.wa.wsdot.mobile.client.util.Consts;
import gov.wa.wsdot.mobile.shared.CacheItem;
import gov.wa.wsdot.mobile.shared.TravelTimes;
import gov.wa.wsdot.mobile.shared.TravelTimesItem;

import java.util.ArrayList;
import java.util.List;

import com.google.code.gwt.database.client.GenericRow;
import com.google.code.gwt.database.client.service.DataServiceException;
import com.google.code.gwt.database.client.service.ListCallback;
import com.google.code.gwt.database.client.service.RowIdListCallback;
import com.google.code.gwt.database.client.service.VoidCallback;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.gwtphonegap.client.notification.AlertCallback;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowStandardHandler;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowStandardHandler.PullActionHandler;

public class TravelTimesActivity extends MGWTAbstractActivity implements
		TravelTimesView.Presenter {

	private final ClientFactory clientFactory;
	private TravelTimesView view;
	private EventBus eventBus;
	private WSDOTDataService dbService;
	private PhoneGap phoneGap;
	private Accessibility accessibility;
	private static List<TravelTimesItem> travelTimesItems = new ArrayList<TravelTimesItem>();
	private static List<Integer> starred = new ArrayList<Integer>();
	private static final String TRAVEL_TIMES_URL = Consts.HOST_URL + "/traveler/api/traveltimes";
	
	public TravelTimesActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getTravelTimesView();
		dbService = clientFactory.getDbService();
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
						createTopicsList(view);							
						view.refresh();
						callback.onSuccess(null);
					}
					
				}.schedule(1);				
			}
		});
		
		view.setHeaderPullHandler(headerHandler);
		createTopicsList(view);
		panel.setWidget(view);

		accessibility.postScreenChangeNotification();
	}

	private void createTopicsList(final TravelTimesView view) {
		
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

				view.showProgressIndicator();
				
				if (shouldUpdate) {
					/**
					 * Check the travel times table for any starred entries. If we find some,
					 * save them to a list so we can re-star those after we flush the database.
					 * 
					 */
					dbService.getStarredTravelTimes(new ListCallback<GenericRow>() {

						@Override
						public void onFailure(DataServiceException error) {
						}

						@Override
						public void onSuccess(List<GenericRow> result) {
							starred.clear();
							
							if (!result.isEmpty()) {
								for (GenericRow row: result) {
									starred.add(row.getInt(TravelTimesColumns.TRAVEL_TIMES_ID));
								}
							}
					
							JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
							// Set timeout for 30 seconds (30000 milliseconds)
							jsonp.setTimeout(30000);
							jsonp.requestObject(TRAVEL_TIMES_URL, new AsyncCallback<TravelTimes>() {
		
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
																dbService.getTravelTimes(new ListCallback<GenericRow>() {
		
																	@Override
																	public void onFailure(DataServiceException error) {
																	}
		
																	@Override
																	public void onSuccess(List<GenericRow> result) {
																		getTravelTimes(view, result);
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
					dbService.getTravelTimes(new ListCallback<GenericRow>() {

						@Override
						public void onFailure(DataServiceException error) {
						}

						@Override
						public void onSuccess(List<GenericRow> result) {
							getTravelTimes(view, result);
						}
					});
				}
				
			}
		});

	}
	
	/**
	 * Get the lastest travel times from the database.
	 * 
	 * @param view
	 * @param result
	 */
	private void getTravelTimes(TravelTimesView view, List<GenericRow> result) {
		travelTimesItems.clear();
		TravelTimesItem item;
		
		int numResults = result.size();
		
		for (int i = 0; i < numResults; i++) {
			item = new TravelTimesItem();
			
			item.setRouteId(result.get(i).getInt(TravelTimesColumns.TRAVEL_TIMES_ID));
			item.setTitle(result.get(i).getString(TravelTimesColumns.TRAVEL_TIMES_TITLE));
			item.setUpdated(result.get(i).getString(TravelTimesColumns.TRAVEL_TIMES_UPDATED));
			item.setDistance(result.get(i).getString(TravelTimesColumns.TRAVEL_TIMES_DISTANCE));
			item.setAverageTime(result.get(i).getInt(TravelTimesColumns.TRAVEL_TIMES_AVERAGE));
			item.setCurrentTime(result.get(i).getInt(TravelTimesColumns.TRAVEL_TIMES_CURRENT));
			item.setIsStarred(result.get(i).getInt(TravelTimesColumns.TRAVEL_TIMES_IS_STARRED));
			
			travelTimesItems.add(item);
			
		}
		
		view.hideProgressIndicator();
		view.render(travelTimesItems);
		view.refresh();		
		accessibility.postScreenChangeNotification();
	}
	
	@Override
	public void onStop() {
		view.setPresenter(null);
	}
	
	@Override
	public void onDoneButtonPressed() {
		ActionEvent.fire(eventBus, ActionNames.BACK);		
	}

	@Override
	public void onSearchTextChanged(final String filter) {
		dbService.getTravelTimes(filter, new ListCallback<GenericRow>() {

			@Override
			public void onFailure(DataServiceException error) {
			}

			@Override
			public void onSuccess(List<GenericRow> result) {
				getTravelTimes(view, result);
			}
		});
	}

	@Override
	public void onTravelTimeSelected(int index) {
		TravelTimesItem item = travelTimesItems.get(index);
		
		clientFactory.getPlaceController()
				.goTo(new TravelTimeDetailsPlace(Integer.toString(item
						.getRouteId())));		
	}

}
