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

package gov.wa.wsdot.mobile.client.activities.socialmedia.facebook;

import gov.wa.wsdot.mobile.client.util.ParserUtils;
import gov.wa.wsdot.mobile.client.widget.SimpleListItem;
import gov.wa.wsdot.mobile.shared.FacebookItem;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellList;
import com.googlecode.mgwt.ui.client.widget.header.HeaderButton;
import com.googlecode.mgwt.ui.client.widget.progress.ProgressBar;
import com.googlecode.mgwt.ui.client.widget.base.HasRefresh;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowHeader;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowWidget;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel.Pullhandler;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellSelectedEvent;

public class FacebookViewGwtImpl extends Composite implements FacebookView {

	/**
	 * The UiBinder interface.
	 */	
	interface FacebookViewGwtImplUiBinder extends
			UiBinder<Widget, FacebookViewGwtImpl> {
	}
	
	/**
	 * The UiBinder used to generate the view.
	 */
	private static FacebookViewGwtImplUiBinder uiBinder = GWT
			.create(FacebookViewGwtImplUiBinder.class);
	
	@UiField(provided = true)
	CellList<FacebookItem> cellList;
	
	@UiField
	HeaderButton backButton;

	@UiField(provided = true)
	PullPanel pullToRefresh;
	
	@UiField
	FlowPanel flowPanel;
	
	@UiField
	ProgressBar progressBar;
	
	private Presenter presenter;
	private PullArrowHeader pullArrowHeader;
	
	public FacebookViewGwtImpl() {
		
		pullToRefresh = new PullPanel();
		pullArrowHeader = new PullArrowHeader();
		pullToRefresh.setHeader(pullArrowHeader);
		
		cellList = new CellList<FacebookItem>(new SimpleListItem<FacebookItem>() {

			@Override
			public String getDisplayDescription(FacebookItem model) {
				return model.getHtmlFormattedMessage();
			}

			@Override
			public String getDisplayLastUpdated(FacebookItem model) {
				return ParserUtils.relativeTime(model.getCreatedAt(),
						"yyyy-MM-dd'T'HH:mm:ss.SSSZ", true);
			}

			@Override
			public boolean canBeSelected(FacebookItem model) {
				return false;
			}

		});
		
		initWidget(uiBinder.createAndBindUi(this));

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
	public void render(List<FacebookItem> createPostList) {
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

}
