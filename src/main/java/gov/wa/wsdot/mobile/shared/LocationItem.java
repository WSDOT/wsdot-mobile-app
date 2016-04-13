package gov.wa.wsdot.mobile.shared;

/**
 * Created by simsl on 4/6/16.
 */
public class LocationItem {

    private String title;
    private Double latitude;
    private Double longitude;
    private int zoom;

    public LocationItem (String title, Double latitude, Double longitude, int zoom){
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.zoom = zoom;
    }

    public String getTitle(){
        return this.title;
    }

    public Double getLatitude(){
        return this.latitude;
    }

    public Double getLongitude(){
        return this.longitude;
    }

    public int getZoom(){
        return this.zoom;
    }
}
