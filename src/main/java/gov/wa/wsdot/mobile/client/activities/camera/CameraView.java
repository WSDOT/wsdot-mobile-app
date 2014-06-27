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

package gov.wa.wsdot.mobile.client.activities.camera;

import gov.wa.wsdot.mobile.shared.CameraItem;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.googlecode.mgwt.ui.client.widget.base.HasRefresh;
import com.googlecode.mgwt.ui.client.widget.base.PullArrowWidget;
import com.googlecode.mgwt.ui.client.widget.base.PullPanel.Pullhandler;

public interface CameraView extends IsWidget {
	
	public void setPresenter(Presenter presenter);
	
	public interface Presenter {
		
		public void onBackButtonPressed();
		
		public void onStarButtonPressed();
		
	}
	
	public void renderCamera(List<CameraItem> createCameraList);
	
	public void renderVideo(List<CameraItem> createVideoList);

	public void setTitle(String title);
	
	public void toggleStarButton(boolean isStarred);
	
	public void cameraRefresh();
	
	public void videoRefresh();

	public void setCameraHeaderPullHandler(Pullhandler pullHandler);
	
	public void setVideoHeaderPullHandler(Pullhandler pullHandler);
	
	public PullArrowWidget getCameraPullHeader();
	
	public PullArrowWidget getVideoPullHeader();
	
	public HasRefresh getCameraPullPanel();
	
	public HasRefresh getVideoPullPanel();

    public void removeTab(int tabIndex);
}
