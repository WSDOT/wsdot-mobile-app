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

public class TwitterFeed extends JavaScriptObject {
	protected TwitterFeed() {}

	public final native JsArray<Posts> getPosts() /*-{ return this }-*/;
	
	static public class Posts extends JavaScriptObject {
		protected Posts() {}
		
		public final native String getText() /*-{ return this.text }-*/;
		public final native String getId() /*-{ return this.id }-*/;
		public final native Entities getEntities() /*-{ return this.entities }-*/;
		public final native User getUser() /*-{ return this.user }-*/;
		public final native String getCreatedAt() /*-{ return this.created_at }-*/;

	}
	
	/* Entities Object */
	static public class Entities extends JavaScriptObject {
		protected Entities() {}
		
		public final native JsArray<Media> getMedia() /*-{ return this.media }-*/;
		public final native JsArray<Urls> getUrls() /*-{ return this.urls }-*/;
	}
	
	static public class Media extends JavaScriptObject {
		protected Media() {}
		
		public final native String getMediaUrl() /*-{ return this.media_url }-*/;
	}
	
	static public class Urls extends JavaScriptObject {
		protected Urls() {}
		
		public final native String getExpandedUrl() /*-{ return this.expanded_url }-*/;
	}
	
	/* User Object */
	static public class User extends JavaScriptObject {
		protected User() {}
		
		public final native String getName() /*-{ return this.name }-*/;
		public final native String getScreenName() /*-{ return this.screen_name }-*/;
	}
	
}
