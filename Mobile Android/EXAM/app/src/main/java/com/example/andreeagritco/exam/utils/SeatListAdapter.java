package com.example.andreeagritco.exam.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.andreeagritco.exam.R;
import com.example.andreeagritco.exam.model.Seat;

import java.util.List;

/**
 * Created by Andreea Gritco on 02-Feb-18.
 */

public class SeatListAdapter extends BaseAdapter {

    private List<Seat> seatLlist;
    private LayoutInflater inflater;
    private boolean isClient;

    public SeatListAdapter(List<Seat> seatLlist, LayoutInflater inflater, boolean isClient) {
        this.seatLlist = seatLlist;
        this.inflater = inflater;
        this.isClient = isClient;
    }


    @Override
    public int getCount() {
        return seatLlist.size();
    }

    @Override
    public Object getItem(int position) {
        return seatLlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return seatLlist.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.seat_layout, null);

        TextView nameText = convertView.findViewById(R.id.nameSeatText);
        TextView typeText = convertView.findViewById(R.id.typeSeatText);
        TextView statusText = convertView.findViewById(R.id.statusSeatText);

        nameText.setText(seatLlist.get(position).getName());
        typeText.setText(seatLlist.get(position).getType().toString());

        if (isClient) {
            statusText.setVisibility(View.GONE);
        } else {
            statusText.setText(seatLlist.get(position).getStatus().toString());
        }


        return convertView;
    }
}
