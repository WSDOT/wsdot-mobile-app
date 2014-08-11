/*
 * Copyright (c) 2014 Washington State Department of Transportation
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
import gov.wa.wsdot.mobile.client.css.AppBundle;
import gov.wa.wsdot.mobile.client.widget.CellDetailsWithIcon;
import gov.wa.wsdot.mobile.shared.CameraItem;
import gov.wa.wsdot.mobile.shared.ForecastItem;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.widget.button.Button;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellList;
import com.googlecode.mgwt.ui.client.widget.header.HeaderButton;
import com.googlecode.mgwt.ui.client.widget.header.HeaderTitle;
import com.googlecode.mgwt.ui.client.widget.panel.scroll.ScrollPanel;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellSelectedEvent;
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
	HeaderButton backButton;
	
	@UiField
	HeaderTitle title;
	
	@UiField
	Button starButton;
	
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
	ScrollPanel cameraScrollPanel;
	
	@UiField
	ScrollPanel forecastScrollPanel;

	private Presenter presenter;
	
	public MountainPassDetailsViewGwtImpl() {

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
		this.title.setText(title);
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
		//this.tabPanel.remove(tabIndex);
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
			starButton.setStyleName(AppBundle.INSTANCE.css().starButtonOn());
		} else {
			starButton.setStyleName(AppBundle.INSTANCE.css().starButtonOff());
		}		
	}

	@Override
	public void refreshReport() {
		reportScrollPanel.refresh();
	}

	@Override
	public void refreshCameras() {
		cameraScrollPanel.refresh();
	}

	@Override
	public void refreshForecast() {
		forecastScrollPanel.refresh();
	}

}
