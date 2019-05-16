package com.example.tarkijowow.rpl;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpHandlerUserContribute extends AsyncTask<String, Void, String> {
    OkHttpClient client = new OkHttpClient();
    OkHttpHandlerUserContribute.OnTaskCompleted onTaskCompleted;
    Context context;
    private ProgressDialog progressDialog;
    private String nama, description, address, deto;

    public OkHttpHandlerUserContribute(OkHttpHandlerUserContribute.OnTaskCompleted onTaskCompleted, Context c,
                                     String nama, String description, String address, String deto){
        this.context = c;
        this.onTaskCompleted = onTaskCompleted;
        this.nama = nama;
        this.description = description;
        this.address = address;
        this.deto = deto;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String...params) {
        Log.d("okhttphandleruserregister", "called");
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("name", nama);
            jsonObject.put("description", description);
            jsonObject.put("address", address);
            jsonObject.put("deto", deto);
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
            String result = reader.getString("message");

            return result;
        }catch (Exception e){
            e.printStackTrace();
            Log.d("error", e.toString());
        }
        return "null pointer";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d(getClass().getName(), s);
        progressDialog.dismiss();
        if (s.toLowerCase().contains("success")) {
            onTaskCompleted.onTaskCompleted(true);
        }
        else if (s.toLowerCase().contains("failed")) {
            onTaskCompleted.onTaskCompleted(false);
        }
        else {
            Log.d(getClass().getName(), "result not succ or fail" + s);
        }
    }

    public interface OnTaskCompleted{
        void onTaskCompleted(Boolean result);
    }
}
