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

import gov.wa.wsdot.mobile.shared.FerriesRouteAlertItem;
import gov.wa.wsdot.mobile.shared.FerriesTerminalItem;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

public interface FerriesRouteSailingsView extends IsWidget {
	
	public void setPresenter(Presenter presenter);
	
	public interface Presenter {
		
		public void onSailingItemSelected(int index);
		
		public void onRouteAlertSelected(int index);
		
		public void onBackButtonPressed();
		
		public void onStarButtonPressed();
		
	}
	
	public void render(List<FerriesTerminalItem> createTopicsList);
	
	public void renderRouteAlerts(List<FerriesRouteAlertItem> createRouteAlertsList);
	
	public void setSelected(int lastIndex, boolean b);
	
	public void setTitle(String title);
	
	public void toggleStarButton(boolean isStarred);
	
	public void showProgressBar();
	
	public void hideProgressBar();
	
	public void refresh();
	
}
