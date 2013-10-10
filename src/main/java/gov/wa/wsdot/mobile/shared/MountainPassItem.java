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

import java.io.Serializable;

public class MountainPassItem implements Serializable {

	private static final long serialVersionUID = -6136235109640235027L;
	private String weatherCondition;
	private Integer elevationInFeet;
	private String travelAdvisoryActive;
	private String longitude;
	private int mountainPassId;
	private String roadCondition;
	private Integer temperatureInFahrenheit;
	private String latitude;
	private String dateUpdated;
	private String mountainPassName;
	private String restrictionOneText;
	private String restrictionOneTravelDirection;
	private String restrictionTwoText;
	private String restrictionTwoTravelDirection;
	private String weatherIcon;
	private boolean selected;
	private String camera;
	private String forecast;
	private int isStarred;
	
	/**
	 * 
	 * @param mountainPassId
	 * @param isStarred
	 */
	public MountainPassItem(int mountainPassId, int isStarred) {
		this.mountainPassId = mountainPassId;
		this.isStarred = isStarred;
	}
	
	public MountainPassItem() {
	}
	
	public String getWeatherCondition() {
		return weatherCondition;
	}
	
	public void setWeatherCondition(String weatherCondition) {
		this.weatherCondition = weatherCondition;
	}
	
	public Integer getElevationInFeet() {
		return elevationInFeet;
	}
	
	public void setElevationInFeet(Integer elevationInFeet) {
		this.elevationInFeet = elevationInFeet;
	}
	
	public String getTravelAdvisoryActive() {
		return travelAdvisoryActive;
	}
	
	public void setTravelAdvisoryActive(String travelAdvisoryActive) {
		this.travelAdvisoryActive = travelAdvisoryActive; 
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public int getMountainPassId() {
		return mountainPassId;
	}
	
	public void setMountainPassId(int mountainPassId) {
		this.mountainPassId = mountainPassId;
	}
	
	public String getRoadCondition() {
		return roadCondition;
	}
	
	public void setRoadCondition(String roadCondition) {
		this.roadCondition = roadCondition;
	}
	
	public Integer getTemperatureInFahrenheit() {
		return temperatureInFahrenheit;
	}
	
	public void setTemperatureInFahrenheit(Integer temperatureInFahrenheit) {
		this.temperatureInFahrenheit = temperatureInFahrenheit;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public String getDateUpdated() {
		return dateUpdated;
	}
	
	public void setDateUpdated(String dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
	
	public String getMountainPassName() {
		return mountainPassName;
	}
	
	public void setMountainPassName(String mountainPassName) {
		this.mountainPassName = mountainPassName;
	}
	
	public String getRestrictionOneText() {
		return restrictionOneText;
	}
	
	public void setRestrictionOneText(String restrictionOneText) {
		this.restrictionOneText = restrictionOneText;
	}
	
	public String getRestrictionOneTravelDirection() {
		return restrictionOneTravelDirection;
	}
	
	public void setRestrictionOneTravelDirection(String restrictionOneTravelDirection) {
		this.restrictionOneTravelDirection = restrictionOneTravelDirection;
	}
	
	public String getRestrictionTwoText() {
		return restrictionTwoText;
	}
	
	public void setRestrictionTwoText(String restrictionTwoText) {
		this.restrictionTwoText = restrictionTwoText;
	}
	
	public String getRestrictionTwoTravelDirection() {
		return restrictionTwoTravelDirection;
	}
	
	public void setRestrictionTwoTravelDirection(String restrictionTwoTravelDirection) {
		this.restrictionTwoTravelDirection = restrictionTwoTravelDirection;
	}
	
	public String getWeatherIcon() {
		return weatherIcon;
	}
	
	public void setWeatherIcon(String weatherIcon) {
		this.weatherIcon = weatherIcon;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getCamera() {
		return camera;
	}

	public void setCamera(String camera) {
		this.camera = camera;
	}

	public String getForecast() {
		return forecast;
	}

	public void setForecast(String forecast) {
		this.forecast = forecast;
	}

	public int getIsStarred() {
		return isStarred;
	}

	public void setIsStarred(int isStarred) {
		this.isStarred = isStarred;
	}	
}