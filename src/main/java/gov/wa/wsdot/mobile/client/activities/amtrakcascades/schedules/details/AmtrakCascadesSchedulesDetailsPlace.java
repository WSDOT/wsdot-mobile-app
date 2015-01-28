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

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class AmtrakCascadesSchedulesDetailsPlace extends Place {
	
    private static String statusDate;
    private String fromLocation;
    private static String toLocation;
    
    /**
     *  
     * @param statusDate
     * @param fromLocation
     * @param toLocation
     */
	public AmtrakCascadesSchedulesDetailsPlace(String statusDate, String fromLocation,
            String toLocation) {
	    
	    AmtrakCascadesSchedulesDetailsPlace.statusDate = statusDate;
	    this.fromLocation = fromLocation;
	    AmtrakCascadesSchedulesDetailsPlace.toLocation = toLocation;
    }
	
	public String getStatusDate() {
	    return statusDate;
	}
	
	public String getFromLocation() {
	    return fromLocation;
	}
	
	public String getToLocation() {
	    return toLocation;
	}

    public static class AmtrakCascadesSchedulesDetailsPlaceTokenizer implements
			PlaceTokenizer<AmtrakCascadesSchedulesDetailsPlace> {

		@Override
		public AmtrakCascadesSchedulesDetailsPlace getPlace(String token) {
			return new AmtrakCascadesSchedulesDetailsPlace(token, statusDate, toLocation);
		}

		@Override
		public String getToken(AmtrakCascadesSchedulesDetailsPlace place) {
			return place.getFromLocation();
		}

	}
}