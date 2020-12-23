package edu.ranken.bbailey.finalproject.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pokedex_entries")
public class PokedexEntries {

    public PokedexEntries(@NonNull Integer pokemonId, @NonNull String pokemonName, @NonNull String pokemonImage) {
        this.pokemonId = pokemonId;
        this.pokemonName = pokemonName;
        this.pokemonImage = pokemonImage;
    }

    @NonNull
    public Integer getPokemonId() {
        return pokemonId;
    }

    @NonNull
    public String getPokemonName() {
        return pokemonName;
    }

    @NonNull
    public String getPokemonImage() {
        return pokemonImage;
    }

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "pokemonId", typeAffinity = ColumnInfo.INTEGER)
    private final Integer pokemonId;

    @NonNull
    @ColumnInfo(name = "pokemonName", typeAffinity = ColumnInfo.TEXT, collate = ColumnInfo.NOCASE)
    private final String pokemonName;

    @NonNull
    @ColumnInfo(name = "pokemonImage", typeAffinity = ColumnInfo.TEXT)
    private final String pokemonImage;

}
