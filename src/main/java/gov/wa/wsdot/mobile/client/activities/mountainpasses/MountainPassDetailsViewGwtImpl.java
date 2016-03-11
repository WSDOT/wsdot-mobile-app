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

package gov.wa.wsdot.mobile.client.activities.mountainpasses;

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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
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
import com.googlecode.mgwt.ui.client.widget.tabbar.TabPanel;

import gov.wa.wsdot.mobile.client.activities.camera.CameraCell;
import gov.wa.wsdot.mobile.client.widget.CellDetailsWithIcon;
import gov.wa.wsdot.mobile.client.widget.button.image.BackImageButton;
import gov.wa.wsdot.mobile.client.widget.tabbar.CameraTabBarButton;
import gov.wa.wsdot.mobile.client.widget.tabbar.ForecastTabBarButton;
import gov.wa.wsdot.mobile.client.widget.tabbar.ReportTabBarButton;
import gov.wa.wsdot.mobile.client.widget.tabbar.SailingsTabBarButton;
import gov.wa.wsdot.mobile.client.widget.tabbar.WarningTabBarButton;
import gov.wa.wsdot.mobile.shared.CameraItem;
import gov.wa.wsdot.mobile.shared.ForecastItem;

public class MountainPassDetailsViewGwtImpl extends Composite implements
		MountainPassDetailsView {

	/**
	 * The UiBinder interface.
	 */	
	interface MountainPassDetailsViewGwtImplUiBinder extends
			UiBinder<Widget, MountainPassDetailsViewGwtImpl> {
	}
	
	/**
	 * The UiBinder used to generate the view.
	 */	
	private static MountainPassDetailsViewGwtImplUiBinder uiBinder = GWT
			.create(MountainPassDetailsViewGwtImplUiBinder.class);
	
	@UiField
	HeaderTitle heading;
	
	@UiField
	RootFlexPanel report;
	
	@UiField
	RootFlexPanel cameras;
	
	@UiField
	RootFlexPanel forecast;
	
	@UiField
	BackImageButton backButton;
	
	@UiField
	FlexSpacer leftFlexSpacer;
	
    @UiField(provided = true)
    NotimportantImageButton starButton;
    
    @UiField
    HTML title;
    
	@UiField
	HTML dateUpdated;
	
	@UiField
	HTML weatherCondition;
	
	@UiField
	HTML temperatureInFahrenheit;
	
	@UiField
	HTML elevationInFeet;
	
	@UiField
	HTML roadCondition;
	
	@UiField
	HTML restrictionOneTravelDirection;
	
	@UiField
	HTML restrictionOneText;
	
	@UiField
	HTML restrictionTwoTravelDirection;

	@UiField
	HTML restrictionTwoText;
	
	@UiField(provided = true)
	CellList<CameraItem> cameraCellList;
	
	@UiField(provided = true)
	CellList<ForecastItem> forecastCellList;
	
	@UiField
	TabPanel tabPanel;
	
	@UiField
	ReportTabBarButton reportTab;
	
	@UiField
	CameraTabBarButton camerasTab;
	
	@UiField
	ForecastTabBarButton forecastTab;
	
	@UiField
	ScrollPanel reportScrollPanel;
	
	@UiField
    static
	ScrollPanel cameraScrollPanel;
	
	@UiField
	ScrollPanel forecastScrollPanel;

	private Presenter presenter;

	public MountainPassDetailsViewGwtImpl() {
	    
	    starButton = new NotimportantImageButton();

	    handleOnLoad();
	       
		cameraCellList = new CellList<CameraItem>(new CameraCell<CameraItem>() {

			@Override
			public String getUrl(CameraItem model) {
				return model.getImageUrl();
			}
            
			@Override
			public boolean canBeSelected(CameraItem model) {
				return true;
			}
		});
		
		forecastCellList = new CellList<ForecastItem>(new CellDetailsWithIcon<ForecastItem>() {

			@Override
			public String getDisplayString(ForecastItem model) {
				return model.getDay();
			}

			@Override
			public String getDisplayImage(ForecastItem model) {
				return model.getWeatherIcon();
			}
			
			@Override
			public String getDisplayDescription(ForecastItem model) {
				return model.getForecastText();
			}

			@Override
			public String getDisplayLastUpdated(ForecastItem model) {
				return "";
			}
			
			@Override
			public boolean canBeSelected(ForecastItem model) {
				return false;
			}

		});

		initWidget(uiBinder.createAndBindUi(this));
<<<<<<< 9b88671fd0e5adfbd4d28bc0e5aa7bd776f24900

=======
        
		accessibilityPrepare();
		
>>>>>>> WIP: scrolling with footer
		if (MGWT.getOsDetection().isAndroid()) {
            leftFlexSpacer.setVisible(false);
            reportScrollPanel.setBounce(false);
            cameraScrollPanel.setBounce(false);
            forecastScrollPanel.setBounce(false);
        }
	}
	
	/**
	 * ScrollPanel doesn't allow scrolling to the bottom if it contains a CellList with images.
	 * 
	 * See: https://code.google.com/p/mgwt/issues/detail?id=276
	 * 
	 * ScrollPanel.refresh() must be explicitly called after the images are loaded.
	 * Since the onload event of images is not bubbling up, the LoadHandler can't be attached
	 * to the CellList. Instead, the onload event needs to be captured at the <img>, and directly
	 * trigger the ScrollPanel.refresh() from there.
	 */
    private native void handleOnLoad() /*-{
	    $wnd.refreshPanel = @gov.wa.wsdot.mobile.client.activities.mountainpasses.MountainPassDetailsViewGwtImpl::refreshPanel();
    }-*/;
		
	public static void refreshPanel() {
	    cameraScrollPanel.refresh();
	}

<<<<<<< 9b88671fd0e5adfbd4d28bc0e5aa7bd776f24900
    @UiHandler("tabPanel")
    protected void onTabSelected(SelectionEvent<Integer> event) {
        if (presenter != null) {
            int index = event.getSelectedItem();
            presenter.onTabSelected(index);
        }
    }

=======
    @UiHandler("reportTab")
    protected void onReportTabPressed(TapEvent event) {
    	if (presenter != null) {
    		accessibilityShowReport();
    	}
    }
    
    @UiHandler("camerasTab")
    protected void onCamerasTabPressed(TapEvent event) {
    	if (presenter != null) {
       		accessibilityShowCameras();
    	}
    }
    @UiHandler("forecastTab")
    protected void onForecastTabPressed(TapEvent event) {
    	if (presenter != null) {
       		accessibilityShowForcast();
    	}
    }
	
	
>>>>>>> WIP: scrolling with footer
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

	@UiHandler("cameraCellList")
	protected void onCameraCellSelected(CellSelectedEvent event) {
		if (presenter != null) {
			int index = event.getIndex();
			presenter.onCameraSelected(index);
		}
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void setTitle(String title) {
		this.title.setHTML(title);
	}

	@Override
	public void setDateUpdated(String dateUpdated) {
		this.dateUpdated.setHTML(dateUpdated);
	}

	@Override
	public void setWeatherCondition(String weatherCondition) {
		this.weatherCondition.setHTML(weatherCondition);
	}

	@Override
	public void setTemperatureInFahrenheit(String temperatureInFahrenheit) {
		this.temperatureInFahrenheit.setHTML(temperatureInFahrenheit);
	}

	@Override
	public void setElevationInFeet(String elevationInFeet) {
		this.elevationInFeet.setHTML(elevationInFeet + " ft");
	}

	@Override
	public void setRoadCondition(String roadCondition) {
		this.roadCondition.setHTML(roadCondition);
	}

	@Override
	public void setRestrictionOneTravelDirection(
			String restrictionOneTravelDirection) {
		this.restrictionOneTravelDirection.setHTML("Restrictions "
				+ restrictionOneTravelDirection + ":");
	}

	@Override
	public void setRestrictionOneText(String restrictionOneText) {
		this.restrictionOneText.setHTML(restrictionOneText);
	}

	@Override
	public void setRestrictionTwoTravelDirection(
			String restrictionTwoTravelDirection) {
		this.restrictionTwoTravelDirection.setHTML("Restrictions "
				+ restrictionTwoTravelDirection + ":");
	}

	@Override
	public void setRestrictionTwoText(String restrictionTwoText) {
		this.restrictionTwoText.setHTML(restrictionTwoText);
	}

	@Override
	public void removeTab(int tabIndex) {
        this.tabPanel.tabBar.remove(tabIndex);
        this.tabPanel.tabContainer.container.remove(tabIndex);
        this.tabPanel.tabContainer.refresh();
	}

	@Override
	public void renderCamera(List<CameraItem> createCameraList) {
		cameraCellList.render(createCameraList);
	}

	@Override
	public void setCameraSelected(int lastIndex, boolean b) {
		cameraCellList.setSelectedIndex(lastIndex, b);
	}

	@Override
	public void renderForecast(List<ForecastItem> createForecastList) {
		forecastCellList.render(createForecastList);		
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
	public void refreshReport() {
        tabPanel.tabContainer.refresh();
	    reportScrollPanel.refresh();
	}

	@Override
	public void refreshCameras() {
        tabPanel.tabContainer.refresh();
	    cameraScrollPanel.refresh();
	}

	@Override
	public void refreshForecast() {
        tabPanel.tabContainer.refresh();
	    forecastScrollPanel.refresh();
	}
	
	private void accessibilityShowReport(){
		Roles.getMainRole().setAriaHiddenState(report.getElement(), false);
		Roles.getMainRole().setAriaHiddenState(cameras.getElement(), true);
		Roles.getMainRole().setAriaHiddenState(forecast.getElement(), true);
		Roles.getTabRole().setAriaSelectedState(reportTab.getElement(), SelectedValue.TRUE);
		Roles.getTabRole().setAriaSelectedState(camerasTab.getElement(), SelectedValue.FALSE);
		Roles.getTabRole().setAriaSelectedState(forecastTab.getElement(), SelectedValue.FALSE);
	}
	
	private void accessibilityShowCameras(){
		Roles.getMainRole().setAriaHiddenState(report.getElement(), true);
		Roles.getMainRole().setAriaHiddenState(cameras.getElement(), false);
		Roles.getMainRole().setAriaHiddenState(forecast.getElement(), true);
		Roles.getTabRole().setAriaSelectedState(reportTab.getElement(), SelectedValue.FALSE);
		Roles.getTabRole().setAriaSelectedState(camerasTab.getElement(), SelectedValue.TRUE);
		Roles.getTabRole().setAriaSelectedState(forecastTab.getElement(), SelectedValue.FALSE);
	}
	
	private void accessibilityShowForcast(){
		Roles.getMainRole().setAriaHiddenState(report.getElement(), true);
		Roles.getMainRole().setAriaHiddenState(cameras.getElement(), true);
		Roles.getMainRole().setAriaHiddenState(forecast.getElement(), false);
		Roles.getTabRole().setAriaSelectedState(reportTab.getElement(), SelectedValue.FALSE);
		Roles.getTabRole().setAriaSelectedState(camerasTab.getElement(), SelectedValue.FALSE);
		Roles.getTabRole().setAriaSelectedState(forecastTab.getElement(), SelectedValue.TRUE);
	}
	
	
	private void accessibilityPrepare(){
		
		// Add ARIA roles for accessibility
		Roles.getButtonRole().set(backButton.getElement());
		Roles.getButtonRole().setAriaLabelProperty(backButton.getElement(), "back");
		
		Roles.getHeadingRole().set(heading.getElement());
		
		Roles.getCheckboxRole().set(starButton.getElement());
		Roles.getCheckboxRole().setAriaLabelProperty(starButton.getElement(), "favorite");
		
		Roles.getTabRole().set(reportTab.getElement());
		Roles.getTabRole().setAriaSelectedState(reportTab.getElement(), SelectedValue.TRUE);
		
		Roles.getTabRole().set(camerasTab.getElement());
		Roles.getTabRole().setAriaSelectedState(camerasTab.getElement(), SelectedValue.FALSE);
		
		Roles.getTabRole().set(forecastTab.getElement());
		Roles.getTabRole().setAriaSelectedState(forecastTab.getElement(), SelectedValue.FALSE);
		
		
		accessibilityShowReport();
	}
}
