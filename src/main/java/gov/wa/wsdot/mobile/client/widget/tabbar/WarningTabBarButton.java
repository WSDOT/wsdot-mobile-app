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

package gov.wa.wsdot.mobile.client.widget.tabbar;

import gov.wa.wsdot.mobile.client.css.AppBundle;

import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.theme.base.TabBarCss;
import com.googlecode.mgwt.ui.client.widget.tabbar.TabBarButton;

public class WarningTabBarButton extends TabBarButton {

	public WarningTabBarButton() {
		this(MGWTStyle.getTheme().getMGWTClientBundle().getTabBarCss());
	}
	
	public WarningTabBarButton(TabBarCss css) {
		super(css, MGWT.getOsDetection().isIOs()
				|| MGWT.getOsDetection().isDesktop() ? AppBundle.INSTANCE.tabBarWarningImage() : null);
		setText("Route Alerts");
	}
	
}
