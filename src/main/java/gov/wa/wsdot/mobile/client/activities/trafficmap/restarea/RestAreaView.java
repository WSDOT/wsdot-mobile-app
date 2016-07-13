package gov.wa.wsdot.mobile.client.activities.trafficmap.restarea;


import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;

public interface RestAreaView extends IsWidget {

    public void setPresenter(Presenter presenter);

    public interface Presenter {
        public void onBackButtonPressed();
    }

    public void setTitle(String title);

    public void setDetails(SafeHtml details);

    public void setAmenities(SafeHtml amenities);

    public void setNotes(String notes);

    public void setLatLon(double latitude, double longitude);

    public void hideAmenitiesHeading();

    public void showAmenitiesHeading();

    public void hideNotesHeading();

    public void showNotesHeading();

    public void refresh();

}