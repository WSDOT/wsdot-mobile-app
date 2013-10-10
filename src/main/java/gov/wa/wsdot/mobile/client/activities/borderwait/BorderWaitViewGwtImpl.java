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

package gov.wa.wsdot.mobile.client.activities.borderwait;

import gov.wa.wsdot.mobile.client.util.ParserUtils;
import gov.wa.wsdot.mobile.shared.BorderWaitItem;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
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
	HeaderButton backButton;
	
	@UiField(provided =  true)
	PullPanel northboundPullToRefresh;
	
	@UiField(provided = true)
	CellList<BorderWaitItem> northbound;
	
	@UiField(provided = true)
	PullPanel southboundPullToRefresh;
	
	@UiField(provided = true)
	CellList<BorderWaitItem> southbound;
	
	@UiField
	ProgressBar northboundProgressBar;
	
	@UiField
	ProgressBar southboundProgressBar;
	
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
		
		northbound.setGroup(false);
		northbound.setRound(true);

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
		
		southbound.setGroup(false);
		southbound.setRound(true);
		
		initWidget(uiBinder.createAndBindUi(this));
		
	}
	
	@UiHandler("backButton")
	protected void onBackButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onBackButtonPressed();
		}
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
		northboundPullToRefresh.refresh();
		southboundPullToRefresh.refresh();		
	}

	@Override
	public void showProgressBar() {
		northboundProgressBar.setVisible(true);
		southboundProgressBar.setVisible(true);
	}

	@Override
	public void hideProgressBar() {
		northboundProgressBar.setVisible(false);
		southboundProgressBar.setVisible(false);
	}

	@Override
	public void setNorthboundHeaderPullHandler(Pullhandler pullHandler) {
		northboundPullToRefresh.setHeaderPullhandler(pullHandler);
	}

	@Override
	public void setSouthboundHeaderPullHandler(Pullhandler pullHandler) {
		southboundPullToRefresh.setHeaderPullhandler(pullHandler);
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

}
