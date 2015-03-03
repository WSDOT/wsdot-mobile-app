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

package gov.wa.wsdot.mobile.client.activities.socialmedia.blogger;

import gov.wa.wsdot.mobile.client.widget.button.image.BackImageButton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.header.HeaderTitle;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.scroll.ScrollPanel;

public class BlogDetailsViewGwtImpl extends Composite implements
		BlogDetailsView {

	/**
	 * The UiBinder interface.
	 */	
	interface BlogDetailsViewGwtImplUiBinder extends
			UiBinder<Widget, BlogDetailsViewGwtImpl> {
	}

	/**
	 * The UiBinder used to generate the view.
	 */
	private static BlogDetailsViewGwtImplUiBinder uiBinder = GWT
			.create(BlogDetailsViewGwtImplUiBinder.class);

	@UiField
	BackImageButton backButton;
	
	@UiField
	FlexSpacer leftFlexSpacer;
	
	@UiField
	ScrollPanel scrollPanel;
	
	@UiField
	HeaderTitle title;
	
	@UiField
	HTML content;
	
	private Presenter presenter;
	
	public BlogDetailsViewGwtImpl() {

		initWidget(uiBinder.createAndBindUi(this));
        
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
	public void setTitle(String title) {
		this.title.setText(title);
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setContent(String content) {
		this.content.setHTML(content);
	}

}