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

package gov.wa.wsdot.mobile.shared;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class YouTubeFeed extends JavaScriptObject {
	protected YouTubeFeed() {}
	
	public final native JsArray<Items> getItems() /*-{ return this.items }-*/;

	static public class Items extends JavaScriptObject {
		protected Items() {}
			
		public final native Snippet getSnippet() /*-{ return this.snippet }-*/;

	}
	
	static public class Snippet extends JavaScriptObject {
		protected Snippet() {}
		
		public final native ResourceId getResourceId() /*-{ return this.resourceId }-*/;
		public final native String getPublished() /*-{ return this.publishedAt }-*/;
		public final native String getTitle() /*-{ return this.title }-*/;
		public final native String getDescription() /*-{ return this.description }-*/;
		public final native Thumbnails getThumbnails() /*-{ return this.thumbnails }-*/;

	}

	static public class ResourceId extends JavaScriptObject {
		protected ResourceId() {}

        public final native String getVideoId() /*-{ return this.videoId }-*/;

	}

	static public class Thumbnails extends JavaScriptObject {
		protected Thumbnails() {}

        public final native Standard getStandard() /*-{ return this.standard }-*/;
	}

	static public class Standard extends JavaScriptObject {
		protected Standard() {}

		public final native String getUrl() /*-{ return this.url }-*/;
	}
}
