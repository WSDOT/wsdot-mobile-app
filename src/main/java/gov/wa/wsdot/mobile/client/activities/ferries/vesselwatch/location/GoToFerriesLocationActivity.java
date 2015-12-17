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

package gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.location;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.client.plugins.analytics.Analytics;
import gov.wa.wsdot.mobile.client.util.Consts;
import gov.wa.wsdot.mobile.shared.Topic;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;

public class GoToFerriesLocationActivity extends MGWTAbstractActivity implements
		GoToFerriesLocationView.Presenter {

	private final ClientFactory clientFactory;
	private GoToFerriesLocationView view;
	private EventBus eventBus;
	private static Storage localStorage = Storage.getLocalStorageIfSupported();
	
	public GoToFerriesLocationActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getFerriesGoToLocationView();
		this.eventBus = eventBus;
		view.setPresenter(this);
		view.render(createTopicsList());
		
		panel.setWidget(view);
	}

	@Override
	public void onStop() {
		view.setPresenter(null);
	}

	@Override
	public void onItemSelected(int index) {
		if (index == 0) {
			storeMapLocation(48.535868, -123.013808, 10); // Anacortes
			if (Consts.ANALYTICS_ENABLED) {
				Analytics.trackScreen("/Ferries/Vessel Watch/Go To Location/Anacortes");
			}
		}
		if (index == 1) {
			storeMapLocation(47.803096, -122.438718, 12); // Edmonds
			if (Consts.ANALYTICS_ENABLED) {
				Analytics.trackScreen("/Ferries/Vessel Watch/Go To Location/Edmonds");
			}
		}
		if (index == 2) {
			storeMapLocation(47.513625, -122.450820, 13); // Fauntleroy
			if (Consts.ANALYTICS_ENABLED) {
				Analytics.trackScreen("/Ferries/Vessel Watch/Go To Location/Fauntleroy");
			}
		}
		if (index == 3) {
			storeMapLocation(47.963857, -122.327721, 13); // Mukilteo
			if (Consts.ANALYTICS_ENABLED) {
				Analytics.trackScreen("/Ferries/Vessel Watch/Go To Location/Mukilteo");
			}
		}
		if (index == 4) {
			storeMapLocation(47.319040, -122.510890, 13); // Pt Defiance
			if (Consts.ANALYTICS_ENABLED) {
				Analytics.trackScreen("/Ferries/Vessel Watch/Go To Location/Pt Defiance");
			}
		}
		if (index == 5) {
			storeMapLocation(48.135562, -122.714449, 12); // Port Townsend
			if (Consts.ANALYTICS_ENABLED) {
				Analytics.trackScreen("/Ferries/Vessel Watch/Go To Location/Port Townsend");
			}
		}
		if (index == 6) {
			storeMapLocation(48.557233, -122.897078, 12); // San Juan Islands
			if (Consts.ANALYTICS_ENABLED) {
				Analytics.trackScreen("/Ferries/Vessel Watch/Go To Location/an Juan Islands");
			}
		}
		if (index == 7) {
			storeMapLocation(47.565125, -122.480508, 11); // Seattle
			if (Consts.ANALYTICS_ENABLED) {
				Analytics.trackScreen("/Ferries/Vessel Watch/Go To Location/Seattle");
			}
		}
		if (index == 8) {
			storeMapLocation(47.600325, -122.437249, 12); // Seattle-Bainbridge
			if (Consts.ANALYTICS_ENABLED) {
				Analytics.trackScreen("/Ferries/Vessel Watch/Go To Location/Seattle-Bainbridge");
			}
		}
		
		onDoneButtonPressed();
		
	}
	
	@Override
	public void onDoneButtonPressed() {
		ActionEvent.fire(eventBus, ActionNames.BACK);
	}
	
	private List<Topic> createTopicsList() {
		ArrayList<Topic> list = new ArrayList<Topic>();
		
		list.add(new Topic("Anacortes / San Juan Islands / Sidney BC"));
		list.add(new Topic("Edmonds / Kingston"));
		list.add(new Topic("Fauntleroy / Vashon / Southworth"));
		list.add(new Topic("Mukilteo / Clinton"));
		list.add(new Topic("Point Defiance / Tahlequah"));
		list.add(new Topic("Port Townsend / Coupeville"));
		list.add(new Topic("San Juan Islands Inter-Island"));
		list.add(new Topic("Seattle"));
		list.add(new Topic("Seattle / Bainbridge"));
		
		return list;
	}
	
	private void storeMapLocation(double latitude, double longitude, int zoom) {
		if (localStorage != null) {
			localStorage.setItem("KEY_MAP_LAT", String.valueOf(latitude));
			localStorage.setItem("KEY_MAP_LON", String.valueOf(longitude));
			localStorage.setItem("KEY_MAP_ZOOM", String.valueOf(zoom));
		}
	}

}
