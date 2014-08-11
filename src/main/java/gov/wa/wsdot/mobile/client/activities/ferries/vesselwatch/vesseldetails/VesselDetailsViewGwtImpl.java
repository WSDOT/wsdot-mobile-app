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

package gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.vesseldetails;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.widget.button.Button;
import com.googlecode.mgwt.ui.client.widget.header.HeaderButton;
import com.googlecode.mgwt.ui.client.widget.header.HeaderTitle;

public class VesselDetailsViewGwtImpl extends Composite implements
		VesselDetailsView {

	/**
	 * The UiBinder interface.
	 */	
	interface VesselDetailsViewGwtImplUiBinder extends
			UiBinder<Widget, VesselDetailsViewGwtImpl> {
	}

	/**
	 * The UiBinder used to generate the view.
	 */
	private static VesselDetailsViewGwtImplUiBinder uiBinder = GWT
			.create(VesselDetailsViewGwtImplUiBinder.class);
	

	@UiField
	HeaderButton backButton;
	
	@UiField
	HeaderTitle title;
	
	@UiField
	HTML route;
	
	@UiField
	HTML departing;
	
	@UiField
	HTML arriving;
	
	@UiField
	HTML scheduledDeparture;
	
	@UiField
	HTML actualDeparture;
	
	@UiField
	HTML estimatedArrival;
	
	@UiField
	HTML heading;
	
	@UiField
	HTML speed;
	
	@UiField
	Button vesselButton;
	
	private Presenter presenter;
	
	public VesselDetailsViewGwtImpl() {
	
		initWidget(uiBinder.createAndBindUi(this));

	}

	@UiHandler("backButton")
	protected void onBackButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onBackButtonPressed();
		}
	}
	
	@UiHandler("vesselButton")
	protected void onVesselButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onVesselButtonPressed();
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
	public void setRoute(String route) {
		if (route.length() == 0) {
			route = "Not available";
		}
		
		this.route.setText(route);
	}

	@Override
	public void setDeparting(String departing) {
		if (departing.length() == 0) {
			departing = "Not available";		
		}
		
		this.departing.setText(departing);
	}

	@Override
	public void setArriving(String arriving) {
		if (arriving.length() == 0) {
			arriving = "Not available";
		}
		
		this.arriving.setText(arriving);
	}

	@Override
	public void setScheduledDeparture(String scheduledDeparture, String ampm) {
		this.scheduledDeparture.setText(scheduledDeparture + " " + ampm);
	}

	@Override
	public void setActualDeparture(String leftDock, String ampm) {
		String actualDeparture;
		
		if (leftDock.length() == 0) {
			actualDeparture = "--:--";
		} else {
			actualDeparture = leftDock + " " + ampm;
		}
		
		this.actualDeparture.setText(actualDeparture);
		
	}

	@Override
	public void setEstimatedArrival(String estimatedArrival, String ampm) {
		this.estimatedArrival.setText(estimatedArrival + " " + ampm);
	}

	@Override
	public void setHeading(Integer heading, String headingText) {
		this.heading.setHTML(heading + "\u00b0 " + headingText);
	}

	@Override
	public void setSpeed(Double speed) {
		this.speed.setText(speed + " knots");
	}

	@Override
	public void setVesselButtonText(String vesselName) {
		this.vesselButton.setText(vesselName + " Web page");
	}

}