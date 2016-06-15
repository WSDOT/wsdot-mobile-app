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

package gov.wa.wsdot.mobile.shared;

/**
 * Created by simsl on 4/6/16.
 */
public class LocationItem {

    private String title;
    private Double latitude;
    private Double longitude;
    private int zoom;
    private int id;

    public LocationItem (String title, Double latitude, Double longitude, int zoom){
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.zoom = zoom;
    }

    public LocationItem (int id, String title, Double latitude, Double longitude, int zoom){
        this.id = id;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.zoom = zoom;
    }

    public int getId() { return this.id;}

    public String getTitle(){
        return this.title;
    }

    public Double getLatitude(){
        return this.latitude;
    }

    public Double getLongitude(){
        return this.longitude;
    }

    public int getZoom(){
        return this.zoom;
    }
}
