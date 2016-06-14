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

package gov.wa.wsdot.mobile.client.util;

public final class Consts {

	public static final String HOST_URL = "http://www.wsdot.wa.gov";
	public static final boolean ANALYTICS_ENABLED = false;

	/**
	 * The Event Category title in Google Analytics.
	 */
	public static final String EVENT_ACCESSIBILITY = "Accessibility";
	/**
	 * Current version number and build of the app.
	 */

	public static final String APP_VERSION = "4.2.1";

    /**
     * Ad unit Id for banner
     */
    public static final String AD_UNIT_ID = "/6499/example/banner";

	/**
	 * Unique Tracking Id from Property Settings in Google Analytics
	 */
	public static final String ANALYTICS_TRACKING_ID = "UA-XXXXXX-XX";

	/**
	 * IDs for the different alert types for TrafficAlertsActivity
	 */
	public static final int BLOCKING = 0;
	public static final int CONSTRUCTION = 1;
	public static final int CLOSURES = 2;

	public static final int SPECIAL = 3;
	public static final int AMBER = 24;
	
	/**
	 * The caller references the constants using <tt>Consts.EMPTY_STRING</tt>,
	 * and so on. Thus, the caller should be prevented from constructing objects of
	 * this class, by declaring this private constructor.
	 */	
	private Consts() {
		//this prevents even the native class from 
		//calling this ctor as well :
		throw new AssertionError();
	}

}
