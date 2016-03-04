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

import gov.wa.wsdot.mobile.client.ClientFactory;
import gov.wa.wsdot.mobile.client.activities.camera.CameraPlace;
import gov.wa.wsdot.mobile.client.event.ActionEvent;
import gov.wa.wsdot.mobile.client.event.ActionNames;
import gov.wa.wsdot.mobile.client.plugins.analytics.Analytics;
import gov.wa.wsdot.mobile.client.service.WSDOTContract.MountainPassesColumns;
import gov.wa.wsdot.mobile.client.util.Consts;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService;
import gov.wa.wsdot.mobile.shared.CameraItem;
import gov.wa.wsdot.mobile.shared.ForecastItem;
import gov.wa.wsdot.mobile.shared.MountainPassItem;

import java.util.ArrayList;
import java.util.List;

import com.google.code.gwt.database.client.GenericRow;
import com.google.code.gwt.database.client.service.DataServiceException;
import com.google.code.gwt.database.client.service.ListCallback;
import com.google.code.gwt.database.client.service.VoidCallback;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;

public class MountainPassDetailsActivity extends MGWTAbstractActivity implements
		MountainPassDetailsView.Presenter {

	private final ClientFactory clientFactory;
	private final MountainPassDetailsView view;
	private EventBus eventBus;
	private WSDOTDataService dbService;
	private Analytics analytics;
	private static List<MountainPassItem> mountainPassItems = new ArrayList<MountainPassItem>();
	private static List<CameraItem> cameraItems = new ArrayList<CameraItem>();
	private static List<ForecastItem> forecastItems = new ArrayList<ForecastItem>();
	private boolean isStarred = false;
	private static int lastTab = 0;

	public MountainPassDetailsActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	    view = clientFactory.getMountainPassDetailsView();
	}
	
	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		dbService = clientFactory.getDbService();
        analytics = clientFactory.getAnalytics();
		this.eventBus = eventBus;
		view.setPresenter(this);
		
		Place place = clientFactory.getPlaceController().getWhere();
		
		if (place instanceof MountainPassDetailsPlace) {
			MountainPassDetailsPlace mountainPassDetailsPlace = (MountainPassDetailsPlace) place;
			
			final String mountainPassId = mountainPassDetailsPlace.getId();
			
			dbService.getMountainPass(mountainPassId, new ListCallback<GenericRow>() {

				@Override
				public void onFailure(DataServiceException error) {
				}

				@Override
				public void onSuccess(List<GenericRow> result) {
					mountainPassItems.clear();
					cameraItems.clear();
					forecastItems.clear();
					CameraItem c;
					ForecastItem f;
					
					isStarred = result.get(0).getInt(MountainPassesColumns.MOUNTAIN_PASS_IS_STARRED) != 0;
					
					mountainPassItems.add(new MountainPassItem(
							Integer.parseInt(mountainPassId),
							result.get(0)
									.getInt(MountainPassesColumns.MOUNTAIN_PASS_IS_STARRED)));
					
					String mountainPassName = result.get(0).getString(MountainPassesColumns.MOUNTAIN_PASS_NAME);
					String dateUpdated = result.get(0).getString(MountainPassesColumns.MOUNTAIN_PASS_DATE_UPDATED);
					String weatherCondition = result.get(0).getString(MountainPassesColumns.MOUNTAIN_PASS_WEATHER_CONDITION);
					
					if (weatherCondition.equals("")) weatherCondition = "Not available";
					
					String temperature = result.get(0).getString(MountainPassesColumns.MOUNTAIN_PASS_TEMPERATURE);
					
					String temperatureInFahrenheit;
					if (temperature == null) {
						temperatureInFahrenheit = "Not available";
					} else {
						temperatureInFahrenheit = temperature.toString() + "\u00b0F";
					}
					
					String elevationInFeet = result.get(0).getString(MountainPassesColumns.MOUNTAIN_PASS_ELEVATION);
					String roadCondition = result.get(0).getString(MountainPassesColumns.MOUNTAIN_PASS_ROAD_CONDITION);
					String restrictionOneTravelDirection = result.get(0).getString(MountainPassesColumns.MOUNTAIN_PASS_RESTRICTION_ONE_DIRECTION);
					String restrictionOneText = result.get(0).getString(MountainPassesColumns.MOUNTAIN_PASS_RESTRICTION_ONE);
					String restrictionTwoTravelDirection = result.get(0).getString(MountainPassesColumns.MOUNTAIN_PASS_RESTRICTION_TWO_DIRECTION);
					String restrictionTwoText = result.get(0).getString(MountainPassesColumns.MOUNTAIN_PASS_RESTRICTION_TWO);

					JSONArray cameras = JSONParser.parseStrict(result.get(0).getString(MountainPassesColumns.MOUNTAIN_PASS_CAMERA)).isArray();
					int numCameras = cameras.size();

					for (int i = 0; i < numCameras; i++) {
						JSONObject camera = cameras.get(i).isObject();
						c = new CameraItem();
						
						c.setImageUrl(camera.get("url").isString().stringValue());
						c.setCameraId(Integer.parseInt(camera.get("id").toString()));
						c.setHasVideo(0);
						
						cameraItems.add(c);						
					}
					
					JSONArray forecasts = JSONParser.parseStrict(result.get(0).getString(MountainPassesColumns.MOUNTAIN_PASS_FORECAST)).isArray();
					int numForecasts = forecasts.size();
					
					for (int i = 0; i < numForecasts; i++) {
						JSONObject forecast = forecasts.get(i).isObject();
						f = new ForecastItem();

						f.setDay(forecast.get("Day").isString().stringValue());
						f.setForecastText(forecast.get("ForecastText").isString().stringValue());
						f.setWeatherIcon(forecast.get("WeatherIcon").isString().stringValue());
						
						forecastItems.add(f);						
					}
					
					view.setTitle(mountainPassName);
					view.toggleStarButton(isStarred);
					view.setDateUpdated(dateUpdated);
					view.setWeatherCondition(weatherCondition);
					view.setTemperatureInFahrenheit(temperatureInFahrenheit);
					view.setElevationInFeet(elevationInFeet);
					view.setRoadCondition(roadCondition);
					view.setRestrictionOneTravelDirection(restrictionOneTravelDirection);
					view.setRestrictionOneText(restrictionOneText);
					view.setRestrictionTwoTravelDirection(restrictionTwoTravelDirection);
					view.setRestrictionTwoText(restrictionTwoText);
					view.refreshReport();

					if (forecastItems.size() == 0) {
						view.removeTab(2);
					} else {
						view.renderForecast(forecastItems);
						view.refreshForecast();
					}
					
					if (cameraItems.size() == 0) {
						view.removeTab(1);
					} else {
						view.renderCamera(cameraItems);
						view.refreshCameras();
					}
				}
			});

		}
		
		panel.setWidget(view);
	}

	@Override
	public void onStop() {
		view.setPresenter(null);
	}
	
	@Override
	public void onBackButtonPressed() {
		ActionEvent.fire(eventBus, ActionNames.BACK);		
	}

	@Override
	public void onCameraSelected(int index) {
		CameraItem item = cameraItems.get(index);
		clientFactory.getPlaceController().goTo(
				new CameraPlace(Integer.toString(item.getCameraId())));		
	}

	@Override
	public void onStarButtonPressed() {
		
		if (isStarred) {
			mountainPassItems.get(0).setIsStarred(0);
			isStarred = false;
		} else {
			mountainPassItems.get(0).setIsStarred(1);
			isStarred = true;
		}
		
		dbService.updateStarredMountainPasses(mountainPassItems, new VoidCallback() {

			@Override
			public void onFailure(DataServiceException error) {			
			}

			@Override
			public void onSuccess() {
				view.toggleStarButton(isStarred);
			}
		});
		
	}

    @Override
    public void onTabSelected(int index) {
        int currentTab = index;

        switch(currentTab){
        case 0:
            if (currentTab != lastTab){
                if (Consts.ANALYTICS_ENABLED) {
                    analytics.trackScreen("/Mountain Passes/Pass/Report");
                }
            }
            break;
        case 1:
            if (currentTab != lastTab){
                if (Consts.ANALYTICS_ENABLED) {
                    analytics.trackScreen("/Mountain Passes/Pass/Cameras");
                }
            }
            break;
        case 2:
            if (currentTab != lastTab){
                if (Consts.ANALYTICS_ENABLED) {
                    analytics.trackScreen("/Mountain Passes/Pass/Forcast");
                }
            }
            break;
        default:
        }

        lastTab = currentTab;
    }

}
