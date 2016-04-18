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

package gov.wa.wsdot.mobile.client.plugins.accessibility;

/**
 * Created by simsl on 3/11/16.
 */
public class AccessibilityCordovaImpl implements Accessibility {

    private boolean initialized;

    @Override
    public void initialize() {
        if (!testForPlugin()) {
            throw new IllegalStateException("cannot find Accessibility plugin - did you include MobileAccessibility.js?");
        }
        initialized = true;
    }

    private native boolean testForPlugin() /*-{
        if (!$wnd.MobileAccessibility) {
            return false;
        }
        return true;
    }-*/;


    @Override
    public void isVoiceOverRunning(boolean analytics){
        if (!initialized) {
            throw new IllegalStateException("you have to initialize MobileAccessibility plugin before using it");
        }
        isVoiceOverRunningNative(analytics);
    }

    private native void isVoiceOverRunningNative(boolean analytics) /*-{

        function callback(isVoiceOverRunning){
            $wnd.initAds(isVoiceOverRunning);
            if (analytics){
                $wnd.voiceOverEvent();
            }
        }

        $wnd.MobileAccessibility.isVoiceOverRunning(callback);
    }-*/;

    @Override
    public void postScreenChangeNotification() {
        if (!initialized) {
            throw new IllegalStateException("you have to initialize MobileAccessibility plugin before using it");
        }
        postScreenChangeNotificationNative();
    }

    private native void postScreenChangeNotificationNative() /*-{
        $wnd.MobileAccessibility.postNotification(
        $wnd.MobileAccessibility.MobileAccessibilityNotifications.SCREEN_CHANGED,
        '',
        function(info) {
            if (info) {
                console.log("Screen Reader notification. success : " + info.wasSuccessful);
            }
        });
    }-*/;

}
