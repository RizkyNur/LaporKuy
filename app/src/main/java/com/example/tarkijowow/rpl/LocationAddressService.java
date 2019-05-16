package com.example.tarkijowow.rpl;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationAddressService extends Service {

    private final IBinder mBinder = new LocalBinder();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("onStartCommand","gets called");

        Location location = intent.getParcelableExtra(StringColle.LOCATION_ADDRESS);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        String result = "";

        List<Address> addresses = null;
        try{
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(addresses == null){
            result = "failed to fetch";
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            return Service.START_NOT_STICKY;
        }
        else{
            Address address = addresses.get(0);
            Log.i("LocationAddressService", "Address Found");

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            String ktp = prefs.getString("ktp", "0");

            JSONObject jsonObject = new JSONObject();
            try{
                jsonObject.put("latitude", address.getLatitude());
                jsonObject.put("longtitude", address.getLongitude());
                jsonObject.put("ktp", ktp);

                LocationAddressData locationAddressData = LocationAddressData.getInstance(this);
                locationAddressData.setLatitude(address.getLatitude());
                locationAddressData.setLongtitude(address.getLongitude());
                locationAddressData.setAddress(address.getAddressLine(0));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String api = intent.getStringExtra(StringColle.API_BACKGROUND);
            sendResult(address.getAddressLine(0), jsonObject, api);
            return Service.START_NOT_STICKY;
        }
    }

    public void sendResult(String result, JSONObject jsonObject, String api){
        //Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        if(checkInternetConenction()) {
            OkHTTPHandlerAll okHTTPHandlerAll = OkHTTPHandlerAll.getInstance(this);
            okHTTPHandlerAll.executo2(api, jsonObject);
        }
    }

    public class LocalBinder extends Binder {
        LocationAddressService getService() {
            return LocationAddressService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("onBind", "gets called");
        return mBinder;
    }

    private boolean checkInternetConenction() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec
                =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() ==
                android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            //Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        }else if (
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                android.net.NetworkInfo.State.DISCONNECTED  ) {
            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }
}
