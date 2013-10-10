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

package gov.wa.wsdot.mobile.client.activities.borderwait;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.css.AppBundle;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.BorderWaitColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.CachesColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService.Tables;
import gov.wa.wsdot.mobile.client.util.Consts;
import gov.wa.wsdot.mobile.shared.BorderCrossings;
import gov.wa.wsdot.mobile.shared.BorderWaitItem;
import gov.wa.wsdot.mobile.shared.CacheItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.code.gwt.database.client.GenericRow;
import com.google.code.gwt.database.client.service.DataServiceException;
import com.google.code.gwt.database.client.service.ListCallback;
import com.google.code.gwt.database.client.service.RowIdListCallback;
import com.google.code.gwt.database.client.service.VoidCallback;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.gwtphonegap.client.notification.AlertCallback;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import com.googlecode.mgwt.ui.client.widget.base.PullArrowStandardHandler;
import com.googlecode.mgwt.ui.client.widget.base.PullArrowStandardHandler.PullActionHandler;

public class BorderWaitActivity extends MGWTAbstractActivity implements BorderWaitView.Presenter {

	private final ClientFactory clientFactory;
	private BorderWaitView view;
	private EventBus eventBus;
	private WSDOTDataService dbService;
	private PhoneGap phoneGap;
	private static HashMap<Integer, String> USRouteIcon = new HashMap<Integer, String>();
	private static HashMap<Integer, String> CanadaRouteIcon = new HashMap<Integer, String>();
	private static List<BorderWaitItem> borderWaitItems = new ArrayList<BorderWaitItem>();
	private static List<Integer> starred = new ArrayList<Integer>();
	private static final String BORDER_WAIT_URL = Consts.HOST_URL + "/traveler/api/bordercrossings";
	
	public BorderWaitActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view = clientFactory.getBorderWaitView();
		dbService = clientFactory.getDbService();
		phoneGap = clientFactory.getPhoneGap();
		this.eventBus = eventBus;
		view.setPresenter(this);
		view.getNorthboundPullHeader().setHTML("pull down");
		view.getSouthboundPullHeader().setHTML("pull down");
		
		PullArrowStandardHandler headerHandler = new PullArrowStandardHandler(
				view.getNorthboundPullHeader(), view.getNorthboundPullPanel());
		
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
						createBorderWaitList(view);							
						view.refresh();
						callback.onSuccess(null);
					}
					
				}.schedule(1);
			}
			
		});
		
		PullArrowStandardHandler headerHandler2 = new PullArrowStandardHandler(
				view.getSouthboundPullHeader(), view.getSouthboundPullPanel());
		
		headerHandler2.setErrorText("Error");
		headerHandler2.setLoadingText("Loading");
		headerHandler2.setNormalText("pull down");
		headerHandler2.setPulledText("release to load");
		headerHandler2.setPullActionHandler(new PullActionHandler() {

			@Override
			public void onPullAction(final AsyncCallback<Void> callback) {

				new Timer() {

					@Override
					public void run() {
						createBorderWaitList(view);							
						view.refresh();
						callback.onSuccess(null);
					}
					
				}.schedule(1);
			}
			
		});		
		
		view.setNorthboundHeaderPullHandler(headerHandler);
		view.setSouthboundHeaderPullHandler(headerHandler2);
		
		buildRouteIcons();
		createBorderWaitList(view);
		panel.setWidget(view);
		
	}	

	private void createBorderWaitList(final BorderWaitView view) {
		
		/** 
		 * Check the cache table for the last time data was downloaded. If we are within
		 * the allowed time period, don't sync, otherwise get fresh data from the server.
		 */
		dbService.getCacheLastUpdated(Tables.BORDER_WAIT, new ListCallback<GenericRow>() {

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

				view.showProgressBar();
				
				if (shouldUpdate) {
					/**
					 * Check the travel border wait table for any starred entries. If we find some,
					 * save them to a list so we can re-star those after we flush the database.
					 */
					dbService.getStarredBorderWaits(new ListCallback<GenericRow>() {

						@Override
						public void onFailure(DataServiceException error) {
						}

						@Override
						public void onSuccess(List<GenericRow> result) {
							starred.clear();
							
							if (!result.isEmpty()) {
								for (GenericRow row: result) {
									starred.add(row.getInt(BorderWaitColumns.BORDER_WAIT_ID));
								}
							}
							
							JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
							// Set timeout for 30 seconds (30000 milliseconds)
							jsonp.setTimeout(30000);
							jsonp.requestObject(BORDER_WAIT_URL, new AsyncCallback<BorderCrossings>() {

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
								public void onSuccess(BorderCrossings result) {
									
									if (result.getWaitTimes() != null) {
										borderWaitItems.clear();
										BorderWaitItem item;
										
										int numItems = result.getWaitTimes().getItems().length();
										
										for (int i = 0; i < numItems; i++) {
											item = new BorderWaitItem();
											
											item.setId(result.getWaitTimes().getItems().get(i).getId());
											item.setTitle(result.getWaitTimes().getItems().get(i).getName());
											item.setUpdated(result.getWaitTimes().getItems().get(i).getUpdated());
											item.setLane(result.getWaitTimes().getItems().get(i).getLane());
											item.setRoute(result.getWaitTimes().getItems().get(i).getRoute());
											item.setDirection(result.getWaitTimes().getItems().get(i).getDirection());
											item.setWait(result.getWaitTimes().getItems().get(i).getWait());
											
											if (starred.contains(result.getWaitTimes().getItems().get(i).getId())) {
												item.setIsStarred(1);
											}
											
											borderWaitItems.add(item);

										}
										
										// Purge existing border wait times covered by incoming data
										dbService.deleteBorderWaits(new VoidCallback() {

											@Override
											public void onFailure(DataServiceException error) {
											}

											@Override
											public void onSuccess() {
												// Bulk insert all the new border wait times
												dbService.insertBorderWaits(borderWaitItems, new RowIdListCallback() {

													@Override
													public void onFailure(DataServiceException error) {
													}

													@Override
													public void onSuccess(List<Integer> rowIds) {
														// Update the cache table with the time we did the update
														ArrayList<CacheItem> cacheItems = new ArrayList<CacheItem>();
														cacheItems.add(new CacheItem(Tables.BORDER_WAIT, System.currentTimeMillis()));
														dbService.updateCachesTable(cacheItems, new VoidCallback() {

															@Override
															public void onFailure(DataServiceException error) {
															}

															@Override
															public void onSuccess() {
																dbService.getBorderWaits(new ListCallback<GenericRow>() {

																	@Override
																	public void onFailure(DataServiceException error) {
																	}

																	@Override
																	public void onSuccess(List<GenericRow> result) {
																		getBorderWaits(view, result);
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
					dbService.getBorderWaits(new ListCallback<GenericRow>() {

						@Override
						public void onFailure(DataServiceException error) {
						}

						@Override
						public void onSuccess(List<GenericRow> result) {
							getBorderWaits(view, result);
						}
					});					
				}
			}
		});
	}

	/**
	 * Get the lastest border wait times from the database.
	 * 
	 * @param view
	 * @param result
	 */
	private void getBorderWaits(BorderWaitView view, List<GenericRow> result) {
		ArrayList<BorderWaitItem> northboundBorderWaitItems = new ArrayList<BorderWaitItem>();
		ArrayList<BorderWaitItem> southboundBorderWaitItems = new ArrayList<BorderWaitItem>();
		BorderWaitItem item;
		
		int numResults = result.size();
		
		for (int i = 0; i < numResults; i++) {
			item = new BorderWaitItem();

			String direction = result.get(i).getString(BorderWaitColumns.BORDER_WAIT_DIRECTION);
			int route = result.get(i).getInt(BorderWaitColumns.BORDER_WAIT_ROUTE);
			
			item.setTitle(result.get(i).getString(BorderWaitColumns.BORDER_WAIT_TITLE));
			item.setLane(result.get(i).getString(BorderWaitColumns.BORDER_WAIT_LANE));
			item.setUpdated(result.get(i).getString(BorderWaitColumns.BORDER_WAIT_UPDATED));
			item.setWait(result.get(i).getInt(BorderWaitColumns.BORDER_WAIT_TIME));
			
			if (direction.equalsIgnoreCase("northbound")) {
				item.setRouteIcon(USRouteIcon.get(route));
				northboundBorderWaitItems.add(item);
			} else {
				item.setRouteIcon(CanadaRouteIcon.get(route));
				southboundBorderWaitItems.add(item);
			}
		}
		
		view.hideProgressBar();
		view.renderNorthbound(northboundBorderWaitItems);
		view.renderSouthbound(southboundBorderWaitItems);
		view.refresh();

	}

	private void buildRouteIcons() {
		USRouteIcon.put(5, AppBundle.INSTANCE.css().i5Icon());
		USRouteIcon.put(9, AppBundle.INSTANCE.css().sr9Icon());
		USRouteIcon.put(539, AppBundle.INSTANCE.css().sr539Icon());
		USRouteIcon.put(543, AppBundle.INSTANCE.css().sr543Icon());
		USRouteIcon.put(97, AppBundle.INSTANCE.css().us97Icon());
		
		CanadaRouteIcon.put(5, AppBundle.INSTANCE.css().bc99Icon());
		CanadaRouteIcon.put(9, AppBundle.INSTANCE.css().bc11Icon());
		CanadaRouteIcon.put(539, AppBundle.INSTANCE.css().bc13Icon());
		CanadaRouteIcon.put(543, AppBundle.INSTANCE.css().bc15Icon());
		CanadaRouteIcon.put(97, AppBundle.INSTANCE.css().bc97Icon());
	}

	@Override
	public void onStop() {
		view.setPresenter(null);
	}

	@Override
	public void onBackButtonPressed() {
		ActionEvent.fire(eventBus, ActionNames.BACK);
	}

	private static SafeHtml makeImage(ImageResource resource) {
		AbstractImagePrototype image = AbstractImagePrototype.create(resource);
		String html = image.getHTML();
		
		return SafeHtmlUtils.fromTrustedString(html);
	}
	
}
