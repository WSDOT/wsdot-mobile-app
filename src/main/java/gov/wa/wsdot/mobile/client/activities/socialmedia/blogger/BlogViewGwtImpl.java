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

package gov.wa.wsdot.mobile.client.activities.socialmedia.blogger;

import gov.wa.wsdot.mobile.client.util.ParserUtils;
import gov.wa.wsdot.mobile.client.widget.CellDetailsWithPhoto;
import gov.wa.wsdot.mobile.shared.BlogItem;

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
import com.googlecode.mgwt.ui.client.widget.celllist.CellSelectedEvent;

public class BlogViewGwtImpl extends Composite implements BlogView {

	/**
	 * The UiBinder interface.
	 */	
	interface BlogViewGwtImplUiBinder extends UiBinder<Widget, BlogViewGwtImpl> {
	}
	
	/**
	 * The UiBinder used to generate the view.
	 */
	private static BlogViewGwtImplUiBinder uiBinder = GWT
			.create(BlogViewGwtImplUiBinder.class);
	
	@UiField(provided = true)
	CellList<BlogItem> cellList;
	
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
	
	public BlogViewGwtImpl() {
		
		pullToRefresh = new PullPanel();
		pullArrowHeader = new PullArrowHeader();
		pullToRefresh.setHeader(pullArrowHeader);
		
		cellList = new CellList<BlogItem>(new CellDetailsWithPhoto<BlogItem>() {

			@Override
			public String getDisplayString(BlogItem model) {
				return model.getTitle();
			}

			@Override
			public String getDisplayImage(BlogItem model) {
				return model.getImageUrl();
			}

			@Override
			public String getDisplayDescription(BlogItem model) {
				return model.getDescription();
			}

			@Override
			public String getDisplayLastUpdated(BlogItem model) {
				return ParserUtils.relativeTime(model.getPublished(),
						"yyyy-MM-dd'T'HH:mm:ss.SSSz", false);
			}

		});
		
		cellList.setRound(true);
		
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
	public void render(List<BlogItem> createBlogList) {
		cellList.render(createBlogList);
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
