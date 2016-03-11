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

package gov.wa.wsdot.mobile.client.activities.socialmedia.youtube;

import gov.wa.wsdot.mobile.client.util.ParserUtils;
import gov.wa.wsdot.mobile.client.widget.button.image.BackImageButton;
import gov.wa.wsdot.mobile.shared.YouTubeItem;

import java.util.List;

import com.google.gwt.aria.client.LiveValue;
import com.google.gwt.aria.client.Roles;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.base.HasRefresh;
import com.googlecode.mgwt.ui.client.widget.header.HeaderTitle;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellList;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellSelectedEvent;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowHeader;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowWidget;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel.Pullhandler;
import com.googlecode.mgwt.ui.client.widget.progress.ProgressIndicator;

public class YouTubeViewGwtImpl extends Composite implements YouTubeView {

	/**
	 * The UiBinder interface.
	 */	
	interface YouTubeViewGwtImplUiBinder extends
			UiBinder<Widget, YouTubeViewGwtImpl> {
	}
	
	/**
	 * The UiBinder used to generate the view.
	 */
	private static YouTubeViewGwtImplUiBinder uiBinder = GWT
			.create(YouTubeViewGwtImplUiBinder.class);
	
	@UiField
	HeaderTitle heading;
	
	@UiField(provided = true)
	CellList<YouTubeItem> cellList;
	
	@UiField
	BackImageButton backButton;

	@UiField
	FlexSpacer leftFlexSpacer;
	
	@UiField(provided = true)
    static
	PullPanel pullToRefresh;
	
	@UiField
	FlowPanel flowPanel;
	
	@UiField
	ProgressIndicator progressIndicator;
	
	private Presenter presenter;
	private PullArrowHeader pullArrowHeader;
	
	public YouTubeViewGwtImpl() {
		
		pullToRefresh = new PullPanel();
		pullArrowHeader = new PullArrowHeader();
		pullToRefresh.setHeader(pullArrowHeader);
		
		handleOnLoad();
		
		cellList = new CellList<YouTubeItem>(new YouTubeCell<YouTubeItem>() {

			@Override
			public SafeUri getThumbnailUrl(YouTubeItem model) {
				return UriUtils.fromString(model.getThumbnailUrl());
			}
			
			@Override
			public String getDescription(YouTubeItem model) {
				String shortDescription = ParserUtils.ellipsis(
						model.getDescription(), 128);
		
				return shortDescription;
			}

			@Override
			public String getUploaded(YouTubeItem model) {
				return ParserUtils.relativeTime(model.getUploaded(),
						"yyyy-MM-dd'T'HH:mm:ss.SSSZ", true);
			}

			@Override
			public boolean canBeSelected(YouTubeItem model) {
				return true;
			}

		});

		initWidget(uiBinder.createAndBindUi(this));
        
		accessibilityPrepare();
		
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
        $wnd.refreshPanel = @gov.wa.wsdot.mobile.client.activities.socialmedia.youtube.YouTubeViewGwtImpl::refreshPanel();
    }-*/;
        
    public static void refreshPanel() {
        pullToRefresh.refresh();
    }
	
	@UiHandler("cellList")
	protected void onCellSelected(CellSelectedEvent event) {
		if (presenter != null) {
			int index = event.getIndex();
			presenter.onItemSelected(index);
		}
	}

	@UiHandler("backButton")
	protected void onBackButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onBackButtonPressed();
		}
	}	
	
	@Override
	public void render(List<YouTubeItem> createPostList) {
		cellList.render(createPostList);
	}

	@Override
	public void showProgressIndicator() {
		progressIndicator.setVisible(true);
	}

	@Override
	public void hideProgressIndicator() {
		progressIndicator.setVisible(false);
	}

	@Override
	public void refresh() {
		pullToRefresh.refresh();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setSelected(int lastIndex, boolean b) {
		cellList.setSelectedIndex(lastIndex, b);
	}
	
	@Override
	public void setHeaderPullHandler(Pullhandler pullHandler) {
		pullToRefresh.setHeaderPullHandler(pullHandler);
	}

	@Override
	public PullArrowWidget getPullHeader() {
		return pullArrowHeader;
	}

	@Override
	public HasRefresh getPullPanel() {
		return pullToRefresh;
	}
	private void accessibilityPrepare(){
		// Add ARIA roles for accessibility
		Roles.getButtonRole().set(backButton.getElement());
		Roles.getButtonRole().setAriaLabelProperty(backButton.getElement(), "back");
		
		Roles.getHeadingRole().set(heading.getElement());
		
		Roles.getProgressbarRole().set(progressIndicator.getElement());
		Roles.getProgressbarRole().setAriaLabelProperty(progressIndicator.getElement(), "loading indicator");
	}
}
