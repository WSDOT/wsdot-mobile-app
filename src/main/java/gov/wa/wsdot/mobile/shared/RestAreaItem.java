package gov.wa.wsdot.mobile.shared;

public class RestAreaItem {
    private int id;
    private String route;
    private String location;
    private String description;
    private int milepost;
    private String direction;
    private String latitude;
    private String longitude;
    private String notes;
    private boolean hasDump;
    private boolean isOpen;
    private String[] amenities;

    public int getId(){ return id;}
    public void setId(int id){ this.id = id;}

    public String getRoute() {
        return route;
    }
    public void setRoute(String route) {
            this.route = route;
        }

    public String getLocation() {
            return location;
        }
    public void setLocation(String location) {
            this.location = location;
        }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getMilepost() {
            return milepost;
        }
    public void setMilepost(int milepost) {
            this.milepost = milepost;
        }

    public String getDirection() {
            return direction;
        }
    public void setDirection(String direction) {
            this.direction = direction;
        }

    public String getLatitude() {
            return latitude;
        }
    public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

    public String getLongitude() { return longitude; }
    public void setLongitude(String longitude) { this.longitude = longitude; }

    public String getNotes() { return notes; }
    public void setNotes(String notes){ this.notes = notes;}

    public boolean hasDump() { return hasDump; }
    public void setHasDump(boolean hasDump){ this.hasDump = hasDump;}

    public boolean isOpen() { return isOpen; }
    public void setOpen(boolean isOpen){ this.isOpen = isOpen;}

    public String[] getAmenities() { return amenities;}
    public void setAmenities(String[] amenities){ this.amenities = amenities;}

}
