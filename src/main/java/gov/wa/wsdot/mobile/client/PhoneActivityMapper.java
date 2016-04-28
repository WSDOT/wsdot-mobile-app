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

package gov.wa.wsdot.mobile.client;

import gov.wa.wsdot.mobile.client.activities.about.AboutActivity;
import gov.wa.wsdot.mobile.client.activities.about.AboutPlace;
import gov.wa.wsdot.mobile.client.activities.alert.AlertActivity;
import gov.wa.wsdot.mobile.client.activities.alert.AlertPlace;
import gov.wa.wsdot.mobile.client.activities.amtrakcascades.AmtrakCascadesActivity;
import gov.wa.wsdot.mobile.client.activities.amtrakcascades.AmtrakCascadesPlace;
import gov.wa.wsdot.mobile.client.activities.amtrakcascades.schedules.AmtrakCascadesSchedulesActivity;
import gov.wa.wsdot.mobile.client.activities.amtrakcascades.schedules.AmtrakCascadesSchedulesPlace;
import gov.wa.wsdot.mobile.client.activities.amtrakcascades.schedules.details.AmtrakCascadesSchedulesDetailsActivity;
import gov.wa.wsdot.mobile.client.activities.amtrakcascades.schedules.details.AmtrakCascadesSchedulesDetailsPlace;
import gov.wa.wsdot.mobile.client.activities.borderwait.BorderWaitActivity;
import gov.wa.wsdot.mobile.client.activities.borderwait.BorderWaitPlace;
import gov.wa.wsdot.mobile.client.activities.callout.CalloutActivity;
import gov.wa.wsdot.mobile.client.activities.callout.CalloutPlace;
import gov.wa.wsdot.mobile.client.activities.camera.CameraActivity;
import gov.wa.wsdot.mobile.client.activities.camera.CameraPlace;
import gov.wa.wsdot.mobile.client.activities.ferries.FerriesActivity;
import gov.wa.wsdot.mobile.client.activities.ferries.FerriesPlace;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.FerriesRouteSchedulesActivity;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.FerriesRouteSchedulesPlace;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.departures.FerriesRouteDeparturesActivity;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.departures.FerriesRouteDeparturesPlace;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.sailings.FerriesRouteAlertDetailsActivity;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.sailings.FerriesRouteAlertDetailsPlace;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.sailings.FerriesRouteSailingsActivity;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.sailings.FerriesRouteSailingsPlace;
import gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.VesselWatchMapActivity;
import gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.VesselWatchMapPlace;
import gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.location.GoToFerriesLocationActivity;
import gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.location.GoToFerriesLocationPlace;
import gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.vesseldetails.VesselDetailsActivity;
import gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.vesseldetails.VesselDetailsPlace;
import gov.wa.wsdot.mobile.client.activities.home.HomeActivity;
import gov.wa.wsdot.mobile.client.activities.home.HomePlace;
import gov.wa.wsdot.mobile.client.activities.mountainpasses.MountainPassDetailsActivity;
import gov.wa.wsdot.mobile.client.activities.mountainpasses.MountainPassDetailsPlace;
import gov.wa.wsdot.mobile.client.activities.mountainpasses.MountainPassesActivity;
import gov.wa.wsdot.mobile.client.activities.mountainpasses.MountainPassesPlace;
import gov.wa.wsdot.mobile.client.activities.socialmedia.SocialMediaActivity;
import gov.wa.wsdot.mobile.client.activities.socialmedia.SocialMediaPlace;
import gov.wa.wsdot.mobile.client.activities.socialmedia.blogger.BlogActivity;
import gov.wa.wsdot.mobile.client.activities.socialmedia.blogger.BlogDetailsActivity;
import gov.wa.wsdot.mobile.client.activities.socialmedia.blogger.BlogDetailsPlace;
import gov.wa.wsdot.mobile.client.activities.socialmedia.blogger.BlogPlace;
import gov.wa.wsdot.mobile.client.activities.socialmedia.facebook.FacebookActivity;
import gov.wa.wsdot.mobile.client.activities.socialmedia.facebook.FacebookPlace;
import gov.wa.wsdot.mobile.client.activities.socialmedia.news.NewsActivity;
import gov.wa.wsdot.mobile.client.activities.socialmedia.news.NewsDetailsActivity;
import gov.wa.wsdot.mobile.client.activities.socialmedia.news.NewsDetailsPlace;
import gov.wa.wsdot.mobile.client.activities.socialmedia.news.NewsPlace;
import gov.wa.wsdot.mobile.client.activities.socialmedia.twitter.TwitterActivity;
import gov.wa.wsdot.mobile.client.activities.socialmedia.twitter.TwitterPlace;
import gov.wa.wsdot.mobile.client.activities.socialmedia.youtube.YouTubeActivity;
import gov.wa.wsdot.mobile.client.activities.socialmedia.youtube.YouTubeDetailsActivity;
import gov.wa.wsdot.mobile.client.activities.socialmedia.youtube.YouTubeDetailsPlace;
import gov.wa.wsdot.mobile.client.activities.socialmedia.youtube.YouTubePlace;
import gov.wa.wsdot.mobile.client.activities.tollrates.TollRatesActivity;
import gov.wa.wsdot.mobile.client.activities.tollrates.TollRatesPlace;
import gov.wa.wsdot.mobile.client.activities.trafficmap.TrafficMapActivity;
import gov.wa.wsdot.mobile.client.activities.trafficmap.TrafficMapPlace;
import gov.wa.wsdot.mobile.client.activities.trafficmap.menu.TrafficMenuActivity;
import gov.wa.wsdot.mobile.client.activities.trafficmap.menu.TrafficMenuPlace;
import gov.wa.wsdot.mobile.client.activities.trafficmap.menu.expresslanes.SeattleExpressLanesActivity;
import gov.wa.wsdot.mobile.client.activities.trafficmap.menu.expresslanes.SeattleExpressLanesPlace;
import gov.wa.wsdot.mobile.client.activities.trafficmap.menu.location.GoToLocationActivity;
import gov.wa.wsdot.mobile.client.activities.trafficmap.menu.location.GoToLocationPlace;
import gov.wa.wsdot.mobile.client.activities.trafficmap.trafficincidents.TrafficAlertsActivity;
import gov.wa.wsdot.mobile.client.activities.trafficmap.trafficincidents.TrafficAlertsPlace;
import gov.wa.wsdot.mobile.client.activities.trafficmap.menu.traveltimes.TravelTimeDetailsActivity;
import gov.wa.wsdot.mobile.client.activities.trafficmap.menu.traveltimes.TravelTimeDetailsPlace;
import gov.wa.wsdot.mobile.client.activities.trafficmap.menu.traveltimes.TravelTimesActivity;
import gov.wa.wsdot.mobile.client.activities.trafficmap.menu.traveltimes.TravelTimesPlace;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

public class PhoneActivityMapper implements ActivityMapper {

	private final ClientFactory clientFactory;

	public PhoneActivityMapper(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public Activity getActivity(Place place) {

		if (place instanceof HomePlace) {
			return new HomeActivity(clientFactory);
		}
		
		if (place instanceof AboutPlace) {
			return new AboutActivity(clientFactory);
		}

		if (place instanceof TrafficMapPlace) {
			return new TrafficMapActivity(clientFactory);
		}
		
		if (place instanceof FerriesPlace) {
			return new FerriesActivity(clientFactory);
		}
		
		if (place instanceof FerriesRouteSchedulesPlace) {
			return new FerriesRouteSchedulesActivity(clientFactory);
		}
		
		if (place instanceof FerriesRouteSailingsPlace) {
			return new FerriesRouteSailingsActivity(clientFactory);
		}
		
		if (place instanceof FerriesRouteDeparturesPlace) {
			return new FerriesRouteDeparturesActivity(clientFactory);
		}
		
		if (place instanceof MountainPassesPlace) {
			return new MountainPassesActivity(clientFactory);
		}

		if (place instanceof MountainPassDetailsPlace) {
			return new MountainPassDetailsActivity(clientFactory);
		}
		
		if (place instanceof SocialMediaPlace) {
			return new SocialMediaActivity(clientFactory);
		}
		
		if (place instanceof TollRatesPlace) {
			return new TollRatesActivity(clientFactory);
		}
		
		if (place instanceof BorderWaitPlace) {
			return new BorderWaitActivity(clientFactory);
		}

		if (place instanceof TrafficMenuPlace) {
			return new TrafficMenuActivity(clientFactory);
		}

		if (place instanceof TravelTimesPlace) {
			return new TravelTimesActivity(clientFactory);
		}
		
		if (place instanceof GoToLocationPlace) {
			return new GoToLocationActivity(clientFactory);
		}
		
		if (place instanceof BlogPlace) {
			return new BlogActivity(clientFactory);
		}
		
		if (place instanceof FacebookPlace) {
			return new FacebookActivity(clientFactory);
		}
		
		if (place instanceof TwitterPlace) {
			return new TwitterActivity(clientFactory);
		}
		
		if (place instanceof YouTubePlace) {
			return new YouTubeActivity(clientFactory);
		}
		
		if (place instanceof NewsPlace) {
			return new NewsActivity(clientFactory);
		}
		
		if (place instanceof NewsDetailsPlace) {
			return new NewsDetailsActivity(clientFactory);
		}
		
		if (place instanceof CameraPlace) {
			return new CameraActivity(clientFactory);
		}
		
		if (place instanceof AlertPlace) {
			return new AlertActivity(clientFactory);
		}
		
		if (place instanceof SeattleExpressLanesPlace) {
			return new SeattleExpressLanesActivity(clientFactory);
		}
		
		if (place instanceof TrafficAlertsPlace) {
			return new TrafficAlertsActivity(clientFactory);
		}
		
		if (place instanceof GoToFerriesLocationPlace) {
			return new GoToFerriesLocationActivity(clientFactory);
		}
		
		if (place instanceof VesselWatchMapPlace) {
			return new VesselWatchMapActivity(clientFactory);
		}
		
		if (place instanceof VesselDetailsPlace) {
			return new VesselDetailsActivity(clientFactory);
		}
		
		if (place instanceof BlogDetailsPlace) {
			return new BlogDetailsActivity(clientFactory);
		}
		
		if (place instanceof YouTubeDetailsPlace) {
			return new YouTubeDetailsActivity(clientFactory);
		}
		
		if (place instanceof TravelTimeDetailsPlace) {
			return new TravelTimeDetailsActivity(clientFactory);
		}
		
		if (place instanceof FerriesRouteAlertDetailsPlace) {
			return new FerriesRouteAlertDetailsActivity(clientFactory);
		}
		
		if (place instanceof AmtrakCascadesPlace) {
		    return new AmtrakCascadesActivity(clientFactory);
		}
		
		if (place instanceof AmtrakCascadesSchedulesPlace) {
		    return new AmtrakCascadesSchedulesActivity(clientFactory);
		}

        if (place instanceof AmtrakCascadesSchedulesDetailsPlace) {
            return new AmtrakCascadesSchedulesDetailsActivity(clientFactory);
        }
        
        if (place instanceof CalloutPlace) {
            return new CalloutActivity(clientFactory);
        }
		
		return new HomeActivity(clientFactory);
	}
}
