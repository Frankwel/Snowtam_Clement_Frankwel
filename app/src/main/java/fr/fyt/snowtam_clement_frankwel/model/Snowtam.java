package fr.fyt.snowtam_clement_frankwel.model;

/**
 * Created by frank on 20/11/2017.
 */

public class Snowtam {

    String airportName;
    String key;
    String code;
    String result;
    double lat;
    double lng;

    //Constructor without argument
    public Snowtam() {

    }

    //Constructor with argument


    public Snowtam(String key, String code, String result) {
        this.airportName = key;
        this.key = key;
        this.code = code;
        this.result = result;
        this.lat = 0;
        this.lng = 0;
    }

    //getters and setters
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }
}
