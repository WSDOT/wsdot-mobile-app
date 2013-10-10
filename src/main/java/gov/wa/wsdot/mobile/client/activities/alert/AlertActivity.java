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

package gov.wa.wsdot.mobile.client.activities.alert;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.HighwayAlertsColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService;

import java.util.List;

import com.google.code.gwt.database.client.GenericRow;
import com.google.code.gwt.database.client.service.DataServiceException;
import com.google.code.gwt.database.client.service.ListCallback;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;

public class AlertActivity extends MGWTAbstractActivity implements
		AlertView.Presenter {

	private final ClientFactory clientFactory;
	private AlertView view;
	private EventBus eventBus;
	private WSDOTDataService dbService;
	private String alertId;

	public AlertActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getAlertView();
		dbService = clientFactory.getDbService();
		this.eventBus = eventBus;
		view.setPresenter(this);
		
		Place place = clientFactory.getPlaceController().getWhere();
		
		if (place instanceof AlertPlace) {
			AlertPlace alertPlace = (AlertPlace) place;
			alertId = alertPlace.getId();

			dbService.getHighwayAlert(alertId, new ListCallback<GenericRow>() {

				@Override
				public void onFailure(DataServiceException error) {
				}

				@Override
				public void onSuccess(List<GenericRow> result) {
                    view.setTitle(result.get(0).getString(
                            HighwayAlertsColumns.HIGHWAY_ALERT_CATEGORY));
                    view.setHeadlineDescription(result.get(0).getString(
                            HighwayAlertsColumns.HIGHWAY_ALERT_HEADLINE));
                    view.setLastUpdatedTime(result.get(0).getString(
                            HighwayAlertsColumns.HIGHWAY_ALERT_LAST_UPDATED));
                    view.setLatLon(result.get(0).getDouble(HighwayAlertsColumns.HIGHWAY_ALERT_LATITUDE),
                            result.get(0).getDouble(HighwayAlertsColumns.HIGHWAY_ALERT_LONGITUDE));
                    
                    view.refresh();
				}
			});
			
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