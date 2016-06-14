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

package gov.wa.wsdot.mobile.client.activities.ferries.schedules.sailings;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.client.plugins.analytics.Analytics;
import gov.wa.wsdot.mobile.client.util.Consts;

public class FerriesRouteAlertDetailsActivity extends MGWTAbstractActivity implements
		FerriesRouteAlertDetailsView.Presenter {

	private final ClientFactory clientFactory;
	private FerriesRouteAlertDetailsView view;
	private EventBus eventBus;
	private Analytics analytics;

	public FerriesRouteAlertDetailsActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getFerriesRouteAlertDetailsView();
		analytics = clientFactory.getAnalytics();
		this.eventBus = eventBus;
		view.setPresenter(this);
		
		Place place = clientFactory.getPlaceController().getWhere();
		
		if (place instanceof FerriesRouteAlertDetailsPlace) {
			FerriesRouteAlertDetailsPlace ferriesRouteAlertDetailsPlace = (FerriesRouteAlertDetailsPlace) place;
			
			view.setPublishDate(ferriesRouteAlertDetailsPlace.getFerriesRouteAlertItem().getPublishDate());
			view.setAlertTitle(ferriesRouteAlertDetailsPlace.getFerriesRouteAlertItem().getAlertFullTitle());
			view.setAlertText(ferriesRouteAlertDetailsPlace.getFerriesRouteAlertItem().getAlertFullText());
		}

		if (Consts.ANALYTICS_ENABLED) {
			analytics.trackScreen("/Ferries/Schedules/Sailings/Alerts");
		}

		panel.setWidget(view);
		captureClickEvents();
	}

	@Override
	public void onStop() {
		view.setPresenter(null);
	}
	
	@Override
	public void onBackButtonPressed() {
		ActionEvent.fire(eventBus, ActionNames.BACK);
	}
	
	/**
	 *  JSNI method to capture click events and open urls in PhoneGap InAppBrowser.
	 * 
	 *  Tapping external will cause those links to open in the same browser
	 *  window as the app with no way for the user to return to the app.
	 *  
	 *  http://docs.phonegap.com/en/2.4.0/cordova_inappbrowser_inappbrowser.md.html
	 */
	public static native void captureClickEvents() /*-{
		anchors = $doc.getElementsByTagName('a');
		for (var i = 0; i < anchors.length; i++) {
			anchors[i].addEventListener('click', function(e) {
				e.preventDefault();
				$wnd.open(this.href, '_blank', 'location=yes,enableViewportScale=yes');
			});
		}
	}-*/;

}