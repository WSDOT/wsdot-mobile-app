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

package gov.wa.wsdot.mobile.client.activities.ferries.terminals;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.activities.ferries.FerriesPlace;
import gov.wa.wsdot.mobile.shared.Topic;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.gwtphonegap.client.inappbrowser.InAppBrowser;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;

public class FerriesTerminalsActivity extends MGWTAbstractActivity implements
		FerriesTerminalsView.Presenter {

	private final ClientFactory clientFactory;
	private FerriesTerminalsView view;
	private PhoneGap phoneGap;
	private InAppBrowser inAppBrowser;
	
	@SuppressWarnings("unused")
	private EventBus eventBus;
	
	public FerriesTerminalsActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getFerriesTerminalsView();
		this.eventBus = eventBus;
	    this.phoneGap = clientFactory.getPhoneGap();
	    inAppBrowser = this.phoneGap.getInAppBrowser();
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
            inAppBrowser.open("http://www.wsdot.wa.gov/ferries/vesselwatch/TerminalDetail.aspx?terminalid=1",
                    "", "enableViewportScale=yes");
			return;
		}
		if (index == 1) {
            inAppBrowser.open("http://www.wsdot.wa.gov/ferries/vesselwatch/TerminalDetail.aspx?terminalid=3",
                    "", "enableViewportScale=yes");
            return;
		}
		if (index == 2) {
            inAppBrowser.open("http://www.wsdot.wa.gov/ferries/vesselwatch/TerminalDetail.aspx?terminalid=4",
                    "", "enableViewportScale=yes");
			return;
		}
        if (index == 3) {
            inAppBrowser.open("http://www.wsdot.wa.gov/ferries/vesselwatch/TerminalDetail.aspx?terminalid=5",
                    "", "enableViewportScale=yes");
            return;
        }
        
        if (index == 4) {
            inAppBrowser.open("http://www.wsdot.wa.gov/ferries/vesselwatch/TerminalDetail.aspx?terminalid=11",
                    "", "enableViewportScale=yes");
            return;
        }
        if (index == 5) {
            inAppBrowser.open("http://www.wsdot.wa.gov/ferries/vesselwatch/TerminalDetail.aspx?terminalid=8",
                    "", "enableViewportScale=yes");
            return;
        }
        if (index == 6) {
            inAppBrowser.open("http://www.wsdot.wa.gov/ferries/vesselwatch/TerminalDetail.aspx?terminalid=9",
                    "", "enableViewportScale=yes");
            return;
        }
        if (index == 7) {
            inAppBrowser.open("http://www.wsdot.wa.gov/ferries/vesselwatch/TerminalDetail.aspx?terminalid=10",
                    "", "enableViewportScale=yes");
            return;
        }
        if (index == 8) {
            inAppBrowser.open("http://www.wsdot.wa.gov/ferries/vesselwatch/TerminalDetail.aspx?terminalid=12",
                    "", "enableViewportScale=yes");
            return;
        }
        if (index == 9) {
            inAppBrowser.open("http://www.wsdot.wa.gov/ferries/vesselwatch/TerminalDetail.aspx?terminalid=13",
                    "", "enableViewportScale=yes");
            return;
        }
        if (index == 10) {
            inAppBrowser.open("http://www.wsdot.wa.gov/ferries/vesselwatch/TerminalDetail.aspx?terminalid=14",
                    "", "enableViewportScale=yes");
            return;
        }
        if (index == 11) {
            inAppBrowser.open("http://www.wsdot.wa.gov/ferries/vesselwatch/TerminalDetail.aspx?terminalid=15",
                    "", "enableViewportScale=yes");
            return;
        }
        if (index == 12) {
            inAppBrowser.open("http://www.wsdot.wa.gov/ferries/vesselwatch/TerminalDetail.aspx?terminalid=16",
                    "", "enableViewportScale=yes");
            return;
        }
        if (index == 13) {
            inAppBrowser.open("http://www.wsdot.wa.gov/ferries/vesselwatch/TerminalDetail.aspx?terminalid=17",
                    "", "enableViewportScale=yes");
            return;
        }
        if (index == 14) {
            inAppBrowser.open("http://www.wsdot.wa.gov/ferries/vesselwatch/TerminalDetail.aspx?terminalid=7",
                    "", "enableViewportScale=yes");
            return;
        }
        if (index == 15) {
            inAppBrowser.open("http://www.wsdot.wa.gov/ferries/vesselwatch/TerminalDetail.aspx?terminalid=18",
                    "", "enableViewportScale=yes");
            return;
        }
        if (index == 16) {
            inAppBrowser.open("http://www.wsdot.wa.gov/ferries/vesselwatch/TerminalDetail.aspx?terminalid=19",
                    "", "enableViewportScale=yes");
            return;
        }
        if (index == 17) {
            inAppBrowser.open("http://www.wsdot.wa.gov/ferries/vesselwatch/TerminalDetail.aspx?terminalid=20",
                    "", "enableViewportScale=yes");
            return;
        }
        if (index == 18) {
            inAppBrowser.open("http://www.wsdot.wa.gov/ferries/vesselwatch/TerminalDetail.aspx?terminalid=21",
                    "", "enableViewportScale=yes");
            return;
        }
        if (index == 19) {
            inAppBrowser.open("http://www.wsdot.wa.gov/ferries/vesselwatch/TerminalDetail.aspx?terminalid=22",
                    "", "enableViewportScale=yes");
            return;
        }

	}
	
	@Override
	public void onBackButtonPressed() {
		clientFactory.getPlaceController().goTo(new FerriesPlace());
	}
	
	private List<Topic> createTopicsList() {
		ArrayList<Topic> list = new ArrayList<Topic>();
		
		list.add(new Topic("Anacortes"));
		list.add(new Topic("Bainbridge Island"));
		list.add(new Topic("Bremerton"));
		list.add(new Topic("Clinton"));
		list.add(new Topic("Coupeville"));
		list.add(new Topic("Edmonds"));
		list.add(new Topic("Fauntleroy"));
		list.add(new Topic("Friday Harbor"));
		list.add(new Topic("Kingston"));
		list.add(new Topic("Lopez Island"));
		list.add(new Topic("Mukilteo"));
		list.add(new Topic("Orcas Island"));
		list.add(new Topic("Point Defiance"));
		list.add(new Topic("Port Townsend"));
		list.add(new Topic("Seattle"));
		list.add(new Topic("Shaw Island"));
		list.add(new Topic("Sidney B.C."));
		list.add(new Topic("Southworth"));
		list.add(new Topic("Tahlequah"));
		list.add(new Topic("Vashon Island"));
		
		return list;
	}

}
