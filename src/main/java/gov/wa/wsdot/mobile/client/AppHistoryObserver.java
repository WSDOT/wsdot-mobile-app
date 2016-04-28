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

package gov.wa.wsdot.mobile.client;

import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.History;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.googlecode.mgwt.dom.client.event.mouse.HandlerRegistrationCollection;
import com.googlecode.mgwt.mvp.client.history.HistoryHandler;
import com.googlecode.mgwt.mvp.client.history.HistoryObserver;

public class AppHistoryObserver implements HistoryObserver {

	@Override
	public void onPlaceChange(Place place, HistoryHandler handler) {}

	@Override
	public void onHistoryChanged(Place place, HistoryHandler handler) {}

	@Override
	public void onAppStarted(Place place, HistoryHandler historyHandler) {
		onPhoneNav(place, historyHandler);
	}

	@Override
	public HandlerRegistration bind(EventBus eventBus, final HistoryHandler historyHandler) {
		
		HandlerRegistration backButtonRegistration = ActionEvent.register(
				eventBus, ActionNames.BACK, new ActionEvent.Handler() {
					@Override
					public void onAction(ActionEvent event) {
						History.back();
                        History.back();
					}
				});

		HandlerRegistration animationEndRegistration = ActionEvent.register(
				eventBus, ActionNames.ANIMATION_END, new ActionEvent.Handler() {

					@Override
					public void onAction(ActionEvent event) {
						History.back();
					}
				});

		HandlerRegistrationCollection col = new HandlerRegistrationCollection();
		col.addHandlerRegistration(backButtonRegistration);
		col.addHandlerRegistration(animationEndRegistration);

		return col;
	}

	private void onPhoneNav(Place place, HistoryHandler historyHandler) {
	}
}