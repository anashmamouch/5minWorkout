package com.benzino.fiveminworkout;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.benzino.fiveminworkout.instructions.AbsInstruction;
import com.benzino.fiveminworkout.instructions.FatCardioInstruction;
import com.benzino.fiveminworkout.instructions.LegInstruction;
import com.benzino.fiveminworkout.instructions.PlyoCardioInstruction;
import com.benzino.fiveminworkout.workouts.AbsWorkout;
import com.benzino.fiveminworkout.workouts.FatCardioWorkout;
import com.benzino.fiveminworkout.workouts.LegWorkout;
import com.benzino.fiveminworkout.workouts.PlyoCardioWorkout;

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
                    context.startActivity(new Intent(context, PlyoCardioInstruction.class));
                }else if(position == 1){
                    context.startActivity(new Intent(context, FatCardioInstruction.class));
                }else if(position == 2){
                    context.startActivity(new Intent(context, AbsInstruction.class));
                }else if(position == 3){
                    context.startActivity(new Intent(context, LegInstruction.class));
                }
            }
        });
        holder.vStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position ==0){
                    context.startActivity(new Intent(context, PlyoCardioWorkout.class));
                }else if(position == 1){
                    context.startActivity(new Intent(context, FatCardioWorkout.class));
                }else if(position == 2){
                    context.startActivity(new Intent(context, AbsWorkout.class));
                }else if(position == 3){
                    context.startActivity(new Intent(context, LegWorkout.class));
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
