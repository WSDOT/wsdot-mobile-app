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
import com.google.gwt.core.client.JsArrayInteger;

public class MountainPassConditions extends JavaScriptObject {
	protected MountainPassConditions() {}

	public final native GetMountainPassConditionsResult getMountainPassConditionsResult() /*-{ return this.GetMountainPassConditionsResult }-*/;

	static public class GetMountainPassConditionsResult extends JavaScriptObject {
		protected GetMountainPassConditionsResult() {}
		
		public final native JsArray<PassCondition> getPassCondition() /*-{ return this.PassCondition }-*/;

	}
	
	static public class PassCondition extends JavaScriptObject {
		protected PassCondition() {}
		
		public final native JsArray<Forecast> getForecast() /*-{ return this.Forecast }-*/;
		public final native String getWeatherCondition() /*-{ return this.WeatherCondition }-*/;
		public final native int getElevationInFeet() /*-{ return this.ElevationInFeet }-*/;
		public final native double getLongitude() /*-{ return this.Longitude }-*/;
		public final native int getMountainPassId() /*-{ return this.MountainPassId }-*/;
		public final native JsArray<Cameras> getCameras() /*-{ return this.Cameras }-*/;
		public final native String getRoadCondition() /*-{ return this.RoadCondition }-*/;

		public final native Integer getTemperatureInFahrenheit() /*-{
			return this.TemperatureInFahrenheit ? @java.lang.Integer::valueOf(I)(this.TemperatureInFahrenheit) : null;
		}-*/;
		
		public final native double getLatitude() /*-{ return this.Latitude }-*/;
		public final native JsArrayInteger getDateUpdated() /*-{ return this.DateUpdated }-*/;
		public final native String getMountainPassName() /*-{ return this.MountainPassName }-*/;
		public final native RestrictionOne getRestrictionOne() /*-{ return this.RestrictionOne }-*/;
		public final native RestrictionTwo getRestrictionTwo() /*-{ return this.RestrictionTwo }-*/;

	}
	
	static public class Forecast extends JavaScriptObject {
		protected Forecast() {}
		
		public final native String getDay() /*-{ return this.Day }-*/;
		public final native String getForecastText() /*-{ return this.ForecastText }-*/;
		public final native String getWeatherIcon() /*-{ return this.WeatherIcon }-*/;
		public final native String setWeatherIcon(String icon) /*-{ return this.WeatherIcon = icon }-*/;
	}
	
	static public class Cameras extends JavaScriptObject {
		protected Cameras() {}
		
		public final native String getUrl() /*-{ return  this.url }-*/;
		public final native double getLat() /*-{ return this.lat }-*/;
		public final native double getLon() /*-{ return this.lon }-*/;
		public final native int getId() /*-{ return this.id }-*/;
		public final native String getTitle() /*-{ return this.title }-*/;
	}
	
	static public class RestrictionOne extends JavaScriptObject {
		protected RestrictionOne() {}
		
		public final native String getRestrictionText() /*-{ return this.RestrictionText }-*/;
		public final native String getTravelDirection() /*-{ return this.TravelDirection }-*/;
	}

	static public class RestrictionTwo extends JavaScriptObject {
		protected RestrictionTwo() {}
		
		public final native String getRestrictionText() /*-{ return this.RestrictionText }-*/;
		public final native String getTravelDirection() /*-{ return this.TravelDirection }-*/;
	}
	
}
