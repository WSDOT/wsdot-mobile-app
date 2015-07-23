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

package gov.wa.wsdot.mobile.shared;

import java.io.Serializable;

public class FerriesRouteItem implements Serializable {

    private static final long serialVersionUID = -8830406769389004396L;
    private int routeID;
	private String description;
	private boolean selected;
	//private ArrayList<FerriesRouteAlertItem> routeAlert = new ArrayList<FerriesRouteAlertItem>();
	//private ArrayList<FerriesScheduleDateItem> scheduleDate = new ArrayList<FerriesScheduleDateItem>();
	private String routeAlert;
	private String scheduleDate;
	private String cacheDate;
	private String crossingTime;
	private int isStarred;

	/**
	 * 
	 * @param routeID
	 * @param isStarred
	 */
	public FerriesRouteItem(Integer routeID, int isStarred) {
		this.routeID = routeID;
		this.isStarred = isStarred;
	}
	
	public FerriesRouteItem() {
	}
	
	public int getRouteID() {
		return routeID;
	}
	
	public void setRouteID(int routeID) {
		this.routeID = routeID;
	}
		
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
    public boolean isSelected() {
        return selected;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }	
	
    /*
	public ArrayList<FerriesRouteAlertItem> getFerriesRouteAlertItem() {
		return routeAlert;
	}
	
	public void setFerriesRouteAlertItem(FerriesRouteAlertItem routeAlert) {
		this.routeAlert.add(routeAlert);
	}
	
	public ArrayList<FerriesScheduleDateItem> getFerriesScheduleDateItem() {
		return scheduleDate;
	}
	
	public void setFerriesScheduleDateItem(FerriesScheduleDateItem scheduleDate) {
		this.scheduleDate.add(scheduleDate);
	}
	*/

	public String getCacheDate() {
		return cacheDate;
	}

	public void setCacheDate(String cacheDate) {
		this.cacheDate = cacheDate;
	}

	public String getRouteAlert() {
		return routeAlert;
	}

	public void setRouteAlert(String routeAlert) {
		this.routeAlert = routeAlert;
	}

	public String getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public int getIsStarred() {
		return isStarred;
	}

	public void setIsStarred(int isStarred) {
		this.isStarred = isStarred;
	}

    public String getCrossingTime() {
        return crossingTime;
    }

    public void setCrossingTime(String crossingTime) {
        this.crossingTime = crossingTime;
    }

}
