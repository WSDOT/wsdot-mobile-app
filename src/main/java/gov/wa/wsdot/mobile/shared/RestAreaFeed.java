package gov.wa.wsdot.mobile.shared;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class RestAreaFeed extends JavaScriptObject {
    protected RestAreaFeed() {}

    public final native JsArray<RestArea> getRestAreas() /*-{ return this }-*/;

    static public class RestArea extends JavaScriptObject {
        protected RestArea() {}

        public final native String getRoute() /*-{ return this.route }-*/;
        public final native String getLocation() /*-{ return this.location }-*/;
        public final native String getDescription() /*-{ return this.description }-*/;
        public final native int getMilepost() /*-{ return this.milepost }-*/;
        public final native String getDirection() /*-{ return this.direction }-*/;
        public final native String getLatitude() /*-{ return this.latitude }-*/;
        public final native String getLongitude() /*-{ return this.longitude }-*/;
        public final native String getNotes() /*-{ return this.notes }-*/;
        public final native boolean hasDump() /*-{ return this.hasDump }-*/;
        public final native boolean isOpen() /*-{ return this.isOpen }-*/;
        public final native String[] getAmenities() /*-{ return this.amenities }-*/;

    }
}