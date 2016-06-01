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

package gov.wa.wsdot.mobile.client.activities.callout;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowStandardHandler;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowStandardHandler.PullActionHandler;
import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.client.plugins.accessibility.Accessibility;
import gov.wa.wsdot.mobile.client.plugins.analytics.Analytics;
import gov.wa.wsdot.mobile.client.util.Consts;

public class CalloutActivity extends MGWTAbstractActivity implements
		CalloutView.Presenter {

	private final ClientFactory clientFactory;
	private CalloutView view;
	private EventBus eventBus;
	private String url;
	private Analytics analytics;
	private Accessibility accessibility;

	public CalloutActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getCalloutView();
		analytics = clientFactory.getAnalytics();
		this.eventBus = eventBus;
		accessibility = clientFactory.getAccessibility();
		view.setPresenter(this);
		
		Place place = clientFactory.getPlaceController().getWhere();
		
		if (place instanceof CalloutPlace) {
			CalloutPlace calloutPlace = (CalloutPlace) place;
			url = calloutPlace.getUrl();
		}

        PullArrowStandardHandler headerHandler = new PullArrowStandardHandler(
                view.getPullHeader(), view.getPullPanel());

        view.getPullHeader().setHTML("pull down");
        headerHandler.setErrorText("Error");
        headerHandler.setLoadingText("Loading");
        headerHandler.setNormalText("pull down");
        headerHandler.setPulledText("release to load");
        headerHandler.setPullActionHandler(new PullActionHandler() {

            @Override
            public void onPullAction(final AsyncCallback<Void> callback) {
                new Timer() {

                    @Override
                    public void run() {
                        view.setImageUrl(url);
                        view.refresh();
                        callback.onSuccess(null);
                        
                    }
                }.schedule(1);
            }
        });
        
        view.setHeaderPullHandler(headerHandler);
        view.setTitle("JBLM");
        view.setImageUrl(url);

        if (Consts.ANALYTICS_ENABLED) {
            analytics.trackScreen("/Traffic/Callout/JBLM");
        }

		panel.setWidget(view);
		accessibility.postScreenChangeNotification();
	}

	@Override
	public void onStop() {
		view.setPresenter(null);
	}
	
	@Override
	public void onBackButtonPressed() {
		ActionEvent.fire(eventBus, ActionNames.BACK);
	}

}