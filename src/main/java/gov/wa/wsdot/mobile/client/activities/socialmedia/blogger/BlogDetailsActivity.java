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

package gov.wa.wsdot.mobile.client.activities.socialmedia.blogger;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;

/**
 * TODO: This class is no longer in use as blogger is now mobile friendly.
 * @author simsl
 *
 */
public class BlogDetailsActivity extends MGWTAbstractActivity implements
		BlogDetailsView.Presenter {

	private final ClientFactory clientFactory;
	private BlogDetailsView view;
	private EventBus eventBus;

	public BlogDetailsActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getBlogDetailsView();
		this.eventBus = eventBus;
		view.setPresenter(this);
		
		Place place = clientFactory.getPlaceController().getWhere();
		
		if (place instanceof BlogDetailsPlace) {
			BlogDetailsPlace blogDetailsPlace = (BlogDetailsPlace) place;
			
			view.setTitle(blogDetailsPlace.getBlogItem().getTitle());
			view.setContent(blogDetailsPlace.getBlogItem().getContent());
			
		}

		panel.setWidget(view);
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