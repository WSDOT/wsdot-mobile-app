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

package gov.wa.wsdot.mobile.client.activities.ferries.schedules;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiRenderer;
import com.google.gwt.uibinder.client.UiTemplate;
import com.googlecode.mgwt.ui.client.widget.list.celllist.Cell;

public abstract class FerriesRouteSchedulesCell<T> implements Cell<T> {
	
	@UiTemplate("FerriesRouteSchedulesCell.ui.xml")
	public interface Renderer extends UiRenderer {
		public void render(SafeHtmlBuilder safeHtmlBuilder, String description,
				String crossingTime, String lastUpdated, SafeHtml alertImage);
	}

	private Renderer renderer = GWT.create(Renderer.class);
	
	public void render(SafeHtmlBuilder sb, T model) {
		renderer.render(sb, getDescription(model), getCrossingTime(model),
		        getLastUpdated(model), getAlertImage(model));

	}
	
	public abstract String getDescription(T model);
	
	public abstract String getCrossingTime(T model);
	
	public abstract String getLastUpdated(T model);
	
	public abstract SafeHtml getAlertImage(T model);

	@Override
	public boolean canBeSelected(T model) {
		return false;
	}
	
}
