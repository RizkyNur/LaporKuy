package com.example.tarkijowow.rpl;

import android.content.Intent;
import android.opengl.Visibility;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    TextView name, title, description, address, date, proses;
    String nameString, titleString, descriptionString, addressString, dateString, prosesString;
    String proceed, wasRead, finish;
    Button backu, kontribusi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getCrimeData();
        viewSet();
    }

    private void viewSet(){
        name = findViewById(R.id.activity_detail_name);
        description = findViewById(R.id.activity_detail_description);
        address = findViewById(R.id.activity_detail_address);
        title = findViewById(R.id.activity_detail_title);
        date = findViewById(R.id.activity_detail_date);
        proses = findViewById(R.id.activity_detail_proses);

        backu = findViewById(R.id.activity_detail_button_back);
        kontribusi = findViewById(R.id.activity_detail_button_kontribusi);

        name.setText(nameString);
        description.setText(descriptionString);
        address.setText(addressString);
        title.setText(titleString);
        date.setText(dateString);

        String text;
        if (finish.matches("1")) {
            findViewById(R.id.activity_detail_button_kontribusi).setVisibility(View.GONE);
            text = "Proses : SUDAH SELESAI";
        }
        else if (proceed.matches("1")) {
            text = "Proses : MASIH DIPROSES";
        }
        else if (wasRead.matches("1")) {
            text = "Proses : SUDAH DIBACA";
        }
        else {
            text = "Proses : -";
        }
        proses.setText(text);

        backu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        kontribusi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ContributionActivity.class);
                i.putExtra("name", nameString);
                i.putExtra("address", addressString);
                i.putExtra("description", descriptionString);
                i.putExtra("date", dateString);
                startActivity(i);
            }
        });
    }

    private void getCrimeData(){
        nameString = getIntent().getStringExtra("name");
        addressString = getIntent().getStringExtra("address");
        titleString = getIntent().getStringExtra("title");
        descriptionString = getIntent().getStringExtra("description");
        dateString = getIntent().getStringExtra("date");

        proceed = getIntent().getStringExtra("proceed");
        wasRead = getIntent().getStringExtra("wasRead");
        finish = getIntent().getStringExtra("finish");
    }
}
