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

import com.google.gwt.aria.client.Roles;
import com.google.gwt.aria.client.SelectedValue;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.base.HasRefresh;
import com.googlecode.mgwt.ui.client.widget.header.HeaderTitle;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellList;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.flex.RootFlexPanel;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowHeader;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowWidget;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel.Pullhandler;
import com.googlecode.mgwt.ui.client.widget.progress.ProgressIndicator;
import com.googlecode.mgwt.ui.client.widget.tabbar.TabPanel;
import gov.wa.wsdot.mobile.client.util.ParserUtils;
import gov.wa.wsdot.mobile.client.widget.button.image.BackImageButton;
import gov.wa.wsdot.mobile.client.widget.tabbar.NorthboundTabBarButton;
import gov.wa.wsdot.mobile.client.widget.tabbar.SouthboundTabBarButton;
import gov.wa.wsdot.mobile.shared.BorderWaitItem;

import java.util.List;

public class BorderWaitViewGwtImpl extends Composite implements BorderWaitView {

	/**
	 * The UiBinder interface.
	 */	
	interface BorderWaitViewGwtImplUiBinder extends
			UiBinder<Widget, BorderWaitViewGwtImpl> {
	}

	/**
	 * The UiBinder used to generate the view.
	 */
	private static BorderWaitViewGwtImplUiBinder uiBinder = GWT
			.create(BorderWaitViewGwtImplUiBinder.class);

	@UiField
	HeaderTitle heading;
	
	@UiField
	BackImageButton backButton;
	
	@UiField
	FlexSpacer leftFlexSpacer;
	
	@UiField(provided =  true)
	PullPanel northboundPullToRefresh;
	
	@UiField(provided = true)
	CellList<BorderWaitItem> northbound;
	
	@UiField(provided = true)
	PullPanel southboundPullToRefresh;
	
	@UiField(provided = true)
	CellList<BorderWaitItem> southbound;
	
	@UiField
	ProgressIndicator northboundProgressIndicator;
	
	@UiField
	ProgressIndicator southboundProgressIndicator;
	
	@UiField
	RootFlexPanel northPanel;
	
	@UiField
	RootFlexPanel southPanel;
	
	@UiField
	TabPanel tabPanel;
	
	@UiField
	NorthboundTabBarButton northTab;
	
	@UiField
	SouthboundTabBarButton southTab;
	
	private Presenter presenter;
	private PullArrowHeader northboundPullArrowHeader;
	private PullArrowHeader southboundPullArrowHeader;
	
	public BorderWaitViewGwtImpl() {
		
		northboundPullToRefresh = new PullPanel();
		northboundPullArrowHeader = new PullArrowHeader();
		northboundPullToRefresh.setHeader(northboundPullArrowHeader);
		
		southboundPullToRefresh = new PullPanel();
		southboundPullArrowHeader = new PullArrowHeader();
		southboundPullToRefresh.setHeader(southboundPullArrowHeader);
		
		northbound = new CellList<BorderWaitItem>(
				new BorderWaitCell<BorderWaitItem>() {

			@Override
			public String getDisplayImage(BorderWaitItem model) {
				return model.getRouteIcon();
			}

			@Override
			public String getDisplayString(BorderWaitItem model) {
				return model.getTitle() + " (" + model.getLane() + ")";
			}

			@Override
			public String getDisplayLastUpdated(BorderWaitItem model) {
				return ParserUtils.relativeTime(model.getUpdated(),
						"yyyy-MM-dd h:mm a", false);
			}

			@Override
			public String getDisplayWaitTime(BorderWaitItem model) {
				int wait = model.getWait();
				String waitTime;
				
				if (wait == -1) {
					waitTime = "N/A";
				} else if (wait < 5) {
					waitTime = "< 5 min";
				} else {
					waitTime = wait + " min";
				}
				
				return waitTime;
			}

		});
		
		southbound = new CellList<BorderWaitItem>(
				new BorderWaitCell<BorderWaitItem>() {

			@Override
			public String getDisplayImage(BorderWaitItem model) {
				return model.getRouteIcon();
			}

			@Override
			public String getDisplayString(BorderWaitItem model) {
				return model.getTitle() + " (" + model.getLane() + ")";
			}

			@Override
			public String getDisplayLastUpdated(BorderWaitItem model) {
				return ParserUtils.relativeTime(model.getUpdated(),
						"yyyy-MM-dd h:mm a", false);
			}

			@Override
			public String getDisplayWaitTime(BorderWaitItem model) {
				int wait = model.getWait();
				String waitTime;
				
				if (wait == -1) {
					waitTime = "N/A";
				} else if (wait < 5) {
					waitTime = "< 5 min";
				} else {
					waitTime = wait + " min";
				}
				
				return waitTime;
			}

		});
		
		initWidget(uiBinder.createAndBindUi(this));

		accessibilityPrepare();
		
        if (MGWT.getOsDetection().isAndroid()) {
            leftFlexSpacer.setVisible(false);
        }
	}

    @UiHandler("tabPanel")
    protected void onTabSelected(SelectionEvent<Integer> event) {
        if (presenter != null) {
            int index = event.getSelectedItem();
            presenter.onTabSelected(index);
        }
    }

	@UiHandler("backButton")
	protected void onBackButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onBackButtonPressed();
		}
	}	
	
    @UiHandler("northTab")
    protected void on167TabPressed(TapEvent event) {
    		accessibilityShowNorth();
    }
    
    @UiHandler("southTab")
    protected void on405TabPressed(TapEvent event) {
    		accessibilityShowSouth();
    }
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void renderNorthbound(List<BorderWaitItem> createNorthboundList) {
		northbound.render(createNorthboundList);
	}

	@Override
	public void renderSouthbound(List<BorderWaitItem> createSouthboundList) {
		southbound.render(createSouthboundList);
	}

	@Override
	public void refresh() {
        tabPanel.tabContainer.refresh();
	    northboundPullToRefresh.refresh();
		southboundPullToRefresh.refresh();
	}

	@Override
	public void showProgressIndicator() {
		northboundProgressIndicator.setVisible(true);
		southboundProgressIndicator.setVisible(true);
	}

	@Override
	public void hideProgressIndicator() {
		northboundProgressIndicator.setVisible(false);
		southboundProgressIndicator.setVisible(false);
	}

	@Override
	public void setNorthboundHeaderPullHandler(Pullhandler pullHandler) {
		northboundPullToRefresh.setHeaderPullHandler(pullHandler);
	}

	@Override
	public void setSouthboundHeaderPullHandler(Pullhandler pullHandler) {
		southboundPullToRefresh.setHeaderPullHandler(pullHandler);
	}

	@Override
	public PullArrowWidget getNorthboundPullHeader() {
		return northboundPullArrowHeader;
	}

	@Override
	public PullArrowWidget getSouthboundPullHeader() {
		return southboundPullArrowHeader;
	}

	@Override
	public HasRefresh getNorthboundPullPanel() {
		return northboundPullToRefresh;
	}

	@Override
	public HasRefresh getSouthboundPullPanel() {
		return southboundPullToRefresh;
	}
	private void accessibilityShowNorth(){
		Roles.getMainRole().setAriaHiddenState(northPanel.getElement(), false);
		Roles.getMainRole().setAriaHiddenState(southPanel.getElement(), true);
		
		Roles.getTabRole().setAriaSelectedState(northTab.getElement(), SelectedValue.TRUE);
		Roles.getTabRole().setAriaSelectedState(southTab.getElement(), SelectedValue.FALSE);
	}
	
	private void accessibilityShowSouth(){
		Roles.getMainRole().setAriaHiddenState(northPanel.getElement(), true);
		Roles.getMainRole().setAriaHiddenState(southPanel.getElement(), false);
		
		Roles.getTabRole().setAriaSelectedState(northTab.getElement(), SelectedValue.FALSE);
		Roles.getTabRole().setAriaSelectedState(southTab.getElement(), SelectedValue.TRUE);
	}
    
    private void accessibilityPrepare(){
		
		// Add ARIA roles for accessibility
		Roles.getButtonRole().set(backButton.getElement());
		Roles.getButtonRole().setAriaLabelProperty(backButton.getElement(), "back");
		
		Roles.getHeadingRole().set(heading.getElement());
		
		Roles.getMainRole().set(northPanel.getElement());
		Roles.getMainRole().set(southPanel.getElement());
		
		Roles.getTabRole().set(northTab.getElement());
		Roles.getTabRole().setAriaSelectedState(northTab.getElement(), SelectedValue.TRUE);
		Roles.getTabRole().setAriaLabelProperty(northTab.getElement(), "north bound");
		
		Roles.getTabRole().set(southTab.getElement());
		Roles.getTabRole().setAriaSelectedState(southTab.getElement(), SelectedValue.FALSE);
		Roles.getTabRole().setAriaLabelProperty(southTab.getElement(), "south bound");
		
		Roles.getProgressbarRole().set(northboundProgressIndicator.getElement());
		Roles.getProgressbarRole().setAriaLabelProperty(northboundProgressIndicator.getElement(), "loading indicator");
		
		Roles.getProgressbarRole().set(southboundProgressIndicator.getElement());
		Roles.getProgressbarRole().setAriaLabelProperty(southboundProgressIndicator.getElement(), "loading indicator");

		// TODO Hide pull down until we can figure out how to get VoiceOver to work with it
		Roles.getButtonRole().setAriaHiddenState(northboundPullArrowHeader.getElement(), true);
        Roles.getButtonRole().setAriaHiddenState(southboundPullArrowHeader.getElement(), true);

		accessibilityShowNorth();	
	}
}
