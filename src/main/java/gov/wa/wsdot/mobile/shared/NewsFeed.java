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

package gov.wa.wsdot.mobile.shared;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class NewsFeed extends JavaScriptObject {
	protected NewsFeed() {}

	public final native News getNews() /*-{ return this.news }-*/;

	static public class News extends JavaScriptObject {
		protected News() {}
		
		public final native JsArray<Items> getItems() /*-{ return this.items }-*/;

	}
	
	static public class Items extends JavaScriptObject {
		protected Items() {}
		
		public final native String getLink() /*-{ return this.link }-*/;
		public final native String getDescription() /*-{ return this.description }-*/;
		public final native String getPubDate() /*-{ return this.pubdate }-*/;
		public final native String getTitle() /*-{ return this.title }-*/;

	}
	
}
