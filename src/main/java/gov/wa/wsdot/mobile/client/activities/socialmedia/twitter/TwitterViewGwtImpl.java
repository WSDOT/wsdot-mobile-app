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

package gov.wa.wsdot.mobile.client.activities.socialmedia.twitter;

import gov.wa.wsdot.mobile.client.util.ParserUtils;
import gov.wa.wsdot.mobile.shared.TwitterItem;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.widget.CellList;
import com.googlecode.mgwt.ui.client.widget.HeaderButton;
import com.googlecode.mgwt.ui.client.widget.MListBox;
import com.googlecode.mgwt.ui.client.widget.ProgressBar;
import com.googlecode.mgwt.ui.client.widget.base.HasRefresh;
import com.googlecode.mgwt.ui.client.widget.base.PullArrowHeader;
import com.googlecode.mgwt.ui.client.widget.base.PullArrowWidget;
import com.googlecode.mgwt.ui.client.widget.base.PullPanel;
import com.googlecode.mgwt.ui.client.widget.base.PullPanel.Pullhandler;
import com.googlecode.mgwt.ui.client.widget.celllist.CellSelectedEvent;

public class TwitterViewGwtImpl extends Composite implements TwitterView {

	/**
	 * The UiBinder interface.
	 */	
	interface TwitterViewGwtImplUiBinder extends
			UiBinder<Widget, TwitterViewGwtImpl> {
	}
	
	/**
	 * The UiBinder used to generate the view.
	 */
	private static TwitterViewGwtImplUiBinder uiBinder = GWT
			.create(TwitterViewGwtImplUiBinder.class);
	
	@UiField(provided = true)
	CellList<TwitterItem> cellList;
	
	@UiField
	HeaderButton backButton;

	@UiField(provided = true)
	PullPanel pullToRefresh;
	
	@UiField
	FlowPanel flowPanel;
	
	@UiField
	ProgressBar progressBar;
	
	@UiField(provided = true)
	MListBox twitterAccounts;
	
	private Presenter presenter;
	private PullArrowHeader pullArrowHeader;
	
	public TwitterViewGwtImpl() {
		
		pullToRefresh = new PullPanel();
		pullArrowHeader = new PullArrowHeader();
		pullToRefresh.setHeader(pullArrowHeader);

		twitterAccounts = new MListBox();
		twitterAccounts.addItem("All Accounts");
		twitterAccounts.addItem("Bertha");
		twitterAccounts.addItem("Ferries");
		twitterAccounts.addItem("Good To Go!");
		twitterAccounts.addItem("Snoqualmie Pass");
		twitterAccounts.addItem("WSDOT");
		twitterAccounts.addItem("WSDOT North");
		twitterAccounts.addItem("WSDOT Southwest");
		twitterAccounts.addItem("WSDOT Tacoma");
		twitterAccounts.addItem("WSDOT Traffic");
		
		cellList = new CellList<TwitterItem>(new TwitterCell<TwitterItem>() {
			
			@Override
			public String getMediaUrl(TwitterItem model) {
				return model.getMediaUrl();
			}			
			@Override
			public String getDisplayString(TwitterItem model) {
				return model.getUserName();
			}
			
			@Override
			public String getDisplayDescription(TwitterItem model) {
				return model.getFormatedHtmlText();
			}

			@Override
			public String getDisplayLastUpdated(TwitterItem model) {
				return ParserUtils.relativeTime(model.getCreatedAt(),
						"yyyy-MM-dd'T'HH:mm:ss.SSSZ", true);
			}

			@Override
			public String getDisplayImage(TwitterItem model) {
				return model.getProfileImage();
			}

		});
		
		cellList.setGroup(true);
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

	@UiHandler("twitterAccounts")
	protected void onChange(ChangeEvent event) {
		if (presenter != null) {
			MListBox source = (MListBox) event.getSource();
			presenter.onAccountSelected((source.getItemText(source.getSelectedIndex())));
		}
	}
	
	@Override
	public void render(List<TwitterItem> createPostList) {
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

	@Override
	public String getAccountSelected() {
		return twitterAccounts.getItemText(twitterAccounts.getSelectedIndex());
	}

}
