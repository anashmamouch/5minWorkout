package com.benzino.fiveminworkout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.benzino.fiveminworkout.fragments.ProgressFragment;
import com.benzino.fiveminworkout.instructions.FatCardio;
import com.benzino.fiveminworkout.instructions.PlyoCardio;
import com.benzino.fiveminworkout.workouts.PlyoWorkout;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Anas on 27/1/16.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

    private List<Model> list;
    private Context context;
    public LayoutInflater inflater;


    public MyAdapter(List<Model> list, Context context){
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        Model item = list.get(position);
        holder.vTitle.setText(item.mTitle);
        holder.vDescription.setText(item.mDescription);
        holder.vHowTo.setText("LEARN");
        holder.vStart.setText("START");
        holder.vHowTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == 0){
                    context.startActivity(new Intent(context, PlyoCardio.class));
                }else if(position == 1){
                    context.startActivity(new Intent(context, FatCardio.class));
                }

            }
        });
        holder.vStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position ==0 ){
                    context.startActivity(new Intent(context, PlyoWorkout.class));
                    Toast.makeText(context, "START WORKOUT # 1" , Toast.LENGTH_LONG).show();
                }else if(position == 1 ){
                    Toast.makeText(context, "START WORKOUT # 2" , Toast.LENGTH_LONG).show();
                }else if(position == 2 ){
                    Toast.makeText(context, "START WORKOUT # 3" , Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(context, "START FOREVER" , Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder{
        protected TextView vTitle;
        protected TextView vDescription;
        protected Button vHowTo;
        protected Button vStart;

        public MyHolder(View view) {
            super(view);
            vTitle = (TextView) view.findViewById(R.id.title);
            vDescription = (TextView) view.findViewById(R.id.description);
            vHowTo = (Button) view.findViewById(R.id.howto_button);
            vStart = (Button) view.findViewById(R.id.start_button);

        }
    }

}