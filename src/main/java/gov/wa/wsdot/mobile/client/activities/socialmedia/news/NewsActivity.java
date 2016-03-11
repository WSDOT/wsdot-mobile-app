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

package gov.wa.wsdot.mobile.client.activities.socialmedia.news;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.client.plugins.analytics.Analytics;
import gov.wa.wsdot.mobile.client.util.Consts;
import gov.wa.wsdot.mobile.client.plugins.accessibility.Accessibility;
import gov.wa.wsdot.mobile.shared.NewsFeed;
import gov.wa.wsdot.mobile.shared.NewsItem;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
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

public class NewsActivity extends MGWTAbstractActivity implements
		NewsView.Presenter {

	private final ClientFactory clientFactory;
	private NewsView view;
	private EventBus eventBus;
	private PhoneGap phoneGap;
	private Analytics analytics;
	private Accessibility accessibility;
	private InAppBrowser inAppBrowser;
	private static ArrayList<NewsItem> newsItems = new ArrayList<NewsItem>();
	private static final String NEWS_FEED_URL = "http://www.wsdot.wa.gov/news/socialroom/posts/News";
	private static DateTimeFormat parseDateFormat = DateTimeFormat.getFormat("EEE, d MMM yyyy HH:mm:ss z");
	private static DateTimeFormat displayDateFormat = DateTimeFormat.getFormat("MMMM d, yyyy h:mm a");
	
	public NewsActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getNewsView();
		accessibility = clientFactory.getAccessibility();
		this.eventBus = eventBus;
		phoneGap = clientFactory.getPhoneGap();
		analytics = clientFactory.getAnalytics();
		inAppBrowser = phoneGap.getInAppBrowser();
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
			analytics.trackScreen("/Social Media/News");
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
			analytics.trackScreen("/Social Media/News/Details Link");
		}

		NewsItem item = newsItems.get(index);

		inAppBrowser.open(item.getLink(), "_blank",
				"enableViewportScale=yes,transitionstyle=fliphorizontal");
	}

	@Override
	public void onBackButtonPressed() {
		ActionEvent.fire(eventBus, ActionNames.BACK);
	}
	
	private void createPostList(final NewsView view) {
		newsItems.clear();
		view.showProgressIndicator();
		JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
		// Set timeout for 30 seconds (30000 milliseconds)
		jsonp.setTimeout(30000);
		jsonp.requestObject(NEWS_FEED_URL, new AsyncCallback<NewsFeed>() {

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
			public void onSuccess(NewsFeed result) {
				NewsItem item = null;
				
				if (result.getNews() != null) {
					int numEntries = result.getNews().getItems().length();
					for (int i = 0; i < numEntries; i++) {
						item = new NewsItem();
						
						item.setTitle(result.getNews().getItems().get(i).getTitle());
						item.setDescription(result.getNews().getItems().get(i).getDescription());
						item.setLink(result.getNews().getItems().get(i).getLink());
						
                        Date date = parseDateFormat.parse(result.getNews()
                                .getItems().get(i).getPubDate());
                        
                        item.setPubDate(displayDateFormat.format(date));
						
						newsItems.add(item);
					}
					
					view.hideProgressIndicator();
					view.render(newsItems);
					view.refresh();
				}
				
			}
		});

	}

}
