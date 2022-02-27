package com.zachbearse.notquiterandom;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class FighterListAdapter extends RecyclerView.Adapter<FighterListAdapter.FighterViewHolder> {

    private final int WHITE = 0xFFFFFFFF;
    private final int GRAY = 0xFF707070;
    private final LayoutInflater mInflater;
    private final ColorMatrixColorFilter mGrayscale;
    private List<Fighter> mFighters;

    FighterListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        mGrayscale = new ColorMatrixColorFilter(matrix);
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
            if (current.isChecked()) {
                holder.portraitView.setColorFilter(null);
                holder.nameView.setTextColor(WHITE);
                holder.numberView.setTextColor(WHITE);
            } else {
                holder.portraitView.setColorFilter(mGrayscale);
                holder.nameView.setTextColor(GRAY);
                holder.numberView.setTextColor(GRAY);
            }
        }
    }

    void setFighters(List<Fighter> fighters) {
        mFighters = fighters;
        notifyDataSetChanged();
    }

    void setAllChecked(boolean checked) {
        for (Fighter fighter : mFighters) {
            fighter.setChecked(checked);
        }
        notifyDataSetChanged();
    }

    List<Fighter> getSelection() {
        List<Fighter> selection = new ArrayList<>();
        for (Fighter fighter : mFighters) {
            if (fighter.isChecked()) {
                selection.add(fighter);
            }
        }
        return selection;
    }

    @Override
    public int getItemCount() {
        if (mFighters != null) {
            return mFighters.size();
        } else {
            return 0;
        }
    }

    public class FighterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView numberView;
        private final TextView nameView;
        private final ImageView portraitView;


        public FighterViewHolder(@NonNull View itemView) {
            super(itemView);
            numberView = itemView.findViewById(R.id.numberView);
            nameView = itemView.findViewById(R.id.nameView);
            portraitView = itemView.findViewById(R.id.portraitView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            Fighter fighter = mFighters.get(position);
            fighter.setChecked(!fighter.isChecked());
            notifyItemChanged(position);
        }
    }
}