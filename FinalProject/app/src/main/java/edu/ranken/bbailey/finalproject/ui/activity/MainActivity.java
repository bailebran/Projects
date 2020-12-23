package edu.ranken.bbailey.finalproject.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import edu.ranken.bbailey.finalproject.data.PokemonRepository;
import edu.ranken.bbailey.finalproject.ui.adapter.MyPagerAdapter;
import edu.ranken.bbailey.finalproject.R;


public class MainActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private LocalBroadcastManager mBroadcastManager;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case PokemonRepository.ACTION_LOADING:
                    displayToast(getString(R.string.loading_pokedex));
                    break;
                case PokemonRepository.ACTION_ADDING:
                    displayToast(getString(R.string.pokemon_added));
                    break;
                case PokemonRepository.ACTION_DELETING:
                    displayToast(getString(R.string.pokemon_deleted));
                    break;
                case PokemonRepository.ACTION_ERROR:
                    displayToast(getString(R.string.pokedex_error));
                    break;
                case PokemonRepository.ACTION_ERROR_REFRESHING:
                    displayToast("Unable to load live Pokedex");
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);

        mBroadcastManager = LocalBroadcastManager.getInstance(this);

        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.pokedex)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.hunting)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getString(R.string.completed)));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);

        mTabLayout.addOnTabSelectedListener(
            new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    mViewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
        mViewPager.addOnPageChangeListener(
            new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PokemonRepository.ACTION_ADDING);
        intentFilter.addAction(PokemonRepository.ACTION_DELETING);
        intentFilter.addAction(PokemonRepository.ACTION_ERROR);
        intentFilter.addAction(PokemonRepository.ACTION_LOADING);
        mBroadcastManager.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        mBroadcastManager.unregisterReceiver(mReceiver);
        super.onStop();
    }

    public void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
