/*
 * Copyright (c) 2015 Washington State Department of Transportation
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

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.base.HasRefresh;
import com.googlecode.mgwt.ui.client.widget.button.image.NotimportantImageButton;
import com.googlecode.mgwt.ui.client.widget.image.ImageHolder;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellList;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowHeader;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowWidget;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel.Pullhandler;
import com.googlecode.mgwt.ui.client.widget.tabbar.TabPanel;
import gov.wa.wsdot.mobile.client.widget.button.image.BackImageButton;
import gov.wa.wsdot.mobile.shared.CameraItem;

import java.util.List;

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
	BackImageButton backButton;
	
	@UiField
	FlexSpacer leftFlexSpacer;
	
	@UiField(provided = true)
	NotimportantImageButton starButton;
	
	@UiField
	TabPanel tabPanel;
	
	@UiField(provided = true)
    static
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
		
		starButton = new NotimportantImageButton();
		
		handleOnLoad();
		
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

        if (MGWT.getOsDetection().isAndroid()) {
            leftFlexSpacer.setVisible(false);
        }
	}
	
    /**
     * PullPanel doesn't allow scrolling to the bottom if it contains a CellList with images.
     * 
     * See: https://code.google.com/p/mgwt/issues/detail?id=276
     * 
     * PullPanel.refresh() must be explicitly called after the images are loaded.
     * Since the onload event of images is not bubbling up, the LoadHandler can't be attached
     * to the CellList. Instead, the onload event needs to be captured at the <img>, and directly
     * trigger the PullPanel.refresh() from there.
     */
    private native void handleOnLoad() /*-{
        $wnd.refreshPanel = @gov.wa.wsdot.mobile.client.activities.camera.CameraViewGwtImpl::refreshPanel();
    }-*/;
        
    public static void refreshPanel() {
        cameraPullToRefresh.refresh();
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
	public void cameraRefresh() {
		cameraPullToRefresh.refresh();
	}

    @Override
    public void videoRefresh() {
        videoPullToRefresh.refresh();
    }
	
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
        videoPullToRefresh.setHeaderPullHandler(pullHandler);
    }

    @Override
    public PullArrowWidget getVideoPullHeader() {
        return videoPullArrowHeader;
    }

    @Override
    public HasRefresh getVideoPullPanel() {
        return videoPullToRefresh;
    }

    @Override
    public void removeTab(int tabIndex) {
        this.tabPanel.tabBar.remove(tabIndex);
        this.tabPanel.tabContainer.container.remove(tabIndex);
        this.tabPanel.tabContainer.refresh();
    }
    
	@Override
	public void toggleStarButton(boolean isStarred) {
		if (isStarred) {
			starButton.setIcon(ImageHolder.get().important());
		} else {
			starButton.setIcon(ImageHolder.get().notImportant());
		}
	}

}
