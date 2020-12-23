package edu.ranken.bbailey.finalproject.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.ranken.bbailey.finalproject.R;
import edu.ranken.bbailey.finalproject.data.entity.HuntingPokemon;
import edu.ranken.bbailey.finalproject.ui.adapter.CompletedListAdapter;
import edu.ranken.bbailey.finalproject.ui.viewmodel.PokemonViewModel;

public class CompletedListFragment extends Fragment {
    private RecyclerView mRecycler;
    private PokemonViewModel mPokemonViewModel;
    private LiveData<List<HuntingPokemon>> mCompletedList;
    private CompletedListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_completed_list, container, false);
        Context context = getActivity();
        mRecycler = contentView.findViewById(R.id.completedRecyclerView);

        mPokemonViewModel = ViewModelProviders.of(this).get(PokemonViewModel.class);
        mCompletedList = mPokemonViewModel.getCompletedList();

        mRecycler.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new CompletedListAdapter(context, mCompletedList.getValue());
        mRecycler.setAdapter(mAdapter);

        mCompletedList.observe(getViewLifecycleOwner(), new Observer<List<HuntingPokemon>>() {
            @Override
            public void onChanged(List<HuntingPokemon> huntingPokemonList) {
                mAdapter.setItems(huntingPokemonList);
            }
        });
        return contentView;
    }
}
