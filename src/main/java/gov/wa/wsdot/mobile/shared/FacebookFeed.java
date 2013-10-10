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

package gov.wa.wsdot.mobile.shared;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class FacebookFeed extends JavaScriptObject {
	protected FacebookFeed() {}

	public final native JsArray<Posts> getPosts() /*-{ return this }-*/;
	
	static public class Posts extends JavaScriptObject {
		protected Posts() {}
		
		public final native String getMessage() /*-{ return this.message }-*/;
		public final native String getCreatedAt() /*-{ return this.created_at }-*/;
		public final native String getId() /*-{ return this.id }-*/;
		public final native String getType() /*-{ return this.type }-*/;
		public final native String getLink() /*-{ return this.link }-*/;

	}
	
}
