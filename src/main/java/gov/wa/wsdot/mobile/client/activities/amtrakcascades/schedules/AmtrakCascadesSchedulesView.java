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

package gov.wa.wsdot.mobile.client.activities.amtrakcascades.schedules;

import gov.wa.wsdot.mobile.shared.AmtrakCascadesStationItem;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

public interface AmtrakCascadesSchedulesView extends IsWidget {
	
	public void setPresenter(Presenter presenter);
	
	public interface Presenter {
		
		public void onBackButtonPressed();
		
		public void onSubmitButtonPressed();
		
	}
	
    public void showProgressIndicator();

    public void hideProgressIndicator();
	
	public void renderDaysOfWeek(List<String> days);
	
	public String getDayOfWeekSelected();
	
    public void renderFromLocation(List<AmtrakCascadesStationItem> stations);
    
	public void renderToLocation(List<AmtrakCascadesStationItem> stations);
	
	public String getFromLocationSelected();
	
	public String getToLocationSelected();
	
	public void setLocationEnabled(boolean locationEnabled);
    
}