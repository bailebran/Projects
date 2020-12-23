package edu.ranken.bbailey.finalproject.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import edu.ranken.bbailey.finalproject.data.entity.HuntingPokemon;
import edu.ranken.bbailey.finalproject.ui.viewmodel.PokemonViewModel;
import edu.ranken.bbailey.finalproject.R;

public class PokemonViewActivity extends AppCompatActivity {
    static final String ENCOUNTERS = "encounters";
    private static final String LOG_TAG = PokemonViewActivity.class.getSimpleName();
    private TextView mDisplayName;
    private ImageView mDisplayImage;

    private Switch mIsHuntingSwitch;
    private Switch mIsCompletedSwitch;

    private TextView mCounter;
    private Button mAddEncounters;
    private Button mSubtractEncounters;

    private PokemonViewModel mPokemonViewModel;
    private int mPokemonId;
    private String mPokemonName;
    private String mPokemonImage;
    private int mCount;
    private int mIsHunting;
    private int mIsCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_view);
        mPokemonViewModel = ViewModelProviders.of(this).get(PokemonViewModel.class);

        mDisplayName = findViewById(R.id.name);
        mDisplayImage = findViewById(R.id.image);
        mIsHuntingSwitch = findViewById(R.id.huntingSwitch);
        mIsCompletedSwitch = findViewById(R.id.completedSwitch);
        mCounter = findViewById(R.id.encounterCounter);
        mAddEncounters = findViewById(R.id.plusButton);
        mSubtractEncounters = findViewById(R.id.minusButton);

        Intent intent = getIntent();
        mDisplayName.setText(intent.getStringExtra("name").toUpperCase());
        Picasso.get()
            .load(intent.getStringExtra("image"))
            .placeholder(R.drawable.pokeball)
            .resize(256, 256)
            .centerCrop()
            .into(mDisplayImage);

        mPokemonId = intent.getIntExtra("id", 0);
        mPokemonName = intent.getStringExtra("name");
        mPokemonImage = intent.getStringExtra("image");
        mCount = intent.getIntExtra("encounters", 0);
        mIsHunting = intent.getIntExtra("isHunting", 0);
        mIsCompleted = intent.getIntExtra("isCompleted", 0);

        if (savedInstanceState != null) {
            mCount = savedInstanceState.getInt(ENCOUNTERS);
        }

        mCounter.setText(String.valueOf(mCount));

        if (mIsHunting == 0) {
            mIsCompletedSwitch.setEnabled(false);
            mAddEncounters.setEnabled(false);
            mSubtractEncounters.setEnabled(false);
        } else {
            mIsHuntingSwitch.setChecked(true);
        }

        if (mIsCompleted == 1) {
            mIsCompletedSwitch.setChecked(true);
            mAddEncounters.setEnabled(false);
            mSubtractEncounters.setEnabled(false);
            mIsHuntingSwitch.setEnabled(false);
        }

        mIsHuntingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mPokemonViewModel.insertHuntingPokemon(new HuntingPokemon(mPokemonId, mPokemonName, mPokemonImage, mCount, 1, 0));
                    mIsCompletedSwitch.setEnabled(true);
                    mAddEncounters.setEnabled(true);
                    mSubtractEncounters.setEnabled(true);
                    showToast(getString(R.string.add_to_hunting));
                } else {
                    mPokemonViewModel.deleteHuntingPokemon(new HuntingPokemon(mPokemonId, mPokemonName, mPokemonImage, mCount, 0, 0));
                    mIsCompletedSwitch.setEnabled(false);
                    mAddEncounters.setEnabled(false);
                    mSubtractEncounters.setEnabled(false);
                    showToast(getString(R.string.removed_from_hunting));
                }
            }
        });

        mIsCompletedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPokemonViewModel.updateHuntingPokemon(new HuntingPokemon(mPokemonId, mPokemonName, mPokemonImage, mCount, 1, 1));
                    mIsHuntingSwitch.setEnabled(false);
                    mAddEncounters.setEnabled(false);
                    mSubtractEncounters.setEnabled(false);
                    showToast(getString(R.string.added_to_completed));
                } else {
                    mPokemonViewModel.updateHuntingPokemon(new HuntingPokemon(mPokemonId, mPokemonName, mPokemonImage, mCount, 1, 0));
                    mIsHuntingSwitch.setEnabled(true);
                    mAddEncounters.setEnabled(true);
                    mSubtractEncounters.setEnabled(true);
                    showToast(getString(R.string.removed_from_completed));
                }
            }
        });
    }
    public void increaseEncounters(View view) {
        mCount++;
        mCounter.setText(String.valueOf(mCount));
        mPokemonViewModel.updateHuntingPokemon(new HuntingPokemon(mPokemonId, mPokemonName, mPokemonImage, mCount, 1, 0));
    }

    public void decreaseEncounters(View view) {
        mCount--;
        mCounter.setText(String.valueOf(mCount));
        mPokemonViewModel.updateHuntingPokemon(new HuntingPokemon(mPokemonId, mPokemonName, mPokemonImage, mCount, 1, 0));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(ENCOUNTERS, mCount);
        super.onSaveInstanceState(outState);
    }

    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
