package edu.ranken.bbailey.finalproject.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.ranken.bbailey.finalproject.data.entity.HuntingPokemon;
import edu.ranken.bbailey.finalproject.data.entity.PokedexEntries;

@Dao
public interface PokemonDao {

    @Query("SELECT * FROM hunting_list WHERE isCompleted = 0 ORDER BY pokemonId")
    LiveData<List<HuntingPokemon>> getHuntingList();

    @Query("SELECT * FROM pokedex_entries ORDER BY pokemonId")
    LiveData<List<PokedexEntries>> getStoredPokedex();

    @Query("SELECT * FROM HUNTING_LIST WHERE isCompleted = 1 ORDER BY pokemonId")
    LiveData<List<HuntingPokemon>> getCompletedList();

    @Query("SELECT * FROM pokedex_entries WHERE pokemonName = :name")
    PokedexEntries getSearch(String name);

    @Insert
    void insertHunting(HuntingPokemon huntingPokemon);

    @Insert
    void insertPokedex(PokedexEntries pokedexEntry);

    @Query("DELETE FROM pokedex_entries")
    void deletePokedex();

    @Update
    void updatePokemon(HuntingPokemon pokemon);

    @Delete
    void deletePokemon(HuntingPokemon pokemon);

}
