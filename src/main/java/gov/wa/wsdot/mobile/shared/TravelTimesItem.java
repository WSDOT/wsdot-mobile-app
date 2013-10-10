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

public class TravelTimesItem {
	private String title;
	private int currentTime;
	private int averageTime;
	private String distance;
	private String updated;
	private int routeId;
	private boolean selected;
	private int isStarred;

	/**
	 * 
	 * @param routeId
	 * @param isStarred
	 */
	public TravelTimesItem(int routeId, int isStarred) {
		this.routeId = routeId;
		this.isStarred = isStarred;
	}
	
	public TravelTimesItem() {
	}
	
	public TravelTimesItem(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}	

	public int getCurrentTime() {
		return currentTime;
	}
	
	public void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
	}
	
	public int getAverageTime() {
		return averageTime;
	}
	
	public void setAverageTime(int averageTime) {
		this.averageTime = averageTime;
	}
	
	public String getDistance() {
		return distance;
	}
	
	public void setDistance(String distance) {
		this.distance = distance;
	}
	
	public int getRouteId() {
		return routeId;
	}
	
	public void setRouteId(int routeId) {
		this.routeId = routeId;
	}
	
	public String getUpdated() {
		return updated;
	}
	
	public void setUpdated(String updated) {
		this.updated = updated;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getIsStarred() {
		return isStarred;
	}

	public void setIsStarred(int isStarred) {
		this.isStarred = isStarred;
	}
}