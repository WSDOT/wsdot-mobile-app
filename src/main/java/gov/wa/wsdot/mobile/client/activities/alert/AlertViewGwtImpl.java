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

package gov.wa.wsdot.mobile.client.activities.alert;

import com.google.gwt.aria.client.Roles;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.header.HeaderTitle;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.scroll.ScrollPanel;
import gov.wa.wsdot.mobile.client.util.ParserUtils;
import gov.wa.wsdot.mobile.client.widget.button.image.BackImageButton;

public class AlertViewGwtImpl extends Composite implements AlertView {

	/**
	 * The UiBinder interface.
	 */	
	interface AlertViewGwtImplUiBinder extends
			UiBinder<Widget, AlertViewGwtImpl> {
	}
	
	/**
	 * The UiBinder used to generate the view.
	 */
	private static AlertViewGwtImplUiBinder uiBinder = GWT
			.create(AlertViewGwtImplUiBinder.class);

	@UiField
	BackImageButton backButton;
	
	@UiField
	FlexSpacer leftFlexSpacer;
	
	@UiField
	HeaderTitle title;
	
	@UiField
	ScrollPanel scrollPanel;
	
	@UiField
	HTML headlineDescription;
	
	@UiField
	HTML lastUpdatedTime;
	
	@UiField
	HTMLPanel mapPanel;
	
	@UiField
	Image staticMapImage;
	
	private Presenter presenter;
	
	public AlertViewGwtImpl() {

		initWidget(uiBinder.createAndBindUi(this));

		accessibilityPrepare();
		
        if (MGWT.getOsDetection().isAndroid()) {
            leftFlexSpacer.setVisible(false);
            scrollPanel.setBounce(false);
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
	public void setHeadlineDescription(String headineDescription) {
		this.headlineDescription.setText(headineDescription);
	}

    @Override
    public void setLastUpdatedTime(String lastUpdatedTime) {
        this.lastUpdatedTime.setText(ParserUtils.relativeTime(lastUpdatedTime,
                "MMMM d, yyyy h:mm a", false));
    }
	
	@Override
	public void setLatLon(double latitude, double longitude) {

		this.staticMapImage.setUrl("http://maps.googleapis.com/maps/api/staticmap?center="
				+ Double.toString(latitude) + "," + Double.toString(longitude)
				+ "&zoom=15&size=320x320&maptype=roadmap&markers="
				+ Double.toString(latitude) + "," + Double.toString(longitude)
				+ "&sensor=false");
		
	}

    @Override
    public void refresh() {
        this.scrollPanel.refresh();
    }
    
	private void accessibilityPrepare(){
		// Add ARIA roles for accessibility
		Roles.getButtonRole().set(backButton.getElement());
		Roles.getButtonRole().setAriaLabelProperty(backButton.getElement(), "back");
	}
}