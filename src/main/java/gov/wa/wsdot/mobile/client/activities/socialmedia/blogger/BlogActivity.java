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

package gov.wa.wsdot.mobile.client.activities.socialmedia.blogger;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.client.plugins.analytics.Analytics;
import gov.wa.wsdot.mobile.client.util.Consts;
import gov.wa.wsdot.mobile.client.plugins.accessibility.Accessibility;
import gov.wa.wsdot.mobile.shared.BlogFeed;
import gov.wa.wsdot.mobile.shared.BlogItem;

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

public class BlogActivity extends MGWTAbstractActivity implements
		BlogView.Presenter {

	private final ClientFactory clientFactory;
	private BlogView view;
	private EventBus eventBus;
	private PhoneGap phoneGap;
	private Analytics analytics;
	private InAppBrowser inAppBrowser;
	private Accessibility accessibility;
	private static ArrayList<BlogItem> blogItems = new ArrayList<BlogItem>();
	private static final String BLOG_FEED_URL = "http://wsdotblog.blogspot.com/feeds/posts/default?alt=json&max-results=10";
	
	public BlogActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getBlogView();
		phoneGap = clientFactory.getPhoneGap();
		analytics = clientFactory.getAnalytics();
		accessibility = clientFactory.getAccessibility();
		inAppBrowser = this.phoneGap.getInAppBrowser();
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
						createBlogList(view);							
						view.refresh();
						callback.onSuccess(null);
					}
					
				}.schedule(1);
			}
			
		});

		view.setHeaderPullHandler(headerHandler);
		createBlogList(view);

		if (Consts.ANALYTICS_ENABLED) {
			analytics.trackScreen("/Social Media/Blog");
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
        if (Consts.ANALYTICS_ENABLED) {
            analytics.trackScreen("/Social Media/Blog/Details Link");
        }
		
		BlogItem item = blogItems.get(index);
		
		inAppBrowser.open(item.getLink(), "_blank",
				"enableViewportScale=yes,transitionstyle=fliphorizontal");
	}

	@Override
	public void onBackButtonPressed() {
		ActionEvent.fire(eventBus, ActionNames.BACK);
	}
	
	private void createBlogList(final BlogView view) {
		blogItems.clear();
		view.showProgressIndicator();
		JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
		// Set timeout for 30 seconds (30000 milliseconds)
		jsonp.setTimeout(30000);
		jsonp.requestObject(BLOG_FEED_URL, new AsyncCallback<BlogFeed>() {

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
			public void onSuccess(BlogFeed result) {
				BlogItem item = null;
				
				if (result.getFeed() != null) {
					int numEntries = result.getFeed().getEntry().length();
					for (int i = 0; i < numEntries; i++) {
						item = new BlogItem();
						
						item.setTitle(result.getFeed().getEntry().get(i).getTitle().getT());
						item.setContent(result.getFeed().getEntry().get(i).getContent().getT());
						item.setDescription("");
						item.setImageUrl("");
						item.setPublished(result.getFeed().getEntry().get(i).getPublished().getT());
						item.setLink(result.getFeed().getEntry().get(i).getLink().get(4).getHref());
						
						blogItems.add(item);
					}
					
					view.hideProgressIndicator();
					view.render(blogItems);
					view.refresh();
					accessibility.postScreenChangeNotification();
				}
				
			}
		});
		
	}

}
