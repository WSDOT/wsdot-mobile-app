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

public class HighwayAlerts extends JavaScriptObject {
	protected HighwayAlerts() {}

	public final native Alerts getAlerts() /*-{ return this.alerts }-*/;

	static public class Alerts extends JavaScriptObject {
		protected Alerts() {}
		
		public final native JsArray<Items> getItems() /*-{ return this.items }-*/;

	}
	
	static public class Items extends JavaScriptObject {
		protected Items() {}
		
		public final native String getPriority() /*-{ return this.Priority }-*/;
		public final native String getLastUpdatedTime() /*-{ return this.LastUpdatedTime }-*/;
		public final native int getAlertID() /*-{ return this.AlertID }-*/;
		public final native String getHeadlineDescription() /*-{ return this.HeadlineDescription }-*/;
		public final native String getEventCategory() /*-{ return this.EventCategory }-*/;
		public final native StartRoadwayLocation getStartRoadwayLocation() /*-{ return this.StartRoadwayLocation }-*/;
	}
	
	static public class StartRoadwayLocation extends JavaScriptObject {
		protected StartRoadwayLocation() {}
		
		public final native double getLatitude() /*-{ return this.Latitude }-*/;
		public final native double getLongitude() /*-{ return this.Longitude }-*/;
		public final native String getRoadName() /*-{ return this.RoadName }-*/;
	}
	
}
