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
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public class LocalImageHolderDefaultAppearance implements
        LocalTabBarImageHolder.Appearance {
    
    interface Resources extends ClientBundle, Images {
        Resources INSTANCE = GWT.create(Resources.class);

        @Source("home_mdpi.png")
        ImageResource home();
        
        @Source("star_mdpi.png")
        ImageResource star();
        
        @Source("cloud_mdpi.png")
        ImageResource cloud();
        
        @Source("note_mdpi.png")
        ImageResource note();
        
        @Source("camera_2_mdpi.png")
        ImageResource camera();
        
        @Source("video_mdpi.png")
        ImageResource video();
        
        @Source("warning_mdpi.png")
        ImageResource warning();
        
        @Source("arrowUp_mdpi.png")
        ImageResource arrowUp();
        
        @Source("arrowDown_mdpi.png")
        ImageResource arrowDown();
        
        @Source("sr16_mdpi.png")
        ImageResource sr16();
        
        @Source("sr167_mdpi.png")
        ImageResource sr167();
        
        @Source("sr520_mdpi.png")
        ImageResource sr520();
        
        @Source("i405_mdpi.png")
        ImageResource i405();

        @Source("time_mdpi.png")
        ImageResource time();
    }

    @Override
    public Images get() {
        return Resources.INSTANCE;
    }
}
