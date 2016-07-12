package gov.wa.wsdot.mobile.client.activities.trafficmap.menu;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.activities.trafficmap.menu.expresslanes.SeattleExpressLanesPlace;
import gov.wa.wsdot.mobile.client.activities.trafficmap.menu.location.GoToLocationPlace;
import gov.wa.wsdot.mobile.client.activities.trafficmap.menu.traveltimes.TravelTimesPlace;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.client.plugins.accessibility.Accessibility;
import gov.wa.wsdot.mobile.client.plugins.analytics.Analytics;
import gov.wa.wsdot.mobile.client.util.Consts;
import gov.wa.wsdot.mobile.shared.Topic;

import java.util.ArrayList;
import java.util.List;

public class TrafficMenuActivity extends MGWTAbstractActivity implements
            TrafficMenuView.Presenter {

    private final ClientFactory clientFactory;
    private TrafficMenuView view;
    private Analytics analytics;
    private Accessibility accessibility;
    @SuppressWarnings("unused")
    private EventBus eventBus;

    public TrafficMenuActivity(ClientFactory clientFactory) {
            this.clientFactory = clientFactory;
        }

    @Override
    public void start(AcceptsOneWidget panel, final EventBus eventBus) {
        view = clientFactory.getTrafficMenuView();
        this.eventBus = eventBus;
        accessibility = clientFactory.getAccessibility();
        analytics = clientFactory.getAnalytics();
        view.setPresenter(this);
        view.render(createTopicsList(), createLayersList());

        if (Consts.ANALYTICS_ENABLED) {
            analytics.trackScreen("/Traffic Map/Menu");
        }

        panel.setWidget(view);
        accessibility.postScreenChangeNotification();
    }

    @Override
    public void onStop() {
            view.setPresenter(null);
        }

    @Override
    public void onItemSelected(int index) {
        if (index == 0) {
            if (Consts.ANALYTICS_ENABLED) {
                analytics.trackScreen("/Traffic Map/Menu/Go To Location");
            }
            clientFactory.getPlaceController().goTo(new GoToLocationPlace());
            return;
        }
        if (index == 1) {
            if (Consts.ANALYTICS_ENABLED) {
                analytics.trackScreen("/Traffic Map/Menu/Seattle Express Lanes");}
            clientFactory.getPlaceController().goTo(new SeattleExpressLanesPlace());
            return;
        }
        if (index == 2){
            if (Consts.ANALYTICS_ENABLED) {
                analytics.trackScreen("/Traffic Map/Menu/Travel Times");
            }
            clientFactory.getPlaceController().goTo(new TravelTimesPlace());
            return;
        }
    }

    @Override
    public void onDoneButtonPressed() {
        ActionEvent.fire(eventBus, ActionNames.BACK);
    }
    private List<Topic> createTopicsList() {
        ArrayList<Topic> list = new ArrayList<>();
        list.add(new Topic("Go To Location"));
        list.add(new Topic("Seattle Express Lanes"));
        list.add(new Topic("Travel Times"));
        return list;
    }

    private List<Topic> createLayersList() {
        ArrayList<Topic> list = new ArrayList<>();
        list.add(new Topic("Rest Areas"));
        return list;
    }
}