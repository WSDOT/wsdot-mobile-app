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

package gov.wa.wsdot.mobile.client;

import gov.wa.wsdot.mobile.client.activities.about.AboutView;
import gov.wa.wsdot.mobile.client.activities.alert.AlertView;
import gov.wa.wsdot.mobile.client.activities.amtrakcascades.AmtrakCascadesView;
import gov.wa.wsdot.mobile.client.activities.amtrakcascades.schedules.AmtrakCascadesSchedulesView;
import gov.wa.wsdot.mobile.client.activities.amtrakcascades.schedules.details.AmtrakCascadesSchedulesDetailsView;
import gov.wa.wsdot.mobile.client.activities.borderwait.BorderWaitView;
import gov.wa.wsdot.mobile.client.activities.camera.CameraView;
import gov.wa.wsdot.mobile.client.activities.ferries.FerriesView;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.FerriesRouteSchedulesView;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.departures.FerriesRouteDeparturesView;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.sailings.FerriesRouteAlertDetailsView;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.sailings.FerriesRouteSailingsView;
import gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.VesselWatchMapView;
import gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.location.GoToFerriesLocationView;
import gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.vesseldetails.VesselDetailsView;
import gov.wa.wsdot.mobile.client.activities.home.HomeView;
import gov.wa.wsdot.mobile.client.activities.mountainpasses.MountainPassDetailsView;
import gov.wa.wsdot.mobile.client.activities.mountainpasses.MountainPassesView;
import gov.wa.wsdot.mobile.client.activities.socialmedia.SocialMediaView;
import gov.wa.wsdot.mobile.client.activities.socialmedia.blogger.BlogDetailsView;
import gov.wa.wsdot.mobile.client.activities.socialmedia.blogger.BlogView;
import gov.wa.wsdot.mobile.client.activities.socialmedia.facebook.FacebookView;
import gov.wa.wsdot.mobile.client.activities.socialmedia.news.NewsDetailsView;
import gov.wa.wsdot.mobile.client.activities.socialmedia.news.NewsView;
import gov.wa.wsdot.mobile.client.activities.socialmedia.twitter.TwitterView;
import gov.wa.wsdot.mobile.client.activities.socialmedia.youtube.YouTubeDetailsView;
import gov.wa.wsdot.mobile.client.activities.socialmedia.youtube.YouTubeView;
import gov.wa.wsdot.mobile.client.activities.tollrates.TollRatesView;
import gov.wa.wsdot.mobile.client.activities.trafficmap.TrafficMapView;
import gov.wa.wsdot.mobile.client.activities.trafficmap.expresslanes.SeattleExpressLanesView;
import gov.wa.wsdot.mobile.client.activities.trafficmap.location.GoToLocationView;
import gov.wa.wsdot.mobile.client.activities.trafficmap.seattleincidents.SeattleTrafficAlertsView;
import gov.wa.wsdot.mobile.client.activities.trafficmap.traveltimes.TravelTimeDetailsView;
import gov.wa.wsdot.mobile.client.activities.trafficmap.traveltimes.TravelTimesView;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.gwtphonegap.client.PhoneGap;


public interface ClientFactory {

	public HomeView getHomeView();
	public EventBus getEventBus();
	public PlaceController getPlaceController();
	public WSDOTDataService getDbService();
	
	/**
	 * @return
	 */
	public AboutView getAboutView();
	public TrafficMapView getTrafficMapView();
	public FerriesView getFerriesView();
	public MountainPassesView getMountainPassesView();
	public MountainPassDetailsView getMountainPassDetailsView();
	public SocialMediaView getSocialMediaView();
	public TollRatesView getTollRatesView();
	public BorderWaitView getBorderWaitView();
	public TravelTimesView getTravelTimesView();
	public GoToLocationView getGoToLocationView();
	public BlogView getBlogView();
	public BlogDetailsView getBlogDetailsView();
	public FacebookView getFacebookView();
	public TwitterView getTwitterView();
	public YouTubeView getYouTubeView();
	public NewsView getNewsView();
	public NewsDetailsView getNewsDetailsView();
	public FerriesRouteSchedulesView getFerriesRouteSchedulesView();
	public FerriesRouteSailingsView getFerriesRouteSailingsView();
	public FerriesRouteDeparturesView getFerriesRouteDeparturesView();
	public CameraView getCameraView();
	public AlertView getAlertView();
	public PhoneGap getPhoneGap();
	public SeattleExpressLanesView getSeattleExpressLanesView();
	public SeattleTrafficAlertsView getSeattleTrafficAlertsView();
	public GoToFerriesLocationView getFerriesGoToLocationView();
	public VesselWatchMapView getVesselWatchMapView();
	public VesselDetailsView getVesselDetailsView();
	public YouTubeDetailsView getYouTubeDetailsView();
	public TravelTimeDetailsView getTravelTimeDetailsView();
	public FerriesRouteAlertDetailsView getFerriesRouteAlertDetailsView();
	public AmtrakCascadesView getAmtrakCascadesView();
	public AmtrakCascadesSchedulesView getAmtrakCascadesSchedulesView();
    public AmtrakCascadesSchedulesDetailsView getAmtrakCascadesDeparturesView();
}
