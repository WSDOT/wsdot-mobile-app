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

import gov.wa.wsdot.mobile.client.css.AppBundle;
import gov.wa.wsdot.mobile.shared.CameraItem;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.widget.button.Button;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellList;
import com.googlecode.mgwt.ui.client.widget.header.HeaderButton;
import com.googlecode.mgwt.ui.client.widget.header.HeaderTitle;
import com.googlecode.mgwt.ui.client.widget.base.HasRefresh;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowHeader;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowWidget;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel.Pullhandler;
import com.googlecode.mgwt.ui.client.widget.tabbar.TabPanel;

public class CameraViewGwtImpl extends Composite implements CameraView {

	/**
	 * The UiBinder interface.
	 */	
	interface CameraViewGwtImplUiBinder extends
			UiBinder<Widget, CameraViewGwtImpl> {
	}
	
	/**
	 * The UiBinder used to generate the view.
	 */
	private static CameraViewGwtImplUiBinder uiBinder = GWT
			.create(CameraViewGwtImplUiBinder.class);

	@UiField
	HeaderButton backButton;
	
	@UiField
	HeaderTitle title;
	
	@UiField
	Button starButton;
	
	@UiField
	TabPanel tabPanel;
	
	@UiField(provided = true)
	PullPanel cameraPullToRefresh;

    @UiField(provided = true)
    PullPanel videoPullToRefresh;
	
	private PullArrowHeader cameraPullArrowHeader;
	private PullArrowHeader videoPullArrowHeader;
	
	@UiField(provided = true)
	CellList<CameraItem> cameraCellList;
	
	@UiField(provided = true)
	CellList<CameraItem> videoCellList;
	
	private Presenter presenter;
	
	public CameraViewGwtImpl() {

		cameraPullToRefresh = new PullPanel();
		cameraPullArrowHeader = new PullArrowHeader();
		cameraPullToRefresh.setHeader(cameraPullArrowHeader);
		
		videoPullToRefresh = new PullPanel();
		videoPullArrowHeader = new PullArrowHeader();
		videoPullToRefresh.setHeader(videoPullArrowHeader);
		
		cameraCellList = new CellList<CameraItem>(new CameraCell<CameraItem>() {

			@Override
			public String getUrl(CameraItem model) {
				return model.getImageUrl();
			}

			@Override
			public boolean canBeSelected(CameraItem model) {
				return false;
			}
		});
	
        videoCellList = new CellList<CameraItem>(new VideoCell<CameraItem>() {

            @Override
            public String getUrl(CameraItem model) {
                return model.getImageUrl();
            }

            @Override
            public String getVideoUrl(CameraItem model) {
                return model.getVideoUrl();
            }
            
            @Override
            public boolean canBeSelected(CameraItem model) {
                return false;
            }

        });
        
		initWidget(uiBinder.createAndBindUi(this));
		
	}
	
	@UiHandler("backButton")
	protected void onBackButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onBackButtonPressed();
		}
	}	
	
	@UiHandler("starButton")
	protected void onStarButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onStarButtonPressed();
		}
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void renderCamera(List<CameraItem> createCameraList) {
		cameraCellList.render(createCameraList);		
	}

    @Override
    public void renderVideo(List<CameraItem> createVideoList) {
        videoCellList.render(createVideoList);
    }
	
	@Override
	public void setTitle(String title) {
		this.title.setText(title);
	}

	@Override
	public void cameraRefresh() {
		cameraPullToRefresh.refresh();
	}

    @Override
    public void videoRefresh() {
        videoPullToRefresh.refresh();
    }
	
	@SuppressWarnings("deprecation")
    @Override
	public void setCameraHeaderPullHandler(Pullhandler pullHandler) {
		cameraPullToRefresh.setHeaderPullHandler(pullHandler);		
	}

	@Override
	public PullArrowWidget getCameraPullHeader() {
		return cameraPullArrowHeader;
	}

	@Override
	public HasRefresh getCameraPullPanel() {
		return cameraPullToRefresh;
	}


    @Override
    public void setVideoHeaderPullHandler(Pullhandler pullHandler) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public PullArrowWidget getVideoPullHeader() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HasRefresh getVideoPullPanel() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void removeTab(int tabIndex) {
        //this.tabPanel.remove(tabIndex);
    }
    
	@Override
	public void toggleStarButton(boolean isStarred) {
		if (isStarred) {
			starButton.setStyleName(AppBundle.INSTANCE.css().starButtonOn());
		} else {
			starButton.setStyleName(AppBundle.INSTANCE.css().starButtonOff());
		}
	}

}
