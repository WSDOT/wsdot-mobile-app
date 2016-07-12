/*
 * Copyright (c) 2015 Washington State Department of Transportation
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

public class FerriesRouteFeed extends JavaScriptObject {
	protected FerriesRouteFeed() {}

	public final native JsArray<Routes> getRoutes() /*-{ return this }-*/;
	
	static public class Routes extends JavaScriptObject {
		protected Routes() {}
		
		public final native JsArray<Date> getDate() /*-{ return this.Date }-*/;
		public final native JsArray<RouteAlert> getRouteAlert() /*-{ return this.RouteAlert }-*/;
		public final native String getCrossingTime() /*-{ return this.CrossingTime }-*/;
		//public final native String getDate() /*-{ return this.Date }-*/;
		//public final native String getRouteAlert() /*-{ return this.RouteAlert }-*/;
		public final native int getRouteID() /*-{ return this.RouteID }-*/;
		public final native String getDescription() /*-{ return this.Description }-*/;
		public final native String getCacheDate() /*-{ return this.CacheDate }-*/;

	}
	
	/* Date Array */
	static public class Date extends JavaScriptObject {
		protected Date() {}
	}
	
	/* RouteAlert Array */
	static public class RouteAlert extends JavaScriptObject {
		protected RouteAlert() {}
		//public final native int getBulletinID() /*-{ return this.BulletinID }-*/;
		//public final native String getPublishDate() /*-{ return this.PublishDate }-*/;
		//public final native String getAlertDescription() /*-{ return this.AlertDescription }-*/;
		//public final native String getAlertFullTitle() /*-{ return this.AlertFullTitle }-*/;
		//public final native String getAlertFullText() /*-{ return this.AlertFullText }-*/;
	}
}
