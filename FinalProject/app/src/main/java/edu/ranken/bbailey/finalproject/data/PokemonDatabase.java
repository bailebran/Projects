package edu.ranken.bbailey.finalproject.data;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import edu.ranken.bbailey.finalproject.data.dao.PokemonDao;
import edu.ranken.bbailey.finalproject.data.entity.HuntingPokemon;
import edu.ranken.bbailey.finalproject.data.entity.PokedexEntries;

@Database(entities = {HuntingPokemon.class, PokedexEntries.class}, version = 2)
public abstract class PokemonDatabase extends RoomDatabase {

    public abstract PokemonDao getPokemonDao();

    public static PokemonDatabase buildDatabase(Application app) {
        return Room.databaseBuilder(app, PokemonDatabase.class, "pokeTracker.db")
            .fallbackToDestructiveMigration()
            .build();
    }

}
