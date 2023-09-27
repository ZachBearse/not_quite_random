package com.zachbearse.notquiterandom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.zachbearse.notquiterandom.databinding.FragmentNewGroupBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class NewGroupFragment extends Fragment {

    private FragmentNewGroupBinding mBinding;
    private FightersViewModel mViewModel;
    private FighterListAdapter mAdapter;
    private String mGroupName;
    private boolean mEditExisting;
    private boolean mSelectAll = true;

    public NewGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(FightersViewModel.class);
        mAdapter = new FighterListAdapter(requireContext());
        NewGroupFragmentArgs args = NewGroupFragmentArgs.fromBundle(getArguments());
        mEditExisting = args.getEditExisting();
        List<Fighter> allFighters = mViewModel.getAllFighters();
        if (mEditExisting) {
            mGroupName = args.getGroupName();
            List<Fighter> group = new ArrayList<>(mViewModel.getGroup(mGroupName));
            for (Fighter fighter : allFighters) {
                if (group.contains(fighter)) {
                    fighter.setChecked(true);
                } else {
                    fighter.setChecked(false);
                }
            }
        }
        mAdapter.setFighters(allFighters);
        //mAdapter.setAllChecked(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentNewGroupBinding.inflate(inflater, container, false);
        ViewCompat.setOnApplyWindowInsetsListener(mBinding.getRoot(), this::onApplyWindowInsets);
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mBinding.buttonSelectAll.setOnClickListener(this::onSelectAllClick);
        mBinding.buttonSave.setOnClickListener(this::onSaveClick);
        Set<String> titlesInUse = mViewModel.getRandomGroups();
        if (mEditExisting) {
            mBinding.titleEditText.setText(mGroupName);
        } else if (titlesInUse.contains("Random Group")) {
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
        if(mEditExisting && !title.equals(mGroupName)){
            mViewModel.renameGroup(mGroupName, title, mAdapter.getSelection());
            Navigation.findNavController(view).popBackStack();
        } else if (mViewModel.getRandomGroups().contains(title) && !mEditExisting) {
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

    /**
     * Apply insets to avoid overlapping the navigation bar.
     * Apply bottom inset directly to RecyclerView to avoid visual bugs
     *
     * @param v            view to apply insets to
     * @param windowInsets current window insets
     * @return consumed insets
     */
    private WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat windowInsets) {
        Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
        v.setPadding(insets.left, 0, insets.right, 0);
        View recyclerView = v.findViewById(R.id.recyclerView);
        // bottom RecyclerView item should come to 16 dp above bottom of screen or nav bar
        // View.setPadding uses pixel units. convert 16 dp to pixels
        int baseMargin = (int) getResources().getDisplayMetrics().density * 16;
        recyclerView.setPadding(0, recyclerView.getPaddingTop(), 0, baseMargin + insets.bottom);
        return WindowInsetsCompat.CONSUMED;
    }
}