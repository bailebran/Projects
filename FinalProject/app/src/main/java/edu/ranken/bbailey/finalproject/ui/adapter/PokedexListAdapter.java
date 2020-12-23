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

import edu.ranken.bbailey.finalproject.data.entity.PokedexEntries;
import edu.ranken.bbailey.finalproject.ui.activity.PokemonViewActivity;
import edu.ranken.bbailey.finalproject.R;

public class PokedexListAdapter extends RecyclerView.Adapter<PokedexListAdapter.PokedexViewHolder> {
    private final Context mContext;
    private final LayoutInflater mInflater;
    private List<PokedexEntries> mPokedexList;

    public PokedexListAdapter(Context context, List<PokedexEntries> pokedexPokemonList) {
        mInflater = LayoutInflater.from(context);
        mPokedexList = pokedexPokemonList;
        mContext = context;
    }

    public void setItems(List<PokedexEntries> items) {
        mPokedexList = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mPokedexList != null ? mPokedexList.size() : 0;
    }

    @NonNull
    @Override
    public PokedexViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View pokemonView = mInflater.inflate(R.layout.item_pokedex, parent, false);
        return new PokedexViewHolder(pokemonView);
    }

    @Override
    public void onBindViewHolder(@NonNull PokedexViewHolder holder, int position) {
        PokedexEntries pokedexEntries = mPokedexList.get(position);
        holder.mNumber.setText(String.format(Locale.US, mContext.getString(R.string.id_formatting), pokedexEntries.getPokemonId()));
        holder.mName.setText(pokedexEntries.getPokemonName().toUpperCase());

        Picasso.get()
            .load(pokedexEntries.getPokemonImage())
            .placeholder(R.drawable.pokeball)
            .resize(256, 256)
            .centerCrop()
            .into(holder.mImage);
    }

    public class PokedexViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mNumber;
        public final TextView mName;
        public final ImageView mImage;

        public PokedexViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.pokemonName);
            mNumber = itemView.findViewById(R.id.pokemonNumber);
            mImage = itemView.findViewById(R.id.pokemonImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            PokedexEntries item = mPokedexList.get(position);
            Intent intent = new Intent(mContext, PokemonViewActivity.class);
            intent.putExtra("image", item.getPokemonImage());
            intent.putExtra("name", item.getPokemonName());
            intent.putExtra("id", item.getPokemonId());
            mContext.startActivity(intent);
        }
    }
}
