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

package gov.wa.wsdot.mobile.client.activities.amtrakcascades.schedules.details;

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.shared.AmtrakCascadesScheduleItem;

import java.util.List;
import java.util.Map;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;

public class AmtrakCascadesSchedulesDetailsActivity extends
        MGWTAbstractActivity implements
        AmtrakCascadesSchedulesDetailsView.Presenter {

    private ClientFactory clientFactory;
    private AmtrakCascadesSchedulesDetailsView view;
    private EventBus eventBus;

    public AmtrakCascadesSchedulesDetailsActivity(ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public void start(AcceptsOneWidget panel, final EventBus eventBus) {
        view = clientFactory.getAmtrakCascadesSchedulesDetailsView();
        this.eventBus = eventBus;
        view.setPresenter(this);
        Place place = clientFactory.getPlaceController().getWhere();

        if (place instanceof AmtrakCascadesSchedulesDetailsPlace) {
            try {
                AmtrakCascadesSchedulesDetailsPlace amtrakCascadesSchedulesDetailsPlace = (AmtrakCascadesSchedulesDetailsPlace) place;
                List<Map<String, AmtrakCascadesScheduleItem>> item = amtrakCascadesSchedulesDetailsPlace.getItem();
                GWT.log(item.get(0).toString());
                view.setTitle("Testing");
                view.render(item);
            } catch (Exception e) {
                GWT.log(e.getMessage());
            }
        }
        
        panel.setWidget(view);

    }

    @Override
    public void onBackButtonPressed() {
        ActionEvent.fire(eventBus, ActionNames.BACK);
    }

}