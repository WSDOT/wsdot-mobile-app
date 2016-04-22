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

package gov.wa.wsdot.mobile.client.activities.trafficmap.trafficincidents;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.activities.alert.AlertPlace;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.CachesColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.HighwayAlertsColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService.Tables;
import gov.wa.wsdot.mobile.client.util.Consts;
import gov.wa.wsdot.mobile.shared.CacheItem;
import gov.wa.wsdot.mobile.shared.HighwayAlertItem;
import gov.wa.wsdot.mobile.shared.HighwayAlerts;
import gov.wa.wsdot.mobile.shared.LatLonItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.Map.Entry;
import java.util.Iterator;
import java.util.HashMap;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

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
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowStandardHandler;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowStandardHandler.PullActionHandler;

public class TrafficAlertsActivity extends MGWTAbstractActivity implements
		TrafficAlertsView.Presenter {

	private final ClientFactory clientFactory;
	private TrafficAlertsView view;
	private EventBus eventBus;
	private WSDOTDataService dbService;
	
	private static ArrayList<HighwayAlertItem> highwayAlertItems = new ArrayList<HighwayAlertItem>();
	private static ArrayList<HighwayAlertItem> alertsToAdd = new ArrayList<HighwayAlertItem>();
	
    private static final String HIGHWAY_ALERTS_URL = Consts.HOST_URL + "/traveler/api/highwayalerts";
    private static DateTimeFormat dateFormat = DateTimeFormat.getFormat("MMMM d, yyyy h:mm a");
    
    private static LatLngBounds bounds;
    
	private ArrayList<HighwayAlertItem> amberAlerts = new ArrayList<HighwayAlertItem>();
	private ArrayList<HighwayAlertItem> blockingIncidents = new ArrayList<HighwayAlertItem>();
	private ArrayList<HighwayAlertItem> constructionClosures = new ArrayList<HighwayAlertItem>();
	private ArrayList<HighwayAlertItem> trafficClosures = new ArrayList<HighwayAlertItem>();
	private ArrayList<HighwayAlertItem> specialEvents = new ArrayList<HighwayAlertItem>();
	
	public TrafficAlertsActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getTrafficAlertsView();
		this.eventBus = eventBus;
		dbService = clientFactory.getDbService();
		view.setPresenter(this);
		
		Place place = clientFactory.getPlaceController().getWhere();
		
		if (place instanceof TrafficAlertsPlace) {
			TrafficAlertsPlace alertPlace = (TrafficAlertsPlace) place;
			bounds = alertPlace.getBounds();
		}
		
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
						getHighwayAlerts();							
						view.refresh();
						callback.onSuccess(null);
					}
					
				}.schedule(1);
			}
			
		});
		
		view.setHeaderPullHandler(headerHandler);
		
		view.showProgressIndicator();
		getHighwayAlerts();
		view.hideProgressIndicator();
		panel.setWidget(view);

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
                            	alertsToAdd.clear();
                                
                                if (result.getAlerts() != null) {
                                    HighwayAlertItem item;
                                    int size = result.getAlerts().getItems().length();
                                    
                                    for (int i = 0; i < size; i++) {
                                        item = new HighwayAlertItem();
                                        
                                        item.setAlertId(result.getAlerts().getItems().get(i).getAlertID());
                                        item.setHeadlineDescription(result.getAlerts().getItems().get(i).getHeadlineDescription());
                                        item.setEventCategory(result.getAlerts().getItems().get(i).getEventCategory());
                                        item.setStartLatitude(result.getAlerts().getItems().get(i).getStartRoadwayLocation().getLatitude());
                                        item.setStartLongitude(result.getAlerts().getItems().get(i).getStartRoadwayLocation().getLongitude());
                                        item.setLastUpdatedTime(dateFormat.format(new Date(
                                                        Long.parseLong(result
                                                                .getAlerts()
                                                                .getItems()
                                                                .get(i)
                                                                .getLastUpdatedTime()
                                                                .substring(6, 19)))));

                                        alertsToAdd.add(item);
                                    }
                                    
                                    // Purge existing highway alerts covered by incoming data
                                    dbService.deleteHighwayAlerts(new VoidCallback() {

                                        @Override
                                        public void onFailure(DataServiceException error) {
                                        }

                                        @Override
                                        public void onSuccess() {
                                            // Bulk insert all the new highway alerts
                                            dbService.insertHighwayAlerts(alertsToAdd, new RowIdListCallback() {

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
                                        					addAlerts();
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
                	addAlerts();
                }
            }
        });
    }
	
	private void addAlerts() {
		
		highwayAlertItems.clear();
		
		dbService.getHighwayAlerts(new ListCallback<GenericRow>() {

			@Override
			public void onFailure(DataServiceException error) {
			}

			@Override
			public void onSuccess(List<GenericRow> result) {
				
				LatLng nePoint = bounds.getNorthEast();
				LatLng swPoint = bounds.getSouthWest();

                ArrayList<LatLonItem> viewableMapArea = new ArrayList<LatLonItem>();
                viewableMapArea.add(new LatLonItem(nePoint.getLatitude(), swPoint.getLongitude()));
                viewableMapArea.add(new LatLonItem(nePoint.getLatitude(), nePoint.getLongitude()));
                viewableMapArea.add(new LatLonItem(swPoint.getLatitude(), nePoint.getLongitude()));
                viewableMapArea.add(new LatLonItem(swPoint.getLatitude(), swPoint.getLongitude()));

				for (GenericRow alert: result) {
                    if (inPolygon(
                            viewableMapArea,
                            alert.getDouble(HighwayAlertsColumns.HIGHWAY_ALERT_LATITUDE),
                            alert.getDouble(HighwayAlertsColumns.HIGHWAY_ALERT_LONGITUDE)) || 
                    		alert.getString(HighwayAlertsColumns.HIGHWAY_ALERT_CATEGORY) == "amber alert") {
                        
                    	HighwayAlertItem item = new HighwayAlertItem();
                    	item.setAlertId(alert.getInt(HighwayAlertsColumns.HIGHWAY_ALERT_ID));
                    	item.setHeadlineDescription(alert.getString(HighwayAlertsColumns.HIGHWAY_ALERT_HEADLINE));
                    	item.setEventCategory(alert.getString(HighwayAlertsColumns.HIGHWAY_ALERT_CATEGORY));
                    	item.setStartLatitude(alert.getDouble(HighwayAlertsColumns.HIGHWAY_ALERT_LATITUDE));
                    	item.setStartLongitude(alert.getDouble(HighwayAlertsColumns.HIGHWAY_ALERT_LONGITUDE));
                    	item.setLastUpdatedTime(alert.getString(HighwayAlertsColumns.HIGHWAY_ALERT_LAST_UPDATED));
                    	
                    	highwayAlertItems.add(item);				        
				    }
				}
				
				if (!result.isEmpty()) {
					categorizeAlerts();
					view.refresh();
				}
			}
		});
	}

	private void categorizeAlerts() {

		Stack<HighwayAlertItem> amberalert = new Stack<HighwayAlertItem>();
		Stack<HighwayAlertItem> blocking = new Stack<HighwayAlertItem>();
    	Stack<HighwayAlertItem> construction = new Stack<HighwayAlertItem>();
    	Stack<HighwayAlertItem> closures = new Stack<HighwayAlertItem>();
    	Stack<HighwayAlertItem> special = new Stack<HighwayAlertItem>();
    	
		amberAlerts.clear();
		blockingIncidents.clear();
		constructionClosures.clear();
		trafficClosures.clear();
		specialEvents.clear();
    	
    	for (HighwayAlertItem item : highwayAlertItems) {
			
    		int category = getCategoryID(item.getEventCategory());
    		
    		// Check if there is an active amber alert
			if (category == Consts.AMBER) {
				amberalert.push(item);
			}
			else if (category == Consts.BLOCKING) {
				blocking.push(item);
			}
            else if (category == Consts.CONSTRUCTION) {
                construction.push(item);
            }
            else if (category == Consts.CLOSURES) {
                closures.push(item);
            }
            else if (category == Consts.SPECIAL) {
                special.push(item);
            }
    	}
    	
    	
    	if (amberalert != null && amberalert.size() != 0) {
    		while (!amberalert.empty()) {
    			amberAlerts.add(amberalert.pop());
    		}
    		view.showAmberAlerts();
    		view.renderAmberAlerts(amberAlerts);
    	} else {
    	    view.hideAmberAlerts();
    	}
    	
		if (blocking.empty()) {
			blockingIncidents.add(new HighwayAlertItem(0, "None reported"));
		} else {
			while (!blocking.empty()) {
				blockingIncidents.add(blocking.pop());
			}					
		}
		view.renderBlocking(blockingIncidents);
		
		if (construction.empty()) {
			constructionClosures.add(new HighwayAlertItem(0, "None reported"));
		} else {
			while (!construction.empty()) {
				constructionClosures.add(construction.pop());
			}					
		}
	
		view.renderConstruction(constructionClosures);

		if (closures.empty()) {
			trafficClosures.add(new HighwayAlertItem(0, "None reported"));
		} else {
			while (!closures.empty()) {
				trafficClosures.add(closures.pop());
			}					
		}
		view.renderClosure(trafficClosures);

		if (special.empty()) {
			specialEvents.add(new HighwayAlertItem(0, "None reported"));
		} else {
			while (!special.empty()) {
				specialEvents.add(special.pop());
			}					
		}
		view.renderSpecial(specialEvents);
    	
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
	public void onItemSelected(int alertType, int index) {
		
		int alertId = 0;
		
		switch (alertType){
			case Consts.BLOCKING:
				alertId = blockingIncidents.get(index).getAlertId();
				break;
			case Consts.CONSTRUCTION:
				alertId = constructionClosures.get(index).getAlertId();
				break;
			case Consts.CLOSURES:
				alertId = trafficClosures.get(index).getAlertId();
				break;
			case Consts.SPECIAL:
				alertId = specialEvents.get(index).getAlertId();
				break;
			default:
				//TODO Shouldn't get here, if we do treat as if nothing was clicked
		}
		if (alertId != 0)	
			clientFactory.getPlaceController().goTo(
				new AlertPlace(Integer.toString(alertId)));
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
	
	/**
	 * 
	 * Maps a category name to one of four possible types. These four IDs
	 * represent the four lists displayed by this activity.
	 * 
	 * @param category The name of the category of alert
	 * @return category ID
	 */
	private int getCategoryID(String category) {
        // Types of categories
        String[] event_closure = {"closed", "closure"};
        String[] event_construction = {"construction", "maintenance", "lane closure"};
        String[] event_amber = {"amber"};
        String[] event_special = {"special event"};

        HashMap<String, String[]> eventCategories = new HashMap<>();
        eventCategories.put("closure", event_closure);
        eventCategories.put("construction", event_construction);
        eventCategories.put("amber", event_amber);
        eventCategories.put("special", event_special);

		Set<Entry<String, String[]>> set = eventCategories.entrySet();
		Iterator<Entry<String, String[]>> i = set.iterator();

        if (category.equals("")) return Consts.BLOCKING;

        while(i.hasNext()) {
        	Entry<String, String[]> me = i.next();
            for (String phrase: me.getValue()) {
            	String patternStr = phrase;
				RegExp pattern = RegExp.compile(patternStr, "i");
				MatchResult matcher = pattern.exec(category);
                boolean matchFound = matcher != null;
                if (matchFound) {
                    String keyWord = me.getKey();
                    if (keyWord.equalsIgnoreCase("closure")) {
                        return Consts.CLOSURES;
                    } else if (keyWord.equalsIgnoreCase("construction")) {
                        return Consts.CONSTRUCTION;
                    } else if (keyWord.equalsIgnoreCase("special")) {
                        return Consts.SPECIAL;    
                    } else if (keyWord.equalsIgnoreCase("amber")){
                        return Consts.AMBER;
                    }
                }
            }
        }
        return Consts.BLOCKING;
    }
	
}