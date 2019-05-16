package com.example.tarkijowow.rpl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReportActivity extends AppCompatActivity {

    Spinner spinner1;
    Button button1;
    EditText edittext1;
    private int spinner_index=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        buttonSet();
    }

    private void buttonSet(){
        spinner1 = findViewById(R.id.spinner_activity_report_1);
        button1 = findViewById(R.id.button_activity_report_1);
        edittext1 = findViewById(R.id.edittext_activity_report_1);

        List<String> list = new ArrayList<String>();
        list.add("PILIH TIPE KRIMINAL");
        list.add("Pencopetan");
        list.add("Begal");
        list.add("Pencurian");
        list.add("Tawuran");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);
        spinner1.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edittext_text = edittext1.getText().toString();
                if(edittext_text.matches("") || spinner_index==0){
                    Toast.makeText(getApplicationContext(), "Pilih tipe kriminal dan masukkan detail kriminal",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                ReportActivityData data = ReportActivityData.getInstance(getApplicationContext());
                data.setReport_detail(edittext_text);
                data.setReport_spinner(String.valueOf(spinner1.getSelectedItem()));

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String ktp = prefs.getString("ktp", "0");
                String nama = prefs.getString("nama", "false");

                List<Crime> alarmList = CrimeList.get(getApplicationContext()).getalaromuList();

                Crime c = new Crime();
                c.setName(nama);
                c.setReport_detail(data.getReport_detail());
                c.setReport_spinner(data.getReport_spinner());
                c.setAddress(LocationAddressData.getInstance(getApplicationContext()).getAddress());
                c.setLatitude(LocationAddressData.getInstance(getApplicationContext()).getLatitude());
                c.setLongtitude(LocationAddressData.getInstance(getApplicationContext()).getLongtitude());
                c.setKtp(ktp);
                c.setTimestamp("");
                c.setFinish("0");
                c.setWasRead("0");
                c.setProceed("0");

                alarmList.add(c);

                Toast.makeText(getApplicationContext(), "Report Sent", Toast.LENGTH_SHORT)
                        .show();

                JSONObject jsonObject = new JSONObject();
                try{
                    jsonObject.put("ktp", ktp);
                    jsonObject.put("name", nama);
                    jsonObject.put("title", c.getReport_spinner());
                    jsonObject.put("description", c.getReport_detail());
                    jsonObject.put("address", c.getAddress());
                    jsonObject.put("latitude", c.getLatitude());
                    jsonObject.put("longtitude", c.getLongtitude());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(checkInternetConenction()) {
                    OkHTTPHandlerAll okHTTPHandler = new OkHTTPHandlerAll(getApplicationContext());
                    okHTTPHandler.executo2(StringColle.url_Create, jsonObject);
                }else{
                    Log.e("internet connection", "not granted");
                }

                //CrimeList.get(getApplicationContext()).saveCrime();

                Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }

    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            spinner_index = spinner1.getSelectedItemPosition();
            //Toast.makeText(getApplicationContext(),
            //        String.valueOf(spinner1.getSelectedItem()) + String.valueOf(spinner_index),
            //        Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

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
