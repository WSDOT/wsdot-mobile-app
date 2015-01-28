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

package gov.wa.wsdot.mobile.client.activities.amtrakcascades.schedules.details;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiRenderer;
import com.google.gwt.uibinder.client.UiTemplate;
import com.googlecode.mgwt.ui.client.widget.list.celllist.Cell;

public abstract class AmtrakCascadesSchedulesDetailsCell<T> implements Cell<T> {
	
	@UiTemplate("AmtrakCascadesDeparturesCell.ui.xml")
	public interface Renderer extends UiRenderer {
	    
        public void render(SafeHtmlBuilder safeHtmlBuilder,
                SafeHtml scheduledDeparture, SafeHtml departureComment,
                SafeHtml scheduledArrival, SafeHtml arrivalComment,
                SafeHtml lastUpdated, SafeHtml trainName);
		
	}
	
	private Renderer renderer = GWT.create(Renderer.class);
	
	public void render(SafeHtmlBuilder sb, T model) {
        renderer.render(sb, getScheduledDeparture(model), getDepartureComment(model), getScheduledArrival(model), getArrivalComment(model), getLastUpdated(model), getTrain(model));
	}
	
	public abstract SafeHtml getScheduledDeparture(T model);
	
	public abstract SafeHtml getScheduledArrival(T model);
	
	public abstract SafeHtml getTrain(T model);
	
	public abstract SafeHtml getLastUpdated(T model);
	
	public abstract SafeHtml getDepartureComment(T model);
	
	public abstract SafeHtml getArrivalComment(T model);

	@Override
	public boolean canBeSelected(T model) {
		return false;
	}
	
}
