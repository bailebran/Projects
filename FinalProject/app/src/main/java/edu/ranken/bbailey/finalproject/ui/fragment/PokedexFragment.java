package edu.ranken.bbailey.finalproject.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;

import edu.ranken.bbailey.finalproject.R;
import edu.ranken.bbailey.finalproject.data.entity.PokedexEntries;
import edu.ranken.bbailey.finalproject.ui.activity.PokemonViewActivity;
import edu.ranken.bbailey.finalproject.ui.adapter.PokedexListAdapter;
import edu.ranken.bbailey.finalproject.ui.viewmodel.PokemonViewModel;

public class PokedexFragment extends Fragment {
    private static final String LOG_TAG = PokedexFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private PokedexListAdapter mAdapter;
    private PokemonViewModel mPokemonViewModel;
    private Button mSearchButton;
    private EditText mUserInput;
    private RequestQueue mRequestQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_pokedex, container, false);
        Context context = getActivity();
        mRecyclerView = contentView.findViewById(R.id.pokedexRecyclerView);

        mPokemonViewModel = ViewModelProviders.of(this).get(PokemonViewModel.class);

        LiveData<List<PokedexEntries>> pokedex = mPokemonViewModel.getPokedex();

        mRequestQueue = Volley.newRequestQueue(context);



        mAdapter = new PokedexListAdapter(context, pokedex.getValue());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(mAdapter);

//        mUserInput = contentView.findViewById(R.id.inputName);
//        mSearchButton = contentView.findViewById(R.id.searchButton);

//        mSearchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String search = mUserInput.getText().toString();
//                SearchPokemon();
//            }
//        });

        pokedex.observe(getViewLifecycleOwner(), new Observer<List<PokedexEntries>>() {
            @Override
            public void onChanged(List<PokedexEntries> pokedexEntries) {
                mAdapter.setItems(pokedexEntries);
            }
        });
        return contentView;
    }
//
//    public void SearchPokemon(){
//        JsonObjectRequest request = new JsonObjectRequest(
//            Request.Method.GET, "https://pokeapi.co/api/v2/pokemon/1", null,
//            new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    try{
//                        int id = response.getInt("id");
//                        String name = response.getString("name");
//                        String image = response.getJSONObject("sprites").getString("front_shiny");
//                        Intent intent = new Intent(getContext(), PokemonViewActivity.class);
//                        Log.e(LOG_TAG, name);
//                        intent.putExtra("id", id);
//                        intent.putExtra("name", name);
//                        intent.putExtra("image", image);
//                        startActivity(intent);
//
//                    } catch (Exception ex) {
//                        Log.e(LOG_TAG, "Failed to load pokemon info", ex);
//                    }
//                }
//            },
//            new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.e(LOG_TAG, "Failed to load pokemon info", error);
//                }
//            });
//        mRequestQueue.add(request);
//    }
}
