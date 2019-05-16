package com.example.tarkijowow.rpl;

import android.content.Context;

public class LocationAddressData {
    private Context context;
    private double latitude, longtitude;
    private String address;
    private static LocationAddressData ourInstance;

    public static LocationAddressData getInstance(Context c) {
        if(ourInstance == null){
            ourInstance = new LocationAddressData(c.getApplicationContext());
        }
        return ourInstance;
    }

    private LocationAddressData(Context c) {
        context = c;
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
}
