package com.zachbearse.notquiterandom;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ViewOnlyFighterAdapter extends RecyclerView.Adapter<ViewOnlyFighterAdapter.FighterViewHolder> {

    private LayoutInflater mInflater;
    private List<Fighter> mFighters;

    ViewOnlyFighterAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public FighterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.fighter_list_item, parent, false);
        return new FighterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FighterViewHolder holder, int position) {
        if (mFighters != null) {
            Fighter current = mFighters.get(position);
            holder.nameView.setText(current.getName());
            holder.numberView.setText(current.getNumber());
            Uri portrait = Uri.parse(current.getImage());
            Glide.with(holder.portraitView).load(portrait).into(holder.portraitView);
        }
    }

    void setFighters(List<Fighter> fighters) {
        mFighters = fighters;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mFighters != null) {
            return mFighters.size();
        } else {
            return 0;
        }
    }

    public class FighterViewHolder extends RecyclerView.ViewHolder {

        private final TextView numberView;
        private final TextView nameView;
        private final ImageView portraitView;

        public FighterViewHolder(@NonNull View itemView) {
            super(itemView);
            numberView = itemView.findViewById(R.id.numberView);
            nameView = itemView.findViewById(R.id.nameView);
            portraitView = itemView.findViewById(R.id.portraitView);
        }
    }
}