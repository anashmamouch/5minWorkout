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
        mList.add(new Model("Plyometric Cardio", "Focus on speed, agility and explosive power."));
        mList.add(new Model("Fat-burning Cardio", "Burn your calories quickly and increase your cardiovascular activity."));
        mList.add(new Model("ABS workout", "Get a flat belly and strenghten your core with these abs exercices."));
        mList.add(new Model("Leg Workout", "The ultimate excuse-free exercises for strong toned legs."));
    }

}
