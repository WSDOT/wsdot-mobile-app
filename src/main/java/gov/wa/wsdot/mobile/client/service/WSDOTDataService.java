/*
 * Copyright (c) 2015 Washington State Department of Transportation
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

import gov.wa.wsdot.mobile.client.service.WSDOTContract.BorderWaitColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.CachesColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.CamerasColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.FerriesSchedulesColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.FerriesTerminalSailingSpaceColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.HighwayAlertsColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.MountainPassesColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.TravelTimesColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.LocationColumns;
import gov.wa.wsdot.mobile.shared.*;

import java.util.List;

import com.google.code.gwt.database.client.GenericRow;
import com.google.code.gwt.database.client.service.Connection;
import com.google.code.gwt.database.client.service.DataService;
import com.google.code.gwt.database.client.service.ListCallback;
import com.google.code.gwt.database.client.service.RowIdListCallback;
import com.google.code.gwt.database.client.service.ScalarCallback;
import com.google.code.gwt.database.client.service.Select;
import com.google.code.gwt.database.client.service.Update;
import com.google.code.gwt.database.client.service.VoidCallback;

@Connection(name="wsdot", version="", description="WSDOT Mobile App Database", maxsize=2000000)
public interface WSDOTDataService extends DataService {
	
	interface Tables {
    	String CACHES = "caches";
        String CAMERAS = "cameras";
        String HIGHWAY_ALERTS = "highway_alerts";
        String MOUNTAIN_PASSES = "mountain_passes";
        String TRAVEL_TIMES = "travel_times";
        String FERRIES_SCHEDULES = "ferries_schedules";
        String FERRIES_TERMINAL_SAILING_SPACE = "ferries_terminal_sailing_space";
        String BORDER_WAIT  = "border_wait";
		String MAP_LOCATION = "map_location";
    }
	
	/**
	 * Create initial table structures in database.
	 * 
	 * @param callback
	 */
	@Update("CREATE TABLE IF NOT EXISTS " + Tables.CACHES + " ("
			+ CachesColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ CachesColumns.CACHE_TABLE_NAME + " TEXT,"
			+ CachesColumns.CACHE_LAST_UPDATED + " INTEGER)")
	void createCachesTable(VoidCallback callback);

	@Update("CREATE TABLE IF NOT EXISTS " + Tables.CAMERAS + " ("
			+ CamerasColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ CamerasColumns.CAMERA_ID + " INTEGER,"
			+ CamerasColumns.CAMERA_TITLE + " TEXT,"
			+ CamerasColumns.CAMERA_URL + " TEXT,"
			+ CamerasColumns.CAMERA_LATITUDE + " REAL,"
			+ CamerasColumns.CAMERA_LONGITUDE + " REAL,"
			+ CamerasColumns.CAMERA_HAS_VIDEO + " INTEGER NOT NULL default 0,"
			+ CamerasColumns.CAMERA_ROAD_NAME + " TEXT,"
			+ CamerasColumns.CAMERA_IS_STARRED + " INTEGER NOT NULL default 0)")
	void createCamerasTable(VoidCallback callback);
	
	@Update("CREATE TABLE IF NOT EXISTS " + Tables.HIGHWAY_ALERTS + " ("
		+ HighwayAlertsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
		+ HighwayAlertsColumns.HIGHWAY_ALERT_ID + " INTEGER,"
	    + HighwayAlertsColumns.HIGHWAY_ALERT_HEADLINE + " TEXT,"
	    + HighwayAlertsColumns.HIGHWAY_ALERT_LATITUDE + " REAL,"
	    + HighwayAlertsColumns.HIGHWAY_ALERT_LONGITUDE + " REAL,"
	    + HighwayAlertsColumns.HIGHWAY_ALERT_CATEGORY + " TEXT,"
	    + HighwayAlertsColumns.HIGHWAY_ALERT_PRIORITY + " TEXT,"
	    + HighwayAlertsColumns.HIGHWAY_ALERT_ROAD_NAME + " TEXT,"
	    + HighwayAlertsColumns.HIGHWAY_ALERT_LAST_UPDATED + " TEXT)")
	void createHighwayAlertsTable(VoidCallback callback);
	
	@Update("CREATE TABLE IF NOT EXISTS " + Tables.MOUNTAIN_PASSES + " ("
		+ MountainPassesColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
	    + MountainPassesColumns.MOUNTAIN_PASS_ID + " INTEGER,"
	    + MountainPassesColumns.MOUNTAIN_PASS_NAME + " TEXT,"
	    + MountainPassesColumns.MOUNTAIN_PASS_WEATHER_CONDITION + " TEXT,"
	    + MountainPassesColumns.MOUNTAIN_PASS_ELEVATION + " TEXT,"
	    + MountainPassesColumns.MOUNTAIN_PASS_TRAVEL_ADVISORY_ACTIVE + " TEXT,"
	    + MountainPassesColumns.MOUNTAIN_PASS_ROAD_CONDITION + " TEXT,"
	    + MountainPassesColumns.MOUNTAIN_PASS_TEMPERATURE + " TEXT,"
	    + MountainPassesColumns.MOUNTAIN_PASS_DATE_UPDATED + " TEXT,"
	    + MountainPassesColumns.MOUNTAIN_PASS_RESTRICTION_ONE + " TEXT,"
	    + MountainPassesColumns.MOUNTAIN_PASS_RESTRICTION_ONE_DIRECTION + " TEXT,"
	    + MountainPassesColumns.MOUNTAIN_PASS_RESTRICTION_TWO + " TEXT,"
	    + MountainPassesColumns.MOUNTAIN_PASS_RESTRICTION_TWO_DIRECTION + " TEXT,"
	    + MountainPassesColumns.MOUNTAIN_PASS_CAMERA + " TEXT,"
	    + MountainPassesColumns.MOUNTAIN_PASS_FORECAST + " TEXT,"
	    + MountainPassesColumns.MOUNTAIN_PASS_WEATHER_ICON + " TEXT,"
	    + MountainPassesColumns.MOUNTAIN_PASS_IS_STARRED + " INTEGER NOT NULL default 0)")
	void createMountainPassesTable(VoidCallback callback);
	
	@Update("CREATE TABLE IF NOT EXISTS " + Tables.TRAVEL_TIMES + " ("
		+ TravelTimesColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
	    + TravelTimesColumns.TRAVEL_TIMES_ID + " INTEGER,"
	    + TravelTimesColumns.TRAVEL_TIMES_TITLE + " TEXT,"
	    + TravelTimesColumns.TRAVEL_TIMES_UPDATED + " TEXT,"
	    + TravelTimesColumns.TRAVEL_TIMES_DISTANCE + " TEXT,"
	    + TravelTimesColumns.TRAVEL_TIMES_AVERAGE + " INTEGER,"
	    + TravelTimesColumns.TRAVEL_TIMES_CURRENT + " INTEGER,"
	    + TravelTimesColumns.TRAVEL_TIMES_IS_STARRED + " INTEGER NOT NULL default 0)")
	void createTravelTimesTable(VoidCallback callback);
	
	@Update("CREATE TABLE IF NOT EXISTS " + Tables.FERRIES_SCHEDULES + " ("
		+ FerriesSchedulesColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
	    + FerriesSchedulesColumns.FERRIES_SCHEDULE_ID + " INTEGER,"
	    + FerriesSchedulesColumns.FERRIES_SCHEDULE_TITLE + " TEXT,"
	    + FerriesSchedulesColumns.FERRIES_SCHEDULE_CROSSING_TIME + " TEXT,"
	    + FerriesSchedulesColumns.FERRIES_SCHEDULE_DATE + " TEXT,"
	    + FerriesSchedulesColumns.FERRIES_SCHEDULE_ALERT + " TEXT,"
	    + FerriesSchedulesColumns.FERRIES_SCHEDULE_UPDATED + " TEXT,"
	    + FerriesSchedulesColumns.FERRIES_SCHEDULE_IS_STARRED + " INTEGER NOT NULL default 0)")
	void createFerriesSchedulesTable(VoidCallback callback);
	
    @Update("CREATE TABLE " + Tables.FERRIES_TERMINAL_SAILING_SPACE + " ("
            + FerriesTerminalSailingSpaceColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + FerriesTerminalSailingSpaceColumns.TERMINAL_ID + " INTEGER,"
            + FerriesTerminalSailingSpaceColumns.TERMINAL_NAME + " TEXT,"
            + FerriesTerminalSailingSpaceColumns.TERMINAL_ABBREV + " TEXT,"
            + FerriesTerminalSailingSpaceColumns.TERMINAL_DEPARTING_SPACES + " TEXT,"
            + FerriesTerminalSailingSpaceColumns.TERMINAL_LAST_UPDATED + " TEXT,"
            + FerriesTerminalSailingSpaceColumns.TERMINAL_IS_STARRED + " INTEGER NOT NULL default 0)")
    void createFerriesTerminalSailingSpaceTable(VoidCallback callback);
	
	@Update("CREATE TABLE IF NOT EXISTS " + Tables.BORDER_WAIT + " ("
		+ BorderWaitColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
		+ BorderWaitColumns.BORDER_WAIT_ID + " INTEGER,"
		+ BorderWaitColumns.BORDER_WAIT_TITLE + " TEXT,"
		+ BorderWaitColumns.BORDER_WAIT_UPDATED + " TEXT,"
		+ BorderWaitColumns.BORDER_WAIT_LANE + " TEXT,"
		+ BorderWaitColumns.BORDER_WAIT_ROUTE + " INTEGER,"
		+ BorderWaitColumns.BORDER_WAIT_DIRECTION + " TEXT,"
		+ BorderWaitColumns.BORDER_WAIT_TIME + " INTEGER,"
		+ BorderWaitColumns.BORDER_WAIT_IS_STARRED + " INTEGER NOT NULL default 0)")
	void createBorderWaitTable(VoidCallback callback);

	@Update("CREATE TABLE IF NOT EXISTS " + Tables.MAP_LOCATION + " ("
			+ LocationColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ LocationColumns.LOCATION_TITLE + " TEXT,"
			+ LocationColumns.LOCATION_LAT + " INTEGER,"
			+ LocationColumns.LOCATION_LONG + " INTEGER,"
			+ LocationColumns.LOCATION_ZOOM + " INTEGER);")
	void createLocationsTable(VoidCallback callback);

	/**
	 * Initialize cache table.
	 * 
	 * @param items
	 * @param callback
	 */
	@Update(sql="INSERT INTO " + Tables.CACHES + " ("
			+ CachesColumns.CACHE_TABLE_NAME + ", "
			+ CachesColumns.CACHE_LAST_UPDATED + ") "
			+ "VALUES ({_.getTableName()}, {_.getLastUpdated()})", foreach="cacheItems")
	void initCachesTable(List<CacheItem> cacheItems, RowIdListCallback callback);

	/**
	 * Update timestamp in cache table.
	 * 
	 * @param cacheItems
	 * @param callback
	 */
	@Update(sql="UPDATE " + Tables.CACHES
			+ " SET " + CachesColumns.CACHE_LAST_UPDATED + " = {_.getLastUpdated()}" 
			+ " WHERE " + CachesColumns.CACHE_TABLE_NAME + " LIKE {_.getTableName()}", foreach="cacheItems")
	void updateCachesTable(List<CacheItem> cacheItems, VoidCallback callback);	
	
	/**
	 * Obtains the number of entries in the caches table.
	 * 
	 * @param callback
	 */
	@Select("SELECT count(*) FROM " + Tables.CACHES)
	void getCachesTableCount(ScalarCallback<Integer> callback);

	/** 
	 * Returns the last time data was downloaded and cached for a specific table.
	 * 
	 * @param tableName
	 * @param callback
	 */
	@Select("SELECT " + CachesColumns.CACHE_LAST_UPDATED
			+ " FROM " + Tables.CACHES
			+ " WHERE "	+ CachesColumns.CACHE_TABLE_NAME
			+ " LIKE {tableName}")
	void getCacheLastUpdated(String tableName, ListCallback<GenericRow> callback);
	
	/**
	 * Initialize camera table.
	 * 
	 * @param cameraItems
	 * @param callback
	 */
	@Update(sql="INSERT INTO " + Tables.CAMERAS + " ("
			+ CamerasColumns.CAMERA_ID + ", "
			+ CamerasColumns.CAMERA_TITLE + ", "
			+ CamerasColumns.CAMERA_URL + ", "
			+ CamerasColumns.CAMERA_LATITUDE + ", "
			+ CamerasColumns.CAMERA_LONGITUDE + ", "
			+ CamerasColumns.CAMERA_HAS_VIDEO + ", "
			+ CamerasColumns.CAMERA_ROAD_NAME + ", "
			+ CamerasColumns.CAMERA_IS_STARRED + ") "
			+ "VALUES "
			+ "({_.getCameraId()}, {_.getTitle()}, {_.getImageUrl()}, {_.getLatitude()}, "
			+ "{_.getLongitude()}, {_.getHasVideo()}, {_.getRoadName()}, "
			+ "{_.getIsStarred()})", foreach="cameraItems")
	void initCamerasTable(List<CameraItem> cameraItems, RowIdListCallback callback);
	
	/**
	 * Obtains the number of entries in the cameras table.
	 * 
	 * @param callback
	 */
	@Select("SELECT count(*) FROM " + Tables.CAMERAS)
	void getCamerasTableCount(ScalarCallback<Integer> callback);
	
	/**
	 * Delete all rows from highway alerts table.
	 * 
	 * @param callback
	 */
	@Update("DELETE FROM " + Tables.HIGHWAY_ALERTS)
	void deleteHighwayAlerts(VoidCallback callback);
	
	/**
	 * Insert highway alerts into table.
	 * 
	 * @param highwayAlertItems
	 * @param callback
	 */
	@Update(sql="INSERT INTO " + Tables.HIGHWAY_ALERTS + " ("
			+ HighwayAlertsColumns.HIGHWAY_ALERT_ID + ", "
			+ HighwayAlertsColumns.HIGHWAY_ALERT_HEADLINE + ", "
			+ HighwayAlertsColumns.HIGHWAY_ALERT_CATEGORY + ", "
			+ HighwayAlertsColumns.HIGHWAY_ALERT_PRIORITY + ", "
			+ HighwayAlertsColumns.HIGHWAY_ALERT_LATITUDE + ", "
			+ HighwayAlertsColumns.HIGHWAY_ALERT_LONGITUDE + ", "
			+ HighwayAlertsColumns.HIGHWAY_ALERT_ROAD_NAME + ", "
			+ HighwayAlertsColumns.HIGHWAY_ALERT_LAST_UPDATED + ") "
			+ "VALUES "
			+ "({_.getAlertId()}, {_.getHeadlineDescription()}, {_.getEventCategory()}, "
			+ "{_.getPriority()}, {_.getStartLatitude()}, {_.getStartLongitude()}, "
			+ "{_.getStartRoadName()}, {_.getLastUpdatedTime()})", foreach="highwayAlertItems")
	void insertHighwayAlerts(List<HighwayAlertItem> highwayAlertItems, RowIdListCallback callback);
	
	/**
	 * Retrieve highway alerts from table.
	 * 
	 * @param callback
	 */
	@Select("SELECT * FROM " + Tables.HIGHWAY_ALERTS)
	void getHighwayAlerts(ListCallback<GenericRow> callback);
	
	/**
	 * Retrieve individual highway alert.
	 * 
	 * @param alertId
	 * @param callback
	 */
	@Select("SELECT * FROM " + Tables.HIGHWAY_ALERTS
			+ " WHERE " + HighwayAlertsColumns.HIGHWAY_ALERT_ID
			+ " = {alertId}")
	void getHighwayAlert(String alertId, ListCallback<GenericRow> callback);	
	
	/**
	 * Delete all rows from border wait table.
	 * 
	 * @param callback
	 */
	@Update("DELETE FROM " + Tables.BORDER_WAIT)
	void deleteBorderWaits(VoidCallback callback);
	
	/**
	 * Insert border wait items into table.
	 * 
	 * @param borderWaitItems
	 * @param callback
	 */
	@Update(sql="INSERT INTO " + Tables.BORDER_WAIT + " ("
			+ BorderWaitColumns.BORDER_WAIT_ID + ", "
			+ BorderWaitColumns.BORDER_WAIT_TITLE + ", "
			+ BorderWaitColumns.BORDER_WAIT_UPDATED + ", "
			+ BorderWaitColumns.BORDER_WAIT_LANE + ", "
			+ BorderWaitColumns.BORDER_WAIT_ROUTE + ", "
			+ BorderWaitColumns.BORDER_WAIT_DIRECTION + ", "
			+ BorderWaitColumns.BORDER_WAIT_TIME + ", "
			+ BorderWaitColumns.BORDER_WAIT_IS_STARRED + ") "
			+ "VALUES "
			+ "({_.getId()}, {_.getTitle()}, {_.getUpdated()}, "
			+ "{_.getLane()}, {_.getRoute()}, {_.getDirection()}, "
			+ "{_.getWait()}, {_.getIsStarred()})", foreach="borderWaitItems")
	void insertBorderWaits(List<BorderWaitItem> borderWaitItems, RowIdListCallback callback);
	
	/**
	 * Retrieve border waits from table.
	 * 
	 * @param callback
	 */
	@Select("SELECT * FROM " + Tables.BORDER_WAIT)
	void getBorderWaits(ListCallback<GenericRow> callback);
	
	/** 
	 * Returns rows from border waits table which have been starred as favorites.
	 * 
	 * @param callback
	 */
	@Select("SELECT * FROM " + Tables.BORDER_WAIT
			+ " WHERE " + BorderWaitColumns.BORDER_WAIT_IS_STARRED
			+ " = 1")
	void getStarredBorderWaits(ListCallback<GenericRow> callback);

	/**
	 * Update rows from border waits table which have been starred or unstarred
	 * as favorites.
	 * 
	 * @param items
	 * @param callback
	 */
	@Update(sql="UPDATE " + Tables.BORDER_WAIT
			+ " SET " + BorderWaitColumns.BORDER_WAIT_IS_STARRED + " = {_.getIsStarred()}" 
			+ " WHERE " + BorderWaitColumns.BORDER_WAIT_ID + " = {_.getId()}", foreach="borderWaitItems")
	void updateStarredBorderWaits(List<BorderWaitItem> borderWaitItems, VoidCallback callback);	
	
	/**
	 * Delete all rows from ferries schedules table.
	 * 
	 * @param callback
	 */
	@Update("DELETE FROM " + Tables.FERRIES_SCHEDULES)
	void deleteFerriesSchedules(VoidCallback callback);
	
	/**
	 * Insert ferries schedule items into table.
	 * 
	 * @param ferriesRouteItems
	 * @param callback
	 */
	@Update(sql="INSERT INTO " + Tables.FERRIES_SCHEDULES + " ("
			+ FerriesSchedulesColumns.FERRIES_SCHEDULE_ID + ", "
			+ FerriesSchedulesColumns.FERRIES_SCHEDULE_TITLE + ", "
			+ FerriesSchedulesColumns.FERRIES_SCHEDULE_CROSSING_TIME + ", "
			+ FerriesSchedulesColumns.FERRIES_SCHEDULE_DATE + ", "
			+ FerriesSchedulesColumns.FERRIES_SCHEDULE_ALERT + ", "
			+ FerriesSchedulesColumns.FERRIES_SCHEDULE_UPDATED + ", "
			+ FerriesSchedulesColumns.FERRIES_SCHEDULE_IS_STARRED + ") "
			+ "VALUES "
			+ "({_.getRouteID()}, {_.getDescription()}, {_.getCrossingTime()}, {_.getScheduleDate()}, "
			+ "{_.getRouteAlert()}, {_.getCacheDate()}, {_.getIsStarred()})", foreach="ferriesRouteItems")
	void insertFerriesSchedules(List<FerriesRouteItem> ferriesRouteItems, RowIdListCallback callback);
	
	/**
	 * Retrieve ferries schedules from table.
	 * 
	 * @param callback
	 */
	@Select("SELECT * FROM " + Tables.FERRIES_SCHEDULES)
	void getFerriesSchedules(ListCallback<GenericRow> callback);
	
	/**
	 * Retrieve ferries schedule for specific route.
	 * 
	 * @param routeId
	 * @param callback
	 */
	@Select("SELECT * FROM " + Tables.FERRIES_SCHEDULES
			+ " WHERE " + FerriesSchedulesColumns.FERRIES_SCHEDULE_ID
			+ " = {routeId}")
	void getFerriesSchedulesRoute(String routeId, ListCallback<GenericRow> callback);
	
	/** 
	 * Returns rows from ferries schedules table which have been starred as favorites.
	 * 
	 * @param callback
	 */
	@Select("SELECT * FROM " + Tables.FERRIES_SCHEDULES
			+ " WHERE " + FerriesSchedulesColumns.FERRIES_SCHEDULE_IS_STARRED
			+ " = 1")
	void getStarredFerriesSchedules(ListCallback<GenericRow> callback);

	/**
	 * Update rows from ferries schedule table which have been starred or unstarred
	 * as favorites.
	 * 
	 * @param items
	 * @param callback
	 */
	@Update(sql="UPDATE " + Tables.FERRIES_SCHEDULES
			+ " SET " + FerriesSchedulesColumns.FERRIES_SCHEDULE_IS_STARRED + " = {_.getIsStarred()}" 
			+ " WHERE " + FerriesSchedulesColumns.FERRIES_SCHEDULE_ID + " = {_.getRouteID()}", foreach="items")
	void updateStarredFerriesSchedules(List<FerriesRouteItem> items, VoidCallback callback);	
	
	/**
     * Delete all rows from ferries terminal sailing space
     * 
     * @param callback
     */
    @Update("DELETE FROM " + Tables.FERRIES_TERMINAL_SAILING_SPACE)
    void deleteFerriesTerminalSailingSpace(VoidCallback callback);
    
    /**
     * Insert ferries terminal sailing space items into table.
     * 
     * @param ferriesTerminalSailingSpaceItems
     * @param callback
     */
    @Update(sql="INSERT INTO " + Tables.FERRIES_TERMINAL_SAILING_SPACE + " ("
            + FerriesTerminalSailingSpaceColumns.TERMINAL_ID + ", "	
            + FerriesTerminalSailingSpaceColumns.TERMINAL_NAME + ", "
            + FerriesTerminalSailingSpaceColumns.TERMINAL_ABBREV + ", "
            + FerriesTerminalSailingSpaceColumns.TERMINAL_DEPARTING_SPACES + ", "
            + FerriesTerminalSailingSpaceColumns.TERMINAL_LAST_UPDATED + ", "
            + FerriesTerminalSailingSpaceColumns.TERMINAL_IS_STARRED + ") "
            + "VALUES "
            + "({_.getTerminalId()}, {_.getTerminalName()}, {_.getTerminalAbbrev()}, {_.getTerminalDepartingSpaces()}, "
            + "{_.getLastUpdated()}, {_.getIsStarred()})", foreach="ferriesTerminalSailingSpaceItems")
    void insertFerriesTerminalSailingSpace(List<FerriesTerminalSailingSpaceItem> ferriesTerminalSailingSpaceItems, RowIdListCallback callback);
    
    /**
     * Retrieve ferries terminal sailing spaces from table.
     * 
     * @param callback
     */
    @Select("SELECT * FROM " + Tables.FERRIES_TERMINAL_SAILING_SPACE)
    void getFerriesTerminalSailingSpaces(ListCallback<GenericRow> callback);
    
    /**
     * Retrieve ferries terminal sailing spaces for a specific terminal.
     * 
     * @param terminalId
     * @param callback
     */
    @Select("SELECT * FROM " + Tables.FERRIES_TERMINAL_SAILING_SPACE
            + " WHERE " + FerriesTerminalSailingSpaceColumns.TERMINAL_ID
            + " = {terminalId}")
    void getFerriesTerminalSailingSpace(String terminalId, ListCallback<GenericRow> callback);
    
	/** 
	 * Returns rows from mountain passes table which have been starred as favorites.
	 * 
	 * @param callback
	 */
	@Select("SELECT * FROM " + Tables.MOUNTAIN_PASSES
			+ " WHERE " + MountainPassesColumns.MOUNTAIN_PASS_IS_STARRED
			+ " = 1")
	void getStarredMountainPasses(ListCallback<GenericRow> callback);

	/**
	 * Update rows from mountain passes table which have been starred or unstarred
	 * as favorites.
	 * 
	 * @param items
	 * @param callback
	 */
	@Update(sql="UPDATE " + Tables.MOUNTAIN_PASSES
			+ " SET " + MountainPassesColumns.MOUNTAIN_PASS_IS_STARRED + " = {_.getIsStarred()}" 
			+ " WHERE " + MountainPassesColumns.MOUNTAIN_PASS_ID + " = {_.getMountainPassId()}", foreach="items")
	void updateStarredMountainPasses(List<MountainPassItem> items, VoidCallback callback);	
	
	/**
	 * Delete all rows from mountain passes table.
	 * 
	 * @param callback
	 */
	@Update("DELETE FROM " + Tables.MOUNTAIN_PASSES)
	void deleteMountainPasses(VoidCallback callback);
	
	/**
	 * Insert mountain pass items into table.
	 * 
	 * @param mountainPassItems
	 * @param callback
	 */
	@Update(sql="INSERT INTO " + Tables.MOUNTAIN_PASSES + " ("
			+ MountainPassesColumns.MOUNTAIN_PASS_ID + ", "
			+ MountainPassesColumns.MOUNTAIN_PASS_ELEVATION + ", "
			+ MountainPassesColumns.MOUNTAIN_PASS_ROAD_CONDITION + ", "
			+ MountainPassesColumns.MOUNTAIN_PASS_TEMPERATURE + ", "
			+ MountainPassesColumns.MOUNTAIN_PASS_DATE_UPDATED + ", "
			+ MountainPassesColumns.MOUNTAIN_PASS_NAME + ", "
			+ MountainPassesColumns.MOUNTAIN_PASS_WEATHER_ICON + " ,"
			+ MountainPassesColumns.MOUNTAIN_PASS_WEATHER_CONDITION + ", "
			+ MountainPassesColumns.MOUNTAIN_PASS_RESTRICTION_ONE + ", "
			+ MountainPassesColumns.MOUNTAIN_PASS_RESTRICTION_ONE_DIRECTION + ", "
			+ MountainPassesColumns.MOUNTAIN_PASS_RESTRICTION_TWO + ", "
			+ MountainPassesColumns.MOUNTAIN_PASS_RESTRICTION_TWO_DIRECTION + ", "
			+ MountainPassesColumns.MOUNTAIN_PASS_CAMERA + ", "
			+ MountainPassesColumns.MOUNTAIN_PASS_FORECAST + ", "
			+ MountainPassesColumns.MOUNTAIN_PASS_IS_STARRED + ") "
			+ "VALUES "
			+ "({_.getMountainPassId()}, {_.getElevationInFeet()}, {_.getRoadCondition()}, "
			+ "{_.getTemperatureInFahrenheit()}, {_.getDateUpdated()}, {_.getMountainPassName()}, "
			+ "{_.getWeatherIcon()}, {_.getWeatherCondition()}, {_.getRestrictionOneText()}, {_.getRestrictionOneTravelDirection()}, "
			+ "{_.getRestrictionTwoText()}, {_.getRestrictionTwoTravelDirection()}, "
			+ "{_.getCamera()}, {_.getForecast()}, {_.getIsStarred()})", foreach="mountainPassItems")
	void insertMountainPasses(List<MountainPassItem> mountainPassItems, RowIdListCallback callback);
	
	/**
	 * Retrieve mountain passes from table.
	 * 
	 * @param callback
	 */
	@Select("SELECT * FROM " + Tables.MOUNTAIN_PASSES)
	void getMountainPasses(ListCallback<GenericRow> callback);
	
	/**
	 * Retrieve content for specific mountain pass.
	 * 
	 * @param routeId
	 * @param callback
	 */
	@Select("SELECT * FROM " + Tables.MOUNTAIN_PASSES
			+ " WHERE " + MountainPassesColumns.MOUNTAIN_PASS_ID
			+ " = {passId}")
	void getMountainPass(String passId, ListCallback<GenericRow> callback);
	
	/**
	 * Delete all rows from travel times table.
	 * 
	 * @param callback
	 */
	@Update("DELETE FROM " + Tables.TRAVEL_TIMES)
	void deleteTravelTimes(VoidCallback callback);
	
	/**
	 * Insert travel times content into table.
	 * 
	 * @param items
	 * @param callback
	 */
	@Update(sql="INSERT INTO " + Tables.TRAVEL_TIMES + " ("
			+ TravelTimesColumns.TRAVEL_TIMES_ID + ", "
			+ TravelTimesColumns.TRAVEL_TIMES_TITLE + ", "
			+ TravelTimesColumns.TRAVEL_TIMES_UPDATED + ", "
			+ TravelTimesColumns.TRAVEL_TIMES_DISTANCE + ", "
			+ TravelTimesColumns.TRAVEL_TIMES_AVERAGE + ", "
			+ TravelTimesColumns.TRAVEL_TIMES_CURRENT + ", "
			+ TravelTimesColumns.TRAVEL_TIMES_IS_STARRED + ") "
			+ "VALUES "
			+ "({_.getRouteId()}, {_.getTitle()}, {_.getUpdated()}, "
			+ "{_.getDistance()}, {_.getAverageTime()}, {_.getCurrentTime()}, "
			+ "{_.getIsStarred()})", foreach="items")
	void insertTravelTimes(List<TravelTimesItem> items, RowIdListCallback callback);
	
	/**
	 * Return travel times from table.
	 * 
	 * @param callback
	 */
	@Select("SELECT * FROM " + Tables.TRAVEL_TIMES)
	void getTravelTimes(ListCallback<GenericRow> callback);

	/**
	 * Return travel times from table which match a search filter.
	 * 
	 * @param filter
	 * @param callback
	 */
	@Select("SELECT * FROM " + Tables.TRAVEL_TIMES
			+ " WHERE " + TravelTimesColumns.TRAVEL_TIMES_TITLE
			+ " LIKE {filter}")
	void getTravelTimes(String filter, ListCallback<GenericRow> callback);
	
	/**
	 * Retrieve content for specific travel time.
	 * 
	 * @param routeId
	 * @param callback
	 */
	@Select("SELECT * FROM " + Tables.TRAVEL_TIMES
			+ " WHERE " + TravelTimesColumns.TRAVEL_TIMES_ID
			+ " = {timesId}")
	void getTravelTime(String timesId, ListCallback<GenericRow> callback);
	
	/** 
	 * Returns rows from travel times table which have been starred as favorites.
	 * 
	 * @param callback
	 */
	@Select("SELECT * FROM " + Tables.TRAVEL_TIMES
			+ " WHERE " + TravelTimesColumns.TRAVEL_TIMES_IS_STARRED
			+ " = 1")
	void getStarredTravelTimes(ListCallback<GenericRow> callback);

	/**
	 * Update rows from travel times table which have been starred or unstarred
	 * as favorites.
	 * 
	 * @param items
	 * @param callback
	 */
	@Update(sql="UPDATE " + Tables.TRAVEL_TIMES
			+ " SET " + TravelTimesColumns.TRAVEL_TIMES_IS_STARRED + " = {_.getIsStarred()}" 
			+ " WHERE " + TravelTimesColumns.TRAVEL_TIMES_ID + " = {_.getRouteId()}", foreach="items")
	void updateStarredTravelTimes(List<TravelTimesItem> items, VoidCallback callback);		
	
	/** 
	 * Returns rows from cameras table which have been starred as favorites.
	 * 
	 * @param callback
	 */
	@Select("SELECT " + CamerasColumns.CAMERA_ID
			+ " ," + CamerasColumns.CAMERA_TITLE
			+ " FROM " + Tables.CAMERAS
			+ " WHERE " + CamerasColumns.CAMERA_IS_STARRED
			+ " = 1")
	void getStarredCameras(ListCallback<GenericRow> callback);

	/**
	 * Update rows from cameras table which have been starred or unstarred as favorites.
	 * 
	 * @param items
	 * @param callback
	 */
	@Update(sql="UPDATE " + Tables.CAMERAS
			+ " SET " + CamerasColumns.CAMERA_IS_STARRED + " = {_.getIsStarred()}" 
			+ " WHERE " + CamerasColumns.CAMERA_ID + " = {_.getCameraId()}", foreach="items")
	void updateStarredCameras(List<CameraItem> items, VoidCallback callback);
	
	/**
	 * Delete all rows from cameras table.
	 * 
	 * @param callback
	 */
	@Update("DELETE FROM " + Tables.CAMERAS)
	void deleteCameras(VoidCallback callback);
	
	/**
	 * Insert cameras into table.
	 * 
	 * @param cameraItems
	 * @param callback
	 */
	@Update(sql="INSERT INTO " + Tables.CAMERAS + " ("
			+ CamerasColumns.CAMERA_ID + ", "
			+ CamerasColumns.CAMERA_TITLE + ", "
			+ CamerasColumns.CAMERA_ROAD_NAME + ", "
			+ CamerasColumns.CAMERA_URL + ", "
			+ CamerasColumns.CAMERA_LATITUDE + ", "
			+ CamerasColumns.CAMERA_LONGITUDE + ", "
			+ CamerasColumns.CAMERA_HAS_VIDEO + ", "
			+ CamerasColumns.CAMERA_IS_STARRED + ") "
			+ "VALUES "
			+ "({_.getCameraId()}, {_.getTitle()}, {_.getRoadName()}, {_.getImageUrl()}, "
			+ "{_.getLatitude()}, {_.getLongitude()}, {_.getHasVideo()}, "
			+ "{_.getIsStarred()})", foreach="cameraItems")
	void insertCameras(List<CameraItem> cameraItems, RowIdListCallback callback);
	
	/**
	 * Retrieve cameras from table.
	 * 
	 * @param callback
	 */
	@Select("SELECT * FROM " + Tables.CAMERAS)
	void getCameras(ListCallback<GenericRow> callback);
	
	/**
	 * Retrieve individual camera.
	 * 
	 * @param cameraId
	 * @param callback
	 */
	@Select("SELECT * FROM " + Tables.CAMERAS
			+ " WHERE " + CamerasColumns.CAMERA_ID
			+ " = {cameraId}")
	void getCamera(String cameraId, ListCallback<GenericRow> callback);

    /**
     * Insert location into table.
     *  @param locationItem
     * @param callback
	 */
    @Update(sql="INSERT INTO " + Tables.MAP_LOCATION + " ("
            + LocationColumns.LOCATION_LAT + ", "
            + LocationColumns.LOCATION_LONG + ", "
            + LocationColumns.LOCATION_TITLE + ", "
            + LocationColumns.LOCATION_ZOOM + ") "
            + "VALUES "
            + "({locationItem.getLatitude()}, {locationItem.getLongitude()}, {locationItem.getTitle()}, {locationItem.getZoom()})")
    void insertLocation(LocationItem locationItem, VoidCallback callback);

	/**
	 * Gets all favorite locations
	 */
	@Select("SELECT * FROM " + Tables.MAP_LOCATION)
	void getLocations(ListCallback<GenericRow> callback);

    /**
     *  Removes a favorite location
     */
    @Update("DELETE FROM " + Tables.MAP_LOCATION
            + " WHERE " + LocationColumns._ID
            + " = {locationItem.getId()}")
    void removeLocation(LocationItem locationItem, VoidCallback callback);


}
