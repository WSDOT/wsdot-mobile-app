package gov.wa.wsdot.mobile.client.activities.trafficmap.menu;

import com.google.gwt.aria.client.Roles;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.button.Button;
import com.googlecode.mgwt.ui.client.widget.header.HeaderTitle;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellList;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellSelectedEvent;
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

    @UiField
    Button doneButton;

    @UiField
    ScrollPanel scrollPanel;

    @UiField
    FlexSpacer leftFlexSpacer;

    private Presenter presenter;

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
    public void render(List<Topic> createTopicsList) {
        cellList.render(createTopicsList);
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
