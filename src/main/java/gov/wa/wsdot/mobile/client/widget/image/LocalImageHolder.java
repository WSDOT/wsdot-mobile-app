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

package gov.wa.wsdot.mobile.client.widget.image;

import gov.wa.wsdot.mobile.client.widget.image.LocalImageHolder.LocalImageHolderAppearance.Images;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ImageResource;

public class LocalImageHolder {
    private static final LocalImageHolderAppearance APPEARANCE = GWT
            .create(LocalImageHolderAppearance.class);
    
    public interface LocalImageHolderAppearance {
        public interface Images {
            ImageResource camera();
            ImageResource location();
            ImageResource navigation();
            ImageResource rocket();
            ImageResource time();
            ImageResource warning();         
        }
        
        Images get();
    }
    
    public static Images get() {
        return APPEARANCE.get();
    }
}
