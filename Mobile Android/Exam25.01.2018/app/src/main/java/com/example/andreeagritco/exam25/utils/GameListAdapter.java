package com.example.andreeagritco.exam25.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.andreeagritco.exam25.R;
import com.example.andreeagritco.exam25.model.Game;

import java.util.List;

/**
 * Created by Andreea Gritco on 01-Feb-18.
 */

public class GameListAdapter extends BaseAdapter {

    private List<Game> gamesList;
    private LayoutInflater inflater;
    private boolean isClient;

    public GameListAdapter(List<Game> gamesList, LayoutInflater inflater, boolean isClient) {
        this.gamesList = gamesList;
        this.inflater = inflater;
        this.isClient = isClient;
    }

    @Override
    public int getCount() {
        return gamesList.size();
    }

    @Override
    public Object getItem(int position) {
        return gamesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return gamesList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.game_layout, null);

        TextView nameText = convertView.findViewById(R.id.gameNameText);
        TextView quantityText = convertView.findViewById(R.id.gameQuantityText);
        TextView typeText = convertView.findViewById(R.id.gameTypeText);
        TextView statusText = convertView.findViewById(R.id.gameStatusText);

        nameText.setText(gamesList.get(position).getName());
        quantityText.setText(gamesList.get(position).getQuantity() + "");
        typeText.setText(gamesList.get(position).getType().toString());

        if (isClient) {
            statusText.setVisibility(View.GONE);
        } else {
            statusText.setText(gamesList.get(position).getStatus().toString());
        }

        return convertView;
    }
}
