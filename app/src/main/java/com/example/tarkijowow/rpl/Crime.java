package com.example.tarkijowow.rpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class Crime {

    private static final String JSON_NAME = "name";
    private static final String JSON_DESCRIPTION = "description";
    private static final String JSON_LATITUDE = "latitude";
    private static final String JSON_LONGTITUDE = "longtitude";
    private static final String JSON_KTP = "ktp";
    private static final String JSON_TITLE = "title";
    private static final String JSON_ADDRESS = "address";

    private String report_spinner;
    private String report_detail;
    private double latitude;
    private double longtitude;
    private String address;
    private String ktp;
    private String timestamp;
    private String name;
    private String proceed;
    private String wasRead;
    private String finish;

    public Crime() {
        setProceed("0");
        setWasRead("0");
        setFinish("0");
    }

    public Crime(JSONObject json) throws JSONException {
        if (json.has(JSON_NAME))
            name = json.getString(JSON_NAME);
        if (json.has(JSON_DESCRIPTION))
            report_detail = json.getString(JSON_DESCRIPTION);
        if (json.has(JSON_LATITUDE))
            latitude = json.getDouble(JSON_LATITUDE);
        if (json.has(JSON_LONGTITUDE))
            longtitude = json.getDouble(JSON_LONGTITUDE);
        if (json.has(JSON_KTP))
            ktp = json.getString(JSON_KTP);
        if (json.has(JSON_TITLE))
            report_spinner = json.getString(JSON_TITLE);
        if (json.has(JSON_ADDRESS))
            address = json.getString(JSON_ADDRESS);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_NAME, name);
        json.put(JSON_DESCRIPTION, report_detail);
        json.put(JSON_LATITUDE, latitude);
        json.put(JSON_LONGTITUDE, longtitude);
        json.put(JSON_KTP, ktp);
        json.put(JSON_TITLE, report_spinner);
        json.put(JSON_ADDRESS, address);
        return json;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getKtp() {
        return ktp;
    }

    public void setKtp(String ktp) {
        this.ktp = ktp;
    }

    public String getReport_spinner() {
        return report_spinner;
    }

    public void setReport_spinner(String report_spinner) {
        this.report_spinner = report_spinner;
    }

    public String getReport_detail() {
        return report_detail;
    }

    public void setReport_detail(String report_detail) {
        this.report_detail = report_detail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProceed() {
        return proceed;
    }

    public void setProceed(String proceed) {
        this.proceed = proceed;
    }

    public String getWasRead() {
        return wasRead;
    }

    public void setWasRead(String wasRead) {
        this.wasRead = wasRead;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }
}
