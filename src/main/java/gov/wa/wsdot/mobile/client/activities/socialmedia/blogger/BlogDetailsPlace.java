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

package gov.wa.wsdot.mobile.client.activities.socialmedia.blogger;

import gov.wa.wsdot.mobile.shared.BlogItem;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class BlogDetailsPlace extends Place {
	
	private static BlogItem item;
	
	public BlogDetailsPlace(BlogItem item) {
		BlogDetailsPlace.item = item;
	}
	
	public BlogItem getBlogItem() {
		return item;
	}

	public static class BlogDetailsPlaceTokenizer implements
			PlaceTokenizer<BlogDetailsPlace> {

		@Override
		public BlogDetailsPlace getPlace(String token) {
			return new BlogDetailsPlace(item);
		}

		@Override
		public String getToken(BlogDetailsPlace place) {
			return "";
		}

	}
}