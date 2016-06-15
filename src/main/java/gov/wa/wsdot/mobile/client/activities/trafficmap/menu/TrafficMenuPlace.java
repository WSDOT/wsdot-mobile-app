package gov.wa.wsdot.mobile.client.activities.trafficmap.menu;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class TrafficMenuPlace extends Place {

    public static class TrafficMenuPlaceTokenizer implements
            PlaceTokenizer<TrafficMenuPlace> {

        @Override
        public TrafficMenuPlace getPlace(String token) {
            return new TrafficMenuPlace();
        }

        @Override
        public String getToken(TrafficMenuPlace place) {
            return "";
        }

    }

}