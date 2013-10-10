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

public class VesselWatchFeed extends JavaScriptObject {
	protected VesselWatchFeed() {}

	public final native JsArray<VesselList> getVesselList() /*-{ return this.vessellist }-*/;

	static public class VesselList extends JavaScriptObject {
		protected VesselList() {}
		
		public final native int getId() /*-{ return this.id }-*/;
		public final native String getInService() /*-{ return this.inservice }-*/;
		public final native int getVesselID() /*-{ return this.vesselID }-*/;
		public final native int getHead() /*-{ return this.head }-*/;
		public final native String getRoute() /*-{ return this.route }-*/;
		public final native String getLastDock() /*-{ return this.lastdock }-*/;
		public final native String getATerm() /*-{ return this.aterm }-*/;
		public final native String getLeftDock() /*-{ return this.leftdock }-*/;
		public final native String getLeftDockAMPM() /*-{ return this.leftdockAMPM }-*/;
		public final native double getLat() /*-{ return this.lat }-*/;
		public final native double getLon() /*-{ return this.lon }-*/;
		public final native String getName() /*-{ return this.name }-*/;
		public final native String getNextDep() /*-{ return this.nextdep }-*/;
		public final native String getNextDepAMPM() /*-{ return this.nextdepAMPM }-*/;
		public final native String getEta() /*-{ return this.eta }-*/;
		public final native String getEtaAMPM() /*-{ return this.etaAMPM }-*/;
		public final native String getHeadTxt() /*-{ return this.headtxt }-*/;
		public final native double getSpeed() /*-{ return this.speed }-*/;

	}
	
}
