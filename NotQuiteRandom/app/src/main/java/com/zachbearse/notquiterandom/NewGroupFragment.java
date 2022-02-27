package com.zachbearse.notquiterandom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.zachbearse.notquiterandom.databinding.FragmentNewGroupBinding;

import java.util.Set;


public class NewGroupFragment extends Fragment {

    private FragmentNewGroupBinding mBinding;
    private FightersViewModel mViewModel;
    private FighterListAdapter mAdapter;
    private boolean mSelectAll = true;

    public NewGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(FightersViewModel.class);
        mAdapter = new FighterListAdapter(requireContext());
        mAdapter.setFighters(mViewModel.getAllFighters());
        mAdapter.setAllChecked(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentNewGroupBinding.inflate(inflater, container, false);
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mBinding.buttonSelectAll.setOnClickListener(this::onSelectAllClick);
        mBinding.buttonSave.setOnClickListener(this::onSaveClick);
        Set<String> titlesInUse = mViewModel.getRandomGroups();
        if (titlesInUse.contains("Random Group")) {
            int i = 1;
            while (titlesInUse.contains("Random Group " + i)) {
                i++;
            }
            mBinding.titleEditText.setText(requireContext().getString(R.string.random_group_count, i));
        } else {
            mBinding.titleEditText.setText(requireContext().getString(R.string.random_group));
        }


        return mBinding.getRoot();
    }


    private void onSelectAllClick(View view) {
        if (mSelectAll) {
            mAdapter.setAllChecked(true);
            mSelectAll = false;
            mBinding.buttonSelectAll.setText(R.string.unselect_all);
        } else {
            mAdapter.setAllChecked(false);
            mSelectAll = true;
            mBinding.buttonSelectAll.setText(R.string.select_all);
        }
    }

    private void onSaveClick(View view) {
        String title = mBinding.titleEditText.getText().toString();
        if (mViewModel.getRandomGroups().contains(title)) {
            mBinding.titleEditText.setError("This title is already in use");
        } else if (title.isEmpty()) {
            mBinding.titleEditText.setError("Needs a title");
        } else {
            if (mAdapter.getSelection().size() < 2) {
                MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(requireContext());
                alert.setTitle("Not Enough Fighters")
                        .setMessage("Please select at least two fighters")
                        .setNeutralButton(getString(R.string.confirm), (dialog, which) -> dialog.cancel())
                        .show();
            } else {
                mViewModel.addGroup(title, mAdapter.getSelection());
                Navigation.findNavController(view).popBackStack();
            }
        }
    }
}