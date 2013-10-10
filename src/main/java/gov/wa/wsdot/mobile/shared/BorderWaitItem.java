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

public class BorderWaitItem implements Serializable {

	private static final long serialVersionUID = 166664752390774881L;
	private int id;
	private String title;
	private int route;
	private int wait;
	private String updated;
	private String lane;
	private String name;
	private String direction;
	private String routeIcon;
	private int isStarred;

	public BorderWaitItem() {
	}

	public BorderWaitItem(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}	
	public int getRoute() {
		return route;
	}
	public void setRoute(int route) {
		this.route = route;
	}
	public int getWait() {
		return wait;
	}
	public void setWait(int wait) {
		this.wait = wait;
	}	
	public String getUpdated() {
		return updated;
	}
	public void setUpdated(String updated) {
		this.updated = updated;
	}
	public String getLane() {
		return lane;
	}
	public void setLane(String lane) {
		this.lane = lane;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getRouteIcon() {
		return routeIcon;
	}

	public void setRouteIcon(String routeIcon) {
		this.routeIcon = routeIcon;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIsStarred() {
		return isStarred;
	}

	public void setIsStarred(int isStarred) {
		this.isStarred = isStarred;
	}
}