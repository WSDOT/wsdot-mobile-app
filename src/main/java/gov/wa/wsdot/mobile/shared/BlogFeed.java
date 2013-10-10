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

public class BlogFeed extends JavaScriptObject {
	protected BlogFeed() {}

	public final native Feed getFeed() /*-{ return this.feed }-*/;

	static public class Feed extends JavaScriptObject {
		protected Feed() {}
		
		public final native JsArray<Entry> getEntry() /*-{ return this.entry }-*/;

	}
	
	static public class Entry extends JavaScriptObject {
		protected Entry() {}
		
		public final native Published getPublished() /*-{ return this.published }-*/;
		public final native Title getTitle() /*-{ return this.title }-*/;
		public final native Content getContent() /*-{ return this.content }-*/;
		public final native JsArray<Link> getLink() /*-{ return this.link }-*/;
	}
	
	static public class Published extends JavaScriptObject {
		protected Published() {}
		
		public final native String getT() /*-{ return this.$t }-*/;
	}
	
	static public class Title extends JavaScriptObject {
		protected Title() {}
		
		public final native String getT() /*-{ return this.$t }-*/;
	}
	
	static public class Content extends JavaScriptObject {
		protected Content() {}
		
		public final native String getT() /*-{ return this.$t }-*/;
	}
	
	static public class Link extends JavaScriptObject {
		protected Link() {}
		
		public final native String getHref() /*-{ return this.href }-*/;
	}
	
}
