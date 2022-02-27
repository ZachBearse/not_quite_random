package com.zachbearse.notquiterandom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class SetListAdapter extends RecyclerView.Adapter<SetListAdapter.SetViewHolder> {

    private final LayoutInflater mInflater;
    private List<String> mRandomGroups;

    public SetListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setRandomGroups(Set<String> groups) {
        mRandomGroups = new ArrayList<>(groups);
        mRandomGroups.sort(Comparator.naturalOrder());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.set_list_item, parent, false);
        return new SetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SetViewHolder holder, int position) {
        String current = mRandomGroups.get(position);
        holder.textView.setText(current);
    }

    @Override
    public int getItemCount() {
        if (mRandomGroups != null) {
            return mRandomGroups.size();
        } else {
            return 0;
        }
    }

    public class SetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView textView;

        public SetViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            String title = mRandomGroups.get(position);
            NavDirections action = HomeFragmentDirections.actionHomeFragmentToViewFragment(title);
            Navigation.findNavController(view).navigate(action);

        }
    }
}
