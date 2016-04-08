package gov.wa.wsdot.mobile.client.activities.trafficmap.menu;

import gov.wa.wsdot.mobile.shared.Topic;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

public interface TrafficMenuView extends IsWidget {

    public void setPresenter(Presenter presenter);

    public interface Presenter {

        public void onItemSelected(int index);

        public void onDoneButtonPressed();

    }

    public void render(List<Topic> createTopicsList);

    public void setSelected(int lastIndex, boolean b);

}
