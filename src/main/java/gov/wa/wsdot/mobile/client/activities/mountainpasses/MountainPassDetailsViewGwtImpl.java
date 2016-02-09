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

package gov.wa.wsdot.mobile.client.activities.mountainpasses;

import gov.wa.wsdot.mobile.client.activities.camera.CameraCell;
import gov.wa.wsdot.mobile.client.plugins.analytics.Analytics;
import gov.wa.wsdot.mobile.client.util.Consts;
import gov.wa.wsdot.mobile.client.widget.CellDetailsWithIcon;
import gov.wa.wsdot.mobile.client.widget.button.image.BackImageButton;
import gov.wa.wsdot.mobile.shared.CameraItem;
import gov.wa.wsdot.mobile.shared.ForecastItem;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.place.shared.Place;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.button.image.NotimportantImageButton;
import com.googlecode.mgwt.ui.client.widget.image.ImageHolder;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellList;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellSelectedEvent;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.scroll.ScrollPanel;
import com.googlecode.mgwt.ui.client.widget.tabbar.TabPanel;

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
	ScrollPanel reportScrollPanel;
	
	@UiField
    static
	ScrollPanel cameraScrollPanel;
	
	@UiField
	ScrollPanel forecastScrollPanel;

	private Presenter presenter;
	
	private static int lastTab = 0;
	
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
		
		// Add selection handler to tabContainer for google analytics tracking
    	tabPanel.tabContainer.addSelectionHandler(new SelectionHandler<Integer>(){
        	@Override
    		public void onSelection(SelectionEvent<Integer> event){
    			
    			int currentTab = tabPanel.tabContainer.getSelectedPage();
    			
    			switch(currentTab){
    			case 0:
    				if (currentTab != lastTab){
        				if (Consts.ANALYTICS_ENABLED) {
        					Analytics.trackScreen("/Mountain Passes/Pass/Report");
            			}
    				}
    				break;
    			case 1:
    				if (currentTab != lastTab){
        				if (Consts.ANALYTICS_ENABLED) {
        					Analytics.trackScreen("/Mountain Passes/Pass/Cameras");
            			}
    				}
    				break;
    			case 2:
    				if (currentTab != lastTab){
        				if (Consts.ANALYTICS_ENABLED) {
        					Analytics.trackScreen("/Mountain Passes/Pass/Forcast");
            			}
    				}
    				break;
    			default:    			
    			}

    			lastTab = currentTab;
    		}
    	});
		
		
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
        } else {
            starButton.setIcon(ImageHolder.get().notImportant());
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

}
