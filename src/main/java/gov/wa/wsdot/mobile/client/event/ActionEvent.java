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

package gov.wa.wsdot.mobile.client.event;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class ActionEvent extends Event<ActionEvent.Handler> {

	public interface Handler {
		void onAction(ActionEvent event);
	}

	private static final Type<ActionEvent.Handler> TYPE = new Type<ActionEvent.Handler>();

	public static void fire(EventBus eventBus, String sourceName) {
		eventBus.fireEventFromSource(new ActionEvent(), sourceName);
	}

	public static HandlerRegistration register(EventBus eventBus,
			String sourceName, Handler handler) {
		
		return eventBus.addHandlerToSource(TYPE, sourceName, handler);
	}

	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ActionEvent.Handler handler) {
		handler.onAction(this);
	}

	protected ActionEvent() {
	}
	
}
