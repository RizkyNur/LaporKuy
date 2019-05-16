package com.example.tarkijowow.rpl;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainMenuActivity extends AppCompatActivity {

    private static int PERMISSION_REQUEST_CODE = 55;

    private ViewPager viewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;

    private SettingsClient settingsClient;
    private FusedLocationProviderClient client;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest.Builder builder;
    private LocationCallback mLocationCallback;
    private Task<LocationSettingsResponse> task;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);

        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        permissionSet();

        ActivityBackground.readBackground(getApplicationContext());

        fusedLocationSet();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    public void onPause() {
        super.onPause();
        //CrimeList.get(getApplicationContext()).saveCrime();
    }

    @Override
    public void onDestroy() {
        stopLocationUpdates();
        super.onDestroy();
    }

    private void stopLocationUpdates() {
        client.removeLocationUpdates(mLocationCallback);
        Log.d("stoplocationupdate", "called");
    }

    private void startLocationUpdates() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(3000);
        mLocationRequest.setMaxWaitTime(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        Log.d("startlocationupdate", "called");
        settingsClient = LocationServices.getSettingsClient(this);
        task = settingsClient.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                //Toast.makeText(getApplicationContext(), "settings success", Toast.LENGTH_SHORT)
                //        .show();
                getLocation();
                if(ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getApplicationContext(),
                                ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    client.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "settings failed", Toast.LENGTH_SHORT)
                                .show();
                        getLocation();
                    }
                });
    }

    private void getLocation(){
        if(ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            client.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                String text = "latitude : " + location.getLatitude() +
                                        " , longtitude : " + location.getLongitude();
                                Log.d("getLocation", "success is " + text);
                            }
                        }
                    });
        }
    }

    private void fusedLocationSet(){
        client = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult == null){
                    Log.d("onLocationResult", "failed");
                    return;
                }
                //Toast.makeText(getApplicationContext(), "fusedcallback", Toast.LENGTH_SHORT)
                //        .show();
                location = locationResult.getLastLocation();
                String text = "latitude : " + location.getLatitude() +
                        " , longtitude : " + location.getLongitude();
                ActivityBackground.sendBackground(getApplicationContext(), location,
                    StringColle.url_Update_User);
                //Log.d("MainActivity", "receiverSet : " + text);
            }
        };
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LaporFragment(), "LAPOR");
        adapter.addFragment(new NotifFragment(), "NOTIF");
        adapter.addFragment(new ProgressFragment(), "HISTORY");
        adapter.addFragment(new AccountFragment(), "ACCOUNT");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        stopLocationUpdates();
                        MainMenuActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    private boolean checkSelfPermit(){
        return ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void permissionSet(){
        if(!checkSelfPermit()){
            requestPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean accept = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean accept2 = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            if (accept && accept2) {
                Toast.makeText(getApplicationContext(), "granted", Toast.LENGTH_SHORT).show();
            }
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                        showMessage("Give permission", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(new String[]{ACCESS_FINE_LOCATION},
                                        PERMISSION_REQUEST_CODE);
                            }
                        });
                        return;
                    }
                    if (shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION)) {
                        showMessage("Give permission", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(new String[]{ACCESS_COARSE_LOCATION},
                                        PERMISSION_REQUEST_CODE);
                            }
                        });
                        return;
                    }
                }
            }
        }
    }

    private void showMessage(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
