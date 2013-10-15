package gov.wa.wsdot.mobile.client.theme;

import gov.wa.wsdot.mobile.client.util.ParserUtils;

import com.google.gwt.core.client.GWT;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.OsDetection;
import com.googlecode.mgwt.ui.client.theme.MGWTClientBundle;
import com.googlecode.mgwt.ui.client.theme.MGWTTheme;

public class CustomTheme implements MGWTTheme {
	private MGWTClientBundle bundle;

	public CustomTheme() {
		// instantiate the right theme!
		OsDetection osDetection = MGWT.getOsDetection();
		
		if (osDetection.isAndroidPhone()) {
			bundle = GWT.create(CustomBundleAndroid.class);
		}

		if (osDetection.isAndroidTablet()) {
			bundle = GWT.create(CustomBundleAndroidTablet.class);
		}
		
		if (osDetection.isIPhone()) {
		    if (osDetection.isRetina()) {
		        if (ParserUtils.iOSversion() >= 7) {
		            bundle = GWT.create(CustomBundleRetinaIOS7.class);
		        } else {
		            bundle = GWT.create(CustomBundleRetina.class);
		        }
			} else {
			    if (ParserUtils.iOSversion() >= 7) {
			        bundle = GWT.create(CustomBundleIPhoneIOS7.class);
			    } else {
			        bundle = GWT.create(CustomBundleIPhone.class);
			    }
			}
		}

		if (osDetection.isIPad()) {
			if (osDetection.isIPadRetina()) {
			    if (ParserUtils.iOSversion() >= 7) {
			        bundle = GWT.create(CustomBundleIpadRetinaIOS7.class);
			    } else {
			        bundle = GWT.create(CustomBundleIpadRetina.class);
			    }
			} else {
			    if (ParserUtils.iOSversion() >= 7) {
			        bundle = GWT.create(CustomBundleIpadIOS7.class);
			    } else {
			        bundle = GWT.create(CustomBundleIpad.class);
			    }
			}

		}

		if (osDetection.isBlackBerry()) {
			bundle = GWT.create(CustomBundleBlackberry.class);
		}

		if (osDetection.isDesktop()) {
			bundle = GWT.create(CustomBundleDesktop.class);
		}
	}

	@Override
	public MGWTClientBundle getMGWTClientBundle() {
		return bundle;
	}

}
