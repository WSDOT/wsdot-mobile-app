package gov.wa.wsdot.mobile.client.plugins.accessibility;

import gov.wa.wsdot.mobile.client.MobileAppEntryPoint;

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
    public native void isVoiceOverRunning() /*-{
        $wnd.MobileAccessibility.isVoiceOverRunning($wnd.initAds);
    }-*/;


    @Override
    public void postScreenChangeNotification() {
        if (!initialized) {
            throw new IllegalStateException("you have to initialize MobileAccessibility plugin before using it");
        }
        postNotificationNative();
    }

    private native void postNotificationNative() /*-{
        $wnd.MobileAccessibility.postScreenChangeNotification(
        $wnd.MobileAccessibility.MobileAccessibilityNotifications.SCREEN_CHANGED,
        '',
        function(info) {
            if (info) {
                console.log("Screen Reader notification. success : " + info.wasSuccessful);
            }
        });
    }-*/;

}
