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

package gov.wa.wsdot.mobile.client.activities.trafficmap.expresslanes;

import gov.wa.wsdot.mobile.client.util.ParserUtils;
import gov.wa.wsdot.mobile.client.widget.CellDetailsWithIcon;
import gov.wa.wsdot.mobile.shared.ExpressLaneItem;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.widget.CellList;
import com.googlecode.mgwt.ui.client.widget.HeaderButton;
import com.googlecode.mgwt.ui.client.widget.ProgressBar;
import com.googlecode.mgwt.ui.client.widget.base.HasRefresh;
import com.googlecode.mgwt.ui.client.widget.base.PullArrowHeader;
import com.googlecode.mgwt.ui.client.widget.base.PullArrowWidget;
import com.googlecode.mgwt.ui.client.widget.base.PullPanel;
import com.googlecode.mgwt.ui.client.widget.base.PullPanel.Pullhandler;

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
	HeaderButton doneButton;
	
	@UiField(provided = true)
	CellList<ExpressLaneItem> cellList;
	
	@UiField(provided = true)
	PullPanel pullToRefresh;
	
	@UiField
	FlowPanel flowPanel;
	
	@UiField
	ProgressBar progressBar;
	
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
		
		cellList.setGroup(false);
		cellList.setRound(false);
	
		initWidget(uiBinder.createAndBindUi(this));

	}

	@UiHandler("doneButton")
	protected void onDoneButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onDoneButtonPressed();
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
	public void showProgressBar() {
		progressBar.setVisible(true);
	}

	@Override
	public void hideProgressBar() {
		progressBar.setVisible(false);
	}

	@Override
	public void refresh() {
		pullToRefresh.refresh();
	}

	@Override
	public void setHeaderPullHandler(Pullhandler pullHandler) {
		pullToRefresh.setHeaderPullhandler(pullHandler);
	}

	@Override
	public PullArrowWidget getPullHeader() {
		return pullArrowHeader;
	}

	@Override
	public HasRefresh getPullPanel() {
		return pullToRefresh;
	}

}