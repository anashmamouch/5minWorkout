package com.benzino.fiveminworkout.instructions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.benzino.fiveminworkout.data.Model;
import com.benzino.fiveminworkout.R;

import java.util.List;

/**
 * Created by Anas on 28/1/16.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.PlyoCardioHolder> {

    public List<Model> list;
    private Context context;

    public CustomAdapter(List<Model> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public PlyoCardioHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_instruction, parent, false);
        return new PlyoCardioHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlyoCardioHolder holder, int position) {
        final Model item = list.get(position);
        holder.titleTextView.setText(item.mTitle);
        holder.descTextView.setText(item.mDescription);
        holder.videoButton.setText("VIDEO");

        holder.videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.mUrl)));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class PlyoCardioHolder extends RecyclerView.ViewHolder{
        protected TextView titleTextView;
        protected TextView descTextView;
        protected Button videoButton;

        public PlyoCardioHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.plyocardio_exercice_title);
            descTextView = (TextView) view.findViewById(R.id.plyocardio_exercice_desc);
            videoButton = (Button) view.findViewById(R.id.plyocardio_video);

        }
    }
}
