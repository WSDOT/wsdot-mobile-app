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

package gov.wa.wsdot.mobile.client.activities.callout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.base.HasRefresh;
import com.googlecode.mgwt.ui.client.widget.header.HeaderTitle;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowHeader;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowWidget;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullPanel.Pullhandler;
import gov.wa.wsdot.mobile.client.widget.button.image.BackImageButton;

public class CalloutViewGwtImpl extends Composite implements CalloutView {

	/**
	 * The UiBinder interface.
	 */	
	interface CalloutViewGwtImplUiBinder extends
			UiBinder<Widget, CalloutViewGwtImpl> {
	}
	
	/**
	 * The UiBinder used to generate the view.
	 */
	private static CalloutViewGwtImplUiBinder uiBinder = GWT
			.create(CalloutViewGwtImplUiBinder.class);

	@UiField
	BackImageButton backButton;
	
	@UiField
	FlexSpacer leftFlexSpacer;
	
	@UiField
	HeaderTitle title;
	
	@UiField
	HTMLPanel htmlPanel;
	
	@UiField
	Image image;
	
    @UiField(provided = true)
    static
    PullPanel pullToRefresh;
    
    private PullArrowHeader pullArrowHeader;
	
	private Presenter presenter;
	
	public CalloutViewGwtImpl() {
	    
        pullToRefresh = new PullPanel();
        pullArrowHeader = new PullArrowHeader();
        pullToRefresh.setHeader(pullArrowHeader);

		initWidget(uiBinder.createAndBindUi(this));
        
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
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@Override
	public void setTitle(String title) {
		this.title.setText(title);
	}

    @Override
    public void refresh() {
        pullToRefresh.refresh();
    }

    @Override
    public void setImageUrl(String url) {
        this.image.setUrl(""); // Force an image reload
        this.image.setUrl(url);        
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