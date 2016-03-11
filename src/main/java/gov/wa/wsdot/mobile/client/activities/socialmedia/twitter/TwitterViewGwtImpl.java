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

package gov.wa.wsdot.mobile.client.activities.socialmedia.twitter;

import gov.wa.wsdot.mobile.client.util.ParserUtils;
import gov.wa.wsdot.mobile.client.widget.button.image.BackImageButton;
import gov.wa.wsdot.mobile.shared.TwitterItem;

import java.util.List;

import com.google.gwt.aria.client.LiveValue;
import com.google.gwt.aria.client.Roles;
import com.google.gwt.aria.client.SelectedValue;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
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
import com.googlecode.mgwt.ui.client.widget.input.listbox.MListBox;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellList;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellSelectedEvent;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowHeader;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowWidget;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel.Pullhandler;
import com.googlecode.mgwt.ui.client.widget.progress.ProgressIndicator;

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
	
	@UiField
	HeaderTitle heading;	
	
	@UiField(provided = true)
	CellList<TwitterItem> cellList;
	
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
		twitterAccounts.addItem("WSDOT 520");
		twitterAccounts.addItem("WSDOT East");
		twitterAccounts.addItem("WSDOT Southwest");
		twitterAccounts.addItem("WSDOT Tacoma");
		twitterAccounts.addItem("WSDOT Traffic");
		
		handleOnLoad();
		
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
		
		initWidget(uiBinder.createAndBindUi(this));
        
		accessibilityPrepare();
		
		if (MGWT.getOsDetection().isAndroid()) {
            leftFlexSpacer.setVisible(false);
        }
	}
	
    /**
     * ScrollPanel doesn't allow scrolling to the bottom if it contains a CellList with images.
     * 
     * See: https://code.google.com/p/mgwt/issues/detail?id=276
     * 
     * ScrollPanel.refresh() must be explicitly called after the images are loaded.
     * Since the onload event of images is not bubbling up, the LoadHandler can't be attached
     * to the CellList. Instead, the onload event needs to be captured at the <img>, and directly
     * trigger the ScrollPanel.refresh() from there.
     */
    private native void handleOnLoad() /*-{
        $wnd.refreshPanel = @gov.wa.wsdot.mobile.client.activities.socialmedia.twitter.TwitterViewGwtImpl::refreshPanel();
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

	@Override
	public String getAccountSelected() {
		return twitterAccounts.getItemText(twitterAccounts.getSelectedIndex());
	}
private void accessibilityPrepare(){
		
		// Add ARIA roles for accessibility
		Roles.getButtonRole().set(backButton.getElement());
		Roles.getButtonRole().setAriaLabelProperty(backButton.getElement(), "back");
		
		Roles.getHeadingRole().set(heading.getElement());
		
		Roles.getMenuRole().set(twitterAccounts.getElement());
		Roles.getMenuRole().setAriaLabelProperty(twitterAccounts.getElement(), "select a twitter account");
		Roles.getMenuRole().setTabindexExtraAttribute(twitterAccounts.getElement(), 0);		
		
		Roles.getProgressbarRole().setAriaLabelProperty(progressIndicator.getElement(), "loading indicator");
		Roles.getProgressbarRole().setAriaLiveProperty(progressIndicator.getElement(), LiveValue.ASSERTIVE);
		
	}
}
