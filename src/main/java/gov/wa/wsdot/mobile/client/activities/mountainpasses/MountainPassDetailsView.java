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

package gov.wa.wsdot.mobile.client.activities.mountainpasses;

import gov.wa.wsdot.mobile.shared.CameraItem;
import gov.wa.wsdot.mobile.shared.ForecastItem;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

public interface MountainPassDetailsView extends IsWidget {
	
	public void setPresenter(Presenter presenter);
	
	public interface Presenter {
		
		public void onCameraSelected(int index);
		
		public void onBackButtonPressed();
		
		public void onStarButtonPressed();
		
	}
	
	public void renderCamera(List<CameraItem> createCameraList);
	
	public void setCameraSelected(int lastIndex, boolean b);
	
	public void renderForecast(List<ForecastItem> createForecastList);
	
	public void setTitle(String title);
	
	public void toggleStarButton(boolean isStarred);
	
	public void setDateUpdated(String dateUpdated);
	
	public void setWeatherCondition(String weatherCondition);
	
	public void setTemperatureInFahrenheit(String temperatureInFahrenheit);
	
	public void setElevationInFeet(String elevationInFeet);
	
	public void setRoadCondition(String roadCondition);
	
	public void setRestrictionOneTravelDirection(String restrictionOneTravelDirection);
	
	public void setRestrictionOneText(String restrictionOneText);
	
	public void setRestrictionTwoTravelDirection(String restrictionTwoTravelDirection);
	
	public void setRestrictionTwoText(String restrictionTwoText);

	public void removeTab(int tabIndex);
	
	public void refreshReport();
	
	public void refreshCameras();
	
	public void refreshForecast();
	
}
