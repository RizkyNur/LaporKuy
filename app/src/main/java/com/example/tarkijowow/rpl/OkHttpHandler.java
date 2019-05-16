package com.example.tarkijowow.rpl;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpHandler extends AsyncTask<String, Void, String> {

    OkHttpClient client = new OkHttpClient();
    OnTaskCompleted onTaskCompleted;
    Context context;
    int len;

    public OkHttpHandler(OnTaskCompleted onTaskCompleted, Context c){
        this.context = c;
        this.onTaskCompleted = onTaskCompleted;
    }

    @Override
    protected String doInBackground(String...params) {
        Log.d("okhttphandler1", "called");
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("deto", CrimeList.get(context).getTimestamp());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        Request.Builder builder = new Request.Builder();
        builder.url(params[0]);
        builder.post(body);
        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            String rValue = response.body().string();

            JSONObject reader = new JSONObject(rValue);
            JSONArray array = reader.getJSONArray("records");

            String val = null;
            CrimeList crimeList = CrimeList.get(context);
            len = array.length();
            for(int i=0 ; i<len ; i++){
                JSONObject c = array.getJSONObject(i);
                Crime crime = new Crime();
                crime.setName(c.getString("name"));
                crime.setReport_spinner(c.getString("title"));
                crime.setReport_detail(c.getString("description"));
                crime.setAddress(c.getString("address"));
                crime.setLatitude(c.getDouble("latitude"));
                crime.setLongtitude(c.getDouble("longtitude"));
                crime.setKtp(c.getString("ktp"));
                crime.setTimestamp(c.getString("deto"));
                crime.setProceed(c.getString("proceed"));
                crime.setWasRead(c.getString("wasRead"));
                crime.setFinish(c.getString("finish"));

                crimeList.add_fetch(crime);
                if(i == 0){
                    crimeList.setTimestamp(c.getString("deto"));
                }
            }

            if(val != null){
                return val;
            }
            else{
                return "nothing";
            }

        }catch (Exception e){
            e.printStackTrace();
            Log.d("error", e.toString());
        }
        return "null pointer";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(len > 0)
            onTaskCompleted.onTaskCompleted();
        Log.d("okhttphandler1", "" + s);
    }

    public interface OnTaskCompleted{
        void onTaskCompleted();
    }
}
