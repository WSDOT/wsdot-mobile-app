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

package gov.wa.wsdot.mobile.client.util;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Window;
import com.googlecode.mgwt.ui.client.MGWT;


public class ParserUtils {

	public static String relativeTime(String createdAt, String datePattern, boolean isUTC) {
		DateTimeFormat parseDateFormat = DateTimeFormat.getFormat(datePattern);
		Date parseDate;

		try {
			if (isUTC) {
				parseDate = parseDateFormat.parse(parseDateFormat.format(
						parseDateFormat.parse(createdAt),
						TimeZone.createTimeZone(0)));
			} else {		
				parseDate = parseDateFormat.parse(createdAt);
			}
		} catch (IllegalArgumentException e) {
			return "Unavailable";
		}

		return getRelative(parseDate);

	}

	public static String relativeTime(Date createdAt) { 
		return(getRelative(createdAt));
	}

	private static String getRelative(Date date) {
		DateTimeFormat displayDateFormat = DateTimeFormat.getFormat("MMMM d, yyyy h:mm a");	
		int delta = 0;

		try {
			Date relativeDate = new Date();
			delta = (int)((relativeDate.getTime() - date.getTime()) / 1000); // convert to seconds
			if (delta < 60) {
				return delta + " seconds ago";
			} else if (delta < 120) {
				return "1 minute ago";
			} else if (delta < (60*60)) {
				return Integer.toString(delta / 60) + " minutes ago";
			} else if (delta < (120*60)) {
				return "1 hour ago";
			} else if (delta < (24*60*60)) {
				return Integer.toString(delta / 3600) + " hours ago";
			} else {
				return displayDateFormat.format(date);
			}
		} catch (Exception e) {
			return "Unavailable";
		}		

	}

	public static String ellipsis(String text, int max) {

	    if (text.length() <= max) {
	        return text;
	    }

	    int end = text.lastIndexOf(' ', max - 1); // Chop at last word.

	    return text.substring(0, end) + "...";
	}
	
    public static int iOSversion() {
        int version = 0;
        // supports iOS 2.0 and later: <http://bit.ly/TJjs1V>
        RegExp regExp = RegExp.compile("OS (\\d+)_(\\d+)_?(\\d+)?");
        MatchResult matcher = regExp.exec(Window.Navigator.getAppVersion());
        boolean matchFound = regExp.test(Window.Navigator.getAppVersion());

        if (matchFound) {
            version = Integer.parseInt(matcher.getGroup(1), 10); // Just get the
                                                                 // major
                                                                 // version.
        }

        return version;
    }
    
    public static int windowUI() {
        int amount = 136; // 86 for nav bars, 50 for bottom ad.
        if (MGWT.getOsDetection().isIOs()) {
            if (iOSversion() >= 7) {
                //amount = 111;
            }
        }
        
        return amount;
    }
    
    /**
     * Returns a singlular or pluralized word.
     * 
     * @param count count to base if the word should be treated as singular or plural
     * @param singular single version of the word
     * @param plural plural version of the word
     * @return pluralized String
     */
    public static String pluralize(int count, String singular, String plural) {
        return (count == 1 ? singular : plural);
    }

}
