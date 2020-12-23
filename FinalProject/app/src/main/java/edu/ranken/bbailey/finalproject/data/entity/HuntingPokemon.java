package edu.ranken.bbailey.finalproject.data.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "hunting_list")
public class HuntingPokemon {

    @NonNull
    public Integer getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getImage() {
        return image;
    }

    public Integer getEncounters() {
        return encounters;
    }

    public Integer getIsHunting() {
        return isHunting;
    }

    public Integer getIsCompleted() {
        return isCompleted;
    }

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "pokemonId", typeAffinity = ColumnInfo.INTEGER)
    private final Integer id;

    @NonNull
    @ColumnInfo(name = "pokemonName", typeAffinity = ColumnInfo.TEXT, collate = ColumnInfo.NOCASE)
    private final String name;

    @NonNull
    @ColumnInfo(name = "pokemonImage", typeAffinity = ColumnInfo.TEXT)
    private final String image;

    @ColumnInfo(name = "encounters", typeAffinity = ColumnInfo.INTEGER)
    private final Integer encounters;

    @ColumnInfo(name = "isHunting", typeAffinity = ColumnInfo.INTEGER)
    private final Integer isHunting;

    @ColumnInfo(name = "isCompleted", typeAffinity = ColumnInfo.INTEGER)
    private final Integer isCompleted;

    public HuntingPokemon(@NonNull Integer id, @NonNull String name, @NonNull String image, Integer encounters, Integer isHunting, Integer isCompleted) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.encounters = encounters;
        this.isHunting = isHunting;
        this.isCompleted = isCompleted;
    }

}
