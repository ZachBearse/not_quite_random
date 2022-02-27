package com.zachbearse.notquiterandom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.zachbearse.notquiterandom.databinding.FragmentViewBinding;

import java.util.List;
import java.util.Objects;
import java.util.Random;


public class ViewFragment extends Fragment {

    private final String TITLE_KEY = "title";
    private ViewOnlyFighterAdapter mAdapter;
    private List<Fighter> mFighterList;

    public ViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FightersViewModel mViewModel = new ViewModelProvider(requireActivity()).get(FightersViewModel.class);
        mAdapter = new ViewOnlyFighterAdapter(requireContext());
        String title = ViewFragmentArgs.fromBundle(getArguments()).getTitle();
        mFighterList = mViewModel.getGroup(title);
        mAdapter.setFighters(mFighterList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.zachbearse.notquiterandom.databinding.FragmentViewBinding mBinding = FragmentViewBinding.inflate(inflater);
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mBinding.buttonRandom.setOnClickListener(this::onClick);

        return mBinding.getRoot();
    }

    private void onClick(View view) {
        Random r = new Random();
        NavDirections action = ViewFragmentDirections
                .actionViewFragmentToSelectedFighterFragment(
                        mFighterList.get(r.nextInt(mFighterList.size())));
        Navigation.findNavController(view).navigate(action);
    }
}