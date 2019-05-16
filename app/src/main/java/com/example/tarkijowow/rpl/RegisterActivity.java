package com.example.tarkijowow.rpl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    EditText ktp, nama, password, password_confirm;
    String ktpString, namaString, passwordString, password_confirmString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void register_activity_onclick(View view){
        ktp = findViewById(R.id.activity_register_no_ktp);
        nama = findViewById(R.id.activity_register_username);
        password = findViewById(R.id.activity_register_password);
        password_confirm = findViewById(R.id.activity_register_passconfirm);

        ktpString = ktp.getText().toString();
        namaString = nama.getText().toString();
        passwordString = password.getText().toString();
        password_confirmString = password_confirm.getText().toString();

        if(ktpString.matches("") || namaString.matches("") ||
                passwordString.matches("") || password_confirmString.matches("")){
            Toast.makeText(this, "please fill all the information", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if(!passwordString.matches(password_confirmString)) {
            Toast.makeText(this, "password confirmation does not match", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        OkHttpHandlerUserRegister userRegister = new OkHttpHandlerUserRegister(new OkHttpHandlerUserRegister.OnTaskCompleted() {
            @Override
            public void onTaskCompleted(Boolean result) {
                if(result) {
                    loginSuccess();
                }
                else {
                    loginFailed();
                }
            }
        }, this, namaString, passwordString, ktpString);
        userRegister.execute(StringColle.url_Register_User);
    }

    private void loginFailed() {
        Toast.makeText(this, "Register Faile",
                Toast.LENGTH_SHORT).show();
    }

    private void loginSuccess() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("nama", namaString);
        edit.putString("password", passwordString);
        edit.putString("ktp", ktpString);
        edit.putBoolean("hajimata", Boolean.TRUE);
        edit.apply();

        ActivityBackground.sendBackgroundInsertUser(this, ktpString);

        Intent i = new Intent(this, MainMenuActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
