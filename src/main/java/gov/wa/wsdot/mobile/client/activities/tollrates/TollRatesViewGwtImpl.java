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

package gov.wa.wsdot.mobile.client.activities.tollrates;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.widget.header.HeaderButton;

public class TollRatesViewGwtImpl extends Composite implements TollRatesView {

	/**
	 * The UiBinder interface.
	 */	
	interface TollRatesViewGwtImplUiBinder extends
			UiBinder<Widget, TollRatesViewGwtImpl> {
	}
	
	/**
	 * The UiBinder used to generate the view.
	 */
	private static TollRatesViewGwtImplUiBinder uiBinder = GWT
			.create(TollRatesViewGwtImplUiBinder.class);

	@UiField
	HeaderButton backButton;
	
	private Presenter presenter;
	
	public TollRatesViewGwtImpl() {
		
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

}
