/*
 * Copyright (c) 2015 Washington State Department of Transportation
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

package gov.wa.wsdot.mobile.client.activities.ferries.schedules.sailings;

import java.util.List;

import com.google.gwt.aria.client.CheckedValue;
import com.google.gwt.aria.client.LiveValue;
import com.google.gwt.aria.client.Roles;
import com.google.gwt.aria.client.SelectedValue;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.button.image.NotimportantImageButton;
import com.googlecode.mgwt.ui.client.widget.header.HeaderTitle;
import com.googlecode.mgwt.ui.client.widget.image.ImageHolder;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellList;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellSelectedEvent;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.flex.RootFlexPanel;
import com.googlecode.mgwt.ui.client.widget.panel.scroll.ScrollPanel;
import com.googlecode.mgwt.ui.client.widget.progress.ProgressIndicator;
import com.googlecode.mgwt.ui.client.widget.tabbar.TabPanel;

import gov.wa.wsdot.mobile.client.util.ParserUtils;
import gov.wa.wsdot.mobile.client.widget.TitleLastUpdatedCell;
import gov.wa.wsdot.mobile.client.widget.button.image.BackImageButton;
import gov.wa.wsdot.mobile.client.widget.tabbar.SailingsTabBarButton;
import gov.wa.wsdot.mobile.client.widget.tabbar.WarningTabBarButton;
import gov.wa.wsdot.mobile.shared.FerriesRouteAlertItem;
import gov.wa.wsdot.mobile.shared.FerriesTerminalItem;
import gov.wa.wsdot.mobile.client.widget.celllist.MyBasicCell;

public class FerriesRouteSailingsViewGwtImpl extends Composite
		implements FerriesRouteSailingsView {
	
	/**
	 * The UiBinder interface.
	 */
	interface FerriesRouteSailingsViewGwtImplUiBinder extends
			UiBinder<Widget, FerriesRouteSailingsViewGwtImpl> {
	}

	/**
	 * The UiBinder used to generate the view.
	 */
	private static FerriesRouteSailingsViewGwtImplUiBinder uiBinder = GWT
			.create(FerriesRouteSailingsViewGwtImplUiBinder.class);	
	
	@UiField
	HeaderTitle heading;
	
	@UiField
	RootFlexPanel sailings;
	
	@UiField
	RootFlexPanel alerts;
	
	@UiField
	ScrollPanel sailingsPanel;
	
	@UiField
	ScrollPanel alertsPanel;
	
	@UiField(provided = true)
	CellList<FerriesTerminalItem> sailingsCellList;
	
	@UiField(provided = true)
	CellList<FerriesRouteAlertItem> alertsCellList;
	
	@UiField
	BackImageButton backButton;
	
	@UiField
	FlexSpacer leftFlexSpacer;
	
	@UiField(provided = true)
	NotimportantImageButton starButton;
	
	@UiField
	ProgressIndicator progressIndicator;
	
	@UiField
	TabPanel tabPanel;
	
	@UiField
	SailingsTabBarButton sailingsTab;
	
	@UiField
	WarningTabBarButton alertsTab;
	
	@UiField
	HTMLPanel emptyAlerts;
	
	private Presenter presenter;
	
	public FerriesRouteSailingsViewGwtImpl() {
	    
	    starButton = new NotimportantImageButton();
		
		sailingsCellList = new CellList<FerriesTerminalItem>(
				new MyBasicCell<FerriesTerminalItem>() {
					
			@Override
			public String getDisplayString(FerriesTerminalItem model) {
				return model.getDepartingTerminalName() + " to " + model.getArrivingTerminalName();
			}

			@Override
			public boolean canBeSelected(FerriesTerminalItem model) {
				return true;
			}
		});
		
		alertsCellList = new CellList<FerriesRouteAlertItem>(
				new TitleLastUpdatedCell<FerriesRouteAlertItem>() {

			@Override
			public String getDisplayDescription(FerriesRouteAlertItem model) {
				return model.getAlertFullTitle();
			}

			@Override
			public String getDisplayLastUpdated(FerriesRouteAlertItem model) {
					return ParserUtils.relativeTime(model.getPublishDate(),
							"MMMM d, yyyy h:mm a", false);
			}
			
			@Override
			public boolean canBeSelected(FerriesRouteAlertItem model) {
				return true;
			}
			
		});
		
		initWidget(uiBinder.createAndBindUi(this));

		accessibilityPrepare();
		
        if (MGWT.getOsDetection().isAndroid()) {
            leftFlexSpacer.setVisible(false);
            sailingsPanel.setBounce(false);
            alertsPanel.setBounce(false);
        }
	}

    @UiHandler("tabPanel")
    protected void onTabSelected(SelectionEvent<Integer> event) {
        if (presenter != null) {
            int index = event.getSelectedItem();
            presenter.onTabSelected(index);
        }
    }
	
    @UiHandler("sailingsTab")
    protected void onTimesTabPressed(TapEvent event) {
    		accessibilityShowSailings();
    }
    
    @UiHandler("alertsTab")
    protected void onCamerasTabPressed(TapEvent event) {
    		accessibilityShowAlerts();
    }
	
	@UiHandler("sailingsCellList")
	protected void onSailingCellSelected(CellSelectedEvent event) {
		if (presenter != null) {
			int index = event.getIndex();
			presenter.onSailingItemSelected(index);
		}
	}
	
	@UiHandler("alertsCellList")
	protected void onRouteAlertCellSelected(CellSelectedEvent event) {
		if (presenter != null) {
			int index = event.getIndex();
			presenter.onRouteAlertSelected(index);
		}
	}

	@UiHandler("backButton")
	protected void onBackButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onBackButtonPressed();
		}
	}
	
	@UiHandler("starButton")
	protected void onStarButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onStarButtonPressed();
		}
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@Override
	public void render(List<FerriesTerminalItem> createTopicsList) {
		sailingsCellList.render(createTopicsList);
	}

	@Override
	public void renderRouteAlerts(
			List<FerriesRouteAlertItem> createRouteAlertsList) {
		alertsCellList.render(createRouteAlertsList);
		
	}
	
	@Override
	public void setSelected(int lastIndex, boolean b) {
		sailingsCellList.setSelectedIndex(lastIndex, b);
	}
	
	@Override
	public void showProgressIndicator() {
		progressIndicator.setVisible(true);
	}

	@Override
	public void hideProgressIndicator() {
		progressIndicator.setVisible(false);
	}

	@Override
	public void refresh() {
        tabPanel.tabContainer.refresh();
	    sailingsPanel.refresh();
		alertsPanel.refresh();
	}

	@Override
	public void toggleStarButton(boolean isStarred) {
        if (isStarred) {
            starButton.setIcon(ImageHolder.get().important());
            Roles.getCheckboxRole().setAriaCheckedState(starButton.getElement(), CheckedValue.TRUE);
        } else {
            starButton.setIcon(ImageHolder.get().notImportant());
            Roles.getCheckboxRole().setAriaCheckedState(starButton.getElement(), CheckedValue.FALSE);
        }
	}
	
	@Override
	public void showEmptyAlertsMessage() {
		emptyAlerts.setVisible(true);
	}

	@Override
	public void hideEmptyAlertsMessage() {
		emptyAlerts.setVisible(false);
	}
	
	private void accessibilityShowSailings(){
		Roles.getMainRole().setAriaHiddenState(sailings.getElement(), false);
		Roles.getMainRole().setAriaHiddenState(alerts.getElement(), true);
		Roles.getTabRole().setAriaSelectedState(sailingsTab.getElement(), SelectedValue.TRUE);
		Roles.getTabRole().setAriaSelectedState(alertsTab.getElement(), SelectedValue.FALSE);
	}
	
	private void accessibilityShowAlerts(){
		Roles.getMainRole().setAriaHiddenState(sailings.getElement(), true);
		Roles.getMainRole().setAriaHiddenState(alerts.getElement(), false);
		Roles.getTabRole().setAriaSelectedState(sailingsTab.getElement(), SelectedValue.FALSE);
		Roles.getTabRole().setAriaSelectedState(alertsTab.getElement(), SelectedValue.TRUE);
	}
	
    private void accessibilityPrepare(){
		// Add ARIA roles for accessibility
		Roles.getButtonRole().set(backButton.getElement());
		Roles.getButtonRole().setAriaLabelProperty(backButton.getElement(), "back");
		
		Roles.getHeadingRole().set(heading.getElement());
		
		Roles.getCheckboxRole().set(starButton.getElement());
		Roles.getCheckboxRole().setAriaLabelProperty(starButton.getElement(), "favorite");
		
		Roles.getTabRole().set(sailingsTab.getElement());
		Roles.getTabRole().setAriaSelectedState(sailingsTab.getElement(), SelectedValue.TRUE);
		Roles.getTabRole().setAriaLabelProperty(sailingsTab.getElement(), "sailings");
		
		Roles.getTabRole().set(alertsTab.getElement());
		Roles.getTabRole().setAriaSelectedState(alertsTab.getElement(), SelectedValue.FALSE);
		Roles.getTabRole().setAriaLabelProperty(alertsTab.getElement(), "route alerts");

		Roles.getProgressbarRole().set(progressIndicator.getElement());
		Roles.getProgressbarRole().setAriaLabelProperty(progressIndicator.getElement(), "loading indicator");
		
		accessibilityShowSailings();
		
    }
}
