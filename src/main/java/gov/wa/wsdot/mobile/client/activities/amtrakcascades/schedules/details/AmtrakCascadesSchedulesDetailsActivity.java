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

package gov.wa.wsdot.mobile.client.activities.amtrakcascades.schedules.details;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.client.plugins.accessibility.Accessibility;
import gov.wa.wsdot.mobile.client.util.Consts;
import gov.wa.wsdot.mobile.shared.AmtrakCascadesScheduleFeed;
import gov.wa.wsdot.mobile.shared.AmtrakCascadesScheduleItem;
import gov.wa.wsdot.mobile.shared.AmtrakCascadesServiceItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowStandardHandler;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowStandardHandler.PullActionHandler;

public class AmtrakCascadesSchedulesDetailsActivity extends MGWTAbstractActivity
        implements AmtrakCascadesSchedulesDetailsView.Presenter {

    private ClientFactory clientFactory;
    private AmtrakCascadesSchedulesDetailsView view;
    private EventBus eventBus;
    private String statusDate;
    private Accessibility accessibility;
    private static Map<String, AmtrakCascadesScheduleItem> stationItems;
    private static List<Map<String, AmtrakCascadesScheduleItem>> locationItems;
    private static List<AmtrakCascadesServiceItem> serviceItems = new ArrayList<AmtrakCascadesServiceItem>();
    private static Map<Integer, String> trainNumberMap = new HashMap<Integer, String>();
    private static Map<String, String> amtrakStations = new HashMap<String, String>();
    private static final String AMTRAK_CASCADES_SCHEDULE_URL = Consts.HOST_URL + "/traveler/api/amtrakcascades/schedule";
    
    public AmtrakCascadesSchedulesDetailsActivity(ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }
    
    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        view = clientFactory.getAmtrakCascadesDeparturesView();
        this.eventBus = eventBus;
        accessibility = clientFactory.getAccessibility();
        view.setPresenter(this);
        final Place place = clientFactory.getPlaceController().getWhere();
        
        getTrainNumbers();
        getAmtrakStations();
        
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
                        if (place instanceof AmtrakCascadesSchedulesDetailsPlace) {
                            AmtrakCascadesSchedulesDetailsPlace amtrakCascadesSchedulesPlace = (AmtrakCascadesSchedulesDetailsPlace) place;
                            statusDate = amtrakCascadesSchedulesPlace.getStatusDate();
                            String fromLocation = amtrakCascadesSchedulesPlace.getFromLocation();
                            String toLocation = amtrakCascadesSchedulesPlace.getToLocation();

                            createTopicsList(statusDate, fromLocation, toLocation);
                        }
                        view.refresh();
                        callback.onSuccess(null);
                    }                    
                }.schedule(1);
            }
            
        });

        if (place instanceof AmtrakCascadesSchedulesDetailsPlace) {
            AmtrakCascadesSchedulesDetailsPlace amtrakCascadesSchedulesPlace = (AmtrakCascadesSchedulesDetailsPlace) place;
            statusDate = amtrakCascadesSchedulesPlace.getStatusDate();
            String fromLocation = amtrakCascadesSchedulesPlace.getFromLocation();
            String toLocation = amtrakCascadesSchedulesPlace.getToLocation();

            view.setHeaderPullHandler(headerHandler);
            createTopicsList(statusDate, fromLocation, toLocation);
            
            panel.setWidget(view);
            accessibility.postScreenChangeNotification();
        }
    }

    private void getTrainNumbers() {
        trainNumberMap.put(7, "Empire Builder Train");
        trainNumberMap.put(8, "Empire Builder Train");
        trainNumberMap.put(11, "Coast Starlight Train");
        trainNumberMap.put(14, "Coast Starlight Train");
        trainNumberMap.put(27, "Empire Builder Train");
        trainNumberMap.put(28, "Empire Builder Train");
        trainNumberMap.put(500, "Amtrak Cascades Train");
        trainNumberMap.put(501, "Amtrak Cascades Train");
        trainNumberMap.put(502, "Amtrak Cascades Train");
        trainNumberMap.put(503, "Amtrak Cascades Train");
        trainNumberMap.put(504, "Amtrak Cascades Train");
        trainNumberMap.put(505, "Amtrak Cascades Train");
        trainNumberMap.put(506, "Amtrak Cascades Train");
        trainNumberMap.put(507, "Amtrak Cascades Train");
        trainNumberMap.put(508, "Amtrak Cascades Train");
        trainNumberMap.put(509, "Amtrak Cascades Train");
        trainNumberMap.put(510, "Amtrak Cascades Train");
        trainNumberMap.put(511, "Amtrak Cascades Train");
        trainNumberMap.put(513, "Amtrak Cascades Train");
        trainNumberMap.put(516, "Amtrak Cascades Train");
        trainNumberMap.put(517, "Amtrak Cascades Train");
    }
    
    private void getAmtrakStations() {
        amtrakStations.put("VAC", "Vancouver, BC");
        amtrakStations.put("BEL", "Bellingham, WA");
        amtrakStations.put("MVW", "Mount Vernon, WA");
        amtrakStations.put("STW", "Stanwood, WA");
        amtrakStations.put("EVR", "Everett, WA");
        amtrakStations.put("EDM", "Edmonds, WA");
        amtrakStations.put("SEA", "Seattle, WA");
        amtrakStations.put("TUK", "Tukwila, WA");
        amtrakStations.put("TAC", "Tacoma, WA");
        amtrakStations.put("OLW", "Olympia/Lacey, WA");
        amtrakStations.put("CTL", "Centralia, WA");
        amtrakStations.put("KEL", "Kelso/Longview, WA");
        amtrakStations.put("VAN", "Vancouver, WA");
        amtrakStations.put("PDX", "Portland, OR");
        amtrakStations.put("ORC", "Oregon City, OR");
        amtrakStations.put("SLM", "Salem, OR");
        amtrakStations.put("ALY", "Albany, OR");
        amtrakStations.put("EUG", "Eugene, OR");
    }

    /**
     * Check URL parameters and decide which way we are accessing the API.
     *
     * @param scheduleDate date of requested schedule
     * @param fromLocation departing station name
     * @param toLocation arriving station name
     */
    private void createTopicsList(String scheduleDate, final String fromLocation, final String toLocation) {
        String url = AMTRAK_CASCADES_SCHEDULE_URL + "/" + scheduleDate + "/-1/" + fromLocation + "/" + toLocation;

        view.showProgressIndicator();
        
        if (!toLocation.equalsIgnoreCase("NA")) {
            getDepartingArrivingTrains(url, fromLocation, toLocation);
        } else {
            getDepartingTrains(url, fromLocation, toLocation);
        }
    }

    /**
     * Get train schedules for those with a departing and arriving station.
     * 
     * @param url URL of the Web services API
     * @param toLocation arriving station name
     * @param fromLocation departing station name
     */
    private void getDepartingArrivingTrains(String url, final String fromLocation, final String toLocation) {
        JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
        jsonp.setTimeout(30000); // Set timeout for 30 seconds
        jsonp.requestObject(url, new AsyncCallback<AmtrakCascadesScheduleFeed>() {

            @Override
            public void onFailure(Throwable caught) {
                view.hideProgressIndicator();
            }

            @Override
            public void onSuccess(AmtrakCascadesScheduleFeed result) {
                if (result.getSchedule() != null) {
                    serviceItems.clear();
                    AmtrakCascadesScheduleItem scheduleItem;
                    int numItems = result.getSchedule().length();

                    int i = 0;
                    int startingTripNumber = 0;
                    int currentTripNumber = 0;
                    while (i < numItems) { // Loop through all trains
                        Date scheduledDepartureTime = null;
                        locationItems = new ArrayList<Map<String, AmtrakCascadesScheduleItem>>();
                        stationItems = new HashMap<String, AmtrakCascadesScheduleItem>();

                        startingTripNumber = result.getSchedule().get(i).getTripNumber();
                        currentTripNumber = startingTripNumber;
                        List<String> trainNameList = new ArrayList<String>();
                        int tripCounter = 0;
                        while (currentTripNumber == startingTripNumber && i < numItems) { // Trains are grouped by two or more 
                            scheduleItem = new AmtrakCascadesScheduleItem();

                            if (result.getSchedule().get(i).getArrivalComment() != null) {
                                scheduleItem.setArrivalComment(result
                                        .getSchedule().get(i)
                                        .getArrivalComment());
                            }

                            if (result.getSchedule().get(i).getArrivalScheduleType() != null) {
                                scheduleItem.setArrivalScheduleType(result
                                        .getSchedule()
                                        .get(i)
                                        .getArrivalScheduleType());
                            }

                            if (result.getSchedule().get(i).getArrivalTime() != null) {
                                scheduleItem.setArrivalTime(result
                                        .getSchedule().get(i)
                                        .getArrivalTime().toString()
                                        .substring(6, 19));
                            }

                            if (result.getSchedule().get(i).getDepartureComment() != null) {
                                scheduleItem.setDepartureComment(result
                                        .getSchedule().get(i)
                                        .getDepartureComment());
                            }

                            if (result.getSchedule().get(i).getDepartureScheduleType() != null) {
                                scheduleItem.setDepartureScheduleType(result
                                        .getSchedule()
                                        .get(i)
                                        .getDepartureScheduleType());
                            }

                            if (result.getSchedule().get(i).getDepartureTime() != null) {
                                scheduleItem.setDepartureTime(result
                                        .getSchedule().get(i)
                                        .getDepartureTime().toString()
                                        .substring(6, 19));
                            }

                            if (result.getSchedule().get(i).getScheduledArrivalTime() != null) {
                                scheduleItem.setScheduledArrivalTime(result
                                        .getSchedule()
                                        .get(i)
                                        .getScheduledArrivalTime()
                                        .toString()
                                        .substring(6, 19));
                            }

                            scheduleItem.setStationName(result.getSchedule().get(i).getStationName());
                            
                            if (result.getSchedule().get(i).getTrainMessage() != "") {
                                scheduleItem.setTrainMessage(result
                                        .getSchedule().get(i)
                                        .getTrainMessage());
                            }

                            if (result.getSchedule().get(i).getScheduledDepartureTime() != null) {
                                scheduleItem.setScheduledDepartureTime(result
                                        .getSchedule()
                                        .get(i)
                                        .getScheduledDepartureTime()
                                        .toString()
                                        .substring(6, 19));
                                
                                // We sort by scheduled departure time of the From station.
                                if (fromLocation.equalsIgnoreCase(scheduleItem.getStationName())) {
                                    scheduledDepartureTime = new Date(
                                            Long.parseLong((scheduleItem
                                                    .getScheduledDepartureTime())));
                                }
                            }

                            int trainNumber = result.getSchedule().get(i).getTrainNumber();
                            scheduleItem.setTrainNumber(trainNumber);
                            String serviceName = trainNumberMap.get(trainNumber);

                            if (serviceName == null) {
                                serviceName = "Bus Service";
                            }

                            scheduleItem.setSortOrder(result.getSchedule().get(i).getSortOrder());
                            String trainName = trainNumber + " " + serviceName;
                            
                            // Add the train name for ever other record. When there is one orgin and destination point
                            // the train name will be the same. If the tripNumber is the same over more than two records
                            // then we have multiple origin and destination points and likely different train names.
                            // e.g. 515 Amtrak Cascades Train, 8911 Bus Service
                            if (tripCounter % 2 == 0) {
                                trainNameList.add(trainName);
                            }
                            scheduleItem.setTrainName(trainName);
                            scheduleItem.setTripNumber(result.getSchedule().get(i).getTripNumber());
                            scheduleItem.setUpdateTime(result
                                    .getSchedule().get(i)
                                    .getUpdateTime().toString()
                                    .substring(6, 19));

                            stationItems.put(scheduleItem.getStationName(), scheduleItem);

                            i++;
                            if (i < numItems) {
                                currentTripNumber = result.getSchedule().get(i).getTripNumber();
                            }

                            tripCounter++;
                        }

                        if (trainNameList.size() > 1) {
                            StringBuilder sb = new StringBuilder();
                            for (String s: trainNameList) {
                                if (sb.length() > 0) sb.append(", ");
                                sb.append(s);
                            }
                            stationItems.get(fromLocation).setTrainName(sb.toString());
                        }

                        locationItems.add(stationItems);
                        serviceItems.add(new AmtrakCascadesServiceItem(scheduledDepartureTime, locationItems));

                    }
                }

                Collections.sort(serviceItems, AmtrakCascadesServiceItem.scheduledDepartureTimeComparator);
                
                view.hideProgressIndicator();
                view.setFromToLocation(fromLocation, toLocation);
                view.setTitle("Departing: "
                        + amtrakStations.get(fromLocation)
                        + " and Arriving: "
                        + amtrakStations.get(toLocation));
                view.render(serviceItems);
                
                view.refresh();
            }
        });        
    }
    
    /**
     * Get train schedules for those with just a departing station.
     * 
     * @param url URL of the Web services API
     * @param toLocation arriving station name
     * @param fromLocation departing station name
     */
    private void getDepartingTrains(String url, final String fromLocation, final String toLocation) {
        JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
        jsonp.setTimeout(30000); // Set timeout for 30 seconds
        jsonp.requestObject(url, new AsyncCallback<AmtrakCascadesScheduleFeed>() {

            @Override
            public void onFailure(Throwable caught) {
                view.hideProgressIndicator();
            }

            @Override
            public void onSuccess(AmtrakCascadesScheduleFeed result) {
                if (result.getSchedule() != null) {
                    serviceItems.clear();
                    AmtrakCascadesScheduleItem scheduleItem;
                    int numItems = result.getSchedule().length();
                    
                    for (int i = 0; i < numItems; i++) { // Loop through all trains
                        Date scheduledTime = null;
                        locationItems = new ArrayList<Map<String, AmtrakCascadesScheduleItem>>();
                        stationItems = new HashMap<String, AmtrakCascadesScheduleItem>();
                        scheduleItem = new AmtrakCascadesScheduleItem();

                        if (result.getSchedule().get(i).getArrivalComment() != null) {
                            scheduleItem.setArrivalComment(result
                                    .getSchedule().get(i)
                                    .getArrivalComment());
                        }

                        if (result.getSchedule().get(i).getArrivalScheduleType() != null) {
                            scheduleItem.setArrivalScheduleType(result
                                    .getSchedule().get(i)
                                    .getArrivalScheduleType());
                        }

                        if (result.getSchedule().get(i).getArrivalTime() != null) {
                            scheduleItem.setArrivalTime(result
                                    .getSchedule().get(i)
                                    .getArrivalTime().toString()
                                    .substring(6, 19));
                        }

                        if (result.getSchedule().get(i).getDepartureComment() != null) {
                            scheduleItem.setDepartureComment(result
                                    .getSchedule().get(i)
                                    .getDepartureComment());
                        }

                        if (result.getSchedule().get(i).getDepartureScheduleType() != null) {
                            scheduleItem.setDepartureScheduleType(result
                                    .getSchedule().get(i)
                                    .getDepartureScheduleType());
                        }

                        if (result.getSchedule().get(i).getDepartureTime() != null) {
                            scheduleItem.setDepartureTime(result
                                    .getSchedule().get(i)
                                    .getDepartureTime().toString()
                                    .substring(6, 19));
                        }

                        scheduleItem.setStationName(result.getSchedule().get(i).getStationName());

                        if (result.getSchedule().get(i).getTrainMessage() != "") {
                            scheduleItem.setTrainMessage(result
                                    .getSchedule().get(i)
                                    .getTrainMessage());
                        }

                        if (result.getSchedule().get(i).getScheduledArrivalTime() != null) {
                            scheduleItem.setScheduledArrivalTime(result
                                    .getSchedule().get(i)
                                    .getScheduledArrivalTime()
                                    .toString().substring(6, 19));
                            
                            if (fromLocation.equalsIgnoreCase(scheduleItem.getStationName())) {
                                scheduledTime = new Date(
                                        Long.parseLong((scheduleItem
                                                .getScheduledArrivalTime())));
                            }
                        }

                        if (result.getSchedule().get(i).getScheduledDepartureTime() != null) {
                            scheduleItem.setScheduledDepartureTime(result
                                    .getSchedule()
                                    .get(i)
                                    .getScheduledDepartureTime()
                                    .toString()
                                    .substring(6, 19));
                            
                            // We sort by scheduled departure time of the From station.
                            if (fromLocation.equalsIgnoreCase(scheduleItem.getStationName())) {
                                scheduledTime = new Date(
                                        Long.parseLong((scheduleItem
                                                .getScheduledDepartureTime())));
                            }
                        }

                        int trainNumber = result.getSchedule().get(i).getTrainNumber();
                        scheduleItem.setTrainNumber(trainNumber);
                        String serviceName = trainNumberMap.get(trainNumber);
                        if (serviceName == null) {
                            serviceName = "Bus Service";
                        }
                        scheduleItem.setTrainName(trainNumber + " " + serviceName);
                        scheduleItem.setTripNumber(result.getSchedule().get(i).getTripNumber());
                        scheduleItem.setUpdateTime(result.getSchedule()
                                .get(i).getUpdateTime().toString()
                                .substring(6, 19));
                        
                        stationItems.put(scheduleItem.getStationName(), scheduleItem);
                        
                        locationItems.add(stationItems);
                        serviceItems.add(new AmtrakCascadesServiceItem(scheduledTime, locationItems));
                    }
                }
                
                Collections.sort(serviceItems, AmtrakCascadesServiceItem.scheduledDepartureTimeComparator);
                
                view.hideProgressIndicator();
                String location = fromLocation;
                view.setFromToLocation(fromLocation, location);
                view.setTitle(amtrakStations.get(fromLocation));
                view.render(serviceItems);
                
                view.refresh();
            }
        });
    }
    
    @Override
    public void onStop() {
        view.setPresenter(null);
    }

    @Override
    public void onBackButtonPressed() {
        ActionEvent.fire(eventBus, ActionNames.BACK);        
    }

    @Override
    public void onItemSelected(int index) {
        /*
        AmtrakCascadesServiceItem item = serviceItems.get(index);
        
        // Only register the click if the cell is selectable.
        if (item.getLocation().get(0).size() > 2) {
            clientFactory.getPlaceController().goTo(
                    new AmtrakCascadesSchedulesDetailsPlace("X", item.getLocation()));
        }
        */
    }

}
