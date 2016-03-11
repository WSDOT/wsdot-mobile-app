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

package gov.wa.wsdot.mobile.client.activities.trafficmap.traveltimes;

import java.util.ArrayList;
import java.util.List;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.client.plugins.accessibility.Accessibility;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.TravelTimesColumns;
import gov.wa.wsdot.mobile.shared.TravelTimesItem;

import com.google.code.gwt.database.client.GenericRow;
import com.google.code.gwt.database.client.service.DataServiceException;
import com.google.code.gwt.database.client.service.ListCallback;
import com.google.code.gwt.database.client.service.VoidCallback;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;

public class TravelTimeDetailsActivity extends MGWTAbstractActivity implements
		TravelTimeDetailsView.Presenter {

	private final ClientFactory clientFactory;
	private TravelTimeDetailsView view;
	private EventBus eventBus;
	private Accessibility accessibility;
	private WSDOTDataService dbService;
	private static List<TravelTimesItem> travelTimesItems = new ArrayList<TravelTimesItem>();
	private boolean isStarred = false;

	public TravelTimeDetailsActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getTravelTimeDetailsView();
		dbService = clientFactory.getDbService();
		accessibility = clientFactory.getAccessibility();
		this.eventBus = eventBus;
		view.setPresenter(this);
		
		Place place = clientFactory.getPlaceController().getWhere();
		
		if (place instanceof TravelTimeDetailsPlace) {
			TravelTimeDetailsPlace travelTimeDetailsPlace = (TravelTimeDetailsPlace) place;
			
			final String routeId = travelTimeDetailsPlace.getId();
			
			dbService.getTravelTime(routeId, new ListCallback<GenericRow>() {

				@Override
				public void onFailure(DataServiceException error) {
				}

				@Override
				public void onSuccess(List<GenericRow> result) {
					travelTimesItems.clear();
					TravelTimesItem t;
					
					for (GenericRow time: result) {
						t = new TravelTimesItem();
						
						t.setRouteId(time.getInt(TravelTimesColumns.TRAVEL_TIMES_ID));
						t.setTitle(time.getString(TravelTimesColumns.TRAVEL_TIMES_TITLE));
						t.setUpdated(time.getString(TravelTimesColumns.TRAVEL_TIMES_UPDATED));
						t.setDistance(time.getString(TravelTimesColumns.TRAVEL_TIMES_DISTANCE));
						t.setAverageTime(time.getInt(TravelTimesColumns.TRAVEL_TIMES_AVERAGE));
						t.setCurrentTime(time.getInt(TravelTimesColumns.TRAVEL_TIMES_CURRENT));
						
						isStarred = time.getInt(TravelTimesColumns.TRAVEL_TIMES_IS_STARRED) != 0;
						
						travelTimesItems.add(t);
					}
					
					view.toggleStarButton(isStarred);
					view.renderTravelTime(travelTimesItems);

				}
			});
			
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

	@Override
	public void onStarButtonPressed() {

		if (isStarred) {
			travelTimesItems.get(0).setIsStarred(0);
			isStarred = false;
		} else {
			travelTimesItems.get(0).setIsStarred(1);
			isStarred = true;
		}
		
		dbService.updateStarredTravelTimes(travelTimesItems, new VoidCallback() {

			@Override
			public void onFailure(DataServiceException error) {			
			}

			@Override
			public void onSuccess() {
				view.toggleStarButton(isStarred);
			}
		});
		
	}

}