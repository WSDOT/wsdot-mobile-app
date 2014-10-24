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

package gov.wa.wsdot.mobile.client.activities.ferries.schedules.sailings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.widget.button.image.PreviousitemImageButton;

public class FerriesRouteAlertDetailsViewGwtImpl extends Composite implements
		FerriesRouteAlertDetailsView {

	/**
	 * The UiBinder interface.
	 */	
	interface FerriesRouteAlertDetailsViewGwtImplUiBinder extends
			UiBinder<Widget, FerriesRouteAlertDetailsViewGwtImpl> {
	}

	/**
	 * The UiBinder used to generate the view.
	 */
	private static FerriesRouteAlertDetailsViewGwtImplUiBinder uiBinder = GWT
			.create(FerriesRouteAlertDetailsViewGwtImplUiBinder.class);

	@UiField
	PreviousitemImageButton backButton;
	
	@UiField
	HTML publishDate;
	
	@UiField
	HTML alertTitle;
	
	@UiField
	HTML alertText;
	
	private Presenter presenter;
	
	public FerriesRouteAlertDetailsViewGwtImpl() {

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
    public void setPublishDate(String publishDate) {
        this.publishDate.setHTML(publishDate);
        
    }

    @Override
    public void setAlertTitle(String alertTitle) {
        this.alertTitle.setHTML(alertTitle);
    }

    @Override
    public void setAlertText(String content) {
        this.alertText.setHTML(content);        
    }

}