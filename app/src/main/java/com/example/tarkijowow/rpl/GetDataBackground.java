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

public class GetDataBackground extends Service{

    private Intent intent;
    private OkHTTPHandlerAll okHTTPHandlerAll;

    public GetDataBackground() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        readData();
        //createNotif();
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void readData(){
        okHTTPHandlerAll = OkHTTPHandlerAll.getInstance(this);
        OkHttpHandlerUserHistory history = new OkHttpHandlerUserHistory(new OkHttpHandlerUserHistory.OnTaskCompleted() {
            @Override
            public void onTaskCompleted() {

            }
        }, this);

        if(checkInternetConenction()) {
            okHTTPHandlerAll.executo(StringColle.url_Read);
            history.execute(StringColle.url_Read_One_History);
            Log.d("GetDataBackground : ", "readData called");
        }else{
            Log.e("internet connection", "not granted");
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
            //Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }
}
