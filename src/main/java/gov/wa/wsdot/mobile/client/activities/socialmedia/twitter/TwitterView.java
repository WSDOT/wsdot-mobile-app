/*
 * Copyright (c) 2014shington State Department of Transportation
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

package gov.wa.wsdot.mobile.client.activities.socialmedia.twitter;

import gov.wa.wsdot.mobile.shared.TwitterItem;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.googlecode.mgwt.ui.client.widget.base.HasRefresh;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowWidget;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel.Pullhandler;

public interface TwitterView extends IsWidget {
	
	public void setPresenter(Presenter presenter);
	
	public interface Presenter {
		
		public void onItemSelected(int index);
		
		public void onBackButtonPressed();
		
		public void onAccountSelected(String screenName);
		
	}
	
	public void render(List<TwitterItem> createPostList);
	
	public void setSelected(int lastIndex, boolean b);

	public void showProgressBar();
	
	public void hideProgressBar();
	
	public void refresh();
	
	public String getAccountSelected();

	public void setHeaderPullHandler(Pullhandler pullHandler);
	
	public PullArrowWidget getPullHeader();
	
	public HasRefresh getPullPanel();

}
