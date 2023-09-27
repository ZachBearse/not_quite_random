package com.zachbearse.notquiterandom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.zachbearse.notquiterandom.databinding.FragmentViewBinding;

import java.util.List;
import java.util.Random;


public class ViewFragment extends Fragment {

    private ViewOnlyFighterAdapter mAdapter;
    private String mGroupName;
    private List<Fighter> mFighterList;
    private FragmentViewBinding mBinding;
    public ViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        FightersViewModel mViewModel = new ViewModelProvider(requireActivity()).get(FightersViewModel.class);
        mAdapter = new ViewOnlyFighterAdapter(requireContext());
        mGroupName = ViewFragmentArgs.fromBundle(getArguments()).getTitle();
        mFighterList = mViewModel.getGroup(mGroupName);
        mAdapter.setFighters(mFighterList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentViewBinding.inflate(inflater);
        ViewCompat.setOnApplyWindowInsetsListener(mBinding.getRoot(), this::onApplyWindowInsets);
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mBinding.buttonRandom.setOnClickListener(this::onClick);
        return mBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.view_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_edit){
            ViewFragmentDirections.ActionViewFragmentToNewGroupFragment action
                    = ViewFragmentDirections.actionViewFragmentToNewGroupFragment();
            action.setEditExisting(true);
            action.setGroupName(mGroupName);
            Navigation.findNavController(mBinding.getRoot()).navigate(action);
        }
        return super.onOptionsItemSelected(item);
    }

    private void onClick(View view) {
        Random r = new Random();
        NavDirections action = ViewFragmentDirections
                .actionViewFragmentToSelectedFighterFragment(
                        mFighterList.get(r.nextInt(mFighterList.size())));
        Navigation.findNavController(view).navigate(action);
    }

    /**
     * Apply insets to avoid overlapping the navigation bar.
     * applying bottom inset directly to root layout causes RecyclerView items to pop in/out
     * while still partially on screen.
     *
     * @param v            view to apply insets to
     * @param windowInsets current window insets
     * @return consumed insets
     */
    private WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat windowInsets) {
        Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
        // apply left/right insets to root layout
        v.setPadding(insets.left, 0, insets.right, 0);
        // apply bottom inset to floating action button, plus 16dp
        View faButton = v.findViewById(R.id.button_random);
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) faButton.getLayoutParams();
        // mlp uses pixel units, so 16dp base margin needs to be converted
        int baseMargin = (int) getResources().getDisplayMetrics().density * 16;
        mlp.bottomMargin = (baseMargin + insets.bottom);
        faButton.setLayoutParams(mlp);
        return WindowInsetsCompat.CONSUMED;
    }
}