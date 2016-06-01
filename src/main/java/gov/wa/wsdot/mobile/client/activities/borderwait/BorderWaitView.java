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

package gov.wa.wsdot.mobile.client.activities.borderwait;

import com.google.gwt.user.client.ui.IsWidget;
import com.googlecode.mgwt.ui.client.widget.base.HasRefresh;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowWidget;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel.Pullhandler;
import gov.wa.wsdot.mobile.shared.BorderWaitItem;

import java.util.List;

public interface BorderWaitView extends IsWidget {
	
	public void setPresenter(Presenter presenter);

	public interface Presenter {

		public void onBackButtonPressed();

		public void onTabSelected(int index);

	}

	public void renderNorthbound(List<BorderWaitItem> createNorthboundList);
	
	public void renderSouthbound(List<BorderWaitItem> createSouthboundList);
	
	public void showProgressIndicator();
	
	public void hideProgressIndicator();
	
	public void refresh();
	
	public void setNorthboundHeaderPullHandler(Pullhandler pullHandler);
	
	public void setSouthboundHeaderPullHandler(Pullhandler pullHandler);
	
	public PullArrowWidget getNorthboundPullHeader();
	
	public PullArrowWidget getSouthboundPullHeader();
	
	public HasRefresh getNorthboundPullPanel();
	
	public HasRefresh getSouthboundPullPanel();
	
}
