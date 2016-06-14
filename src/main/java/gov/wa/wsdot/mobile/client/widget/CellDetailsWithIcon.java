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

package gov.wa.wsdot.mobile.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.googlecode.mgwt.ui.client.widget.list.celllist.Cell;
import gov.wa.wsdot.mobile.client.css.AppBundle;

public abstract class CellDetailsWithIcon<T> implements Cell<T> {

	private static Template TEMPLATE = GWT.create(Template.class);

	public interface Template extends SafeHtmlTemplates {
		@SafeHtmlTemplates.Template("<div role=\"button\" tabindex=\"0\" class=\"{0}\"><div class=\"{1}\"></div><div class=\"{2}\"><div>{3}</div><div>{4}</div><div>{5}</div></div></div>")
		SafeHtml content(String class1, String cellImage, String class2, String cellContents, String description, String lastUpdated);
	}

	@Override
	public void render(SafeHtmlBuilder safeHtmlBuilder, final T model) {
		SafeHtml content = TEMPLATE.content(
				AppBundle.INSTANCE.css().cellImage(),
				getDisplayImage(model),
				AppBundle.INSTANCE.css().cellDetails(),
				SafeHtmlUtils.htmlEscape(getDisplayString(model)),
				SafeHtmlUtils.htmlEscape(getDisplayDescription(model)),
				SafeHtmlUtils.htmlEscape(getDisplayLastUpdated(model)));
		safeHtmlBuilder.append(content);
	}

	public abstract String getDisplayString(T model);
	
	public abstract String getDisplayImage(T model);
	
	public abstract String getDisplayDescription(T model);
	
	public abstract String getDisplayLastUpdated(T model);

	@Override
	public boolean canBeSelected(T model) {
		return false;
	}

}
