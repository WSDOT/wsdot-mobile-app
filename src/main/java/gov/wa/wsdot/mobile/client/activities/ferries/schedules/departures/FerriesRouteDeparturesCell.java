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

package gov.wa.wsdot.mobile.client.activities.ferries.schedules.departures;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiRenderer;
import com.google.gwt.uibinder.client.UiTemplate;
import com.googlecode.mgwt.ui.client.widget.list.celllist.Cell;

public abstract class FerriesRouteDeparturesCell<T> implements Cell<T> {
	
	@UiTemplate("FerriesRouteDeparturesCell.ui.xml")
	public interface Renderer extends UiRenderer {

	    CellStyle getCellStyle();
	    
	    public void render(SafeHtmlBuilder safeHtmlBuilder, String departing,
				String arriving, SafeHtml annotation, String driveUpSpaces, String driveUpSpacesDisplayStyle, String progress, String lastUpdated);
		
	}
	
	interface CellStyle extends CssResource {
	    String hideDriveUpSpaces();
	    String showDriveUpSpaces();
	}

	private Renderer renderer = GWT.create(Renderer.class);
	
	public void render(SafeHtmlBuilder sb, T model) {
	    
	    double maxSpaceCount = Integer.parseInt(getMaxSpaceCount(model));
	    double driveUpSpaceCount = Integer.parseInt(getDriveUpSpaces(model));
        // Calculate how full the sailing is
	    int capacity = (int) (((maxSpaceCount - driveUpSpaceCount) / maxSpaceCount) * 100) ;
	    
	    String nextDeg;
	    String backgroundImage;
	    
	    if (capacity < 50) {
	        nextDeg = String.valueOf(90 + (3.6 * capacity)) + "deg";
            backgroundImage = "background-image: linear-gradient(90deg, #d7d7d7 50%, transparent 50%, transparent), linear-gradient("
                    + nextDeg + ", #006f80 50%, #d7d7d7 50%, #d7d7d7)";
	    } else {
	        nextDeg = String.valueOf(-90 + (3.6 * (capacity - 50))) + "deg";
            backgroundImage = "background-image: linear-gradient("
                    + nextDeg
                    + ", #006f80 50%, transparent 50%, transparent), linear-gradient(270deg, #006f80 50%, #d7d7d7 50%, #d7d7d7)";
	    }
	    
	    if (getDriveUpSpaces(model).equals("-1")) {
	        renderer.render(sb, getDeparting(model), getArriving(model),
	                getAnnotation(model), getDriveUpSpaces(model), renderer.getCellStyle().hideDriveUpSpaces(), "", getLastUpdated(model));

	    } else {
            renderer.render(sb, getDeparting(model), getArriving(model),
                    getAnnotation(model), getDriveUpSpaces(model), renderer.getCellStyle().showDriveUpSpaces(), backgroundImage, getLastUpdated(model));
	    }

	}
	
	public abstract String getDeparting(T model);
	
	public abstract String getArriving(T model);
	
	public abstract SafeHtml getAnnotation(T model);
	
	public abstract String getDriveUpSpaces(T model);
	
	public abstract String getMaxSpaceCount(T model);
	
	public abstract String getLastUpdated(T model);

	@Override
	public boolean canBeSelected(T model) {
		return false;
	}
	
}
