package edu.ranken.bbailey.finalproject.ui.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import edu.ranken.bbailey.finalproject.data.entity.HuntingPokemon;
import edu.ranken.bbailey.finalproject.data.PokemonApp;
import edu.ranken.bbailey.finalproject.data.PokemonRepository;
import edu.ranken.bbailey.finalproject.data.entity.PokedexEntries;

public class PokemonViewModel extends AndroidViewModel {
    private static final String LOG_TAG = PokemonViewModel.class.getSimpleName();

    private PokemonApp mApp;
    private PokemonRepository mRepository;
    private LiveData<List<HuntingPokemon>> mHuntingList;
    private LiveData<List<PokedexEntries>> mPokedex;
    private LiveData<List<HuntingPokemon>> mCompletedList;

    public PokemonViewModel(Application app) {
        super(app);
        mApp= (PokemonApp) app;
        mRepository = mApp.getRepository();
        mHuntingList = mRepository.getHuntingList();
        mPokedex = mRepository.getPokedex();
        mCompletedList = mRepository.getCompletedList();
    }

    public LiveData<List<HuntingPokemon>> getHuntingList() {return mHuntingList;}

    public LiveData<List<PokedexEntries>> getPokedex() {return mPokedex;}

    public LiveData<List<HuntingPokemon>> getCompletedList() {return mCompletedList;}

    //public PokedexEntries getSearchResults(String name) {return mRepository.getSearch(name);}

    public void insertHuntingPokemon(HuntingPokemon huntingPokemon) {
        mRepository.insertHuntedPokemon(huntingPokemon);
        Log.i(LOG_TAG, "HuntingPokemon added to hunting list!");
    }

    public void updateHuntingPokemon(HuntingPokemon huntingPokemon) {
        mRepository.updatePokemon(huntingPokemon);
    }

    public void deleteHuntingPokemon(HuntingPokemon huntingPokemon) {
        mRepository.deletePokemon(huntingPokemon);
    }
}
