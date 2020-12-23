package edu.ranken.bbailey.finalproject.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import edu.ranken.bbailey.finalproject.data.entity.HuntingPokemon;
import edu.ranken.bbailey.finalproject.R;
import edu.ranken.bbailey.finalproject.ui.activity.PokemonViewActivity;

public class HuntingListAdapter extends RecyclerView.Adapter<HuntingListAdapter.PokemonViewHolder> {
    private final Context mContext;
    private final LayoutInflater mInflater;
    private List<HuntingPokemon> mHuntingPokemonList;

    public HuntingListAdapter(Context context, List<HuntingPokemon> huntingPokemonList) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mHuntingPokemonList = huntingPokemonList;
    }

    public void setItems(List<HuntingPokemon> items) {
        mHuntingPokemonList = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_hunting_pokemon, parent, false);
        return new PokemonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        HuntingPokemon huntingPokemon = mHuntingPokemonList.get(position);
        holder.mNumber.setText(String.format(Locale.US, mContext.getString(R.string.id_formatting), huntingPokemon.getId()));
        holder.mName.setText(huntingPokemon.getName().toUpperCase());
        holder.mCount.setText(String.format(Locale.US, mContext.getString(R.string.encounters_formatting), huntingPokemon.getEncounters()));

        Picasso.get()
            .load(huntingPokemon.getImage())
            .placeholder(R.drawable.pokeball)
            .resize(256, 256)
            .centerCrop()
            .into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        return mHuntingPokemonList != null ? mHuntingPokemonList.size() : 0;
    }

    public HuntingPokemon getPokemonAtPosition (int position) {return mHuntingPokemonList.get(position);}


    public class PokemonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mNumber;
        public final TextView mName;
        public final ImageView mImage;
        public final TextView mCount;

        public PokemonViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.pokemonName);
            mNumber = itemView.findViewById(R.id.pokemonNumber);
            mImage = itemView.findViewById(R.id.pokemonImage);
            mCount = itemView.findViewById(R.id.displayCount);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            HuntingPokemon item = mHuntingPokemonList.get(position);
            Intent intent = new Intent(mContext, PokemonViewActivity.class);
            intent.putExtra("image", item.getImage());
            intent.putExtra("name", item.getName());
            intent.putExtra("id", item.getId());
            intent.putExtra("encounters", item.getEncounters());
            intent.putExtra("isHunting", item.getIsHunting());
            intent.putExtra("isCompleted", item.getIsCompleted());
            mContext.startActivity(intent);
        }
    }
}
