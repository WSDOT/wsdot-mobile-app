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

package gov.wa.wsdot.mobile.client.activities.amtrakcascades;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.activities.amtrakcascades.schedules.AmtrakCascadesSchedulesPlace;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.shared.AmtrakCascadesStationItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.gwtphonegap.client.geolocation.GeolocationCallback;
import com.googlecode.gwtphonegap.client.geolocation.Position;
import com.googlecode.gwtphonegap.client.geolocation.PositionError;
import com.googlecode.gwtphonegap.client.notification.AlertCallback;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;

public class AmtrakCascadesActivity extends MGWTAbstractActivity implements
    AmtrakCascadesView.Presenter {

	private ClientFactory clientFactory;
	private AmtrakCascadesView view;
	private EventBus eventBus;
	private PhoneGap phoneGap;
    private List<AmtrakCascadesStationItem> amtrakStationItems = new ArrayList<AmtrakCascadesStationItem>();
    private DateTimeFormat dateFormat = DateTimeFormat.getFormat("MMMM d, yyyy h:mm a");
	
	public AmtrakCascadesActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getAmtrakCascadesView();
		this.eventBus = eventBus;
		phoneGap = clientFactory.getPhoneGap();
		view.setPresenter(this);
		
        view.showProgressIndicator();
		getDaysOfWeek();
		getAmtrakStations();
		getCurrentLocation();

		panel.setWidget(view);

	}

	private void getDaysOfWeek() {
	    List<String> daysOfWeek = new ArrayList<String>();
	    
	    Date startDate = new Date();
	    
	    for (int i = 0; i < 7; i++) {
	        Date nextDay = new Date(startDate.getTime() + i * 24 * 3600 * 1000);
	        daysOfWeek.add(dateFormat.format(nextDay));
	    }
	    
	    view.renderDaysOfWeek(daysOfWeek);
    }

    private void getAmtrakStations() {
	    amtrakStationItems.add(new AmtrakCascadesStationItem("VAC", "Vancouver, BC", 1, 49.2737293, -123.0979175));
	    amtrakStationItems.add(new AmtrakCascadesStationItem("BEL", "Bellingham, WA", 2, 48.720423, -122.5109386));
	    amtrakStationItems.add(new AmtrakCascadesStationItem("MVW", "Mount Vernon, WA", 3, 48.4185923, -122.334973));
	    amtrakStationItems.add(new AmtrakCascadesStationItem("STW", "Stanwood, WA", 4, 48.2417732, -122.3495322));
	    amtrakStationItems.add(new AmtrakCascadesStationItem("EVR", "Everett, WA", 5, 47.975512, -122.197854));
	    amtrakStationItems.add(new AmtrakCascadesStationItem("EDM", "Edmonds, WA", 6, 47.8111305, -122.3841639));
	    amtrakStationItems.add(new AmtrakCascadesStationItem("SEA", "Seattle, WA", 7, 47.6001899, -122.3314322));
	    amtrakStationItems.add(new AmtrakCascadesStationItem("TUK", "Tukwila, WA", 8, 47.461079, -122.242693));
	    amtrakStationItems.add(new AmtrakCascadesStationItem("TAC", "Tacoma, WA", 9, 47.2419939, -122.4205623));
	    amtrakStationItems.add(new AmtrakCascadesStationItem("OLW", "Olympia/Lacey, WA", 10, 46.9913576, -122.793982));
	    amtrakStationItems.add(new AmtrakCascadesStationItem("CTL", "Centralia, WA", 11, 46.7177596, -122.9528291));
	    amtrakStationItems.add(new AmtrakCascadesStationItem("KEL", "Kelso/Longview, WA", 12, 46.1422504, -122.9132438));
	    amtrakStationItems.add(new AmtrakCascadesStationItem("VAN", "Vancouver, WA", 13, 45.6294472, -122.685568));
	    amtrakStationItems.add(new AmtrakCascadesStationItem("PDX", "Portland, OR", 14, 45.528639, -122.676284));
	    amtrakStationItems.add(new AmtrakCascadesStationItem("ORC", "Oregon City, OR", 15, 45.3659422, -122.5960671));
	    amtrakStationItems.add(new AmtrakCascadesStationItem("SLM", "Salem, OR", 16, 44.9323665, -123.0281591));
	    amtrakStationItems.add(new AmtrakCascadesStationItem("ALY", "Albany, OR", 17, 44.6300975, -123.1041787));
	    amtrakStationItems.add(new AmtrakCascadesStationItem("EUG", "Eugene, OR", 18, 44.055506, -123.094523));

	    Collections.sort(amtrakStationItems, AmtrakCascadesStationItem.stationNameComparator);
    }

    private void getCurrentLocation() {
        phoneGap.getGeolocation().getCurrentPosition(new GeolocationCallback() {

            @Override
            public void onSuccess(Position position) {
                double latitude = position.getCoordinates().getLatitude();
                double longitude = position.getCoordinates().getLongitude();

                getDistanceFromStation(latitude, longitude);
                view.hideProgressIndicator();
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
                    
                        view.renderFromLocation(amtrakStationItems);
                        view.renderToLocation(amtrakStationItems);
                        view.hideProgressIndicator();
                        
                        break;
                    default:
                        view.renderFromLocation(amtrakStationItems);
                        view.renderToLocation(amtrakStationItems);
                        view.hideProgressIndicator();
                        
                        break;
                }

            }
        });        
    }

    /**
     * Haversine formula
     * 
     * Provides great-circle distances between two points on a sphere from their longitudes and latitudes
     * 
     * http://en.wikipedia.org/wiki/Haversine_formula
     * 
     * @param latitude
     * @param longitude
     */
    protected void getDistanceFromStation(double latitude, double longitude) {
        for (AmtrakCascadesStationItem station: amtrakStationItems) {
            double earthRadius = 3958.75; // miles
            double dLat = Math.toRadians(station.getLatitude() - latitude);
            double dLng = Math.toRadians(station.getLongitude() - longitude);
            double sindLat = Math.sin(dLat / 2);
            double sindLng = Math.sin(dLng / 2);
            double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                    * Math.cos(Math.toRadians(latitude))
                    * Math.cos(Math.toRadians(station.getLatitude()));
            
            double c = 2 * Math.asin(Math.sqrt(a));
            int distance = (int) Math.round(earthRadius * c);

            station.setDistance(distance);
        }
        
        view.setLocationEnabled(true);
        view.renderFromLocation(amtrakStationItems);
        view.renderToLocation(amtrakStationItems);
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
    public void onSubmitButtonPressed() {
        String statusDate = view.getDayOfWeekSelected();
        String fromLocation = view.getFromLocationSelected();
        String toLocation = view.getToLocationSelected();
        
        if (fromLocation.equalsIgnoreCase("NA")) {
            phoneGap.getNotification().alert(
                    "Please select a point of origin from the selection box.",
                    new AlertCallback() {

                        @Override
                        public void onOkButtonClicked() {
                            // TODO Auto-generated method stub
                        }
                    }, "Point of origin needed");
        } else if (fromLocation.equalsIgnoreCase(toLocation)) {
            // User picked the same destination as the origin. Just ignore it.
            toLocation = "NA";
            clientFactory.getPlaceController().goTo(
                    new AmtrakCascadesSchedulesPlace(statusDate, fromLocation,
                            toLocation));
        } else {
            clientFactory.getPlaceController().goTo(
                    new AmtrakCascadesSchedulesPlace(statusDate, fromLocation,
                            toLocation));
        }
    }

}