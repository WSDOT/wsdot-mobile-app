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

import gov.wa.wsdot.mobile.client.activities.about.AboutPlace.AboutPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.alert.AlertPlace.AlertPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.amtrakcascades.AmtrakCascadesPlace.AmtrakCascadesPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.amtrakcascades.schedules.AmtrakCascadesSchedulesPlace.AmtrakCascadesSchedulesPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.amtrakcascades.schedules.details.AmtrakCascadesSchedulesDetailsPlace.AmtrakCascadesSchedulesDetailsPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.borderwait.BorderWaitPlace.BorderWaitPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.callout.CalloutPlace.CalloutPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.camera.CameraPlace.CameraPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.ferries.FerriesPlace.FerriesPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.FerriesRouteSchedulesPlace.FerriesRouteSchedulesPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.departures.FerriesRouteDeparturesPlace.FerriesRouteDeparturesPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.sailings.FerriesRouteAlertDetailsPlace.FerriesRouteAlertDetailsPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.ferries.schedules.sailings.FerriesRouteSailingsPlace.FerriesRouteSailingsPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.VesselWatchMapPlace.VesselWatchMapPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.location.GoToFerriesLocationPlace.GoToFerriesLocationPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.ferries.vesselwatch.vesseldetails.VesselDetailsPlace.VesselDetailsPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.home.HomePlace.HomePlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.mountainpasses.MountainPassDetailsPlace.MountainPassDetailsPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.mountainpasses.MountainPassesPlace.MountainPassesPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.socialmedia.SocialMediaPlace.SocialMediaPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.socialmedia.blogger.BlogDetailsPlace.BlogDetailsPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.socialmedia.blogger.BlogPlace.BlogPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.socialmedia.facebook.FacebookPlace.FacebookPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.socialmedia.news.NewsDetailsPlace.NewsDetailsPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.socialmedia.news.NewsPlace.NewsPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.socialmedia.twitter.TwitterPlace.TwitterPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.socialmedia.youtube.YouTubeDetailsPlace.YouTubeDetailsPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.socialmedia.youtube.YouTubePlace.YouTubePlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.tollrates.TollRatesPlace.TollRatesPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.trafficmap.TrafficMapPlace.TrafficMapPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.trafficmap.expresslanes.SeattleExpressLanesPlace.SeattleExpressLanesPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.trafficmap.location.GoToLocationPlace.GoToLocationPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.trafficmap.trafficincidents.TrafficAlertsPlace.TrafficAlertsPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.trafficmap.traveltimes.TravelTimeDetailsPlace.TravelTimeDetailsPlaceTokenizer;
import gov.wa.wsdot.mobile.client.activities.trafficmap.traveltimes.TravelTimesPlace.TravelTimesPlaceTokenizer;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

@WithTokenizers({ HomePlaceTokenizer.class, AboutPlaceTokenizer.class,
        FerriesPlaceTokenizer.class, SocialMediaPlaceTokenizer.class,
        MountainPassesPlaceTokenizer.class,
        MountainPassDetailsPlaceTokenizer.class,
        TrafficMapPlaceTokenizer.class, TollRatesPlaceTokenizer.class,
        BorderWaitPlaceTokenizer.class, TravelTimesPlaceTokenizer.class,
        GoToLocationPlaceTokenizer.class, BlogPlaceTokenizer.class,
        FacebookPlaceTokenizer.class, TwitterPlaceTokenizer.class,
        YouTubePlaceTokenizer.class, NewsPlaceTokenizer.class,
        FerriesRouteSchedulesPlaceTokenizer.class,
        FerriesRouteSailingsPlaceTokenizer.class,
        FerriesRouteDeparturesPlaceTokenizer.class, CameraPlaceTokenizer.class,
        AlertPlaceTokenizer.class, SeattleExpressLanesPlaceTokenizer.class,
        TrafficAlertsPlaceTokenizer.class,
        GoToFerriesLocationPlaceTokenizer.class,
        VesselWatchMapPlaceTokenizer.class, NewsDetailsPlaceTokenizer.class,
        VesselDetailsPlaceTokenizer.class, BlogDetailsPlaceTokenizer.class,
        YouTubeDetailsPlaceTokenizer.class,
        TravelTimeDetailsPlaceTokenizer.class,
        FerriesRouteAlertDetailsPlaceTokenizer.class,
        AmtrakCascadesPlaceTokenizer.class,
        AmtrakCascadesSchedulesPlaceTokenizer.class,
        AmtrakCascadesSchedulesDetailsPlaceTokenizer.class,
        CalloutPlaceTokenizer.class})
public interface AppPlaceHistoryMapper extends PlaceHistoryMapper {
}
