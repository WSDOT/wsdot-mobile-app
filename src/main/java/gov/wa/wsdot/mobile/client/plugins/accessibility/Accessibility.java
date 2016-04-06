package gov.wa.wsdot.mobile.client.plugins.accessibility;

import com.googlecode.gwtphonegap.client.plugins.PhoneGapPlugin;

/**
 * Created by simsl on 3/11/16.
 */
public interface Accessibility extends PhoneGapPlugin{


    /**
     *  Calls MobileAccessibility.isVoiceOverRunning(callback)
     */
    public void isVoiceOverRunning();

    /**
     *  Sends a screen change notification to VoiceOver
     */
    public void postScreenChangeNotification();

}
