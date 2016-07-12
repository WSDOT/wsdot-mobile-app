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

package gov.wa.wsdot.mobile.client.activities.trafficmap.restarea;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.place.shared.Place;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.css.AppBundle;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.client.plugins.accessibility.Accessibility;
import gov.wa.wsdot.mobile.client.plugins.analytics.Analytics;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService;
import gov.wa.wsdot.mobile.client.util.Consts;
import gov.wa.wsdot.mobile.shared.RestAreaFeed;
import gov.wa.wsdot.mobile.shared.RestAreaItem;

public class RestAreaActivity extends MGWTAbstractActivity implements
        RestAreaView.Presenter {

    private final ClientFactory clientFactory;
    private RestAreaView view;
    private EventBus eventBus;
    private Analytics analytics;
    private Accessibility accessibility;

    public RestAreaActivity(ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public void start(AcceptsOneWidget panel, final EventBus eventBus) {
        view = clientFactory.getRestAreaView();
        analytics = clientFactory.getAnalytics();
        accessibility = clientFactory.getAccessibility();
        this.eventBus = eventBus;
        view.setPresenter(this);

        Place place = clientFactory.getPlaceController().getWhere();

        if (place instanceof RestAreaPlace) {
            RestAreaPlace restAreaPlace = (RestAreaPlace) place;

            int restAreaId = Integer.valueOf(restAreaPlace.getId());

            String jsonString = AppBundle.INSTANCE.restAreaData().getText();
            RestAreaFeed restAreas = JsonUtils.safeEval(jsonString);

            view.setTitle("Safety Rest Area");


            SafeHtmlBuilder detailsHTMLBuilder = new SafeHtmlBuilder();

            detailsHTMLBuilder.appendEscaped(restAreas.getRestAreas().get(restAreaId).getRoute() + " - "
                    + restAreas.getRestAreas().get(restAreaId).getLocation());

            detailsHTMLBuilder.appendHtmlConstant("<br>");

            detailsHTMLBuilder.appendEscaped("Milepost: " + restAreas.getRestAreas().get(restAreaId).getMilepost() + " - "
                    + restAreas.getRestAreas().get(restAreaId).getDirection());

            view.setDetails(detailsHTMLBuilder.toSafeHtml());

            view.setNotes(restAreas.getRestAreas().get(restAreaId).getNotes());

            SafeHtmlBuilder amenitiesHTMLBuilder = new SafeHtmlBuilder();

            amenitiesHTMLBuilder.appendHtmlConstant("<ul>");
            for (int i = 0; i < restAreas.getRestAreas().get(restAreaId).getAmenities().length; i++){
                amenitiesHTMLBuilder.appendHtmlConstant("<li>");
                    amenitiesHTMLBuilder.appendEscaped(restAreas.getRestAreas().get(restAreaId).getAmenities()[i]);
                amenitiesHTMLBuilder.appendHtmlConstant("</li>");
            }

            if (restAreas.getRestAreas().get(restAreaId).getAmenities().length == 0){
                view.hideAmenitiesHeading();
            }

            amenitiesHTMLBuilder.appendHtmlConstant("</ul>");

            view.setAmenities(amenitiesHTMLBuilder.toSafeHtml());

            view.setLatLon(Double.valueOf(restAreas.getRestAreas().get(restAreaId).getLatitude()),
                    Double.valueOf(restAreas.getRestAreas().get(restAreaId).getLongitude()));


            view.refresh();


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
}