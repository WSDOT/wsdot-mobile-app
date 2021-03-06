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

package gov.wa.wsdot.mobile.client.activities.trafficmap.menu.expresslanes;

import com.google.gwt.aria.client.Roles;
import com.google.gwt.core.client.GWT;
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
import gov.wa.wsdot.mobile.client.util.ParserUtils;
import gov.wa.wsdot.mobile.client.widget.CellDetailsWithIcon;
import gov.wa.wsdot.mobile.client.widget.button.image.BackImageButton;
import gov.wa.wsdot.mobile.client.widget.celllist.MyBasicCell;
import gov.wa.wsdot.mobile.shared.ExpressLaneItem;
import gov.wa.wsdot.mobile.shared.Topic;

import java.util.List;

public class SeattleExpressLanesViewGwtImpl extends Composite implements
		SeattleExpressLanesView {

	/**
	 * The UiBinder interface.
	 */	
	interface SeattleExpressLanesViewGwtImplUiBinder extends
			UiBinder<Widget, SeattleExpressLanesViewGwtImpl> {
	}

	/**
	 * The UiBinder used to generate the view.
	 */
	private static SeattleExpressLanesViewGwtImplUiBinder uiBinder = GWT
			.create(SeattleExpressLanesViewGwtImplUiBinder.class);

	@UiField
	HeaderTitle heading;

	@UiField
	BackImageButton backButton;
	
	@UiField
	FlexSpacer leftFlexSpacer;
	
	@UiField(provided = true)
	CellList<ExpressLaneItem> cellList;
	
	@UiField(provided = true)
	CellList<Topic> schedules;
	
	@UiField(provided = true)
	PullPanel pullToRefresh;
	
	@UiField
	FlowPanel flowPanel;
	
	@UiField
	ProgressIndicator progressIndicator;
	
	private Presenter presenter;
	private PullArrowHeader pullArrowHeader;
	
	public SeattleExpressLanesViewGwtImpl() {
		
		pullToRefresh = new PullPanel();
		pullArrowHeader = new PullArrowHeader();
		pullToRefresh.setHeader(pullArrowHeader);
		
		cellList = new CellList<ExpressLaneItem>(new CellDetailsWithIcon<ExpressLaneItem>() {


			@Override
			public String getDisplayImage(ExpressLaneItem model) {
				return model.getRouteIcon();
			}
			
			@Override
			public String getDisplayString(ExpressLaneItem model) {
				return model.getTitle() + " " + model.getStatus();
			}

			@Override
			public String getDisplayDescription(ExpressLaneItem model) {
				return "";
			}

			@Override
			public String getDisplayLastUpdated(ExpressLaneItem model) {
				return ParserUtils.relativeTime(model.getUpdated(), "yyyy-MM-dd h:mm a", false);
			}
		});
	
		
		schedules = new CellList<Topic>(new MyBasicCell<Topic>() {

			@Override
			public String getDisplayString(Topic model) {
				return model.getName();
			}

			@Override
			public boolean canBeSelected(Topic model) {
				return true;
			}
		});
		
		
		initWidget(uiBinder.createAndBindUi(this));

		accessibilityPrepare();

		if (MGWT.getOsDetection().isAndroid()) {
            leftFlexSpacer.setVisible(false);
        }
	}

	@UiHandler("backButton")
	protected void onBackButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onBackButtonPressed();
		}
	}
	

	@UiHandler("schedules")
	protected void onCellSelected(CellSelectedEvent event) {
		if (presenter != null) {
			int index = event.getIndex();
			presenter.onItemSelected(index);
		}
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void render(List<ExpressLaneItem> createPostList) {
		cellList.render(createPostList);
	}
	
	@Override
	public void scheduleRender(List<Topic> createPostList) {
		schedules.render(createPostList);
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
	
	@Override
	public void setSelected(int lastIndex, boolean b) {
		schedules.setSelectedIndex(lastIndex, b);
	}

	private void accessibilityPrepare(){
		// Add ARIA roles for accessibility
		Roles.getHeadingRole().set(heading.getElement());

		// TODO Hide pull down until we can figure out how to get VoiceOver to work with it
		Roles.getButtonRole().setAriaHiddenState(pullArrowHeader.getElement(), true);
	}

}