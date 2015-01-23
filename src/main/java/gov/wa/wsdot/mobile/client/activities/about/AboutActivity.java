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

package gov.wa.wsdot.mobile.client.activities.about;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;

public class AboutActivity extends MGWTAbstractActivity implements
		AboutView.Presenter {

	private final ClientFactory clientFactory;
	private AboutView view;
	private EventBus eventBus;
	private PhoneGap phoneGap;

	public AboutActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getAboutView();
		this.eventBus = eventBus;
		phoneGap = clientFactory.getPhoneGap();
		view.setPresenter(this);
		
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
     * JSNI method to capture click events and open urls in PhoneGap
     * InAppBrowser.
     * 
     * Tapping external links on the Google map like the Google logo and 'Terms
     * of Use' will cause those links to open in the same browser window as the
     * app with no way for the user to return to the app.
     * 
     * http://docs.phonegap.com/en/2.4.0/cordova_inappbrowser_inappbrowser.md.html
     */
    public static native void captureClickEvents() /*-{
        var anchors = $doc.getElementsByTagName('a');
        for ( var i = 0; i < anchors.length; i++) {
            anchors[i].addEventListener('click', function(e) {
                e.preventDefault();
                $wnd.open(this.href, '_blank',
                        'location=yes,enableViewportScale=yes');
            });
        }
    }-*/;

}