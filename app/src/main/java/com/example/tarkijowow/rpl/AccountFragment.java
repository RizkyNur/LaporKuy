package com.example.tarkijowow.rpl;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class AccountFragment extends Fragment {

    public AccountFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account, container, false);

        Button button = v.findViewById(R.id.fragment_account_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to Log Out?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
                                prefs.edit().remove("hajimata").apply();
                                prefs.edit().remove("username").apply();
                                prefs.edit().remove("password").apply();

                                Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
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
        });

        TextView textView = v.findViewById(R.id.fragment_account_textview);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String ktp = prefs.getString("ktp", "none");
        if(!ktp.matches("none")){
            textView.setText(ktp);
        }

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //Toast.makeText(v.getContext(), "ProgressFragment created", Toast.LENGTH_SHORT)
        //        .show();
    }
}
