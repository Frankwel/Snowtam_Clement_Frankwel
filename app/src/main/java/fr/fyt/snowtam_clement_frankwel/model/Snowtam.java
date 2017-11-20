package fr.fyt.snowtam_clement_frankwel.model;

/**
 * Created by frank on 20/11/2017.
 */

public class Snowtam {

    String key;
    String code;
    String result;

    //Constructor without argument
    public Snowtam() {

    }

    //Constructor with argument


    public Snowtam(String key, String code, String result) {
        this.key = key;
        this.code = code;
        this.result = result;
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
}
