package com.example.tarkijowow.rpl;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivityBackground {

    public static void sendBackground(Context c, Location location, String api){
        Intent intent = new Intent(c, LocationAddressService.class);
        intent.putExtra(StringColle.LOCATION_ADDRESS, location);
        intent.putExtra(StringColle.API_BACKGROUND, api);

        c.startService(intent);
    }

    public static void sendBackgroundInsertUser(Context c, String ktp){
        Log.d("sendBackInsertUser", "called");
        OkHTTPHandlerAll okHTTPHandlerAll = OkHTTPHandlerAll.getInstance(c);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ktp", ktp);
            jsonObject.put("latitude", 1);
            jsonObject.put("longtitude", 1);
            okHTTPHandlerAll.executo2(StringColle.url_Create_User, jsonObject);
            Log.d("JSON", "Success");
        }catch (JSONException json){
            json.printStackTrace();
        }
    }

    public static void readBackground(Context c){
        Intent intent = new Intent(c, GetDataBackground.class);
        PendingIntent pendingIntent = PendingIntent.getService(
                c, 21232, intent, 0);
        AlarmManager alarmManager = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        if(alarmManager == null){
            Toast.makeText(c, "NAH", Toast.LENGTH_LONG).show();
            return;
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                (60 * 1000), pendingIntent);
        Log.d("readbackground", "should have been set");
        //Toast.makeText(c, "UserNotif should be set",
        //        Toast.LENGTH_LONG).show();
    }

}
