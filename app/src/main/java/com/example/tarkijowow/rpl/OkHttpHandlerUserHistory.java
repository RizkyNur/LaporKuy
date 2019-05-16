package com.example.tarkijowow.rpl;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpHandlerUserHistory extends AsyncTask<String, Void, String> {

    OkHttpClient client = new OkHttpClient();
    OkHttpHandlerUserHistory.OnTaskCompleted onTaskCompleted;
    Context context;
    int len;

    public OkHttpHandlerUserHistory(OkHttpHandlerUserHistory.OnTaskCompleted onTaskCompleted,
                                    Context c){
        this.context = c;
        this.onTaskCompleted = onTaskCompleted;
    }

    @Override
    protected String doInBackground(String...params) {
        Log.d("okhttphandlerhistory", "called");
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        String ktp = prefs.getString("ktp", "0");
        String nama = prefs.getString("nama", "false");

        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("deto", CrimeList.get(context).getTimestamp_read_user());
            jsonObject.put("name", nama);
            jsonObject.put("ktp", ktp);
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

                crimeList.add(crime);
                if(i == 0){
                    crimeList.setTimestamp_read_user(c.getString("deto"));
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
        Log.d("okhttphandleruserhistory", "" + s);
    }

    public interface OnTaskCompleted{
        void onTaskCompleted();
    }
}