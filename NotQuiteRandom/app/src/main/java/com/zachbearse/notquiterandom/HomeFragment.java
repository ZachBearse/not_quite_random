package com.zachbearse.notquiterandom;

import android.content.ClipData;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.zachbearse.notquiterandom.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FightersViewModel mViewModel;
    private SetListAdapter mAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(FightersViewModel.class);
        mAdapter = new SetListAdapter(requireContext());
        mAdapter.setRandomGroups(mViewModel.getRandomGroups());
    }

    @Override
    public void onResume() {
        Log.d("DEBUG", "onResume");
        mAdapter.setRandomGroups(mViewModel.getRandomGroups());
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        com.zachbearse.notquiterandom.databinding.FragmentHomeBinding mBinding = FragmentHomeBinding.inflate(inflater, container, false);
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mBinding.addButton.setOnClickListener(this::onFabClick);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // remove the group from list and returns its name
                String groupName = mAdapter.remove(viewHolder.getAdapterPosition());
                mViewModel.removeGroup(groupName);
                Snackbar.make(mBinding.getRoot(), groupName + " Deleted", Snackbar.LENGTH_LONG).show();
            }
        });
        helper.attachToRecyclerView(mBinding.recyclerView);
        return mBinding.getRoot();
    }

    private void onFabClick(View view) {
        NavDirections action = HomeFragmentDirections.actionHomeFragmentToNewGroupFragment();
        Navigation.findNavController(view).navigate(action);
    }
}