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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    EditText editText_username;
    EditText editText_password;
    String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkLogin();
    }

    public void main_activity_onclick(View view){
        int id = view.getId();
        editText_username = findViewById(R.id.edittext_name_login);
        editText_password = findViewById(R.id.edittext_password_login);

        if(id == R.id.button_login){
            username = editText_username.getText().toString();
            password = editText_password.getText().toString();
            if(username.matches("") || password.matches("")){
                Toast.makeText(getApplicationContext(), "Please fill it first",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            OkHttpHandlerUserLogin userLogin = new OkHttpHandlerUserLogin(new OkHttpHandlerUserLogin.OnTaskCompleted() {
                @Override
                public void onTaskCompleted(Boolean result) {
                    if(result) {
                        loginSuccess();
                    }
                    else {
                        loginFailed();
                    }
                }
            }, this, username, password);
            userLogin.execute(StringColle.url_Login_User);
        }

        if(id == R.id.button_register){
            Intent i = new Intent(this, RegisterActivity.class);
            startActivity(i);
        }
    }

    private void loginFailed() {
        Toast.makeText(this, "Login Failed Please Use Correct Name and Password",
                Toast.LENGTH_SHORT).show();
    }

    private void loginSuccess() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("hajimata", Boolean.TRUE);
        edit.putString("nama", username);
        edit.putString("password", password);
        edit.apply();

        checkLogin();
    }

    private void checkLogin() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean("hajimata", false);
        if(previouslyStarted) {
            Intent i = new Intent(this, MainMenuActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }
}
