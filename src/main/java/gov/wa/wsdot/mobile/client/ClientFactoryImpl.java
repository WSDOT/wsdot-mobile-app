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

package gov.wa.wsdot.mobile.client;

import gov.wa.wsdot.mobile.client.activities.about.AboutView;
import gov.wa.wsdot.mobile.client.activities.about.AboutViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.alert.AlertView;
import gov.wa.wsdot.mobile.client.activities.alert.AlertViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.amtrakcascades.AmtrakCascadesView;
import gov.wa.wsdot.mobile.client.activities.amtrakcascades.AmtrakCascadesViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.amtrakcascades.schedules.AmtrakCascadesSchedulesView;
import gov.wa.wsdot.mobile.client.activities.amtrakcascades.schedules.AmtrakCascadesSchedulesViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.amtrakcascades.schedules.details.AmtrakCascadesSchedulesDetailsView;
import gov.wa.wsdot.mobile.client.activities.amtrakcascades.schedules.details.AmtrakCascadesSchedulesDetailsViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.borderwait.BorderWaitView;
import gov.wa.wsdot.mobile.client.activities.borderwait.BorderWaitViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.callout.CalloutView;
import gov.wa.wsdot.mobile.client.activities.callout.CalloutViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.camera.CameraView;
import gov.wa.wsdot.mobile.client.activities.camera.CameraViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.ferries.FerriesView;
import gov.wa.wsdot.mobile.client.activities.ferries.FerriesViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.FerriesRouteSchedulesView;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.FerriesRouteSchedulesViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.departures.FerriesRouteDeparturesView;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.departures.FerriesRouteDeparturesViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.sailings.FerriesRouteAlertDetailsView;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.sailings.FerriesRouteAlertDetailsViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.sailings.FerriesRouteSailingsView;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.sailings.FerriesRouteSailingsViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.VesselWatchMapView;
import gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.VesselWatchMapViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.location.GoToFerriesLocationView;
import gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.location.GoToFerriesLocationViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.vesseldetails.VesselDetailsView;
import gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.vesseldetails.VesselDetailsViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.home.HomeView;
import gov.wa.wsdot.mobile.client.activities.home.HomeViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.mountainpasses.MountainPassDetailsView;
import gov.wa.wsdot.mobile.client.activities.mountainpasses.MountainPassDetailsViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.mountainpasses.MountainPassesView;
import gov.wa.wsdot.mobile.client.activities.mountainpasses.MountainPassesViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.socialmedia.SocialMediaView;
import gov.wa.wsdot.mobile.client.activities.socialmedia.SocialMediaViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.socialmedia.blogger.BlogDetailsView;
import gov.wa.wsdot.mobile.client.activities.socialmedia.blogger.BlogDetailsViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.socialmedia.blogger.BlogView;
import gov.wa.wsdot.mobile.client.activities.socialmedia.blogger.BlogViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.socialmedia.facebook.FacebookView;
import gov.wa.wsdot.mobile.client.activities.socialmedia.facebook.FacebookViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.socialmedia.news.NewsDetailsView;
import gov.wa.wsdot.mobile.client.activities.socialmedia.news.NewsDetailsViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.socialmedia.news.NewsView;
import gov.wa.wsdot.mobile.client.activities.socialmedia.news.NewsViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.socialmedia.twitter.TwitterView;
import gov.wa.wsdot.mobile.client.activities.socialmedia.twitter.TwitterViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.socialmedia.youtube.YouTubeDetailsView;
import gov.wa.wsdot.mobile.client.activities.socialmedia.youtube.YouTubeDetailsViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.socialmedia.youtube.YouTubeView;
import gov.wa.wsdot.mobile.client.activities.socialmedia.youtube.YouTubeViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.tollrates.TollRatesView;
import gov.wa.wsdot.mobile.client.activities.tollrates.TollRatesViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.trafficmap.TrafficMapView;
import gov.wa.wsdot.mobile.client.activities.trafficmap.TrafficMapViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.trafficmap.expresslanes.SeattleExpressLanesView;
import gov.wa.wsdot.mobile.client.activities.trafficmap.expresslanes.SeattleExpressLanesViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.trafficmap.location.GoToLocationView;
import gov.wa.wsdot.mobile.client.activities.trafficmap.location.GoToLocationViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.trafficmap.seattleincidents.SeattleTrafficAlertsView;
import gov.wa.wsdot.mobile.client.activities.trafficmap.seattleincidents.SeattleTrafficAlertsViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.trafficmap.traveltimes.TravelTimeDetailsView;
import gov.wa.wsdot.mobile.client.activities.trafficmap.traveltimes.TravelTimeDetailsViewGwtImpl;
import gov.wa.wsdot.mobile.client.activities.trafficmap.traveltimes.TravelTimesView;
import gov.wa.wsdot.mobile.client.activities.trafficmap.traveltimes.TravelTimesViewGwtImpl;
import gov.wa.wsdot.mobile.client.plugins.analytics.Analytics;
import gov.wa.wsdot.mobile.client.plugins.accessibility.Accessibility;
import gov.wa.wsdot.mobile.client.service.WSDOTDataService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.googlecode.gwtphonegap.client.PhoneGap;

public class ClientFactoryImpl implements ClientFactory {

	private EventBus eventBus;
	private PlaceController placeController;
	private HomeView homeView;
	private WSDOTDataService dbService;
	private AboutViewGwtImpl aboutView;
	private FerriesViewGwtImpl ferriesView;
	private SocialMediaViewGwtImpl socialMediaView;
	private MountainPassesViewGwtImpl mountainPassesView;
	private MountainPassDetailsViewGwtImpl mountainPassDetailsView;
	private TrafficMapViewGwtImpl trafficMapView;
	private TollRatesViewGwtImpl tollRatesView;
	private BorderWaitViewGwtImpl borderWaitView;
	private TravelTimesViewGwtImpl travelTimesView;
	private GoToLocationViewGwtImpl goToLocationView;
	private BlogViewGwtImpl blogView;
	private FacebookViewGwtImpl facebookView;
	private TwitterViewGwtImpl twitterView;
	private YouTubeViewGwtImpl youTubeView;
	private NewsViewGwtImpl newsView;
	private NewsDetailsViewGwtImpl newsDetailsView;
	private FerriesRouteSchedulesViewGwtImpl ferriesRouteSchedulesView;
	private FerriesRouteSailingsViewGwtImpl ferriesRouteSailingsView;
	private FerriesRouteDeparturesViewGwtImpl ferriesRouteDeparturesView;
	private CameraViewGwtImpl cameraView;
	private AlertViewGwtImpl alertView;
	private PhoneGap phoneGap;
	private SeattleExpressLanesViewGwtImpl seattleExpressLanesView;
	private SeattleTrafficAlertsViewGwtImpl seattleTrafficAlertsView;
	private GoToFerriesLocationViewGwtImpl ferriesGoToLocationView;
	private VesselWatchMapViewGwtImpl vesselWatchMapView;
	private VesselDetailsViewGwtImpl vesselDetailsView;
	private BlogDetailsViewGwtImpl blogDetailsView;
	private YouTubeDetailsViewGwtImpl youTubeDetailsView;
	private TravelTimeDetailsViewGwtImpl travelTimeDetailsView;
	private FerriesRouteAlertDetailsViewGwtImpl ferriesRouteAlertDetailsView;
	private AmtrakCascadesViewGwtImpl amtrakCascadesView;
	private AmtrakCascadesSchedulesViewGwtImpl amtrakCascadesSchedulesView;
    private AmtrakCascadesSchedulesDetailsViewGwtImpl amtrakCascadesDeparturesView;
    private CalloutViewGwtImpl calloutView;
    private Analytics analytics;
	private Accessibility accessibility;

	public ClientFactoryImpl() {
		eventBus = new SimpleEventBus();
		placeController = new PlaceController(eventBus);
		homeView = new HomeViewGwtImpl();
		dbService = GWT.create(WSDOTDataService.class);
	}

	@Override
	public HomeView getHomeView() {
        /**
         * Instantiating a new View is a temporary measure until
         * I can figure out why Views which use tabs throw NPEs
         * on a device configuration change.
         */
	    homeView = new HomeViewGwtImpl();
		
		return homeView;
	}	
	
	@Override
	public EventBus getEventBus() {
		return eventBus;
	}

	@Override
	public PlaceController getPlaceController() {
		return placeController;
	}

	@Override
	public WSDOTDataService getDbService() {
		return dbService;
	}

    public void setPhoneGap(PhoneGap phoneGap) {
        this.phoneGap = phoneGap;
    }

	@Override
	public PhoneGap getPhoneGap() {
		return phoneGap;
	}

	public void setAnalytics(Analytics analytics) {
	    this.analytics = analytics;
	}

	@Override
	public Analytics getAnalytics() {
	    return analytics;
	}

	@Override
	public AboutView getAboutView() {
		if (aboutView == null) {
			aboutView = new AboutViewGwtImpl();
		}
		
		return aboutView;
	}

	@Override
	public FerriesView getFerriesView() {
		if (ferriesView == null) {
			ferriesView = new FerriesViewGwtImpl();
		}
		
		return ferriesView;
	}

	@Override
	public SocialMediaView getSocialMediaView() {
		if (socialMediaView == null) {
			socialMediaView = new SocialMediaViewGwtImpl();
		}
		return socialMediaView;
	}

	@Override
	public MountainPassesView getMountainPassesView() {
		if (mountainPassesView == null) {
			mountainPassesView = new MountainPassesViewGwtImpl();
		}
		return mountainPassesView;
	}

	@Override
	public MountainPassDetailsView getMountainPassDetailsView() {
		/**
		 * Because we are modifying the layout of the view by removing tabs
		 * if there is no weather forecast or cameras we need to re-create
		 * it each time. Otherwise we try and reference tabs which no longer
		 * exist in the layout.
		 */
		mountainPassDetailsView = new MountainPassDetailsViewGwtImpl();

		return mountainPassDetailsView;
	}

	@Override
	public TrafficMapView getTrafficMapView() {
		if (trafficMapView == null) {
			trafficMapView = new TrafficMapViewGwtImpl();
		}
		return trafficMapView;
	}

	@Override
	public TollRatesView getTollRatesView() {
        /**
         * Instantiating a new View is a temporary measure until
         * I can figure out why Views which use tabs throw NPEs
         * on a device configuration change.
         */
	    tollRatesView = new TollRatesViewGwtImpl();

		return tollRatesView;
	}

	@Override
	public BorderWaitView getBorderWaitView() {
        /**
         * Instantiating a new View is a temporary measure until
         * I can figure out why Views which use tabs throw NPEs
         * on a device configuration change.
         */
	    borderWaitView = new BorderWaitViewGwtImpl();

		return borderWaitView;
	}

	@Override
	public TravelTimesView getTravelTimesView() {
		if (travelTimesView == null) {
			travelTimesView = new TravelTimesViewGwtImpl();
		}
		return travelTimesView;
	}

	@Override
	public GoToLocationView getGoToLocationView() {
		if (goToLocationView == null) {
			goToLocationView = new GoToLocationViewGwtImpl();
		}
		return goToLocationView;
	}

	@Override
	public BlogView getBlogView() {
		if (blogView == null) {
			blogView = new BlogViewGwtImpl();
		}
		return blogView;
	}

	@Override
	public FacebookView getFacebookView() {
		if (facebookView == null) {
			facebookView = new FacebookViewGwtImpl();
		}
		return facebookView;
	}

	@Override
	public TwitterView getTwitterView() {
		if (twitterView == null) {
			twitterView = new TwitterViewGwtImpl();
		}
		return twitterView;
	}


	@Override
	public YouTubeView getYouTubeView() {
		if (youTubeView == null) {
			youTubeView = new YouTubeViewGwtImpl();
		}
		return youTubeView;
	}

	@Override
	public NewsView getNewsView() {
		if (newsView == null) {
			newsView = new NewsViewGwtImpl();
		}
		return newsView;
	}

	@Override
	public NewsDetailsView getNewsDetailsView() {
		if (newsDetailsView == null) {
			newsDetailsView = new NewsDetailsViewGwtImpl();
		}
		return newsDetailsView;
	}

	@Override
	public FerriesRouteSchedulesView getFerriesRouteSchedulesView() {
		if (ferriesRouteSchedulesView == null) {
			ferriesRouteSchedulesView = new FerriesRouteSchedulesViewGwtImpl();
		}
		return ferriesRouteSchedulesView;
	}

	@Override
	public FerriesRouteSailingsView getFerriesRouteSailingsView() {
        /**
         * Instantiating a new View is a temporary measure until
         * I can figure out why Views which use tabs throw NPEs
         * on a device configuration change.
         */
		ferriesRouteSailingsView = new FerriesRouteSailingsViewGwtImpl();

		return ferriesRouteSailingsView;
	}

	@Override
	public FerriesRouteDeparturesView getFerriesRouteDeparturesView() {
		ferriesRouteDeparturesView = new FerriesRouteDeparturesViewGwtImpl();

		return ferriesRouteDeparturesView;
	}

	@Override
	public CameraView getCameraView() {
        /**
         * Because we are modifying the layout of the view by removing tabs
         * if there is no video component we need to re-create
         * it each time. Otherwise we try and reference tabs which no longer
         * exist in the layout.
         */
        cameraView = new CameraViewGwtImpl();

		return cameraView;
	}

	@Override
	public AlertView getAlertView() {
		if (alertView == null) {
			alertView = new AlertViewGwtImpl();
		}
		return alertView;
	}

	@Override
	public SeattleExpressLanesView getSeattleExpressLanesView() {
		if (seattleExpressLanesView == null) {
			seattleExpressLanesView = new SeattleExpressLanesViewGwtImpl();
		}
		return seattleExpressLanesView;
	}

	@Override
	public SeattleTrafficAlertsView getSeattleTrafficAlertsView() {
		if (seattleTrafficAlertsView == null) {
			seattleTrafficAlertsView = new SeattleTrafficAlertsViewGwtImpl();
		}
		return seattleTrafficAlertsView;
	}

	@Override
	public GoToFerriesLocationView getFerriesGoToLocationView() {
		if (ferriesGoToLocationView == null) {
			ferriesGoToLocationView = new GoToFerriesLocationViewGwtImpl();
		}
		return ferriesGoToLocationView;
	}

	@Override
	public VesselWatchMapView getVesselWatchMapView() {
		if (vesselWatchMapView == null) {
			vesselWatchMapView = new VesselWatchMapViewGwtImpl();
		}
		return vesselWatchMapView;
	}


	@Override
	public VesselDetailsView getVesselDetailsView() {
		if (vesselDetailsView == null) {
			vesselDetailsView = new VesselDetailsViewGwtImpl();
		}
		return vesselDetailsView;
	}

	@Override
	public BlogDetailsView getBlogDetailsView() {
		if (blogDetailsView == null) {
			blogDetailsView = new BlogDetailsViewGwtImpl();
		}
		return blogDetailsView;
	}


	@Override
	public YouTubeDetailsView getYouTubeDetailsView() {
		if (youTubeDetailsView == null) {
			youTubeDetailsView = new YouTubeDetailsViewGwtImpl();
		}
		return youTubeDetailsView;
	}

	@Override
	public TravelTimeDetailsView getTravelTimeDetailsView() {
		if (travelTimeDetailsView == null) {
			travelTimeDetailsView = new TravelTimeDetailsViewGwtImpl();
		}
		return travelTimeDetailsView;
	}

	@Override
	public FerriesRouteAlertDetailsView getFerriesRouteAlertDetailsView() {
		if (ferriesRouteAlertDetailsView == null) {
			ferriesRouteAlertDetailsView = new FerriesRouteAlertDetailsViewGwtImpl();
		}
		return ferriesRouteAlertDetailsView;
	}

    @Override
    public AmtrakCascadesView getAmtrakCascadesView() {
        if (amtrakCascadesView == null) {
            amtrakCascadesView = new AmtrakCascadesViewGwtImpl();
        }
        return amtrakCascadesView;
    }

    @Override
    public AmtrakCascadesSchedulesView getAmtrakCascadesSchedulesView() {
        if (amtrakCascadesSchedulesView == null) {
            amtrakCascadesSchedulesView = new AmtrakCascadesSchedulesViewGwtImpl();
        }
        return amtrakCascadesSchedulesView;
    }

    @Override
    public AmtrakCascadesSchedulesDetailsView getAmtrakCascadesDeparturesView() {
        amtrakCascadesDeparturesView = new AmtrakCascadesSchedulesDetailsViewGwtImpl();

        return amtrakCascadesDeparturesView;
    }

    @Override
    public CalloutView getCalloutView() {
        if (calloutView == null) {
            calloutView = new CalloutViewGwtImpl();
        }
        return calloutView;
    }

	public void setAccessibility(Accessibility accessibility){
		this.accessibility = accessibility;
	}

	@Override
	public Accessibility getAccessibility() {
		return accessibility;
	}
}
