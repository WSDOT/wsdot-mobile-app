/*
 * Copyright (c) 2016 Washington State Department of Transportation
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

package gov.wa.wsdot.mobile.client.activities.trafficmap.menu.location;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.activities.trafficmap.TrafficMapPlace;
import gov.wa.wsdot.mobile.client.activities.trafficmap.menu.expresslanes.SeattleExpressLanesPlace;
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

public class GoToLocationActivity extends MGWTAbstractActivity implements
		GoToLocationView.Presenter {

	private final ClientFactory clientFactory;
	private GoToLocationView view;
	private EventBus eventBus;
	private Analytics analytics;
	private static Storage localStorage = Storage.getLocalStorageIfSupported();
	
	public GoToLocationActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getGoToLocationView();
		this.eventBus = eventBus;
		analytics = clientFactory.getAnalytics();
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
			if (Consts.ANALYTICS_ENABLED) {
				analytics.trackScreen("/Traffic Map/Menu/Go To Location/Bellingham");
			}
			storeMapLocation(48.756302, -122.46151, 12); // Bellingham
		}
		if (index == 1) {
			if (Consts.ANALYTICS_ENABLED) {
				analytics.trackScreen("/Traffic Map/Menu/Go To Location/Canadian Border");
			}
			storeMapLocation(48.991651, -122.746124, 14); // Canadian Border
		}
		if (index == 2) {
			if (Consts.ANALYTICS_ENABLED) {
				analytics.trackScreen("/Traffic Map/Menu/Go To Location/Chehalis");
			}
			storeMapLocation(46.635529, -122.937698, 13); // Chehalis
		}
		if (index == 3) {
			if (Consts.ANALYTICS_ENABLED) {
				analytics.trackScreen("/Traffic Map/Menu/Go To Location/Hood Canal Area");
			}
			storeMapLocation(47.85268, -122.628365, 13); // Hood Canal Area
		}
		if (index == 4) {
			if (Consts.ANALYTICS_ENABLED) {
				analytics.trackScreen("/Traffic Map/Menu/Go To Location/Monroe");
			}
			storeMapLocation(47.859476, -121.972446,14); // Monroe
		}
		if (index == 5) {
			if (Consts.ANALYTICS_ENABLED) {
				analytics.trackScreen("/Traffic Map/Menu/Go To Location/Mount Vernon");
			}
			storeMapLocation(48.420657, -122.334824, 13); // Mount Vernon
		}
		if (index == 6) {
			if (Consts.ANALYTICS_ENABLED) {
				analytics.trackScreen("/Traffic Map/Menu/Go To Location/Olympia");
			}
			storeMapLocation(47.021461, -122.899933, 13); // Olympia
		}
		if (index == 7) {
			if (Consts.ANALYTICS_ENABLED) {
				analytics.trackScreen("/Traffic Map/Menu/Go To Location/Seattle Area");
			}
			storeMapLocation(47.5990, -122.3350, 12); // Seattle Area
		}
		if (index == 8) {
			if (Consts.ANALYTICS_ENABLED) {
				analytics.trackScreen("/Traffic Map/Menu/Go To Location/Snoqualmie Pass");
			}
		    storeMapLocation(47.3756596, -121.4195662, 11); // Snoqualmie Pass
		}
		if (index == 9) {
			if (Consts.ANALYTICS_ENABLED) {
				analytics.trackScreen("/Traffic Map/Menu/Go To Location/Spokane Area");
			}
			storeMapLocation(47.658566, -117.425995, 12); // Spokane Area
		}
		if (index == 10) {
			if (Consts.ANALYTICS_ENABLED) {
				analytics.trackScreen("/Traffic Map/Menu/Go To Location/Stanwood");
			}
			storeMapLocation(48.22959, -122.34581, 13); // Stanwood
		}
		if (index == 11) {
			if (Consts.ANALYTICS_ENABLED) {
				analytics.trackScreen("/Traffic Map/Menu/Go To Location/Sultan");
			}
			storeMapLocation(47.86034, -121.812286, 14); // Sultan
		}
		if (index == 12) {
			if (Consts.ANALYTICS_ENABLED) {
				analytics.trackScreen("/Traffic Map/Menu/Go To Location/ Tacoma");
			}
			storeMapLocation(47.206275, -122.46254, 12); // Tacoma
		}
		if (index == 13) {
			if (Consts.ANALYTICS_ENABLED) {
				analytics.trackScreen("/Traffic Map/Menu/Go To Location/Tri-Cities");
			}
		    storeMapLocation(46.2225766, -119.2039748, 11); // Tri-Cities
		}
		if (index == 14) {
			if (Consts.ANALYTICS_ENABLED) {
				analytics.trackScreen("/Traffic Map/Menu/Go To Location/US 97 Border");
			}
			storeMapLocation(48.998775, -119.462714, 15); // US 97 Border
		}
		if (index == 15) {
			if (Consts.ANALYTICS_ENABLED) {
				analytics.trackScreen("/Traffic Map/Menu/Go To Location/Vancouver Area");
			}
			storeMapLocation(45.639968, -122.610512, 12); // Vancouver Area
		}
		if (index == 16) {
			if (Consts.ANALYTICS_ENABLED) {
				analytics.trackScreen("/Traffic Map/Menu/Go To Location/Wenatchee");
			}
			storeMapLocation(47.435867, -120.309563, 13); // Wenatchee
		}
		if (index == 17) {
			if (Consts.ANALYTICS_ENABLED) {
				analytics.trackScreen("/Traffic Map/Menu/Go To Location/Yakima");
			}
		    storeMapLocation(46.6002534, -120.4885235, 11); // Yakima
		}

		clientFactory.getPlaceController().goTo(new TrafficMapPlace());
		
	}
	
	@Override
	public void onDoneButtonPressed() {
		ActionEvent.fire(eventBus, ActionNames.BACK);
	}
	
	private List<Topic> createTopicsList() {
		ArrayList<Topic> list = new ArrayList<Topic>();
		
		list.add(new Topic("Bellingham"));
		list.add(new Topic("Canadian Border"));
		list.add(new Topic("Chehalis"));
		list.add(new Topic("Hood Canal Area"));
		list.add(new Topic("Monroe"));
		list.add(new Topic("Mount Vernon"));
		list.add(new Topic("Olympia"));
		list.add(new Topic("Seattle Area"));
		list.add(new Topic("Snoqualmie Pass"));
		list.add(new Topic("Spokane Area"));
		list.add(new Topic("Stanwood"));
		list.add(new Topic("Sultan"));
		list.add(new Topic("Tacoma"));
		list.add(new Topic("Tri-Cities"));
		list.add(new Topic("US 97 Border"));
		list.add(new Topic("Vancouver Area"));
		list.add(new Topic("Wenatchee"));
		list.add(new Topic("Yakima"));
		
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
