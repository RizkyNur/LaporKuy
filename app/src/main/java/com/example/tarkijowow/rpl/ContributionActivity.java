package com.example.tarkijowow.rpl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ContributionActivity extends AppCompatActivity {

    TextView textviewContribution;
    Button send;
    EditText edittext;
    String nama, date, description, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribution);

        setData();
    }

    private void setData(){
        edittext = findViewById(R.id.edittext_activity_contribution_1);
        textviewContribution = findViewById(R.id.textview_activity_contribution_2);

        nama = getIntent().getStringExtra("name");
        date = getIntent().getStringExtra("date");
        description = getIntent().getStringExtra("description");
        address = getIntent().getStringExtra("address");

        String total = address + " " + description;
        if(total.length() > 70){
            total = total.substring(0, 48) + "...";
        }
        textviewContribution.setText(total);

        send = findViewById(R.id.button_activity_contribution_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description = edittext.getText().toString();
                startContribute();
            }
        });
    }

    private void startContribute(){
        OkHttpHandlerUserContribute contribute = new OkHttpHandlerUserContribute(
                new OkHttpHandlerUserContribute.OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted(Boolean result) {
                        if(result){
                            Toast.makeText(getApplicationContext(), "response sent",
                                    Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "response not sent",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                this, nama, description, address, date);
        contribute.execute(StringColle.url_Report_User);
    }
}
