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

package gov.wa.wsdot.mobile.client.activities.trafficmap.traveltimes;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class TravelTimesPlace extends Place {

	public static class TravelTimesPlaceTokenizer implements
			PlaceTokenizer<TravelTimesPlace> {

		@Override
		public TravelTimesPlace getPlace(String token) {
			return new TravelTimesPlace();
		}

		@Override
		public String getToken(TravelTimesPlace place) {
			return "";
		}

	}

}