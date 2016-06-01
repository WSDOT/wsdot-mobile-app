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

package gov.wa.wsdot.mobile.client.widget.tabbar.resources;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ImageResource;
import gov.wa.wsdot.mobile.client.widget.tabbar.resources.LocalTabBarImageHolder.Appearance.Images;

public class LocalTabBarImageHolder {
    private static final Appearance APPEARANCE = GWT.create(Appearance.class);

    public interface Appearance {
        public interface Images {
            ImageResource home();
            
            ImageResource star();
            
            ImageResource cloud();
            
            ImageResource note();
            
            ImageResource camera();
            
            ImageResource video();
            
            ImageResource warning();
            
            ImageResource arrowUp();
            
            ImageResource arrowDown();
            
            ImageResource sr16();
            
            ImageResource sr167();
            
            ImageResource sr520();
            
            ImageResource i405();
            
            ImageResource time();
        }

        Images get();
    }

    public static Images get() {
        return APPEARANCE.get();
    }
}
