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

package gov.wa.wsdot.mobile.shared;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class FerriesTerminalSailingSpaceFeed extends JavaScriptObject {
    protected FerriesTerminalSailingSpaceFeed() {}
    
    public final native JsArray<Terminals> getTerminals() /*-{ return this }-*/;
    
    public static class Terminals extends JavaScriptObject {
        protected Terminals() {}
        
        public final native int getTerminalId() /*-{ return this.TerminalID }-*/;
        public final native String getTerminalName() /*-{ return this.TerminalName }-*/;
        public final native String getTerminalAbbrev() /*-{ return this.TerminalAbbrev }-*/;
        public final native JsArray<DepartingSpaces> getDepartingSpaces() /*-{ return this.DepartingSpaces }-*/;
    }
    
    public static class DepartingSpaces extends JavaScriptObject {
        protected DepartingSpaces() {}
    }
}
