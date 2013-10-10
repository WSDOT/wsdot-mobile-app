/*
 * Copyright (c) 2013 Washington State Department of Transportation
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

import gov.wa.wsdot.mobile.client.activities.about.AboutPlace;
import gov.wa.wsdot.mobile.client.activities.alert.AlertPlace;
import gov.wa.wsdot.mobile.client.activities.borderwait.BorderWaitPlace;
import gov.wa.wsdot.mobile.client.activities.camera.CameraPlace;
import gov.wa.wsdot.mobile.client.activities.ferries.FerriesPlace;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.FerriesRouteSchedulesPlace;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.departures.FerriesRouteDeparturesPlace;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.sailings.FerriesRouteAlertDetailsPlace;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.sailings.FerriesRouteSailingsPlace;
import gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.VesselWatchMapPlace;
import gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.location.GoToFerriesLocationPlace;
import gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.vesseldetails.VesselDetailsPlace;
import gov.wa.wsdot.mobile.client.activities.home.HomePlace;
import gov.wa.wsdot.mobile.client.activities.mountainpasses.MountainPassDetailsPlace;
import gov.wa.wsdot.mobile.client.activities.mountainpasses.MountainPassesPlace;
import gov.wa.wsdot.mobile.client.activities.socialmedia.SocialMediaPlace;
import gov.wa.wsdot.mobile.client.activities.socialmedia.blogger.BlogDetailsPlace;
import gov.wa.wsdot.mobile.client.activities.socialmedia.blogger.BlogPlace;
import gov.wa.wsdot.mobile.client.activities.socialmedia.facebook.FacebookPlace;
import gov.wa.wsdot.mobile.client.activities.socialmedia.news.NewsDetailsPlace;
import gov.wa.wsdot.mobile.client.activities.socialmedia.news.NewsPlace;
import gov.wa.wsdot.mobile.client.activities.socialmedia.twitter.TwitterPlace;
import gov.wa.wsdot.mobile.client.activities.socialmedia.youtube.YouTubeDetailsPlace;
import gov.wa.wsdot.mobile.client.activities.socialmedia.youtube.YouTubePlace;
import gov.wa.wsdot.mobile.client.activities.tollrates.TollRatesPlace;
import gov.wa.wsdot.mobile.client.activities.trafficmap.TrafficMapPlace;
import gov.wa.wsdot.mobile.client.activities.trafficmap.expresslanes.SeattleExpressLanesPlace;
import gov.wa.wsdot.mobile.client.activities.trafficmap.location.GoToLocationPlace;
import gov.wa.wsdot.mobile.client.activities.trafficmap.seattleincidents.SeattleTrafficAlertsPlace;
import gov.wa.wsdot.mobile.client.activities.trafficmap.traveltimes.TravelTimeDetailsPlace;
import gov.wa.wsdot.mobile.client.activities.trafficmap.traveltimes.TravelTimesPlace;

import com.google.gwt.place.shared.Place;
import com.googlecode.mgwt.mvp.client.Animation;
import com.googlecode.mgwt.mvp.client.AnimationMapper;

public class PhoneAnimationMapper implements AnimationMapper {

	@Override
	public Animation getAnimation(Place oldPlace, Place newPlace) {
		if (oldPlace instanceof AboutPlace && newPlace instanceof HomePlace) {
			return Animation.SLIDE_REVERSE;
		}

		if (oldPlace instanceof TrafficMapPlace && newPlace instanceof HomePlace) {
			return Animation.SLIDE_REVERSE;
		}
		
		if (oldPlace instanceof FerriesPlace && newPlace instanceof HomePlace) {
			return Animation.SLIDE_REVERSE;
		}
		
		if (oldPlace instanceof FerriesRouteSchedulesPlace && newPlace instanceof FerriesPlace) {
			return Animation.SLIDE_REVERSE;
		}
		
		if (oldPlace instanceof VesselWatchMapPlace && newPlace instanceof FerriesPlace) {
			return Animation.SLIDE_REVERSE;
		}
		
		if (oldPlace instanceof FerriesRouteSailingsPlace
				&& newPlace instanceof FerriesRouteSchedulesPlace) {
			return Animation.SLIDE_REVERSE;
		}
		
		if (oldPlace instanceof FerriesRouteSailingsPlace
				&& newPlace instanceof HomePlace) {
			return Animation.SLIDE_REVERSE;
		}
		
		if (oldPlace instanceof MountainPassDetailsPlace && newPlace instanceof HomePlace) {
			return Animation.SLIDE_REVERSE;
		}
		
		if (oldPlace instanceof FerriesRouteDeparturesPlace
				&& newPlace instanceof FerriesRouteSailingsPlace) {
			return Animation.SLIDE_REVERSE;
		}

		if (oldPlace instanceof MountainPassesPlace && newPlace instanceof HomePlace) {
			return Animation.SLIDE_REVERSE;
		}
		
		if (oldPlace instanceof TravelTimeDetailsPlace && newPlace instanceof HomePlace) {
			return Animation.SLIDE_REVERSE;
		}
		
		if (oldPlace instanceof MountainPassDetailsPlace && newPlace instanceof MountainPassesPlace) {
			return Animation.SLIDE_REVERSE;
		}
		
		if (oldPlace instanceof SocialMediaPlace && newPlace instanceof HomePlace) {
			return Animation.SLIDE_REVERSE;
		}

		if (oldPlace instanceof BlogPlace && newPlace instanceof SocialMediaPlace) {
			return Animation.SLIDE_REVERSE;
		}

		if (oldPlace instanceof FacebookPlace && newPlace instanceof SocialMediaPlace) {
			return Animation.SLIDE_REVERSE;
		}
		
		if (oldPlace instanceof TwitterPlace && newPlace instanceof SocialMediaPlace) {
			return Animation.SLIDE_REVERSE;
		}

		if (oldPlace instanceof YouTubePlace && newPlace instanceof SocialMediaPlace) {
			return Animation.SLIDE_REVERSE;
		}
		
		if (oldPlace instanceof NewsPlace && newPlace instanceof SocialMediaPlace) {
			return Animation.SLIDE_REVERSE;
		}
		
		if (oldPlace instanceof NewsDetailsPlace && newPlace instanceof NewsPlace) {
			return Animation.SLIDE_REVERSE;
		}
		
		if (oldPlace instanceof TollRatesPlace && newPlace instanceof HomePlace) {
			return Animation.SLIDE_REVERSE;
		}

		if (oldPlace instanceof BorderWaitPlace && newPlace instanceof HomePlace) {
			return Animation.SLIDE_REVERSE;
		}
		
		if (oldPlace instanceof TrafficMapPlace && newPlace instanceof TravelTimesPlace) {
			return Animation.SLIDE_UP;
		}

		if (oldPlace instanceof TravelTimesPlace && newPlace instanceof TrafficMapPlace) {
			return Animation.SLIDE_UP_REVERSE;
		}
		
		if (oldPlace instanceof TravelTimeDetailsPlace && newPlace instanceof TravelTimesPlace) {
			return Animation.SLIDE_REVERSE;
		}
		
		if (oldPlace instanceof TrafficMapPlace && newPlace instanceof GoToLocationPlace) {
			return Animation.SLIDE_UP;
		}
		
		if (oldPlace instanceof GoToLocationPlace && newPlace instanceof TrafficMapPlace) {
			return Animation.SLIDE_UP_REVERSE;
		}
		
		if (oldPlace instanceof TrafficMapPlace && newPlace instanceof CameraPlace) {
			return Animation.FLIP;
		}
		
		if (oldPlace instanceof CameraPlace && newPlace instanceof TrafficMapPlace) {
			return Animation.FLIP_REVERSE;
		}

		if (oldPlace instanceof AlertPlace && newPlace instanceof HomePlace) {
			return Animation.SLIDE_REVERSE;
		}
		
		if (oldPlace instanceof TrafficMapPlace && newPlace instanceof AlertPlace) {
			return Animation.FLIP;
		}
		
		if (oldPlace instanceof AlertPlace && newPlace instanceof TrafficMapPlace) {
			return Animation.FLIP_REVERSE;
		}

		if (oldPlace instanceof TrafficMapPlace && newPlace instanceof SeattleExpressLanesPlace) {
			return Animation.SLIDE_UP;
		}
		
		if (oldPlace instanceof SeattleExpressLanesPlace && newPlace instanceof TrafficMapPlace) {
			return Animation.SLIDE_UP_REVERSE;
		}

		if (oldPlace instanceof TrafficMapPlace && newPlace instanceof SeattleTrafficAlertsPlace) {
			return Animation.SLIDE_UP;
		}
		
		if (oldPlace instanceof SeattleTrafficAlertsPlace && newPlace instanceof TrafficMapPlace) {
			return Animation.SLIDE_UP_REVERSE;
		}
		
		if (oldPlace instanceof VesselWatchMapPlace && newPlace instanceof GoToFerriesLocationPlace) {
			return Animation.SLIDE_UP;
		}
		
		if (oldPlace instanceof GoToFerriesLocationPlace && newPlace instanceof VesselWatchMapPlace) {
			return Animation.SLIDE_UP_REVERSE;
		}
		
		if (oldPlace instanceof VesselWatchMapPlace && newPlace instanceof CameraPlace) {
			return Animation.FLIP;
		}
		
		if (oldPlace instanceof CameraPlace && newPlace instanceof VesselWatchMapPlace) {
			return Animation.FLIP_REVERSE;
		}
		
		if (oldPlace instanceof VesselWatchMapPlace && newPlace instanceof VesselDetailsPlace) {
			return Animation.FLIP;
		}
		
		if (oldPlace instanceof VesselDetailsPlace && newPlace instanceof VesselWatchMapPlace) {
			return Animation.FLIP_REVERSE;
		}
		
		if (oldPlace instanceof CameraPlace && newPlace instanceof MountainPassDetailsPlace) {
			return Animation.SLIDE_REVERSE;
		}
		
		if (oldPlace instanceof BlogDetailsPlace && newPlace instanceof BlogPlace) {
			return Animation.SLIDE_REVERSE;
		}
		
		if (oldPlace instanceof YouTubeDetailsPlace && newPlace instanceof YouTubePlace) {
			return Animation.SLIDE_REVERSE;
		}
		
		if (oldPlace instanceof CameraPlace && newPlace instanceof HomePlace) {
			return Animation.SLIDE_REVERSE;
		}
		
		if (oldPlace instanceof FerriesRouteAlertDetailsPlace
				&& newPlace instanceof FerriesRouteSailingsPlace) {
			return Animation.SLIDE_REVERSE;
		}
		
		if (oldPlace == null && newPlace instanceof HomePlace) {
			return Animation.FADE;
		}
		
		return Animation.SLIDE;
	}

}
