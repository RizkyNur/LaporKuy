package com.example.tarkijowow.rpl;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class CrimeList {

    private static final String FILENAME = "crime.json";

    private static CrimeList ourInstance;
    private Context context;
    private ArrayList<Crime> crimeList;
    private ArrayList<Crime> crimeFetch;
    private CrimeJSONSerializer mSerializer;
    private String timestamp;
    private String timestamp_read_user;

    public static CrimeList get(Context c){
        if(ourInstance == null){
            ourInstance = new CrimeList(c.getApplicationContext());
        }
        return ourInstance;
    }

    private CrimeList(Context appcontext) {
        context = appcontext;
        crimeList = new ArrayList<>();
        crimeFetch = new ArrayList<>();
        timestamp = "0";
        timestamp_read_user = "0";

        mSerializer = new CrimeJSONSerializer(context, FILENAME);
        /*try {
            crimeList = mSerializer.loadCrimes();
        } catch (Exception e) {
            crimeList = new ArrayList<Crime>();
            Log.e("non non biyori : ", "Error loading crimes: ", e);
        }*/
    }

    public void CrimeDeleteAll(Context c){
        ourInstance = new CrimeList(c.getApplicationContext());
    }

    public void CrimeDeleteFetch(){
        crimeFetch = new ArrayList<>();
        timestamp = "0";
    }

    public void CrimeDeleteList(){
        crimeList = new ArrayList<>();
        timestamp_read_user = "0";
    }

    public boolean saveCrime() {

        try {
            mSerializer.saveAlarumo(crimeList);
            Log.d("AlaromuList", "crimes saved to file");
            return true;
        } catch (Exception e) {
            Log.e("AlaromuList", "Error saving crimes: ", e);
            return false;
        }
    }

    public void add(Crime e){
        crimeList.add(e);
    }

    public void add_fetch(Crime e){
        crimeFetch.add(e);
    }

    public void dell(Crime c) {
        crimeList.remove(c);
    }

    public void dell(int position){
        crimeList.remove(position);
    }

    public ArrayList getalaromuList() {
        return crimeList;
    }

    public ArrayList getalaromuFetch(){
        return crimeFetch;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp_read_user() {
        return timestamp_read_user;
    }

    public void setTimestamp_read_user(String timestamp_read_user) {
        this.timestamp_read_user = timestamp_read_user;
    }
}
