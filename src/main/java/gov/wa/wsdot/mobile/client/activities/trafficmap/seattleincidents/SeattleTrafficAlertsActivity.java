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

package gov.wa.wsdot.mobile.client.activities.trafficmap.seattleincidents;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.client.plugins.accessibility.Accessibility;
import gov.wa.wsdot.mobile.client.util.Consts;
import gov.wa.wsdot.mobile.shared.SeattleIncidentItem;
import gov.wa.wsdot.mobile.shared.SeattleIncidentsFeed;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.gwtphonegap.client.notification.AlertCallback;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowStandardHandler;
import com.googlecode.mgwt.ui.client.widget.panel.pull.PullArrowStandardHandler.PullActionHandler;

public class SeattleTrafficAlertsActivity extends MGWTAbstractActivity implements
		SeattleTrafficAlertsView.Presenter {

	private final ClientFactory clientFactory;
	private SeattleTrafficAlertsView view;
	private EventBus eventBus;
	private PhoneGap phoneGap;
	private Accessibility accessibility;
	private static ArrayList<SeattleIncidentItem> seattleIncidentItems = new ArrayList<SeattleIncidentItem>();
	private static List<Integer> blockingCategory = new ArrayList<Integer>();
    private static List<Integer> constructionCategory = new ArrayList<Integer>();
    private static List<Integer> specialCategory = new ArrayList<Integer>();
    private static final String SEATTLE_INCIDENTS_URL = Consts.HOST_URL + "/traveler/api/seattleincidents";
    private static DateTimeFormat dateFormat = DateTimeFormat.getFormat("MMMM d, yyyy h:mm a");
	
	public SeattleTrafficAlertsActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getSeattleTrafficAlertsView();
		accessibility = clientFactory.getAccessibility();
		accessibility.postScreenChangeNotification();
		this.eventBus = eventBus;
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
						createTopicsList();							
						view.refresh();
						callback.onSuccess(null);
					}
					
				}.schedule(1);
			}
			
		});
		
		view.setHeaderPullHandler(headerHandler);
		
		buildCategories();
		createTopicsList();
		panel.setWidget(view);

		accessibility.postScreenChangeNotification();
	}

	private void buildCategories() {
		
		blockingCategory.add(0); // Traffic conditions
		blockingCategory.add(4); // Incident
		blockingCategory.add(5); // Collision
		blockingCategory.add(6); // Disabled vehicle
		blockingCategory.add(10); // Water over roadway
		blockingCategory.add(11); // Obstruction
		blockingCategory.add(30); // Fallen tree

		constructionCategory.add(7); // Closures
		constructionCategory.add(8); // Road work
		constructionCategory.add(9); // Maintenance

		specialCategory.add(2); // Winter driving restriction
		specialCategory.add(12); // Sporting event
		specialCategory.add(13); // Seahawks game
		specialCategory.add(28); // Sounders game
		specialCategory.add(14); // Mariners game
		specialCategory.add(15); // Special event
		specialCategory.add(16); // Restriction
		specialCategory.add(17); // Flammable restriction
		specialCategory.add(29); // Huskies game
		
	}
	
	private void createTopicsList() {
		seattleIncidentItems.clear();
		view.showProgressIndicator();
		
		JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
		// Set timeout for 30 seconds (30000 milliseconds)
		jsonp.setTimeout(30000);
		jsonp.requestObject(SEATTLE_INCIDENTS_URL, new AsyncCallback<SeattleIncidentsFeed>() {

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
			public void onSuccess(SeattleIncidentsFeed result) {
				SeattleIncidentItem item = null;
				
				if (result.getSeattleIncidents() != null) {
					int numEntries = result.getSeattleIncidents().getItems().length();
					for (int i = 0; i < numEntries; i++) {
						item = new SeattleIncidentItem();
						
						item.setCategory(result.getSeattleIncidents().getItems().get(i).getCategory());
						item.setDescription(result.getSeattleIncidents().getItems().get(i).getDescription());
                        item.setLastUpdatedTime(dateFormat
                                .format(new Date(Long.parseLong(result
                                        .getSeattleIncidents()
                                        .getItems().get(i)
                                        .getLastUpdatedTime()
                                        .substring(6, 19)))));
						
						seattleIncidentItems.add(item);
					}
					
					view.hideProgressIndicator();
					categorizeAlerts();
					view.refresh();
				}
				
			}
		});
		
	}
	
	private void categorizeAlerts() {

		Stack<SeattleIncidentItem> amberalert = new Stack<SeattleIncidentItem>();
		Stack<SeattleIncidentItem> blocking = new Stack<SeattleIncidentItem>();
    	Stack<SeattleIncidentItem> construction = new Stack<SeattleIncidentItem>();
    	Stack<SeattleIncidentItem> special = new Stack<SeattleIncidentItem>();
    	
    	ArrayList<SeattleIncidentItem> amberAlerts = new ArrayList<SeattleIncidentItem>();
    	ArrayList<SeattleIncidentItem> blockingIncidents = new ArrayList<SeattleIncidentItem>();
    	ArrayList<SeattleIncidentItem> constructionClosures = new ArrayList<SeattleIncidentItem>();
    	ArrayList<SeattleIncidentItem> specialEvents = new ArrayList<SeattleIncidentItem>();
    	
    	for (SeattleIncidentItem item : seattleIncidentItems) {
			// Check if there is an active amber alert
			if (item.getCategory().equals(24)) {
				amberalert.push(item);
			}
			else if (blockingCategory.contains(item.getCategory())) {
				blocking.push(item);
			}
            else if (constructionCategory.contains(item.getCategory())) {
                construction.push(item);
            }
            else if (specialCategory.contains(item.getCategory())) {
                special.push(item);
            }
    	}
    	
    	if (amberalert != null && amberalert.size() != 0) {
    		while (!amberalert.empty()) {
    			amberAlerts.add(amberalert.pop());
    		}
    		view.showAmberAlerts();
    		view.renderAmberAlerts(amberAlerts);
    	} else {
    	    view.hideAmberAlerts();
    	}
    	
		if (blocking.empty()) {
			blockingIncidents.add(new SeattleIncidentItem("None reported"));
		} else {
			while (!blocking.empty()) {
				blockingIncidents.add(blocking.pop());
			}					
		}

		view.renderBlocking(blockingIncidents);
		
		if (construction.empty()) {
			constructionClosures.add(new SeattleIncidentItem("None reported"));
		} else {
			while (!construction.empty()) {
				constructionClosures.add(construction.pop());
			}					
		}
		
		view.renderConstruction(constructionClosures);

		if (special.empty()) {
			specialEvents.add(new SeattleIncidentItem("None reported"));
		} else {
			while (!special.empty()) {
				specialEvents.add(special.pop());
			}					
		}
		
		view.renderSpecial(specialEvents);
    	
	}
	
	@Override
	public void onStop() {
		view.setPresenter(null);
	}
	
	@Override
	public void onDoneButtonPressed() {
		ActionEvent.fire(eventBus, ActionNames.BACK);
	}

}