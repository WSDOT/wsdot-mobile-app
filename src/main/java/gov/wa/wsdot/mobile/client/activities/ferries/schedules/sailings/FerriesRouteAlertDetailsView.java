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

package gov.wa.wsdot.mobile.client.activities.ferries.schedules.sailings;

import com.google.gwt.user.client.ui.IsWidget;

public interface FerriesRouteAlertDetailsView extends IsWidget {
	
	public void setPresenter(Presenter presenter);
	
	public interface Presenter {
		
		public void onBackButtonPressed();
		
	}
	
	public void setPublishDate(String publishDate);
	
	public void setAlertTitle(String alertTitle);
	
	public void setAlertText(String content);
	
}