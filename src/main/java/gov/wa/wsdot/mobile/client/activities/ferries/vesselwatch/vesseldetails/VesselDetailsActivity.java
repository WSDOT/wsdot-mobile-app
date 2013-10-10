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

package gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.vesseldetails;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.gwtphonegap.client.inappbrowser.InAppBrowser;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;

public class VesselDetailsActivity extends MGWTAbstractActivity implements
		VesselDetailsView.Presenter {

	private final ClientFactory clientFactory;
	private VesselDetailsView view;
	private EventBus eventBus;
	private PhoneGap phoneGap;
	private InAppBrowser inAppBrowser;
	private int vesselId;

	public VesselDetailsActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getVesselDetailsView();
		this.eventBus = eventBus;
		this.phoneGap = clientFactory.getPhoneGap();
		inAppBrowser = this.phoneGap.getInAppBrowser();
		view.setPresenter(this);
		
		Place place = clientFactory.getPlaceController().getWhere();
		
		if (place instanceof VesselDetailsPlace) {
			VesselDetailsPlace vesselDetailsPlace = (VesselDetailsPlace) place;
			vesselId = vesselDetailsPlace.getVessel().getVesselId();
			
			view.setTitle(vesselDetailsPlace.getVessel().getName());
			view.setRoute(vesselDetailsPlace.getVessel().getRoute());
			view.setDeparting(vesselDetailsPlace.getVessel().getLastDock());
			view.setArriving(vesselDetailsPlace.getVessel().getArrivingTerminal());
			
			view.setScheduledDeparture(vesselDetailsPlace.getVessel().getNextDep(),
					vesselDetailsPlace.getVessel().getNextDepAMPM());
			
			view.setActualDeparture(vesselDetailsPlace.getVessel().getLeftDock(),
					vesselDetailsPlace.getVessel().getLeftDockAMPM());
			
			view.setEstimatedArrival(vesselDetailsPlace.getVessel().getEta(),
					vesselDetailsPlace.getVessel().getEtaAMPM());
			
			view.setHeading(vesselDetailsPlace.getVessel().getHead(),
					vesselDetailsPlace.getVessel().getHeadTxt());
			
			view.setSpeed(vesselDetailsPlace.getVessel().getSpeed());
			view.setVesselButtonText(vesselDetailsPlace.getVessel().getName());
			
		}

		panel.setWidget(view);

	}

	@Override
	public void onStop() {
		view.setPresenter(null);
	}
	
	@Override
	public void onBackButtonPressed() {
		ActionEvent.fire(eventBus, ActionNames.BACK);
	}

	@Override
	public void onVesselButtonPressed() {
		inAppBrowser.open(
				"http://www.wsdot.com/ferries/vesselwatch/VesselDetail.aspx?vessel_id="
						+ vesselId, "", "enableViewportScale=yes");
		
	}


}