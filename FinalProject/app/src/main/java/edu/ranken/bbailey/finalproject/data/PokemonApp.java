package edu.ranken.bbailey.finalproject.data;

import android.app.Application;

public class PokemonApp extends Application {

    private PokemonDatabase mDatabase;
    private PokemonRepository mRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        mDatabase = PokemonDatabase.buildDatabase(this);
        mRepository = new PokemonRepository(this, mDatabase);
        mRepository.refreshPokedex();
    }

    public PokemonRepository getRepository() {return mRepository;}
}
