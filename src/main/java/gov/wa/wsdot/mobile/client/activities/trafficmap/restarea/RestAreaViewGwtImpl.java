package gov.wa.wsdot.mobile.client.activities.trafficmap.restarea;
import com.google.gwt.aria.client.Roles;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.header.HeaderTitle;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.scroll.ScrollPanel;
import gov.wa.wsdot.mobile.client.util.ParserUtils;
import gov.wa.wsdot.mobile.client.widget.button.image.BackImageButton;

public class RestAreaViewGwtImpl extends Composite implements RestAreaView {

    /**
     * The UiBinder interface.
     */
    interface AlertViewGwtImplUiBinder extends
            UiBinder<Widget, RestAreaViewGwtImpl> {
    }

    /**
     * The UiBinder used to generate the view.
     */
    private static AlertViewGwtImplUiBinder uiBinder = GWT
            .create(AlertViewGwtImplUiBinder.class);

    @UiField
    BackImageButton backButton;

    @UiField
    FlexSpacer leftFlexSpacer;

    @UiField
    HeaderTitle title;

    @UiField
    ScrollPanel scrollPanel;

    @UiField
    HTML headlineDescription;

    @UiField
    HTML amenities;

    @UiField
    HTML notes;

    @UiField
    HTMLPanel mapPanel;

    @UiField
    Image staticMapImage;

    private Presenter presenter;

    public RestAreaViewGwtImpl() {

        initWidget(uiBinder.createAndBindUi(this));

        accessibilityPrepare();

        if (MGWT.getOsDetection().isAndroid()) {
            leftFlexSpacer.setVisible(false);
            scrollPanel.setBounce(false);
        }
    }

    @UiHandler("backButton")
    protected void onBackButtonPressed(TapEvent event) {
        if (presenter != null) {
            presenter.onBackButtonPressed();
        }
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setTitle(String title) {
        this.title.setText(title);
    }

    @Override
    public void setDetails(SafeHtml details) {
        this.headlineDescription.setHTML(details);
    }

    @Override
    public void setAmenities(SafeHtml amenities){
        this.amenities.setHTML(amenities);
    }

    @Override
    public void setNotes(String notes){
        this.notes.setText(notes);
    }

    @Override
    public void setLatLon(double latitude, double longitude) {

        this.staticMapImage.setUrl("http://maps.googleapis.com/maps/api/staticmap?center="
                + Double.toString(latitude) + "," + Double.toString(longitude)
                + "&zoom=15&size=320x320&maptype=roadmap&markers="
                + Double.toString(latitude) + "," + Double.toString(longitude)
                + "&sensor=false");

    }

    @Override
    public void refresh() {
        this.scrollPanel.refresh();
    }

    private void accessibilityPrepare(){
        // Add ARIA roles for accessibility
        Roles.getButtonRole().set(backButton.getElement());
        Roles.getButtonRole().setAriaLabelProperty(backButton.getElement(), "back");
    }
}