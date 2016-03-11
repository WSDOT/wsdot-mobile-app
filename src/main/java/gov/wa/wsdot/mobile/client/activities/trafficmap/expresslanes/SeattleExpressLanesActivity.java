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

package gov.wa.wsdot.mobile.client.activities.trafficmap.expresslanes;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.css.AppBundle;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.client.plugins.accessibility.Accessibility;
import gov.wa.wsdot.mobile.client.util.Consts;
import gov.wa.wsdot.mobile.shared.ExpressLaneItem;
import gov.wa.wsdot.mobile.shared.ExpressLanesFeed;
import gov.wa.wsdot.mobile.shared.Topic;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.gwtphonegap.client.inappbrowser.InAppBrowser;
import com.googlecode.gwtphonegap.client.notification.AlertCallback;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowStandardHandler;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowStandardHandler.PullActionHandler;

public class SeattleExpressLanesActivity extends MGWTAbstractActivity implements
		SeattleExpressLanesView.Presenter {

	private final ClientFactory clientFactory;
	private SeattleExpressLanesView view;
	private EventBus eventBus;
	private PhoneGap phoneGap;
	private Accessibility accessibility;
	private InAppBrowser inAppBrowser;

	private static final String EXPRESS_LANES_URL = Consts.HOST_URL + "/traveler/api/expresslanes";
	
	public SeattleExpressLanesActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view = clientFactory.getSeattleExpressLanesView();
		this.eventBus = eventBus;
		this.phoneGap = this.clientFactory.getPhoneGap();
		accessibility = clientFactory.getAccessibility();
		view.setPresenter(this);
		view.getPullHeader().setHTML("pull down");
		inAppBrowser = this.phoneGap.getInAppBrowser();
		
		PullArrowStandardHandler headerHandler = new PullArrowStandardHandler(
				view.getPullHeader(), view.getPullPanel());
		
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
						createPostList();							
						view.refresh();
						callback.onSuccess(null);
					}
					
				}.schedule(1);
			}
			
		});

		view.setHeaderPullHandler(headerHandler);

		createPostList();
		createSchedulesLink();

		panel.setWidget(view);

		accessibility.postScreenChangeNotification();
	}

    private void createPostList() {
        view.showProgressIndicator();
		
		JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
		// Set timeout for 30 seconds (30000 milliseconds)
		jsonp.setTimeout(30000);
		jsonp.requestObject(EXPRESS_LANES_URL, new AsyncCallback<ExpressLanesFeed>() {

			@Override
			public void onFailure(Throwable caught) {
				view.hideProgressIndicator();
				phoneGap.getNotification()
				.alert("Can't load data. Check your connection.",
						new AlertCallback() {
							@Override
							public void onOkButtonClicked() {
								// TODO Auto-generated method stub
							}
						}, "Connection Error");
			}

			@Override
			public void onSuccess(ExpressLanesFeed result) {
				ExpressLaneItem item = null;
				
				if (result.getExpressLanes() != null) {
			        ArrayList<ExpressLaneItem> expressLaneItems = new ArrayList<ExpressLaneItem>();
				    HashMap<Integer, String> routeIcon = new HashMap<Integer, String>();
			        routeIcon.put(5, AppBundle.INSTANCE.css().i5Icon());
			        routeIcon.put(90, AppBundle.INSTANCE.css().i90Icon());

				    int numEntries = result.getExpressLanes().getRoutes().length();
					for (int i = 0; i < numEntries; i++) {
						item = new ExpressLaneItem();
						
						item.setStatus(result.getExpressLanes().getRoutes().get(i).getStatus());
						item.setRoute(result.getExpressLanes().getRoutes().get(i).getRoute());
						item.setTitle(result.getExpressLanes().getRoutes().get(i).getTitle());
						item.setUpdated(result.getExpressLanes().getRoutes().get(i).getUpdated());
						item.setRouteIcon(routeIcon.get(result.getExpressLanes().getRoutes().get(i).getRoute()));
						
						expressLaneItems.add(item);
					}
					
					view.hideProgressIndicator();
					view.render(expressLaneItems);
					view.refresh();
				}
				
			}
		});
		
	}

    private void createSchedulesLink() {
        ArrayList<Topic> schedules = new ArrayList<Topic>();
        schedules.add(new Topic("Check Express Lanes Schedule"));

        view.scheduleRender(schedules);
    }

	@Override
	public void onStop() {
		view.setPresenter(null);
	}

	@Override
	public void onDoneButtonPressed() {
		ActionEvent.fire(eventBus, ActionNames.BACK);
	}
	
	private static SafeHtml makeImage(ImageResource resource) {
		AbstractImagePrototype image = AbstractImagePrototype.create(resource);
		String html = image.getHTML();
		
		return SafeHtmlUtils.fromTrustedString(html);
	}

	@Override
	public void onItemSelected(int index) {
        inAppBrowser.open("http://www.wsdot.wa.gov/Northwest/King/ExpressLanes", "_blank",
                "enableViewportScale=yes,transitionstyle=fliphorizontal");
	}

}
