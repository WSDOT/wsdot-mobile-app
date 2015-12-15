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

package gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.vesseldetails;

import gov.wa.wsdot.mobile.client.widget.button.image.BackImageButton;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.button.Button;
import com.googlecode.mgwt.ui.client.widget.header.HeaderTitle;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.scroll.ScrollPanel;

import com.google.gwt.i18n.client.DateTimeFormat;


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
	BackImageButton backButton;
	
	@UiField
	FlexSpacer leftFlexSpacer;
	
	@UiField
	ScrollPanel scrollPanel;
	
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
		
		if (route == null) {
			route = "Not available";
		}else{
			route = route.toUpperCase();
		}
		
		this.route.setText(route);
	}

	@Override
	public void setDeparting(String departing) {
		if (departing == null) {
			departing = "Not available";		
		}
		
		this.departing.setText(departing);
	}

	@Override
	public void setArriving(String arriving) {
		if (arriving == null) {
			arriving = "Not available";
		}
		
		this.arriving.setText(arriving);
	}

	@Override
	public void setScheduledDeparture(String scheduledDeparture) {		
		DateTimeFormat fmt = DateTimeFormat.getFormat("h:mm a");
		
		if (scheduledDeparture == null){
			this.scheduledDeparture.setText("--:--");
		} else{
			this.scheduledDeparture.setText(fmt.format(new Date(Long.parseLong(scheduledDeparture.substring(6, 19)))));
		}
	}

	@Override
	public void setActualDeparture(String leftDock) {
		DateTimeFormat fmt = DateTimeFormat.getFormat("h:mm a");

		if (leftDock == null) {
			this.actualDeparture.setText("--:--");
		} else {
			this.actualDeparture.setText(fmt.format(new Date(Long.parseLong(leftDock.substring(6, 19)))));
		}
		
	}

	@Override
	public void setEstimatedArrival(String estimatedArrival) {
		DateTimeFormat fmt = DateTimeFormat.getFormat("h:mm a");
		
		if (estimatedArrival == null) {
			this.estimatedArrival.setText("--:--");
		}else{
			this.estimatedArrival.setText(fmt.format(new Date(Long.parseLong(estimatedArrival.substring(6, 19)))));
		}
	}

	@Override
	public void setHeading(Integer heading) {

		String directions[] = {"N", "NxE", "E", "SxE", "S", "SxW", "W", "NxW", "N"};
		String headingText =  directions[ (int)Math.round((  ((double)heading % 360) / 45)) ];
		
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