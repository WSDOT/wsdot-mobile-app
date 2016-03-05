/*
 * Copyright (c) 2016 Washington State Department of Transportation
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

public class AnalyticsCordovaImpl implements Analytics {

    private boolean initialized;

    @Override
    public void initialize() {
        if (!testForPlugin()) {
            throw new IllegalStateException("cannot find Analytics plugin - did you include analytics.js?");
        }
        initialized = true;
    }
    
    private native boolean testForPlugin() /*-{
		if (!$wnd.analytics) {
			return false;
		}
		return true;
    }-*/;

    @Override
    public void startTrackerWithId(String id) {
        if (!initialized) {
            throw new IllegalStateException("you have to initialize Analytics plugin before using it");
        }
        startTrackerWithIdNative(id);
    }

    private native void startTrackerWithIdNative(String id) /*-{
        $wnd.analytics.startTrackerWithId(id)
    }-*/;
    
    @Override
    public void trackScreen(String screen) {
        if (!initialized) {
            throw new IllegalStateException("you have to initialize Analytics plugin before using it");
        }
        trackScreenNative(screen);
    }

    private native void trackScreenNative(String screen) /*-{
        $wnd.analytics.trackView(screen);
    }-*/;
    
    @Override
    public void trackEvent(String category, String action, String label) {
        if (!initialized) {
            throw new IllegalStateException("you have to initialize Analytics plugin before using it");
        }
        trackEventNative(category, action, label);
    }

    private native void trackEventNative(String category, String action, String label) /*-{
        $wnd.analytics.trackEvent(category, action, label);
    }-*/;

}
