package com.zachbearse.notquiterandom;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        return mBinding.getRoot();
    }

    private void onFabClick(View view) {
        NavDirections action = HomeFragmentDirections.actionHomeFragmentToNewGroupFragment();
        Navigation.findNavController(view).navigate(action);
    }
}