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

public class VesselWatchFeed extends JavaScriptObject {
	protected VesselWatchFeed() {}

	public final native Vessel get(int i) /*-{ return this[i] }-*/;
	
	public final native int length() /*-{ return this.length }-*/;
		
	public static class Vessel extends JavaScriptObject {
		protected Vessel() {}
	
		public final native boolean getInService() /*-{ return this.InService }-*/;
		public final native int getVesselID() /*-{ return this.VesselID }-*/;
		public final native int getHead() /*-{ return this.Heading }-*/;
		public final native String getRoute() /*-{ return this.OpRouteAbbrev[0] }-*/;
		public final native String getLastDock() /*-{ return this.DepartingTerminalName }-*/;
		public final native String getATerm() /*-{ return this.ArrivingTerminalName }-*/;
		public final native String getLeftDock() /*-{ return this.LeftDock}-*/;
		public final native double getLat() /*-{ return this.Latitude }-*/;
		public final native double getLon() /*-{ return this.Longitude }-*/;
		public final native String getName() /*-{ return this.VesselName }-*/;
		public final native String getNextDep() /*-{ return this.ScheduledDeparture }-*/;
		public final native String getEta() /*-{ return this.Eta }-*/;
		public final native double getSpeed() /*-{ return this.Speed }-*/;
	}

	
}