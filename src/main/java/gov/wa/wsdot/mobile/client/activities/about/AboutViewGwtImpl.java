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

package gov.wa.wsdot.mobile.client.activities.about;

import gov.wa.wsdot.mobile.client.util.Consts;
import gov.wa.wsdot.mobile.client.widget.button.image.BackImageButton;

import com.google.gwt.aria.client.Roles;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.header.HeaderTitle;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.scroll.ScrollPanel;

public class AboutViewGwtImpl extends Composite implements AboutView {

	/**
	 * The UiBinder interface.
	 */	
	interface AboutViewGwtImplUiBinder extends
			UiBinder<Widget, AboutViewGwtImpl> {
	}

	/**
	 * The UiBinder used to generate the view.
	 */
	private static AboutViewGwtImplUiBinder uiBinder = GWT
			.create(AboutViewGwtImplUiBinder.class);
	
	@UiField
	HeaderTitle heading;
	
	@UiField
	BackImageButton backButton;
	
	@UiField
	FlexSpacer leftFlexSpacer;
	
	@UiField
	ScrollPanel scrollPanel;
	
	@UiField
	Anchor email;
	
	private Presenter presenter;
	
	public AboutViewGwtImpl() {
	
	    String appVersion = Consts.APP_VERSION;
	    String mailToAddressSubject = "mailto:webfeedback@wsdot.wa.gov?subject=WSDOT ";
	    email = new Anchor();
	    
		initWidget(uiBinder.createAndBindUi(this));
        
		accessibilityPrepare();
		
		if (MGWT.getOsDetection().isAndroid()) {
            leftFlexSpacer.setVisible(false);
            scrollPanel.setBounce(false);
            email.setHref(mailToAddressSubject + "Android App v" + appVersion);
        } else if (MGWT.getOsDetection().isIOs()) {
            email.setHref(mailToAddressSubject + "iPhone App v" + appVersion);
        } else {
            email.setHref(mailToAddressSubject + "Mobile App v" + appVersion);
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
    public ScrollPanel getScrollPanel() {
        return scrollPanel;
    }
	private void accessibilityPrepare(){
		// Add ARIA roles for accessibility
		Roles.getButtonRole().set(backButton.getElement());
		Roles.getHeadingRole().set(heading.getElement());
	}
}