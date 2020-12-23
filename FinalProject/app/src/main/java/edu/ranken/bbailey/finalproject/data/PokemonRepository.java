package edu.ranken.bbailey.finalproject.data;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.ranken.bbailey.finalproject.data.dao.PokemonDao;
import edu.ranken.bbailey.finalproject.data.entity.HuntingPokemon;
import edu.ranken.bbailey.finalproject.data.entity.PokedexEntries;

public class PokemonRepository {
    private static final String LOG_TAG = PokemonRepository.class.getSimpleName();
    public static final String ACTION_ERROR = "edu.ranken.bbailey.finalproject.ERROR";
    public static final String ACTION_LOADING = "edu.ranken.bbailey.finalproject.LOADING";
    public static final String ACTION_ADDING = "edu.ranken.bbailey.finalproject.ADDING";
    public static final String ACTION_DELETING = "edu.ranken.bbailey.finalproject.DELETING";
    public static final String ACTION_ERROR_REFRESHING = "edu.ranken.bbailey.finalproject.ERROR_REFRESHING";


    private PokemonApp mApp;
    private PokemonDatabase mDatabase;
    private RequestQueue mRequestQueue;
    private LocalBroadcastManager mBroadcastManager;

    private LiveData<List<HuntingPokemon>> mHuntingList;
    private LiveData<List<PokedexEntries>> mDatabasePokedex;
    private LiveData<List<HuntingPokemon>> mCompletedList;
    private MutableLiveData<List<PokedexEntries>> mNetworkPokedex;
    private MediatorLiveData<List<PokedexEntries>> mMergedPokedex;

    public PokemonRepository(PokemonApp app, PokemonDatabase db) {
        mApp = app;
        mDatabase = db;
        mRequestQueue = Volley.newRequestQueue(app);
        mBroadcastManager = LocalBroadcastManager.getInstance(app);

        mHuntingList = db.getPokemonDao().getHuntingList();
        mCompletedList = db.getPokemonDao().getCompletedList();
        mNetworkPokedex = new MutableLiveData<>();
        mDatabasePokedex = mDatabase.getPokemonDao().getStoredPokedex();
        mMergedPokedex = new MediatorLiveData<>();
        mMergedPokedex.addSource(mDatabasePokedex, new Observer<List<PokedexEntries>>() {
            @Override
            public void onChanged(List<PokedexEntries> pokedexEntries) {
                if (mNetworkPokedex.getValue() == null) {
                    mMergedPokedex.setValue(pokedexEntries);
                    Log.i(LOG_TAG, "Pokemon loaded from database");
                }
            }
        });
        mMergedPokedex.addSource(mNetworkPokedex, new Observer<List<PokedexEntries>>() {
            @Override
            public void onChanged(List<PokedexEntries> pokedexEntries) {
                if (pokedexEntries != null) {
                    mMergedPokedex.setValue(pokedexEntries);
                    mMergedPokedex.removeSource(mDatabasePokedex);
                    Log.i(LOG_TAG, "Pokemon loaded from network");
                }
            }
        });

    }

    public LiveData<List<HuntingPokemon>> getHuntingList() {return mHuntingList;}

    public LiveData<List<PokedexEntries>> getPokedex() {return mMergedPokedex;}

    public LiveData<List<HuntingPokemon>> getCompletedList() {return mCompletedList;}

    public void insertHuntedPokemon(HuntingPokemon huntingPokemon) {new InsertPokemonTask().execute(huntingPokemon);}

    public void insertPokedexPokemon(PokedexEntries pokedexEntries) {new InsertPokedexTask().execute(pokedexEntries);}

    public void updatePokemon(HuntingPokemon huntingPokemon) {new UpdatePokemonTask().execute(huntingPokemon);}

    public void deletePokedex() {new DeletePokedexTask(mDatabase.getPokemonDao()).execute();}

    public void deletePokemon(HuntingPokemon huntingPokemon) {new DeletePokemonAsyncTask().execute(huntingPokemon);}

    public void sendBroadcast(String action) {
        Intent intent = new Intent(action);
        mBroadcastManager.sendBroadcast(intent);
    }

    private class InsertPokemonTask extends AsyncTask<HuntingPokemon, Void, Void> {

        @Override
        protected Void doInBackground(HuntingPokemon[] params) {
            try {
                HuntingPokemon huntingPokemon = params[0];
                mDatabase.getPokemonDao().insertHunting(huntingPokemon);
                sendBroadcast(ACTION_ADDING);
                return null;
            } catch (Exception ex) {
                Log.e(LOG_TAG, "Failed to add pokemon", ex);
                return null;
            }
        }
    }

    private class InsertPokedexTask extends AsyncTask<PokedexEntries, Void, Void> {

        @Override
        protected Void doInBackground(PokedexEntries[] params) {
            try {
                PokedexEntries pokemon = params[0];
                mDatabase.getPokemonDao().insertPokedex(pokemon);
                return null;
            } catch (Exception ex) {
                Log.e(LOG_TAG, "Failed to insert pokedex", ex);
                sendBroadcast(ACTION_ERROR);
                return null;
            }
        }
    }

    private class UpdatePokemonTask extends AsyncTask<HuntingPokemon, Void, Void> {

        @Override
        protected Void doInBackground(HuntingPokemon[] params) {
            try {
                HuntingPokemon huntingPokemon = params[0];
                mDatabase.getPokemonDao().updatePokemon(huntingPokemon);
                return null;
            } catch (Exception ex) {
                Log.e(LOG_TAG, "Failed to update pokemon", ex);
                return null;
            }
        }
    }

    private class DeletePokemonAsyncTask extends AsyncTask<HuntingPokemon, Void, Void> {

        @Override
        protected Void doInBackground(HuntingPokemon[] params) {
            try {
                HuntingPokemon huntingPokemon = params[0];
                mDatabase.getPokemonDao().deletePokemon(huntingPokemon);
                return null;
            } catch (Exception ex) {
                Log.e(LOG_TAG, "Failed to delete pokemon", ex);
                return null;
            }
        }
    }

    private class DeletePokedexTask extends AsyncTask<Void, Void, Void> {
        private PokemonDao mAsyncTaskDao;

        DeletePokedexTask(PokemonDao dao) {mAsyncTaskDao = dao;}

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                mAsyncTaskDao.deletePokedex();
                return null;
            } catch (Exception ex) {
                Log.e(LOG_TAG, "Failed to delete pokedex", ex);
                sendBroadcast(ACTION_ERROR);
                return null;
            }
        }
    }

    public void refreshPokedex() {
        mRequestQueue.add(new JsonObjectRequest(
            Request.Method.GET,
            "https://pokeapi.co/api/v2/pokemon?limit=151",
            null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    new ProcessPokedexTread(response).start();
                    sendBroadcast(ACTION_LOADING);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(LOG_TAG, "Failed to refresh pokedex");
                    sendBroadcast(ACTION_ERROR_REFRESHING);
                }
            }
        ));
    }

    private class ProcessPokedexTread extends Thread{
        private JSONObject mResponse;
        private ArrayList<PokedexEntries> mPokedex;
        private int mPokemonCount;

        public ProcessPokedexTread(JSONObject reponse) {mResponse = reponse;}

        @Override
        public void run() {
            Log.i(LOG_TAG, "Processing pokedex");
            try{
                JSONArray items = mResponse.getJSONArray("results");

                mPokemonCount = items.length();
                mPokedex = new ArrayList<>(mPokemonCount);
                for (int i = 0; i < mPokemonCount; i++) {
                    JSONObject pokemon = items.getJSONObject(i);
                    String url = pokemon .getString("url");
                    loadPokemonInfo(url);
                }
            } catch (Exception ex){
                Log.e(LOG_TAG, "failed to process pokedex", ex);
            }
        }

        public void loadPokemonInfo(String url) {
            JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            int id = response.getInt("id");
                            String name = response.getString("name");
                            String image = response.getJSONObject("sprites").getString("front_shiny");
                            mPokedex.add(new PokedexEntries(id, name, image));
                            if (mPokedex.size() == mPokemonCount) {
                                savePokedexInfo();
                            }
                        } catch (Exception ex) {
                            Log.e(LOG_TAG, "Failed to load pokemon info", ex);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LOG_TAG, "Failed to load pokemon info", error);
                    }
                });
            mRequestQueue.add(request);
        }

        public void savePokedexInfo() {
            try {
//                mPokedex.sort(new Comparator<PokedexEntries>() {
//                    @Override
//                    public int compare(PokedexEntries o1, PokedexEntries o2) {
//                        return o1.getPokemonId() - o2.getPokemonId();
//                    }
//                });

                Collections.sort(mPokedex, new Comparator<PokedexEntries>() {
                    @Override
                    public int compare(PokedexEntries o1, PokedexEntries o2) {
                        return o1.getPokemonId() - o2.getPokemonId();
                    }
                });
                mNetworkPokedex.postValue(mPokedex);

                deletePokedex();
                for (PokedexEntries item : mPokedex) {
                    insertPokedexPokemon(item);
                }
                Log.i(LOG_TAG, "Pokedex Saved");
            } catch (Exception ex) {
                Log.e(LOG_TAG, "Failed to save pokedex", ex);
            }
        }
    }
}
