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

package gov.wa.wsdot.mobile.client.activities.socialmedia.twitter;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.css.AppBundle;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.shared.TwitterFeed;
import gov.wa.wsdot.mobile.shared.TwitterFeed.Media;
import gov.wa.wsdot.mobile.shared.TwitterFeed.Urls;
import gov.wa.wsdot.mobile.shared.TwitterItem;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.JsArray;
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

public class TwitterActivity extends MGWTAbstractActivity implements
		TwitterView.Presenter {

	private final ClientFactory clientFactory;
	private TwitterView view;
	private EventBus eventBus;
	private PhoneGap phoneGap;
	private InAppBrowser inAppBrowser;
	private static ArrayList<TwitterItem> twitterItems = new ArrayList<TwitterItem>();
	private static HashMap<String, String> twitterProfileImages = new HashMap<String, String>();
	private static HashMap<String, String> twitterScreenNames = new HashMap<String, String>();
	private static final String TWITTER_FEED_URL = "http://www.wsdot.wa.gov/news/socialroom/posts/twitter";
	
	public TwitterActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getTwitterView();
		this.eventBus = eventBus;
		phoneGap = clientFactory.getPhoneGap();
		inAppBrowser = this.phoneGap.getInAppBrowser();
		
		view.setPresenter(this);
		view.getPullHeader().setHTML("pull down");
		
		twitterScreenNames.put("All Accounts", "all");
		twitterScreenNames.put("Bertha", "BerthaDigsSR99");
		twitterScreenNames.put("Ferries", "wsferries");
		twitterScreenNames.put("Good To Go!", "GoodToGoWSDOT");
		twitterScreenNames.put("Snoqualmie Pass", "SnoqualmiePass");
		twitterScreenNames.put("WSDOT", "wsdot");
		twitterScreenNames.put("WSDOT North", "wsdot_north");
		twitterScreenNames.put("WSDOT Southwest", "wsdot_sw");
		twitterScreenNames.put("WSDOT Tacoma", "wsdot_tacoma");
		twitterScreenNames.put("WSDOT Traffic", "wsdot_traffic");		
		
		twitterProfileImages.put("wsferries", AppBundle.INSTANCE.css().wsdotFerriesIcon());
		twitterProfileImages.put("GoodToGoWSDOT", AppBundle.INSTANCE.css().wsdotGoodToGoIcon());
		twitterProfileImages.put("SnoqualmiePass", AppBundle.INSTANCE.css().wsdotSnoqualmiePassIcon());
		twitterProfileImages.put("wsdot", AppBundle.INSTANCE.css().wsdotIcon());
		twitterProfileImages.put("BerthaDigsSR99", AppBundle.INSTANCE.css().wsdotIcon());
		twitterProfileImages.put("wsdot_north", AppBundle.INSTANCE.css().wsdotNorthIcon());
		twitterProfileImages.put("wsdot_sw", AppBundle.INSTANCE.css().wsdotSwIcon());
		twitterProfileImages.put("wsdot_tacoma", AppBundle.INSTANCE.css().wsdotTacomaIcon());
		twitterProfileImages.put("wsdot_traffic", AppBundle.INSTANCE.css().wsdotTrafficIcon());		
		
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
						createPostList(view, twitterScreenNames.get(view.getAccountSelected()));							
						view.refresh();
						callback.onSuccess(null);
					}
					
				}.schedule(1);
			}
			
		});
		
		view.setHeaderPullHandler(headerHandler);
		createPostList(view, twitterScreenNames.get(view.getAccountSelected()));
		
		panel.setWidget(view);
	}
	
	@Override
	public void onStop() {
		view.setPresenter(null);
	}

	@Override
	public void onItemSelected(int index) {
		TwitterItem item = twitterItems.get(index);

		inAppBrowser.open("https://twitter.com/" + item.getScreenName()
				+ "/status/" + item.getId(), "",
				"enableViewportScale=yes,transitionstyle=fliphorizontal");
	}

	@Override
	public void onAccountSelected(String screenName) {
		createPostList(view, twitterScreenNames.get(screenName));		
	}
	
	@Override
	public void onBackButtonPressed() {
		ActionEvent.fire(eventBus, ActionNames.BACK);
	}
	
	private void createPostList(final TwitterView view, String screenName) {
		String url;
		
		if (screenName.equals("all")) {
			url = TWITTER_FEED_URL;
		} else {
			url = TWITTER_FEED_URL + "/" + screenName;
		}
		
		twitterItems.clear();
		view.showProgressIndicator();
		JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
		// Set timeout for 30 seconds (30000 milliseconds)
		jsonp.setTimeout(30000);
		jsonp.requestObject(url, new AsyncCallback<TwitterFeed>() {

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
			public void onSuccess(TwitterFeed result) {
				TwitterItem item = null;
				//String urlPattern = "(https?:\\/\\/[-a-zA-Z0-9._~:\\/?#@!$&\'()*+,;=%]+)";
				//String atPattern = "@+([_a-zA-Z0-9-]+)";
				//String hashPattern = "#+([_a-zA-Z0-9-]+)";
				String text;
				//String htmlText;
				
				if (result.getPosts() != null) {
					int numEntries = result.getPosts().length();
					for (int i = 0; i < numEntries; i++) {
						item = new TwitterItem();
						
						//htmlText = "";
						text = result.getPosts().get(i).getText();
						//htmlText = text.replaceAll(urlPattern, "<a href=\"$1\">$1</a>");
						//htmlText = htmlText.replaceAll(atPattern, "<a href=\"http://twitter.com/#!/$1\">@$1</a>");
						//htmlText = htmlText.replaceAll(hashPattern, "<a href=\"http://twitter.com/#!/search?q=%23$1\">#$1</a>");

						item.setText(text);
						item.setId(result.getPosts().get(i).getId());
						item.setFormatedHtmlText(text);
						item.setScreenName(result.getPosts().get(i).getUser().getScreenName());
						item.setCreatedAt(result.getPosts().get(i).getCreatedAt());
						
						item.setUserName(result.getPosts().get(i).getUser().getName());
						item.setProfileImage(twitterProfileImages.get(result.getPosts().get(i).getUser().getScreenName()));
						
						JsArray<Media> media = result.getPosts().get(i).getEntities().getMedia();
						if (media != null) {
							item.setMediaUrl(result.getPosts().get(i).getEntities().getMedia().get(0).getMediaUrl());
						}

						if (item.getMediaUrl() == null) {
							JsArray<Urls> urls = result.getPosts().get(i).getEntities().getUrls();
							if (urls != null) {
								if (result.getPosts().get(i).getEntities().getUrls().get(0) != null) {
									String expandedUrl = result.getPosts().get(i).getEntities().getUrls().get(0).getExpandedUrl();
									if (expandedUrl.matches("(.*)twitpic.com(.*)")) {
										item.setMediaUrl(expandedUrl);
									}
								}
							}
						}
						
						if (item.getMediaUrl() == null) {
							item.setMediaUrl("");
						}
						
						twitterItems.add(item);
					}
					
					view.hideProgressIndicator();
					view.render(twitterItems);
					view.refresh();
				}
				
			}
		});

	}
	
	private static SafeHtml makeImage(ImageResource resource) {
		AbstractImagePrototype image = AbstractImagePrototype.create(resource);
		String html = image.getHTML();
		
		return SafeHtmlUtils.fromTrustedString(html);
	}

}
