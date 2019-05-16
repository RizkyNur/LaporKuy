package com.example.tarkijowow.rpl;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NotifFragment extends Fragment {

    private List<Crime> alarmList;
    private RecyclerView recyclerView;
    private CrimeAdapter mAdapter;
    private SwipeRefreshLayout layoutRefresh;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notif, container, false);
        view = v;
        String fragment_title = getClass().getName();

        alarmList = CrimeList.get(getContext()).getalaromuFetch();

        recyclerView = v.findViewById(R.id.recyclerview_fragment_notif);
        mAdapter = new CrimeAdapter(alarmList, fragment_title);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity().getApplicationContext()));

        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        layoutRefresh = v.findViewById(R.id.swipelayout_fragment_notif);

        layoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layoutRefresh.setRefreshing(true);
                CrimeList.get(view.getContext()).CrimeDeleteFetch();
                OkHttpHandler okHttpHandler = new OkHttpHandler(new OkHttpHandler.OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted() {
                        fetchNotifFragment();
                        layoutRefresh.setRefreshing(false);
                    }
                }, view.getContext());
                okHttpHandler.execute(StringColle.url_Read);
            }
        });

        return v;
    }

    private void fetchNotifFragment(){
        alarmList.clear();
        alarmList.addAll(CrimeList.get(view.getContext()).getalaromuFetch());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }
}
