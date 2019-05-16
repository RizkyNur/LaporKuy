package com.example.tarkijowow.rpl;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Hashtable;

public class CrimeAdapter extends RecyclerView.Adapter<CrimeAdapter.MyViewHolder> {

    private List<Crime> crimeList;
    private String fragment_title;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView spinner, detail, teemo;
        private ProgressDialog progressDialog;
        public View view;

        public MyViewHolder(View view) {
            super(view);
            this.view = view;
            spinner = (TextView) view.findViewById(R.id.spinner_crime_list_row);
            detail = (TextView) view.findViewById(R.id.detail_crime_list_row);
            teemo = (TextView) view.findViewById(R.id.time_crime_list_row);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog = new ProgressDialog(v.getContext());
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();

                    if (fragment_title.toLowerCase().contains("notif")) {
                        OkHttpHandlerGetOne getOne = new OkHttpHandlerGetOne(new OkHttpHandlerGetOne.OnTaskCompleted() {
                            @Override
                            public void onTaskCompleted(Crime c) {
                                afterDialog(c);
                            }
                        }, v.getContext(), crimeList.get(getAdapterPosition()).getTimestamp());
                        Log.d("crimeadapter", crimeList.get(getAdapterPosition()).getTimestamp().toString());
                        getOne.execute(StringColle.url_Read_One);
                    }

                    else if (fragment_title.toLowerCase().contains("progress")) {
                        OkHttpHandlerGetOneNT nt = new OkHttpHandlerGetOneNT(new OkHttpHandlerGetOneNT.OnTaskCompleted() {
                            @Override
                            public void onTaskCompleted(Crime c) {
                                afterDialog(c);
                            }
                        }, v.getContext(), crimeList.get(getAdapterPosition()));
                        nt.execute(StringColle.url_Read_One_NT);
                    }
                }
            });
        }

        public void afterDialog(Crime c){
            progressDialog.dismiss();
            Intent i = new Intent(view.getContext(), DetailActivity.class);
            i.putExtra("address", c.getAddress());
            i.putExtra("title", c.getReport_spinner());
            i.putExtra("name", c.getName());
            i.putExtra("description", c.getReport_detail());
            i.putExtra("date", c.getTimestamp());
            i.putExtra("proceed", c.getProceed());
            i.putExtra("wasRead", c.getWasRead());
            i.putExtra("finish", c.getFinish());
            view.getContext().startActivity(i);
        }
    }


    public CrimeAdapter(List<Crime> crimeList, String fragment_title) {
        this.crimeList = crimeList;
        this.fragment_title = fragment_title;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.crime_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Crime crime = crimeList.get(position);
        holder.spinner.setText(crime.getReport_spinner());
        holder.detail.setText(crime.getReport_detail());
        holder.teemo.setText(crime.getTimestamp());
        if (crime.getFinish().matches("1")) {
            holder.view.setBackgroundColor(Color.parseColor("#33cc00"));
        }
    }

    @Override
    public int getItemCount() {
        return crimeList.size();
    }

}