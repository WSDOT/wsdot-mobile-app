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

package gov.wa.wsdot.mobile.client.activities.ferries;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.FerriesRouteSchedulesPlace;
import gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.VesselWatchMapPlace;
import gov.wa.wsdot.mobile.client.activities.home.HomePlace;
import gov.wa.wsdot.mobile.client.plugins.analytics.Analytics;
import gov.wa.wsdot.mobile.client.util.Consts;
import gov.wa.wsdot.mobile.shared.Topic;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.gwtphonegap.client.inappbrowser.InAppBrowser;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;

public class FerriesActivity extends MGWTAbstractActivity implements
		FerriesView.Presenter {

	private final ClientFactory clientFactory;
	private FerriesView view;
	private PhoneGap phoneGap;
	private InAppBrowser inAppBrowser;
	
	@SuppressWarnings("unused")
	private EventBus eventBus;
	
	public FerriesActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getFerriesView();
		this.eventBus = eventBus;
	    this.phoneGap = clientFactory.getPhoneGap();
	    inAppBrowser = this.phoneGap.getInAppBrowser();
		view.setPresenter(this);
		view.render(createTopicsList());
		
		panel.setWidget(view);
		
		if (Consts.ANALYTICS_ENABLED) {
			Analytics.trackScreen("/Ferries");
		}
	}

	@Override
	public void onStop() {
		view.setPresenter(null);
	}

	@Override
	public void onItemSelected(int index) {
		if (index == 0) {
			clientFactory.getPlaceController().goTo(new FerriesRouteSchedulesPlace());
			return;
		}
		if (index == 1) {
			if (Consts.ANALYTICS_ENABLED) {
				Analytics.trackScreen("/Ferries/Vehicle Reservations");
			}
            inAppBrowser.open("http://www.wsdot.wa.gov/ferries/reservations",
                    "", "enableViewportScale=yes");
            return;
		}
		if (index == 2) {
			clientFactory.getPlaceController().goTo(new VesselWatchMapPlace());
			return;
		}		
	}
	
	@Override
	public void onBackButtonPressed() {
		clientFactory.getPlaceController().goTo(new HomePlace());
	}
	
	private List<Topic> createTopicsList() {
		ArrayList<Topic> list = new ArrayList<Topic>();
		
		list.add(new Topic("Route Schedules"));
		list.add(new Topic("Vehicle Reservations"));
		list.add(new Topic("VesselWatch"));
		
		return list;
	}

}
