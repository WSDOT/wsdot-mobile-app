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

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class AmtrakCascadesPlace extends Place {
	
	public static class AmtrakCascadesPlaceTokenizer implements
			PlaceTokenizer<AmtrakCascadesPlace> {

		@Override
		public AmtrakCascadesPlace getPlace(String token) {
			return new AmtrakCascadesPlace();
		}

		@Override
		public String getToken(AmtrakCascadesPlace place) {
			return "";
		}

	}
}