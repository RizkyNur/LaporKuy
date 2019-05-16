package com.example.tarkijowow.rpl;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class SendDataBackground extends Service{

    private OkHTTPHandlerAll okHTTPHandlerAll;

    public SendDataBackground() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sendData();
        //createNotif();
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void sendData(){
        okHTTPHandlerAll = OkHTTPHandlerAll.getInstance(this);

        if(checkInternetConenction()) {
            String et1 = "test1";
            String et2 = "test2";
            try{
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", et1);
                jsonObject.put("description", et2);
                okHTTPHandlerAll.executo2(StringColle.url_Create, jsonObject);
                Log.d("SendBackground", "sendDataProcessed");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("SendDataBackground", "JSON Exception");
            }

        }else{
            Log.d("internet connection", "not granted");
        }
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
