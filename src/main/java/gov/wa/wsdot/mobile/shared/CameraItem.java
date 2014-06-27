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

import com.google.gwt.safehtml.shared.SafeHtml;

public class CameraItem implements Serializable {
	private static final long serialVersionUID = 7852844361445836316L;
	private String title;
	private String imageUrl;
	private String videoUrl;
	private String roadName;
	private Double longitude;
	private Double latitude;
	private SafeHtml image;
	private int cameraId;
	private int isStarred;
	private int hasVideo;
	
	/**
	 * 
	 * @param cameraId Camera id
	 * @param title Camera display title
	 * @param imageUrl Camera url
	 * @param latitude Camera latitude
	 * @param longitude Camera longitude
	 * @param hasVideo Whether or not the camera has a video feed available
	 */
	public CameraItem(int cameraId, String title, String imageUrl,
			Double latitude, Double longitude, int hasVideo) {
		this.cameraId = cameraId;
		this.title = title;
		this.imageUrl = imageUrl;
		this.latitude = latitude;
		this.longitude = longitude;
		this.hasVideo = hasVideo;
	}
	
	/**
	 * 
	 * @param cameraId Camera id
	 * @param title Camera display title
	 * @param imageUrl Camera url
	 * @param latitude Camera latitude
	 * @param longitude Camera longitude
	 * @param hasVideo Whether or not the camera has a video feed available
	 * @param roadName Highway or location the camera is located on
	 * @param isStarred Whether or not the camera is starred as a favorite
	 */
	public CameraItem(int cameraId, String title, String imageUrl,
			Double latitude, Double longitude, int hasVideo, String roadName,
			int isStarred) {
		this.cameraId = cameraId;
		this.title = title;
		this.imageUrl = imageUrl;
		this.latitude = latitude;
		this.longitude = longitude;
		this.hasVideo = hasVideo;
		this.roadName = roadName;
		this.isStarred = isStarred;
	}
	
	public CameraItem() {
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public String getRoadName() {
		return roadName;
	}
	
	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}
	
	public Double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	public Double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public SafeHtml getImage() {
		return image;
	}
	public void setImage(SafeHtml image) {
		this.image = image;
	}
	
	public int getCameraId() {
		return cameraId;
	}
	
	public void setCameraId(int cameraId) {
		this.cameraId = cameraId;
	}
	
	public int getIsStarred() {
		return isStarred;
	}
	
	public void setIsStarred(int isStarred) {
		this.isStarred = isStarred;
	}
	
	public int getHasVideo() {
		return hasVideo;
	}
	
	public void setHasVideo(int hasVideo) {
		this.hasVideo = hasVideo;
	}

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}