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

package gov.wa.wsdot.mobile.client.activities.ferries.schedules.departures;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.FerriesSchedulesColumns;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService;
import gov.wa.wsdot.mobile.shared.FerriesAnnotationsItem;
import gov.wa.wsdot.mobile.shared.FerriesScheduleDateItem;
import gov.wa.wsdot.mobile.shared.FerriesScheduleTimesItem;
import gov.wa.wsdot.mobile.shared.FerriesTerminalItem;

import java.util.ArrayList;
import java.util.List;

import com.google.code.gwt.database.client.GenericRow;
import com.google.code.gwt.database.client.service.DataServiceException;
import com.google.code.gwt.database.client.service.ListCallback;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;

public class FerriesRouteDeparturesActivity extends
		MGWTAbstractActivity implements
		FerriesRouteDeparturesView.Presenter {

	private final ClientFactory clientFactory;
	private FerriesRouteDeparturesView view;
	private EventBus eventBus;
	private WSDOTDataService dbService;
	private static ArrayList<String> daysOfWeek = new ArrayList<String>();
    private static ArrayList<FerriesScheduleDateItem> scheduleDateItems = new ArrayList<FerriesScheduleDateItem>();
	private String routeId;
	private int sailingsIndex;
	
	public FerriesRouteDeparturesActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getFerriesRouteDeparturesView();
		dbService = clientFactory.getDbService();
		this.eventBus = eventBus;
		view.setPresenter(this);
		
		Place place = clientFactory.getPlaceController().getWhere();
		if (place instanceof FerriesRouteDeparturesPlace) {
            FerriesRouteDeparturesPlace ferriesRouteSchedulesDayDeparturesPlace = (FerriesRouteDeparturesPlace) place;
			routeId = ferriesRouteSchedulesDayDeparturesPlace.getId();
			sailingsIndex = ferriesRouteSchedulesDayDeparturesPlace.getIndex();
			createTopicsList(view, routeId, 0, sailingsIndex);
			panel.setWidget(view);
		}
		
	}

	@Override
	public void onDayOfWeekSelected(int position) {
		createTopicsList(view, routeId, position, sailingsIndex);
		
	}
	
	private void createTopicsList(final FerriesRouteDeparturesView view,
			String routeId, final int dayIndex, final int sailingsIndex) {

		dbService.getFerriesSchedulesRoute(routeId, new ListCallback<GenericRow>() {

			@Override
			public void onFailure(DataServiceException error) {
			}

			@Override
			public void onSuccess(List<GenericRow> result) {
				scheduleDateItems.clear();
				daysOfWeek.clear();
				FerriesScheduleDateItem scheduleDate;
				FerriesTerminalItem terminal;
				FerriesAnnotationsItem notes;
				FerriesScheduleTimesItem timesItem;
				
				view.showProgressBar();
				
				JSONValue value = JSONParser.parseStrict(result.get(0).getString(FerriesSchedulesColumns.FERRIES_SCHEDULE_DATE));
				JSONArray dates = value.isArray();
				
				int numDates = dates.size();
				for (int i = 0; i < numDates; i++) {
					scheduleDate = new FerriesScheduleDateItem();
					JSONObject date = dates.get(i).isObject();
					scheduleDate.setDate(date.get("Date").isString().stringValue().substring(6, 19));
					
					JSONArray sailings = date.get("Sailings").isArray();
					
					int numSailings = sailings.size();
					for (int j=0; j < numSailings; j++) {
						terminal = new FerriesTerminalItem();
						JSONObject sailing = sailings.get(j).isObject();

						terminal.setArrivingTerminalID(Integer.parseInt(sailing.get("ArrivingTerminalID").toString()));
						terminal.setArrivingTerminalName(sailing.get("ArrivingTerminalName").isString().stringValue());
						terminal.setDepartingTerminalID(Integer.parseInt(sailing.get("DepartingTerminalID").toString()));
						terminal.setDepartingTerminalName(sailing.get("DepartingTerminalName").isString().stringValue());
						
						JSONArray annotations = sailing.get("Annotations").isArray();
						int numAnnotations = annotations.size();
						for (int k = 0; k < numAnnotations; k++) {
							notes = new FerriesAnnotationsItem();
							notes.setAnnotation(annotations.get(k).isString().stringValue());
							terminal.setAnnotations(notes);	
						}
						
						JSONArray times = sailing.get("Times").isArray();
						int numTimes = times.size();
						for (int l = 0; l < numTimes; l++) {
							JSONObject time = times.get(l).isObject();
							timesItem = new FerriesScheduleTimesItem();
							timesItem.setDepartingTime(time.get("DepartingTime").isString().stringValue().substring(6, 19));

							try {
								timesItem.setArrivingTime(time.get("ArrivingTime").isString().stringValue().substring(6, 19));	
							} catch (Exception e) {
								timesItem.setArrivingTime("N/A");
							}
							
							JSONArray annotationIndexes = time.get("AnnotationIndexes").isArray();
							int numIndexes = annotationIndexes.size();
							String annotation = "";
							for (int m=0; m < numIndexes; m++) {
								FerriesAnnotationsItem p = terminal.getAnnotations().get(Integer.parseInt(annotationIndexes.get(m).toString()));
								annotation += p.getAnnotation();

								timesItem.setAnnotations(annotation);
							}
							terminal.setScheduleTimes(timesItem);
						}
						scheduleDate.setFerriesTerminalItem(terminal);
					}
					scheduleDateItems.add(scheduleDate);
				}
				
				String terminalTitle = scheduleDateItems.get(0)
						.getFerriesTerminalItem().get(sailingsIndex)
						.getDepartingTerminalName()
						+ " to "
						+ scheduleDateItems.get(0)
								.getFerriesTerminalItem()
								.get(sailingsIndex)
								.getArrivingTerminalName(); 
				
				int numItems = scheduleDateItems.size();
				
				for (int i=0; i < numItems; i++) {
					daysOfWeek.add(scheduleDateItems.get(i).getDate());
		        }
				
		        view.renderDaysOfWeek(daysOfWeek);
		        view.setDayOfWeekSelected(dayIndex);
		        
				view.hideProgressBar();
				view.setTitle(terminalTitle);
				view.render(scheduleDateItems
						.get(view.getDayOfWeekSelected())
						.getFerriesTerminalItem().get(sailingsIndex)
						.getScheduleTimes());
				
				view.refresh();			

			}
		});

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
