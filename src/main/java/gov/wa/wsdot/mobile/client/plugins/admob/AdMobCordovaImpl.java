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

public class AdMobCordovaImpl implements AdMob {

    private boolean initialized;
    
    @Override
    public void initialize() {
        if (!testForPlugin()) {
            throw new IllegalStateException("cannot find AdMob plugin - did you include AdMob.js?");
        }
        initialized = true;
    }
    
    private native boolean testForPlugin() /*-{
        if (!$wnd.window.AdMob) {
            return false;
        }
        return true;
    }-*/;

    @Override
    public void createBanner(AdMobOptions options) {
        if (!initialized) {
            throw new IllegalStateException("you have to initialize AdMob plugin before using it");
        }
        createBannerNative(options);
    }
    
    private native void createBannerNative(AdMobOptions options) /*-{
        $wnd.window.AdMob.createBanner(options);
    }-*/;

    @Override
    public void removeBanner() {
        if (!initialized) {
            throw new IllegalStateException("you have to initialize AdMob plugin before using it");
        }
        removeBannerNative();
    }

    private native void removeBannerNative() /*-{
        $wnd.window.AdMob.removeBanner();
    }-*/;
    
    @Override
    public void hideBanner() {
        if (!initialized) {
            throw new IllegalStateException("you have to initialize AdMob plugin before using it");
        }
        hideBannerNative();
    }
    
    private native void hideBannerNative() /*-{
        $wnd.window.AdMob.hideBanner();
    }-*/;
    
    @Override
    public void showBanner(int position) {
        if (!initialized) {
            throw new IllegalStateException("you have to initialize AdMob plugin before using it");
        }
        showBannerNative(position);
    }
    
    private native void showBannerNative(int position) /*-{
        $wnd.window.AdMob.showBanner(position);
    }-*/;
    
    @Override
    public void showBannerAtXY(int x, int y) {
        if (!initialized) {
            throw new IllegalStateException("you have to initialize AdMob plugin before using it");
        }
        showBannerAtXYNative(x, y);
    }

    private native void showBannerAtXYNative(int x, int y) /*-{
        $wnd.window.AdMob.showBannerAtXY(x, y);
    }-*/;
    
    @Override
    public void prepareInterstitial(AdMobOptions options) {
        if (!initialized) {
            throw new IllegalStateException("you have to initialize AdMob plugin before using it");
        }
        prepareInterstitialNative(options);
    }
    
    private native void prepareInterstitialNative(AdMobOptions options) /*-{
        $wnd.window.AdMob.prepareInterstitial(options);
    }-*/;

    @Override
    public void showInterstitial() {
        if (!initialized) {
            throw new IllegalStateException("you have to initialize AdMob plugin before using it");
        }
        showInterstitialNative();
    }
    
    private native void showInterstitialNative() /*-{
        $wnd.window.AdMob.showInterstitial();
    }-*/;

}
