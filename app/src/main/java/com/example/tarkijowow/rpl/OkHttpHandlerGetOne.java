package com.example.tarkijowow.rpl;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpHandlerGetOne extends AsyncTask<String, Void, String> {
    OkHttpClient client = new OkHttpClient();
    OkHttpHandlerGetOne.OnTaskCompleted onTaskCompleted;
    Context context;
    String deto;
    Crime c;
    int len;

    public OkHttpHandlerGetOne(OkHttpHandlerGetOne.OnTaskCompleted onTaskCompleted,
                               Context c, String deto){
        this.onTaskCompleted = onTaskCompleted;
        this.deto = deto;
        this.c = new Crime();
    }

    @Override
    protected String doInBackground(String...params) {

        Log.d("okhttphandler1", "called");
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("deto", deto);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        String rValue = "cacad";
        Request.Builder builder = new Request.Builder();
        builder.url(params[0]);
        builder.post(body);
        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            rValue = response.body().string();

            JSONObject reader = new JSONObject(rValue);
            JSONArray array = reader.getJSONArray("records");

            len = array.length();
            for(int i=0 ; i<len ; i++){
                JSONObject jsonObject1 = array.getJSONObject(i);
                this.c.setName(jsonObject1.getString("name"));
                this.c.setReport_spinner(jsonObject1.getString("title"));
                this.c.setReport_detail(jsonObject1.getString("description"));
                this.c.setAddress(jsonObject1.getString("address"));
                this.c.setLatitude(jsonObject1.getDouble("latitude"));
                this.c.setLongtitude(jsonObject1.getDouble("longtitude"));
                this.c.setKtp(jsonObject1.getString("ktp"));
                this.c.setTimestamp(jsonObject1.getString("deto"));
                this.c.setProceed(jsonObject1.getString("proceed"));
                this.c.setWasRead(jsonObject1.getString("wasRead"));
                this.c.setFinish(jsonObject1.getString("finish"));
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
            onTaskCompleted.onTaskCompleted(c);
        Log.d("okhttphandlergetone", "" + s);
    }

    public interface OnTaskCompleted{
        void onTaskCompleted(Crime c);
    }
}
