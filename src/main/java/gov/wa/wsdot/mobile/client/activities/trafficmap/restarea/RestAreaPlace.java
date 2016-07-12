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

package gov.wa.wsdot.mobile.client.activities.trafficmap.restarea;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class RestAreaPlace extends Place {

    private final String id;

    public RestAreaPlace(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static class AlertPlaceTokenizer implements
            PlaceTokenizer<RestAreaPlace> {

        @Override
        public RestAreaPlace getPlace(String token) {
            return new RestAreaPlace(token);
        }

        @Override
        public String getToken(RestAreaPlace place) {
            return place.getId();
        }

    }
}