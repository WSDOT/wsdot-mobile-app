/*
 * Copyright (c) 2014 Washington State Department of Transportation
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
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public class LocalImageHolderDefaultHighAppearance implements
        LocalTabBarImageHolder.Appearance {
    
    interface Resources extends ClientBundle, Images {
        Resources INSTANCE = GWT.create(Resources.class);

        @Source("home_hdpi.png")
        ImageResource home();
        
        @Source("star_hdpi.png")
        ImageResource star();
        
        @Source("cloud_hdpi.png")
        ImageResource cloud();
        
        @Source("note_hdpi.png")
        ImageResource note();
        
        @Source("camera_hdpi.png")
        ImageResource camera();
        
        @Source("video_hdpi.png")
        ImageResource video();
        
        @Source("warning_hdpi.png")
        ImageResource warning();
        
        @Source("arrowUp_hdpi.png")
        ImageResource arrowUp();
        
        @Source("arrowDown_hdpi.png")
        ImageResource arrowDown();
        
        @Source("sr16_hdpi.png")
        ImageResource sr16();
        
        @Source("sr167_hdpi.png")
        ImageResource sr167();
        
        @Source("sr520_hdpi.png")
        ImageResource sr520();
    }

    @Override
    public Images get() {
        return Resources.INSTANCE;
    }
}
