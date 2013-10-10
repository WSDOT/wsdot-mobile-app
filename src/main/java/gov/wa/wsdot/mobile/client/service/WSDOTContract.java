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

package gov.wa.wsdot.mobile.client.service;

public interface WSDOTContract {

	interface CachesColumns {
		String _ID = "_ID";
		String CACHE_TABLE_NAME = "cache_table_name";
		String CACHE_LAST_UPDATED = "cache_last_updated";
	}
	
	interface CamerasColumns {
		String _ID = "_ID";
	    String CAMERA_ID = "id";
	    String CAMERA_TITLE = "title";
	    String CAMERA_URL = "url";
	    String CAMERA_LATITUDE = "latitude";
	    String CAMERA_LONGITUDE = "longitude";
	    String CAMERA_HAS_VIDEO = "has_video";
	    String CAMERA_ROAD_NAME = "road_name";
	    String CAMERA_IS_STARRED = "is_starred";
	}
	
	interface HighwayAlertsColumns {
		String _ID = "_ID";
		String HIGHWAY_ALERT_ID = "highway_alert_id";
		String HIGHWAY_ALERT_HEADLINE = "highway_alert_headline";
		String HIGHWAY_ALERT_LATITUDE = "highway_alert_latitude";
		String HIGHWAY_ALERT_LONGITUDE = "highway_alert_longitude";
		String HIGHWAY_ALERT_CATEGORY = "highway_alert_category";
		String HIGHWAY_ALERT_PRIORITY = "highway_alert_priority";
		String HIGHWAY_ALERT_ROAD_NAME = "highway_alert_road_name";
		String HIGHWAY_ALERT_LAST_UPDATED = "highway_alert_last_updated";
	}

	interface MountainPassesColumns {
		String _ID = "_ID";
		String MOUNTAIN_PASS_ID = "id";
		String MOUNTAIN_PASS_NAME = "name";
		String MOUNTAIN_PASS_WEATHER_CONDITION = "weather_condition";
		String MOUNTAIN_PASS_ELEVATION = "elevation";
		String MOUNTAIN_PASS_TRAVEL_ADVISORY_ACTIVE = "travel_advisory_active";
		String MOUNTAIN_PASS_ROAD_CONDITION = "road_condition";
		String MOUNTAIN_PASS_TEMPERATURE = "temperature";
		String MOUNTAIN_PASS_DATE_UPDATED = "date_updated";
		String MOUNTAIN_PASS_RESTRICTION_ONE = "restriction_one";
		String MOUNTAIN_PASS_RESTRICTION_ONE_DIRECTION = "restriction_one_direction";
		String MOUNTAIN_PASS_RESTRICTION_TWO = "restriction_two";
		String MOUNTAIN_PASS_RESTRICTION_TWO_DIRECTION = "restriction_two_direction";
		String MOUNTAIN_PASS_CAMERA = "camera";
		String MOUNTAIN_PASS_FORECAST = "forecast";
		String MOUNTAIN_PASS_WEATHER_ICON = "weather_icon";
		String MOUNTAIN_PASS_IS_STARRED = "is_starred";
	}
	
	interface TravelTimesColumns {
		String _ID = "_ID";
		String TRAVEL_TIMES_ID = "id";
		String TRAVEL_TIMES_TITLE = "title";
		String TRAVEL_TIMES_UPDATED = "updated";
		String TRAVEL_TIMES_DISTANCE = "distance";
		String TRAVEL_TIMES_AVERAGE = "average";
		String TRAVEL_TIMES_CURRENT = "current";
		String TRAVEL_TIMES_IS_STARRED = "is_starred";
	}
	
	interface FerriesSchedulesColumns {
		String _ID = "_ID";
		String FERRIES_SCHEDULE_ID = "id";
		String FERRIES_SCHEDULE_TITLE = "title";
		String FERRIES_SCHEDULE_DATE = "date";
		String FERRIES_SCHEDULE_ALERT = "alert";
		String FERRIES_SCHEDULE_UPDATED = "updated";
		String FERRIES_SCHEDULE_IS_STARRED = "is_starred";
	}
	
	interface BorderWaitColumns {
		String _ID = "_ID";
		String BORDER_WAIT_ID = "id";
		String BORDER_WAIT_TITLE = "title";
		String BORDER_WAIT_UPDATED = "updated";
		String BORDER_WAIT_LANE = "lane";
		String BORDER_WAIT_ROUTE = "route";
		String BORDER_WAIT_DIRECTION = "direction";
		String BORDER_WAIT_TIME = "wait";
		String BORDER_WAIT_IS_STARRED = "is_starred";
	}

}
