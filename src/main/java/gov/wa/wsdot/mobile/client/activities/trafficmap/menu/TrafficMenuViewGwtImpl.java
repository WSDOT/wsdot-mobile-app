package gov.wa.wsdot.mobile.client.activities.trafficmap.menu;

import com.google.gwt.aria.client.Roles;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.button.Button;
import com.googlecode.mgwt.ui.client.widget.header.HeaderTitle;
import com.googlecode.mgwt.ui.client.widget.input.checkbox.MCheckBox;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellList;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellSelectedEvent;
import com.googlecode.mgwt.ui.client.widget.list.widgetlist.WidgetList;
import com.googlecode.mgwt.ui.client.widget.panel.Panel;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.scroll.ScrollPanel;
import gov.wa.wsdot.mobile.client.widget.celllist.MyBasicCell;
import gov.wa.wsdot.mobile.shared.Topic;

import java.util.List;

/**
 * Created by simsl on 4/8/16.
 */
public class TrafficMenuViewGwtImpl extends Composite implements TrafficMenuView {

    /**
     * The UiBinder interface.
     */
    interface MenuViewGwtImplUiBinder extends
            UiBinder<Widget, TrafficMenuViewGwtImpl> {
    }

    /**
     * The UiBinder used to generate the view.
     */
    private static MenuViewGwtImplUiBinder uiBinder = GWT
            .create(MenuViewGwtImplUiBinder.class);

    @UiField
    HeaderTitle heading;

    @UiField(provided = true)
    CellList<Topic> cellList;

    @UiField(provided = true)
    WidgetList mapLayersList;

    @UiField
    Button doneButton;

    @UiField
    ScrollPanel scrollPanel;

    @UiField
    FlexSpacer leftFlexSpacer;

    private Presenter presenter;

    private static Storage localStorage = Storage.getLocalStorageIfSupported();

    public TrafficMenuViewGwtImpl() {

        cellList = new CellList<Topic>(new MyBasicCell<Topic>() {
            @Override
            public String getDisplayString(Topic model) {
                return model.getName();
            }

            @Override
            public boolean canBeSelected(Topic model) {
                return true;
            }
        });

        mapLayersList = new WidgetList();

        initWidget(uiBinder.createAndBindUi(this));

        accessibilityPrepare();

        if (MGWT.getOsDetection().isAndroid()) {
            leftFlexSpacer.setVisible(false);
            scrollPanel.setBounce(false);
        }
    }

    @UiHandler("cellList")
    protected void onCellSelected(CellSelectedEvent event) {
        if (presenter != null) {
            int index = event.getIndex();
            presenter.onItemSelected(index);
        }
    }

    @UiHandler("doneButton")
    protected void onDoneButtonPressed(TapEvent event) {
        if (presenter != null) {
            presenter.onDoneButtonPressed();
        }
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void render(List<Topic> createTopicsList, List<Topic> layersList) {
        mapLayersList.clear();

        cellList.render(createTopicsList);

        for (Topic layer : layersList){
            HorizontalPanel cell = new HorizontalPanel();
            if (layer.getName().equals("Rest Areas")) {
                MCheckBox mCheckBox = new MCheckBox();
                mCheckBox.setValue(Boolean.valueOf(localStorage.getItem("KEY_SHOW_RESTAREAS")));
                mCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
                    @Override
                    public void onValueChange(ValueChangeEvent<Boolean> event) {
                        localStorage.setItem("KEY_SHOW_RESTAREAS", String.valueOf(event.getValue()));
                    }
                });

                cell.setWidth("100%");

                Label title = new Label(layer.getName());
                cell.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
                cell.add(title);

                cell.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

                cell.add(mCheckBox);
            }
            mapLayersList.add(cell);
        }
    }

    @Override
    public void setSelected(int lastIndex, boolean b) {
        cellList.setSelectedIndex(lastIndex, b);
    }

    private void accessibilityPrepare(){

        // Add ARIA roles for accessibility
        Roles.getButtonRole().set(doneButton.getElement());
        Roles.getButtonRole().setAriaLabelProperty(doneButton.getElement(), "back");

        Roles.getHeadingRole().set(heading.getElement());
    }
}
