/*
 * Copyright (c) 2015 Washington State Department of Transportation
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.gwtphonegap.client.notification.AlertCallback;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowStandardHandler;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowStandardHandler.PullActionHandler;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.activities.camera.CameraPlace;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.client.plugins.analytics.Analytics;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.CachesColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.CamerasColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.FerriesSchedulesColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.FerriesTerminalSailingSpaceColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService.Tables;
import gov.wa.wsdot.mobile.client.util.Consts;
import gov.wa.wsdot.mobile.shared.CacheItem;
import gov.wa.wsdot.mobile.shared.CameraItem;
import gov.wa.wsdot.mobile.shared.CamerasFeed;
import gov.wa.wsdot.mobile.shared.FerriesAnnotationsItem;
import gov.wa.wsdot.mobile.shared.FerriesScheduleDateItem;
import gov.wa.wsdot.mobile.shared.FerriesScheduleTimesItem;
import gov.wa.wsdot.mobile.shared.FerriesTerminalItem;
import gov.wa.wsdot.mobile.shared.FerriesTerminalSailingSpaceFeed;
import gov.wa.wsdot.mobile.shared.FerriesTerminalSailingSpaceItem;

public class FerriesRouteDeparturesActivity extends
		MGWTAbstractActivity implements
		FerriesRouteDeparturesView.Presenter {

	private final ClientFactory clientFactory;
	private FerriesRouteDeparturesView view;
	private EventBus eventBus;
	private PhoneGap phoneGap;
	private WSDOTDataService dbService;
	private static ArrayList<String> daysOfWeek = new ArrayList<String>();
    private static ArrayList<FerriesScheduleDateItem> scheduleDateItems = new ArrayList<FerriesScheduleDateItem>();
    private static List<FerriesTerminalSailingSpaceItem> ferriesTerminalSailingSpaceItems = new ArrayList<FerriesTerminalSailingSpaceItem>();
    private static Map<Integer, FerriesTerminalItem> ferriesTerminalMap = new HashMap<Integer, FerriesTerminalItem>();
    private static List<Integer> starred = new ArrayList<Integer>();
    private static List<CameraItem> cameraItems = new ArrayList<CameraItem>();
	private static String routeId;
	private static int sailingsIndex;
	private static int terminalId;
	private static final String TERMINAL_SAILING_SPACE_URL = Consts.HOST_URL + "/traveler/api/ferries/terminalsailingspace";
	private static final String CAMERAS_URL = Consts.HOST_URL + "/traveler/api/cameras";
	
	public FerriesRouteDeparturesActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getFerriesRouteDeparturesView();
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
                        createDepartureTimesList(routeId, 0, sailingsIndex);
                        view.refresh();
                        callback.onSuccess(null);
                    }
                    
                }.schedule(1);
            }
            
        });
        
        createTerminalLocations();
		
		Place place = clientFactory.getPlaceController().getWhere();
		if (place instanceof FerriesRouteDeparturesPlace) {
            FerriesRouteDeparturesPlace ferriesRouteSchedulesDayDeparturesPlace = (FerriesRouteDeparturesPlace) place;
			routeId = ferriesRouteSchedulesDayDeparturesPlace.getId();
			sailingsIndex = ferriesRouteSchedulesDayDeparturesPlace.getIndex();
			terminalId = ferriesRouteSchedulesDayDeparturesPlace.getTerminalId();
            view.setHeaderPullHandler(headerHandler);
			createDepartureTimesList(routeId, 0, sailingsIndex);
			panel.setWidget(view);
			if (Consts.ANALYTICS_ENABLED) {
				Analytics.trackScreen("/Ferries/Schedules/Sailings/Departures");
			}
		}
	}
	
	private void createTerminalLocations() {
	    ferriesTerminalMap.put(1, new FerriesTerminalItem(1, "Anacortes", 48.507351, -122.677));
	    ferriesTerminalMap.put(3, new FerriesTerminalItem(3, "Bainbridge Island", 47.622339, -122.509617));
	    ferriesTerminalMap.put(4, new FerriesTerminalItem(4, "Bremerton", 47.561847, -122.624089));
	    ferriesTerminalMap.put(5, new FerriesTerminalItem(5, "Clinton", 47.9754, -122.349581));
	    ferriesTerminalMap.put(11, new FerriesTerminalItem(11, "Coupeville", 48.159008, -122.672603));
	    ferriesTerminalMap.put(8, new FerriesTerminalItem(8, "Edmonds", 47.813378, -122.385378));
	    ferriesTerminalMap.put(9, new FerriesTerminalItem(9, "Fauntleroy", 47.5232, -122.3967));
	    ferriesTerminalMap.put(10, new FerriesTerminalItem(10, "Friday Harbor", 48.535783, -123.013844));
	    ferriesTerminalMap.put(12, new FerriesTerminalItem(12, "Kingston", 47.794606, -122.494328));
	    ferriesTerminalMap.put(13, new FerriesTerminalItem(13, "Lopez Island", 48.570928, -122.882764));
	    ferriesTerminalMap.put(14, new FerriesTerminalItem(14, "Mukilteo", 47.949544, -122.304997));
	    ferriesTerminalMap.put(15, new FerriesTerminalItem(15, "Orcas Island", 48.597333, -122.943494));
	    ferriesTerminalMap.put(16, new FerriesTerminalItem(16, "Point Defiance", 47.306519, -122.514053));
	    ferriesTerminalMap.put(17, new FerriesTerminalItem(17, "Port Townsend", 48.110847, -122.759039));
	    ferriesTerminalMap.put(7, new FerriesTerminalItem(7, "Seattle", 47.602501, -122.340472));
	    ferriesTerminalMap.put(18, new FerriesTerminalItem(18, "Shaw Island", 48.584792, -122.92965));
	    ferriesTerminalMap.put(19, new FerriesTerminalItem(19, "Sidney B.C.", 48.643114, -123.396739));
	    ferriesTerminalMap.put(20, new FerriesTerminalItem(20, "Southworth", 47.513064, -122.495742));
	    ferriesTerminalMap.put(21, new FerriesTerminalItem(21, "Tahlequah", 47.331961, -122.507786));
	    ferriesTerminalMap.put(22, new FerriesTerminalItem(22, "Vashon Island", 47.51095, -122.463639));
	}

	@Override
	public void onDayOfWeekSelected(int position) {
	    createDepartureTimesList(routeId, position, sailingsIndex);
	}
	
	private void createDepartureTimesList(String routeId, final int dayIndex, final int sailingsIndex) {

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
				
				view.showProgressIndicator();
				
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

				createFerryTerminalCameras();
			}
		});

	}

	protected void createFerryTerminalSailingSpaces() {
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
                
                view.showProgressIndicator();

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
                                    view.hideProgressIndicator();
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
        if (result.size() != 0) {
            JSONValue value = JSONParser.parseStrict(result.get(0).getString(FerriesTerminalSailingSpaceColumns.TERMINAL_DEPARTING_SPACES));
            JSONArray departingSpaces = value.isArray();
            List<FerriesScheduleTimesItem> times = scheduleDateItems.get(view.getDayOfWeekSelected()).getFerriesTerminalItem().get(sailingsIndex).getScheduleTimes();
            DateTimeFormat dateFormat = DateTimeFormat.getFormat("MMMM d, yyyy h:mm a");
            final TimeZoneConstants timeZoneConstants = GWT.create(TimeZoneConstants.class);
            final TimeZone usPacific = TimeZone.createTimeZone(TimeZoneInfo.buildTimeZoneData(timeZoneConstants.americaLosAngeles()));
    
            for (int i = 0; i < departingSpaces.size(); i++) {
                JSONObject spaces = departingSpaces.get(i).isObject();
                String departure = dateFormat.format(new Date(Long.parseLong(spaces.get("Departure").toString().substring(7, 20))));
                JSONArray spaceForArrivalTerminals = spaces.get("SpaceForArrivalTerminals").isArray();
                
                for (int j=0; j < spaceForArrivalTerminals.size(); j++) {
                    JSONObject terminals = spaceForArrivalTerminals.get(j).isObject();
                    if (Integer.parseInt(terminals.get("TerminalID")
                            .toString()) != scheduleDateItems
                                    .get(view.getDayOfWeekSelected())
                                    .getFerriesTerminalItem().get(sailingsIndex)
                                    .getArrivingTerminalID()) {
                        continue;
                    } else {
                        JSONBoolean displayDriveUpSpace = terminals.get("DisplayDriveUpSpace").isBoolean();
                        boolean showIndicator = displayDriveUpSpace.booleanValue();
                        if (showIndicator) {
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
            }
        }
        
        view.hideProgressIndicator();
        view.render(scheduleDateItems
                .get(view.getDayOfWeekSelected())
                .getFerriesTerminalItem().get(sailingsIndex)
                .getScheduleTimes());
        
        view.refresh();

    }
	
    private void createFerryTerminalCameras() {
        
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
                                                                getFerryTerminalCameras();
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
                    getFerryTerminalCameras();
                }
            }
        });
    }
    
    private void getFerryTerminalCameras() {
        dbService.getCameras(new ListCallback<GenericRow>() {

            @Override
            public void onFailure(DataServiceException error) {
            }

            @Override
            public void onSuccess(List<GenericRow> result) {
                cameraItems.clear();
                int numRows = result.size();

                view.showProgressIndicator();

                for (int i = 0; i < numRows; i++) {
                    if (result.get(i).getString(CamerasColumns.CAMERA_ROAD_NAME).toLowerCase() == "ferries") {
                        int distance = getDistanceFromTerminal(terminalId,
                                result.get(i).getDouble(CamerasColumns.CAMERA_LATITUDE),
                                result.get(i).getDouble(CamerasColumns.CAMERA_LONGITUDE));

                        // If less than a mile from terminal, show the camera
                        if (distance < 5280) { // in feet
                            CameraItem camera = new CameraItem();
                            camera.setCameraId(result.get(i).getInt(CamerasColumns.CAMERA_ID));
                            camera.setTitle(result.get(i).getString(CamerasColumns.CAMERA_TITLE));
                            camera.setImageUrl(result.get(i).getString(CamerasColumns.CAMERA_URL));
                            camera.setLatitude(result.get(i).getDouble(CamerasColumns.CAMERA_LATITUDE));
                            camera.setLongitude(result.get(i).getDouble(CamerasColumns.CAMERA_LONGITUDE));
                            camera.setHasVideo(result.get(i).getInt(CamerasColumns.CAMERA_HAS_VIDEO));
                            camera.setDistance(distance);

                            cameraItems.add(camera);
                        }
                    }
                }
                
                // If we've already removed the tab don't try and do it again
                if (cameraItems.size() == 0 && view.getTabCount() != 1) {
                    view.removeTab(1);
                } else {
                    Collections.sort(cameraItems, CameraItem.cameraDistanceComparator);
                    view.renderCameras(cameraItems);
                    view.refreshCameras();
                }
                
                createFerryTerminalSailingSpaces();
            }
        });
    }

    @Override
    public void onCameraSelected(int index) {
        CameraItem item = cameraItems.get(index);
        clientFactory.getPlaceController().goTo(
                new CameraPlace(Integer.toString(item.getCameraId())));
    }

    /**
     * Haversine formula
     * 
     * Provides great-circle distances between two points on a sphere from
     * their longitudes and latitudes.
     * 
     * http://en.wikipedia.org/wiki/Haversine_formula
     * 
     * @param latitude
     * @param longitude
     */
    protected int getDistanceFromTerminal(int terminalId, double latitude, double longitude) {
        FerriesTerminalItem terminal = ferriesTerminalMap.get(terminalId);
        double earthRadius = 20902200; // feet
        double dLat = Math.toRadians(terminal.getLatitude() - latitude);
        double dLng = Math.toRadians(terminal.getLongitude() - longitude);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(latitude))
                * Math.cos(Math.toRadians(terminal.getLatitude()));
        
        double c = 2 * Math.asin(Math.sqrt(a));
        int distance = (int) Math.round(earthRadius * c);
        
        return distance;
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
