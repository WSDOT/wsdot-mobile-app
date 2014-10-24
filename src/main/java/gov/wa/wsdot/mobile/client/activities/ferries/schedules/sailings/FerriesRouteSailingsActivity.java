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

package gov.wa.wsdot.mobile.client.activities.ferries.schedules.sailings;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.departures.FerriesRouteDeparturesPlace;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.FerriesSchedulesColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService;
import gov.wa.wsdot.mobile.shared.FerriesRouteAlertItem;
import gov.wa.wsdot.mobile.shared.FerriesRouteItem;
import gov.wa.wsdot.mobile.shared.FerriesScheduleDateItem;
import gov.wa.wsdot.mobile.shared.FerriesTerminalItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.code.gwt.database.client.GenericRow;
import com.google.code.gwt.database.client.service.DataServiceException;
import com.google.code.gwt.database.client.service.ListCallback;
import com.google.code.gwt.database.client.service.VoidCallback;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;

public class FerriesRouteSailingsActivity extends MGWTAbstractActivity implements
		FerriesRouteSailingsView.Presenter {

	private final ClientFactory clientFactory;
	private FerriesRouteSailingsView view;
	private EventBus eventBus;
	private WSDOTDataService dbService;
	private static List<FerriesScheduleDateItem> scheduleDateItems = new ArrayList<FerriesScheduleDateItem>();
	private static List<FerriesRouteItem> ferriesRouteItems = new ArrayList<FerriesRouteItem>();
	private static List<FerriesRouteAlertItem> routeAlertItems = new ArrayList<FerriesRouteAlertItem>();
	private static DateTimeFormat dateFormat = DateTimeFormat.getFormat("MMMM d, yyyy h:mm a");
	private boolean isStarred = false;
	
	public FerriesRouteSailingsActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getFerriesRouteSailingsView();
		dbService = clientFactory.getDbService();
		this.eventBus = eventBus;
		view.setPresenter(this);
		
		Place place = clientFactory.getPlaceController().getWhere();
		if (place instanceof FerriesRouteSailingsPlace) {
			FerriesRouteSailingsPlace ferriesRouteSchedulesDaySailingsPlace = (FerriesRouteSailingsPlace) place;
			String routeId = ferriesRouteSchedulesDaySailingsPlace.getId();
			createTopicsList(routeId);
			panel.setWidget(view);
		}
		
	}
	
	private void createTopicsList(final String routeId) {

		dbService.getFerriesSchedulesRoute(routeId, new ListCallback<GenericRow>() {

			@Override
			public void onFailure(DataServiceException error) {
			}

			@Override
			public void onSuccess(List<GenericRow> result) {
				ferriesRouteItems.clear();
				scheduleDateItems.clear();
				routeAlertItems.clear();
				FerriesScheduleDateItem scheduleDate;
				FerriesTerminalItem terminal;
				FerriesRouteAlertItem routeAlert;
				
				view.showProgressIndicator();
				
				isStarred = result.get(0).getInt(FerriesSchedulesColumns.FERRIES_SCHEDULE_IS_STARRED) != 0;
				
				ferriesRouteItems.add(new FerriesRouteItem(
						Integer.parseInt(routeId),
						result.get(0)
								.getInt(FerriesSchedulesColumns.FERRIES_SCHEDULE_IS_STARRED)));
				
				JSONValue scheduleDateValue = JSONParser.parseStrict(result.get(0).getString(FerriesSchedulesColumns.FERRIES_SCHEDULE_DATE));
				JSONArray dates = scheduleDateValue.isArray();
				JSONValue routeAlertValue = JSONParser.parseStrict(result.get(0).getString(FerriesSchedulesColumns.FERRIES_SCHEDULE_ALERT));
				JSONArray alerts = routeAlertValue.isArray();
				
				int numDates = dates.size();
				for (int i = 0; i < numDates; i++) {
					scheduleDate = new FerriesScheduleDateItem();
					JSONObject date = dates.get(i).isObject();
					scheduleDate.setDate(date.get("Date").toString().substring(6, 19));
					
					JSONArray sailings = date.get("Sailings").isArray();
					
					int numSailings = sailings.size();
					for (int j=0; j < numSailings; j++) {
						terminal = new FerriesTerminalItem();
						JSONObject sailing = sailings.get(j).isObject();

						terminal.setArrivingTerminalID(Integer.parseInt(sailing.get("ArrivingTerminalID").toString()));
						terminal.setArrivingTerminalName(sailing.get("ArrivingTerminalName").isString().stringValue());
						terminal.setDepartingTerminalID(Integer.parseInt(sailing.get("DepartingTerminalID").toString()));
						terminal.setDepartingTerminalName(sailing.get("DepartingTerminalName").isString().stringValue());

						scheduleDate.setFerriesTerminalItem(terminal);
					}
					
					scheduleDateItems.add(scheduleDate);
				}
				
				int numAlerts = alerts.size();
				for (int i = 0; i < numAlerts; i++) {
					routeAlert = new FerriesRouteAlertItem();
					JSONObject alert = alerts.get(i).isObject();
					
					routeAlert.setBulletinID(Integer.parseInt(alert.get("BulletinID").toString()));
					
					routeAlert.setPublishDate(dateFormat
							.format(new Date(Long.parseLong(alert
									.get("PublishDate").isString().stringValue()
									.substring(6, 19)))));
					
					routeAlert.setAlertDescription(alert.get("AlertDescription").isString().stringValue());
					routeAlert.setAlertFullTitle(alert.get("AlertFullTitle").isString().stringValue());

					if (alert.get("AlertFullText").toString() != "null") {
						routeAlert.setAlertFullText(alert.get("AlertFullText").isString().stringValue());
					} else {
						routeAlert.setAlertFullText("");
					}
					
					routeAlertItems.add(routeAlert);
				}

				view.hideProgressIndicator();
				view.toggleStarButton(isStarred);
				view.render(scheduleDateItems.get(0).getFerriesTerminalItem());
				view.renderRouteAlerts(routeAlertItems);
				view.refresh();			

			}
		});

	}

	@Override
	public void onStop() {
		view.setPresenter(null);
	}

	@Override
	public void onSailingItemSelected(int index) {

		clientFactory.getPlaceController().goTo(
				new FerriesRouteDeparturesPlace(Integer
						.toString(ferriesRouteItems.get(0).getRouteID()), index));
	}

	@Override
	public void onRouteAlertSelected(int index) {
		FerriesRouteAlertItem item = routeAlertItems.get(index);
		
		clientFactory.getPlaceController().goTo(
				new FerriesRouteAlertDetailsPlace(item));
	}
	
	@Override
	public void onBackButtonPressed() {
		ActionEvent.fire(eventBus, ActionNames.BACK);
	}

	@Override
	public void onStarButtonPressed() {
		
		if (isStarred) {
			ferriesRouteItems.get(0).setIsStarred(0);
			isStarred = false;
		} else {
			ferriesRouteItems.get(0).setIsStarred(1);
			isStarred = true;
		}
		
		dbService.updateStarredFerriesSchedules(ferriesRouteItems, new VoidCallback() {

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
