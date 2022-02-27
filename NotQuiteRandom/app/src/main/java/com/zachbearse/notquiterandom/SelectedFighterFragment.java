package com.zachbearse.notquiterandom;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.zachbearse.notquiterandom.databinding.FragmentSelectedFighterBinding;


public class SelectedFighterFragment extends Fragment {

    private Fighter mFighter;

    public SelectedFighterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFighter = SelectedFighterFragmentArgs.fromBundle(getArguments()).getFighter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentSelectedFighterBinding binding = FragmentSelectedFighterBinding.inflate(inflater);
        binding.fighterView.nameView.setText(mFighter.getName());
        binding.fighterView.numberView.setText(mFighter.getNumber());
        Uri portrait = Uri.parse(mFighter.getImage());
        Glide.with(requireContext()).load(portrait).into(binding.fighterView.portraitView);
        return binding.getRoot();
    }
}