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
        if (!$wnd.window.MobileAccessibility) {
            return false;
        }
        return true;
    }-*/;


    @Override
    public void postNotification() {
        if (!initialized) {
            throw new IllegalStateException("you have to initialize MobileAccessibility plugin before using it");
        }
        postNotificationNative();
    }

    private native void postNotificationNative() /*-{
        $wnd.window.MobileAccessibility.postNotification(
        $wnd.window.MobileAccessibility.MobileAccessibilityNotifications.SCREEN_CHANGED,
        'Testing notifications',
        function(info) {
            if (info) {
                console.log("Screen Reader notification. success : " + info.wasSuccessful);
            }
        });
    }-*/;

}
