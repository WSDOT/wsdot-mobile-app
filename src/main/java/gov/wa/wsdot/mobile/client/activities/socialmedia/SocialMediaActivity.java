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

package gov.wa.wsdot.mobile.client.activities.socialmedia;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.activities.socialmedia.blogger.BlogPlace;
import gov.wa.wsdot.mobile.client.activities.socialmedia.facebook.FacebookPlace;
import gov.wa.wsdot.mobile.client.activities.socialmedia.news.NewsPlace;
import gov.wa.wsdot.mobile.client.activities.socialmedia.twitter.TwitterPlace;
import gov.wa.wsdot.mobile.client.activities.socialmedia.youtube.YouTubePlace;
import gov.wa.wsdot.mobile.client.css.AppBundle;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.client.plugins.analytics.Analytics;
import gov.wa.wsdot.mobile.client.util.Consts;
import gov.wa.wsdot.mobile.shared.TopicWithImage;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;

public class SocialMediaActivity extends MGWTAbstractActivity implements
		SocialMediaView.Presenter {

	private final ClientFactory clientFactory;
	private SocialMediaView view;
	private EventBus eventBus;
	private Analytics analytics;
	
	public SocialMediaActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getSocialMediaView();
		analytics = clientFactory.getAnalytics();
		this.eventBus = eventBus;
		view.setPresenter(this);

		view.render(createTopicsList());

		if (Consts.ANALYTICS_ENABLED) {
		    analytics.trackScreen("/Social Media");
		}

        panel.setWidget(view);
	}
	
	@Override
	public void onStop() {
		view.setPresenter(null);
	}

	@Override
	public void onItemSelected(int index) {
		if (index == 0) {
			clientFactory.getPlaceController().goTo(new BlogPlace());
			return;
		}
		if (index == 1) {
			clientFactory.getPlaceController().goTo(new FacebookPlace());
			return;
		}
		/*
		if (index == 2) {
			clientFactory.getPlaceController().goTo(new FlickrPlace());
			return;
		}
		*/
		if (index == 2) {
			clientFactory.getPlaceController().goTo(new NewsPlace());
			return;
		}
		if (index == 3) {
			clientFactory.getPlaceController().goTo(new TwitterPlace());
			return;
		}
		if (index == 4) {
			clientFactory.getPlaceController().goTo(new YouTubePlace());
			return;
		}		
	}
	
	@Override
	public void onBackButtonPressed() {
		ActionEvent.fire(eventBus, ActionNames.BACK);		
	}

	private List<TopicWithImage> createTopicsList() {
		ArrayList<TopicWithImage> list = new ArrayList<TopicWithImage>();
		
		final String bloggerHtml = AppBundle.INSTANCE.css().bloggerIcon();
		final String facebookHtml = AppBundle.INSTANCE.css().facebookIcon();
		final String feedHtml = AppBundle.INSTANCE.css().feedIcon();
		//final String flickrHtml = AppBundle.INSTANCE.css().flickrIcon();
		final String twitterHtml = AppBundle.INSTANCE.css().twitterIcon();
		final String youtubeHtml = AppBundle.INSTANCE.css().youtubeIcon();
		
		list.add(new TopicWithImage("Blogger", bloggerHtml));
		list.add(new TopicWithImage("Facebook", facebookHtml));
		//list.add(new TopicWithImage("Flickr", flickrHtml));
		list.add(new TopicWithImage("News", feedHtml));
		list.add(new TopicWithImage("Twitter", twitterHtml));
		list.add(new TopicWithImage("YouTube", youtubeHtml));
		
		return list;
	}
	
	private static SafeHtml makeImage(ImageResource resource) {
		AbstractImagePrototype image = AbstractImagePrototype.create(resource);
		String html = image.getHTML();
		
		return SafeHtmlUtils.fromTrustedString(html);
	}

}
