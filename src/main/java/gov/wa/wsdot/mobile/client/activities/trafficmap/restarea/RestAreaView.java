package gov.wa.wsdot.mobile.client.activities.trafficmap.restarea;


import com.google.gwt.user.client.ui.IsWidget;

public interface RestAreaView extends IsWidget {

    public void setPresenter(Presenter presenter);

    public interface Presenter {

        public void onBackButtonPressed();

    }

    public void setTitle(String title);

    public void setHeadlineDescription(String headineDescription);

    public void setLastUpdatedTime(String lastUpdatedTime);

    public void setLatLon(double latitude, double longitude);

    public void refresh();

}