package com.benzino.fiveminworkout.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.benzino.fiveminworkout.data.Model;
import com.benzino.fiveminworkout.helpers.MyAdapter;
import com.benzino.fiveminworkout.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anas on 23/1/16.
 */
public class WorkoutFragment extends android.support.v4.app.Fragment{

    public List<Model> mList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        MyAdapter adapter = new MyAdapter(mList, getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void initData(){
        mList = new ArrayList<Model>();
        mList.add(new Model(getResources().getString(R.string.plyometric_title), getResources().getString(R.string.plyometric_description)));
        mList.add(new Model(getResources().getString(R.string.fatburning_title), getResources().getString(R.string.fatburning_description)));
        mList.add(new Model(getResources().getString(R.string.abs_title), getResources().getString(R.string.abs_description)));
        mList.add(new Model(getResources().getString(R.string.leg_title), getResources().getString(R.string.leg_description)));

    }

}
