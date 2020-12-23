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
import edu.ranken.bbailey.finalproject.ui.adapter.HuntingListAdapter;
import edu.ranken.bbailey.finalproject.ui.viewmodel.PokemonViewModel;

public class HuntingListFragment extends Fragment {
    private LiveData<List<HuntingPokemon>> mPokemonList;
    private PokemonViewModel mPokemonViewModel;
    private RecyclerView mRecycler;
    private HuntingListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_hunting_list, container, false);
        Context context = getActivity();
        mRecycler = contentView.findViewById(R.id.huntingRecyclerView);

        mPokemonViewModel = ViewModelProviders.of(this).get(PokemonViewModel.class);
        mPokemonList = mPokemonViewModel.getHuntingList();

        mRecycler.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new HuntingListAdapter(context, mPokemonList.getValue());
        mRecycler.setAdapter(mAdapter);

        mPokemonList.observe(getViewLifecycleOwner(), new Observer<List<HuntingPokemon>>() {
            @Override
            public void onChanged(List<HuntingPokemon> huntingPokemons) {
                mAdapter.setItems(huntingPokemons);
            }
        });
        return contentView;
    }
}
