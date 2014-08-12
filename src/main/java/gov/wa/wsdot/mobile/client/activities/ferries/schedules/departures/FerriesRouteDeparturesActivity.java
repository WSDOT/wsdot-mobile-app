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

package gov.wa.wsdot.mobile.client.activities.ferries.schedules.departures;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.CachesColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.FerriesSchedulesColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.FerriesTerminalSailingSpaceColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService.Tables;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService;
import gov.wa.wsdot.mobile.shared.CacheItem;
import gov.wa.wsdot.mobile.shared.FerriesAnnotationsItem;
import gov.wa.wsdot.mobile.shared.FerriesScheduleDateItem;
import gov.wa.wsdot.mobile.shared.FerriesScheduleTimesItem;
import gov.wa.wsdot.mobile.shared.FerriesTerminalItem;
import gov.wa.wsdot.mobile.shared.FerriesTerminalSailingSpaceFeed;
import gov.wa.wsdot.mobile.shared.FerriesTerminalSailingSpaceItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.code.gwt.database.client.GenericRow;
import com.google.code.gwt.database.client.service.DataServiceException;
import com.google.code.gwt.database.client.service.ListCallback;
import com.google.code.gwt.database.client.service.RowIdListCallback;
import com.google.code.gwt.database.client.service.VoidCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.i18n.client.TimeZoneInfo;
import com.google.gwt.i18n.client.constants.TimeZoneConstants;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;

public class FerriesRouteDeparturesActivity extends
		MGWTAbstractActivity implements
		FerriesRouteDeparturesView.Presenter {

	private final ClientFactory clientFactory;
	private FerriesRouteDeparturesView view;
	private EventBus eventBus;
	private WSDOTDataService dbService;
	private static ArrayList<String> daysOfWeek = new ArrayList<String>();
    private static ArrayList<FerriesScheduleDateItem> scheduleDateItems = new ArrayList<FerriesScheduleDateItem>();
    private static List<FerriesTerminalSailingSpaceItem> ferriesTerminalSailingSpaceItems = new ArrayList<FerriesTerminalSailingSpaceItem>();
	private String routeId;
	private int sailingsIndex;
	
	public FerriesRouteDeparturesActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getFerriesRouteDeparturesView();
		dbService = clientFactory.getDbService();
		this.eventBus = eventBus;
		view.setPresenter(this);
		
		Place place = clientFactory.getPlaceController().getWhere();
		if (place instanceof FerriesRouteDeparturesPlace) {
            FerriesRouteDeparturesPlace ferriesRouteSchedulesDayDeparturesPlace = (FerriesRouteDeparturesPlace) place;
			routeId = ferriesRouteSchedulesDayDeparturesPlace.getId();
			sailingsIndex = ferriesRouteSchedulesDayDeparturesPlace.getIndex();
			createTopicsList(routeId, 0, sailingsIndex);
			panel.setWidget(view);
		}
		
	}

	@Override
	public void onDayOfWeekSelected(int position) {
		createTopicsList(routeId, position, sailingsIndex);
		
	}
	
	private void createTopicsList(String routeId, final int dayIndex, final int sailingsIndex) {

		dbService.getFerriesSchedulesRoute(routeId, new ListCallback<GenericRow>() {

			@Override
			public void onFailure(DataServiceException error) {
			}

			@Override
			public void onSuccess(List<GenericRow> result) {
				scheduleDateItems.clear();
				daysOfWeek.clear();
				FerriesScheduleDateItem scheduleDate;
				FerriesTerminalItem terminal;
				FerriesAnnotationsItem notes;
				FerriesScheduleTimesItem timesItem;
				Date now = new Date();
				
				view.showProgressBar();
				
				JSONValue value = JSONParser.parseStrict(result.get(0).getString(FerriesSchedulesColumns.FERRIES_SCHEDULE_DATE));
				JSONArray dates = value.isArray();
				
				int numDates = dates.size();
				for (int i = 0; i < numDates; i++) {
					scheduleDate = new FerriesScheduleDateItem();
					JSONObject date = dates.get(i).isObject();
					scheduleDate.setDate(date.get("Date").isString().stringValue().substring(6, 19));
					
					JSONArray sailings = date.get("Sailings").isArray();
					
					int numSailings = sailings.size();
					for (int j=0; j < numSailings; j++) {
						terminal = new FerriesTerminalItem();
						JSONObject sailing = sailings.get(j).isObject();

						terminal.setArrivingTerminalID(Integer.parseInt(sailing.get("ArrivingTerminalID").toString()));
						terminal.setArrivingTerminalName(sailing.get("ArrivingTerminalName").isString().stringValue());
						terminal.setDepartingTerminalID(Integer.parseInt(sailing.get("DepartingTerminalID").toString()));
						terminal.setDepartingTerminalName(sailing.get("DepartingTerminalName").isString().stringValue());
						
						JSONArray annotations = sailing.get("Annotations").isArray();
						int numAnnotations = annotations.size();
						for (int k = 0; k < numAnnotations; k++) {
							notes = new FerriesAnnotationsItem();
							notes.setAnnotation(annotations.get(k).isString().stringValue());
							terminal.setAnnotations(notes);	
						}
						
						JSONArray times = sailing.get("Times").isArray();
						int numTimes = times.size();
						for (int l = 0; l < numTimes; l++) {
							JSONObject time = times.get(l).isObject();
							
                            // Don't display past sailing times. Doesn't make sense.
                            if (now.after(new Date(Long.parseLong(time
                                    .get("DepartingTime").isString().stringValue()
                                    .substring(6, 19))))) {
                                continue;
                            }							
							
							timesItem = new FerriesScheduleTimesItem();
							timesItem.setDepartingTime(time.get("DepartingTime").isString().stringValue().substring(6, 19));

							try {
								timesItem.setArrivingTime(time.get("ArrivingTime").isString().stringValue().substring(6, 19));	
							} catch (Exception e) {
								timesItem.setArrivingTime("N/A");
							}
							
							JSONArray annotationIndexes = time.get("AnnotationIndexes").isArray();
							int numIndexes = annotationIndexes.size();
							String annotation = "";
							for (int m=0; m < numIndexes; m++) {
								FerriesAnnotationsItem p = terminal.getAnnotations().get(Integer.parseInt(annotationIndexes.get(m).toString()));
								annotation += p.getAnnotation();

								timesItem.setAnnotations(annotation);
							}
							terminal.setScheduleTimes(timesItem);
						}
						scheduleDate.setFerriesTerminalItem(terminal);
					}
					scheduleDateItems.add(scheduleDate);
				}
				
				String terminalTitle = scheduleDateItems.get(0)
						.getFerriesTerminalItem().get(sailingsIndex)
						.getDepartingTerminalName()
						+ " to "
						+ scheduleDateItems.get(0)
								.getFerriesTerminalItem()
								.get(sailingsIndex)
								.getArrivingTerminalName(); 
				
				int numItems = scheduleDateItems.size();
				
				for (int i=0; i < numItems; i++) {
					daysOfWeek.add(scheduleDateItems.get(i).getDate());
		        }
				
				view.setTitle(terminalTitle);
		        view.renderDaysOfWeek(daysOfWeek);
				view.setDayOfWeekSelected(dayIndex);
				
                createFerryTerminalSailingSpaces();

			}
		});

	}

	protected void createFerryTerminalSailingSpaces() {
	    final String TERMINAL_SAILING_SPACE_URL = "http://www.wsdot.wa.gov/ferries/api/terminals/rest/terminalsailingspace?"
	            + "apiaccesscode={API_ACCESS_CODE}&callback";
        /** 
         * Check the cache table for the last time data was downloaded. If we are within
         * the allowed time period, don't sync, otherwise get fresh data from the server.
         */
        dbService.getCacheLastUpdated(Tables.FERRIES_TERMINAL_SAILING_SPACE, new ListCallback<GenericRow>() {

            @Override
            public void onFailure(DataServiceException error) {
            }

            @Override
            public void onSuccess(List<GenericRow> result) {
                boolean shouldUpdate = true;
                
                if (!result.isEmpty()) {
                    double now = System.currentTimeMillis();
                    double lastUpdated = result.get(0).getDouble(CachesColumns.CACHE_LAST_UPDATED);
                    shouldUpdate = (Math.abs(now - lastUpdated) > (15 * 1000)); // Refresh every 15 seconds.
                }
                
                if (shouldUpdate) {
                    JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
                    // Set timeout for 30 seconds (30000 milliseconds)
                    jsonp.setTimeout(30000);
                    jsonp.requestObject(
                            TERMINAL_SAILING_SPACE_URL,
                            new AsyncCallback<FerriesTerminalSailingSpaceFeed>() {

                                @Override
                                public void onFailure(Throwable caught) {
                                }

                                @Override
                                public void onSuccess(FerriesTerminalSailingSpaceFeed result) {                                   
                                    if (result.getTerminals() != null) {
                                        DateTimeFormat dateFormat = DateTimeFormat.getFormat("MMMM d, yyyy h:mm a");
                                        ferriesTerminalSailingSpaceItems.clear();
                                        FerriesTerminalSailingSpaceItem item;
                                        int numItems = result.getTerminals().length();
                                        
                                        for (int i = 0; i < numItems; i++) {
                                            item = new FerriesTerminalSailingSpaceItem();
                                            item.setTerminalId(result.getTerminals().get(i).getTerminalId());
                                            item.setTerminalName(result.getTerminals().get(i).getTerminalName());
                                            item.setTerminalAbbrev(result.getTerminals().get(i).getTerminalAbbrev());
                                            item.setTerminalDepartingSpaces(new JSONArray(result.getTerminals().get(i).getDepartingSpaces()).toString());
                                            item.setLastUpdated(dateFormat.format(new Date(System.currentTimeMillis())));
                                            
                                            ferriesTerminalSailingSpaceItems.add(item);
                                        }
                                        
                                        // Purge existing terminal items covered by incoming data
                                        dbService.deleteFerriesTerminalSailingSpace(new VoidCallback() {

                                            @Override
                                            public void onFailure(DataServiceException error) {
                                            }

                                            @Override
                                            public void onSuccess() {
                                                // Bulk insert all the new ferries terminal sailing space items
                                                dbService.insertFerriesTerminalSailingSpace(ferriesTerminalSailingSpaceItems, new RowIdListCallback() {

                                                    @Override
                                                    public void onFailure(DataServiceException error) {
                                                    }

                                                    @Override
                                                    public void onSuccess(List<Integer> rowIds) {
                                                        // Update the cache table with the time we did the update
                                                        List<CacheItem> cacheItems = new ArrayList<CacheItem>();
                                                        cacheItems.add(new CacheItem(Tables.FERRIES_TERMINAL_SAILING_SPACE, System.currentTimeMillis()));
                                                        dbService.updateCachesTable(cacheItems, new VoidCallback() {

                                                            @Override
                                                            public void onFailure(DataServiceException error) {
                                                            }

                                                            @Override
                                                            public void onSuccess() {
                                                                // Get available drive-up vehicle spaces.
                                                                dbService.getFerriesTerminalSailingSpace(
                                                                        scheduleDateItems
                                                                                .get(view.getDayOfWeekSelected())
                                                                                .getFerriesTerminalItem()
                                                                                .get(sailingsIndex)
                                                                                .getDepartingTerminalID().toString(),
                                                                        new ListCallback<GenericRow>() {

                                                                    @Override
                                                                    public void onFailure(DataServiceException error) {
                                                                    }

                                                                    @Override
                                                                    public void onSuccess(List<GenericRow> result) {
                                                                        getFerryTerminalSailingSpaces(result);
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
                    // Get available drive-up vehicle spaces.
                    dbService.getFerriesTerminalSailingSpace(
                            scheduleDateItems
                                    .get(view.getDayOfWeekSelected())
                                    .getFerriesTerminalItem()
                                    .get(sailingsIndex)
                                    .getDepartingTerminalID().toString(),
                            new ListCallback<GenericRow>() {

                        @Override
                        public void onFailure(DataServiceException error) {
                        }

                        @Override
                        public void onSuccess(List<GenericRow> result) {
                            getFerryTerminalSailingSpaces(result);
                        }

                    }); 
                }
            }
        });
       
    }

    private void getFerryTerminalSailingSpaces(List<GenericRow> result) {
        JSONValue value = JSONParser.parseStrict(result.get(0).getString(FerriesTerminalSailingSpaceColumns.TERMINAL_DEPARTING_SPACES));
        JSONArray departingSpaces = value.isArray();
        List<FerriesScheduleTimesItem> times = scheduleDateItems.get(view.getDayOfWeekSelected()).getFerriesTerminalItem().get(sailingsIndex).getScheduleTimes();
        DateTimeFormat dateFormat = DateTimeFormat.getFormat("hh:mm a");
        final TimeZoneConstants timeZoneConstants = GWT.create(TimeZoneConstants.class);
        final TimeZone usPacific = TimeZone.createTimeZone(TimeZoneInfo.buildTimeZoneData(timeZoneConstants.americaLosAngeles()));

        for (int i = 0; i < departingSpaces.size(); i++) {
            JSONObject spaces = departingSpaces.get(i).isObject();
            String departure = dateFormat.format(new Date(Long.parseLong(spaces.get("Departure").toString().substring(7, 20))));
            JSONArray spaceForArrivalTerminals = spaces.get("SpaceForArrivalTerminals").isArray();
            
            for (int j=0; j < spaceForArrivalTerminals.size(); j++) {
                JSONObject terminals = spaceForArrivalTerminals.get(j).isObject();
                if (Integer.parseInt(terminals.get("TerminalID").toString()) != scheduleDateItems.get(view.getDayOfWeekSelected()).getFerriesTerminalItem().get(sailingsIndex).getArrivingTerminalID()) {
                    continue;
                } else {
                    int driveUpSpaceCount = Integer.parseInt(terminals.get("DriveUpSpaceCount").toString());
                    int maxSpaceCount = Integer.parseInt(terminals.get("MaxSpaceCount").toString());
                    
                    for (FerriesScheduleTimesItem time: times) {
                        if (dateFormat.format(new Date(Long.parseLong(time.getDepartingTime()))).equals(departure)) {
                            time.setDriveUpSpaceCount(driveUpSpaceCount);
                            time.setMaxSpaceCount(maxSpaceCount);
                            time.setLastUpdated(result.get(0).getString(FerriesTerminalSailingSpaceColumns.TERMINAL_LAST_UPDATED));
                        }
                    }
                }
            }
        }
        
        view.hideProgressBar();
        view.render(scheduleDateItems
                .get(view.getDayOfWeekSelected())
                .getFerriesTerminalItem().get(sailingsIndex)
                .getScheduleTimes());
        
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
