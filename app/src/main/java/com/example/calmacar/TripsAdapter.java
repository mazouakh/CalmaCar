package com.example.calmacar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TripsAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<Trip> trips;

    public TripsAdapter(Context context, ArrayList<Trip> trips) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.trips = trips;
    }

    @Override
    public int getCount() {
        return trips.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.listview_trips, null);

        // Hooks
        TextView tv_tripID = view.findViewById(R.id.tv_tripID);
        TextView tv_tripDate = view.findViewById(R.id.tv_tripDate);
        TextView tv_tripStartCity = view.findViewById(R.id.tv_tripStartCity);
        TextView tv_tripEndCity = view.findViewById(R.id.tv_tripEndCity);

        // Set Values
        tv_tripID.setText(trips.get(i).getId());
        tv_tripDate.setText(trips.get(i).getDate());
        tv_tripStartCity.setText(trips.get(i).getStartCity());
        tv_tripEndCity.setText(trips.get(i).getEndCity());

        return view;
    }
}
