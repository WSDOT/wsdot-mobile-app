/*
 * Copyright (c) 2014 Washington State Department of Transportation
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

import java.io.Serializable;

public class HighwayAlertItem implements Serializable {

	private static final long serialVersionUID = -7587936426209570414L;
	private String priority;
	private String lastUpdatedTime;
	private int alertId;
	private Double endLatitude;
	private Double endLongitude;
	private String region;
	private String eventCategory;
	private String county;
	private String extendedDescription;
	private String eventStatus;
	private String startTime;
	private String endTime;
	private String headlineDescription;
	private Double startLatitude;
	private Double startLongitude;
	private Integer categoryIcon;
	private String startRoadName;
	
	public HighwayAlertItem() {
	}
	
	/**
	 * 
	 * @param alertId Alert id
	 * @param category Category of alert
	 * @param description Headline description of alert
	 * @param latitude Latitude of alert
	 * @param longitude Longitude of alert
	 */
	public HighwayAlertItem(int alertId, String category, String description,
			double latitude, double longitude, String priority) {
		
		this.alertId = alertId;
		this.eventCategory = category;
		this.headlineDescription = description;
		this.startLatitude = latitude;
		this.startLongitude = longitude;
		this.priority = priority;
	}
	
	public HighwayAlertItem(int alertId, String headlineDescription) {
	    this.alertId = alertId;
		this.headlineDescription = headlineDescription;
	}
	
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getLastUpdatedTime() {
		return lastUpdatedTime;
	}
	public void setLastUpdatedTime(String lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}
	public int getAlertId() {
		return alertId;
	}
	public void setAlertId(int alertId) {
		this.alertId = alertId; 
	}
	public Double getEndLatitude() {
		return endLatitude;
	}
	public void setEndLatitude(Double endLatitude) {
		this.endLatitude = endLatitude;
	}
	public Double getEndLongitude() {
		return endLongitude;
	}
	public void setEndLongitude(Double endLongitude) {
		this.endLongitude = endLongitude;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getEventCategory() {
		return eventCategory;
	}
	public void setEventCategory(String eventCategory) {
		this.eventCategory = eventCategory;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getExtendedDescription() {
		return extendedDescription;
	}
	public void setExtendedDescription(String extendedDescription) {
		this.extendedDescription = extendedDescription;
	}
	public String getEventStatus() {
		return eventStatus;
	}
	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getHeadlineDescription() {
		return headlineDescription;
	}
	public void setHeadlineDescription(String headlineDescription) {
		this.headlineDescription = headlineDescription;
	}
	public Double getStartLatitude() {
		return startLatitude;
	}
	public void setStartLatitude(Double startLatitude) {
		this.startLatitude = startLatitude;
	}
	public Double getStartLongitude() {
		return startLongitude;
	}
	public void setStartLongitude(Double startLongitude) {
		this.startLongitude = startLongitude;
	}
	public Integer getCategoryIcon() {
		return categoryIcon;
	}
	public void setCategoryIcon(Integer categoryIcon) {
		this.categoryIcon = categoryIcon;
	}
	public String getStartRoadName() {
		return startRoadName;
	}
	public void setStartRoadName(String startRoadName) {
		this.startRoadName = startRoadName;
	}
}
