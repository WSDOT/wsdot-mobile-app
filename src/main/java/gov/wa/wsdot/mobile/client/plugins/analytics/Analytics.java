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


package gov.wa.wsdot.mobile.client.plugins.analytics;

/**
 * This class is a wrapper for the google-analytics-plugin for Cordova. 
 * 
 *  @see <a href="https://github.com/danwilson/google-analytics-plugin">https://github.com/danwilson/google-analytics-plugin</a>
 */
public class Analytics {
	
	public static native void startTracker() /*-{
		$wnd.analytics.startTrackerWithId('INSERT_GA_TRACKING_ID_HERE')
	}-*/;
	
	public static native void trackScreen(String name) /*-{
		$wnd.analytics.trackView(name);
	}-*/;
	
	public static native void trackEvent(String category, String action, String label) /*-{
		$wnd.analytics.trackEvent(category, action, label);
	}-*/;


}
