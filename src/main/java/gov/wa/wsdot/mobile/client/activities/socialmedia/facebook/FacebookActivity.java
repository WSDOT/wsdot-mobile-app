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

package gov.wa.wsdot.mobile.client.activities.socialmedia.facebook;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.client.plugins.analytics.Analytics;
import gov.wa.wsdot.mobile.client.util.Consts;
import gov.wa.wsdot.mobile.client.plugins.accessibility.Accessibility;
import gov.wa.wsdot.mobile.shared.FacebookFeed;
import gov.wa.wsdot.mobile.shared.FacebookItem;

import java.util.ArrayList;

import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.gwtphonegap.client.inappbrowser.InAppBrowser;
import com.googlecode.gwtphonegap.client.notification.AlertCallback;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowStandardHandler;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowStandardHandler.PullActionHandler;

public class FacebookActivity extends MGWTAbstractActivity implements
		FacebookView.Presenter {

	private final ClientFactory clientFactory;
	private FacebookView view;
	private EventBus eventBus;
	private PhoneGap phoneGap;
	private Analytics analytics;
	private Accessibility accessibility;
	private InAppBrowser inAppBrowser;
	private static ArrayList<FacebookItem> facebookItems = new ArrayList<FacebookItem>();
	private static final String FACEBOOK_FEED_URL = "http://www.wsdot.wa.gov/news/socialroom/posts/facebook";
	
	public FacebookActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getFacebookView();
		phoneGap = clientFactory.getPhoneGap();
		analytics = clientFactory.getAnalytics();
		accessibility = clientFactory.getAccessibility();
		inAppBrowser = phoneGap.getInAppBrowser();
		this.eventBus = eventBus;
		view.setPresenter(this);
		view.getPullHeader().setHTML("pull down");
		
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
						createPostList(view);							
						view.refresh();
						callback.onSuccess(null);
					}
					
				}.schedule(1);
			}
			
		});

		view.setHeaderPullHandler(headerHandler);
		createPostList(view);

		if (Consts.ANALYTICS_ENABLED) {
			analytics.trackScreen("/Social Media/Facebook");
		}

		panel.setWidget(view);

		accessibility.postNotification();
	}
	
	@Override
	public void onStop() {
		view.setPresenter(null);
	}

	@Override
	public void onItemSelected(int index) {
		if (Consts.ANALYTICS_ENABLED) {
			analytics.trackScreen("/Social Media/Facebook/Details Link");
		}
		
		FacebookItem item = facebookItems.get(index);
		
		if (item.getType().equalsIgnoreCase("photo")) {
			inAppBrowser.open(item.getLink(), "_blank",
					"enableViewportScale=yes,transitionstyle=fliphorizontal");
		} else {
			inAppBrowser.open("https://www.facebook.com/" + item.getId(), "_blank",
					"enableViewportScale=yes,transitionstyle=fliphorizontal");
		}
	}

	@Override
	public void onBackButtonPressed() {
		ActionEvent.fire(eventBus, ActionNames.BACK);
	}
	
	private void createPostList(final FacebookView view) {
		facebookItems.clear();
		view.showProgressIndicator();
		JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
		// Set timeout for 30 seconds (30000 milliseconds)
		jsonp.setTimeout(30000);
		jsonp.requestObject(FACEBOOK_FEED_URL, new AsyncCallback<FacebookFeed>() {

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
			public void onSuccess(FacebookFeed result) {
				FacebookItem item = null;
				//String urlPattern = "(https?:\\/\\/[-a-zA-Z0-9._~:\\/?#@!$&\'()*+,;=%]+)";
				String text;
				//String htmlText;
				
				if (result.getPosts() != null) {
					int numEntries = result.getPosts().length();
					for (int i = 0; i < numEntries; i++) {
						item = new FacebookItem();
						
						//htmlText = "";
						text = result.getPosts().get(i).getMessage();
						//htmlText = text.replaceAll(urlPattern, "<a href=\"$1\">$1</a>");
						
						item.setMessage(text);
						item.setHtmlFormattedMessage(text);
						item.setCreatedAt(result.getPosts().get(i).getCreatedAt());
						item.setId(result.getPosts().get(i).getId());
						item.setType(result.getPosts().get(i).getType());
						
						if (item.getType().equalsIgnoreCase("photo")) {
							item.setLink(result.getPosts().get(i).getLink());
						}
						
						facebookItems.add(item);
					}
					
					view.hideProgressIndicator();
					view.render(facebookItems);
					view.refresh();
				}
				
			}
		});

	}

}
