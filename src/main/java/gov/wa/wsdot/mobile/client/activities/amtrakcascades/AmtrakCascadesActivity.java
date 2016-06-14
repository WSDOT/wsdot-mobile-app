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

package gov.wa.wsdot.mobile.client.activities.amtrakcascades;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.gwtphonegap.client.inappbrowser.InAppBrowser;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.activities.amtrakcascades.schedules.AmtrakCascadesSchedulesPlace;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.client.plugins.accessibility.Accessibility;
import gov.wa.wsdot.mobile.client.plugins.analytics.Analytics;
import gov.wa.wsdot.mobile.client.util.Consts;
import gov.wa.wsdot.mobile.shared.Topic;

import java.util.ArrayList;
import java.util.List;

public class AmtrakCascadesActivity extends MGWTAbstractActivity implements
		AmtrakCascadesView.Presenter {

	private final ClientFactory clientFactory;
	private AmtrakCascadesView view;
	private PhoneGap phoneGap;
	private InAppBrowser inAppBrowser;
	private Analytics analytics;
	private Accessibility accessibility;
	@SuppressWarnings("unused")
	private EventBus eventBus;
	
	public AmtrakCascadesActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getAmtrakCascadesView();
		this.eventBus = eventBus;
	    this.phoneGap = clientFactory.getPhoneGap();
		accessibility = clientFactory.getAccessibility();
	    inAppBrowser = this.phoneGap.getInAppBrowser();
	    analytics = clientFactory.getAnalytics();
		view.setPresenter(this);
		view.render(createTopicsList());

		if (Consts.ANALYTICS_ENABLED) {
			analytics.trackScreen("/Amtrak Cascades");
		}

		panel.setWidget(view);
		accessibility.postScreenChangeNotification();
	}

	@Override
	public void onStop() {
		view.setPresenter(null);
	}

	@Override
	public void onItemSelected(int index) {
		if (index == 0) {
			
			if (Consts.ANALYTICS_ENABLED) {
				analytics.trackScreen("/Amtrak Cascades/Buy Tickets");
			}
			
            inAppBrowser.open("http://m.amtrak.com", "_blank",
                    "enableViewportScale=yes,transitionstyle=fliphorizontal");
            
			return;
		}
		if (index == 1) {
            clientFactory.getPlaceController().goTo(new AmtrakCascadesSchedulesPlace());
            
            return;
		}
	}
	
	@Override
	public void onBackButtonPressed() {
		ActionEvent.fire(eventBus, ActionNames.BACK);
	}
	
	private List<Topic> createTopicsList() {
		ArrayList<Topic> list = new ArrayList<Topic>();
		
        list.add(new Topic("Buy Tickets"));
		list.add(new Topic("Check Schedules and Status"));
		
		return list;
	}

}
