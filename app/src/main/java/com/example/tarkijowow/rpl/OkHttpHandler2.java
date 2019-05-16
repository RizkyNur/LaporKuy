package com.example.tarkijowow.rpl;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpHandler2 extends AsyncTask<String, Void, String> {

    OkHttpClient client = new OkHttpClient();
    OnTaskCompleted2 onTaskCompleted;
    private JSONObject jsonObject;

    public OkHttpHandler2(OnTaskCompleted2 onTaskCompleted){
        this.onTaskCompleted = onTaskCompleted;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    protected String doInBackground(String...params) {

        Log.d("OkHttpHandler2", "doinbackground");
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        Request.Builder builder = new Request.Builder();
        builder.url(params[0]);
        builder.post(body);
        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            String rValue = response.body().string();
            return rValue;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "error bro";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        onTaskCompleted.onTaskCompleted2();
        Log.d("OkHTTPHandler2", s);
    }

    public interface OnTaskCompleted2{
        void onTaskCompleted2();
    }
}