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

package gov.wa.wsdot.mobile.client.plugins.admob;

import com.googlecode.gwtphonegap.client.plugins.PhoneGapPlugin;

/**
 * This class is a wrapper for the cordova-admob-pro plugin.
 * <p>
 * The implementation is based on 
 * <a href="https://github.com/floatinghotpot/cordova-admob-pro/blob/master/www/AdMob.js">AdMob.js</a>. 
 */
public interface AdMob extends PhoneGapPlugin {
    
    /**
     * Create a banner Ad.
     * 
     * @param options  see methods in {@link AdMobOptions}
     */
    public void createBanner(AdMobOptions options);
    
    /**
     * Destroy the banner, remove it from screen.
     */    
    public void removeBanner();

    /**
     * Hide the banner, remove it from screen, but can show it later.
     */
    public void hideBanner();

    /**
     * Show banner at given position.
     * 
     * @param position
     */
    public void showBanner(int position);
    
    /**
     * Show banner at given position with (x,y).
     * 
     * @param x  in pixels. Offset from screen left
     * @param y  in pixels. Offset from screen top
     */
    public void showBannerAtXY(int x, int y);

    /**
     * Prepare an interstitial Ad for showing.
     */
    public void prepareInterstitial(AdMobOptions options);

    /**
     * Show interstitial Ad when it's ready.
     */
    public void showInterstitial();
}
