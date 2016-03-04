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

import com.googlecode.gwtphonegap.client.plugins.PhoneGapPlugin;

/**
 * This class is a wrapper for the google-analytics-plugin for Cordova. 
 * <p>
 * The implementation is based on
 *  <a href="https://github.com/danwilson/google-analytics-plugin/blob/master/www/analytics.js">analytics.js</a>
 */
public interface Analytics extends PhoneGapPlugin {
	
    /**
     * Set up Analytics tracker
     *
     * @param id
     */
    public void startTrackerWithId(String id);

    /**
     * Track a Screen (PageView)
     *
     * @param screen
     */
    public void trackScreen(String screen);

    /**
     * Track an Event
     *
     * @param category
     * @param action
     * @param label
     */
    public void trackEvent(String category, String action, String label);

}
