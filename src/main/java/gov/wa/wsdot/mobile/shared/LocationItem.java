package gov.wa.wsdot.mobile.shared;

/**
 * Created by simsl on 4/6/16.
 */
public class LocationItem {

    private String title;
    private Double latitude;
    private Double longitude;
    private int zoom;
    private int id;

    public LocationItem (String title, Double latitude, Double longitude, int zoom){
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.zoom = zoom;
    }

    public LocationItem (int id, String title, Double latitude, Double longitude, int zoom){
        this.id = id;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.zoom = zoom;
    }

    public int getId() { return this.id;}

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
