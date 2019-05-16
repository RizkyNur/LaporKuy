package com.example.tarkijowow.rpl;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

public class ProgressFragment extends Fragment {

    private List<Crime> alarmList;
    private RecyclerView recyclerView;
    public CrimeAdapter mAdapter;
    private SwipeRefreshLayout layoutRefresh;
    private View view;

    public ProgressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_progress, container, false);
        view = v;
        String fragment_title = getClass().getName();

        alarmList = CrimeList.get(getContext()).getalaromuList();

        recyclerView = v.findViewById(R.id.recyclerview_fragment_progress);
        mAdapter = new CrimeAdapter(alarmList, fragment_title);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity().getApplicationContext()));

        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        layoutRefresh = v.findViewById(R.id.swipelayout_fragment_progress);

        layoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layoutRefresh.setRefreshing(true);
                CrimeList.get(view.getContext()).CrimeDeleteList();
                OkHttpHandlerUserHistory history = new OkHttpHandlerUserHistory(new OkHttpHandlerUserHistory.OnTaskCompleted() {
                    @Override
                    public void onTaskCompleted() {
                        fetchProgressFragment();
                        layoutRefresh.setRefreshing(false);
                    }
                }, view.getContext());
                history.execute(StringColle.url_Read_One_History);
            }
        });

        return v;
    }

    private void fetchProgressFragment(){
        alarmList.clear();
        alarmList.addAll(CrimeList.get(view.getContext()).getalaromuList());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }
}
