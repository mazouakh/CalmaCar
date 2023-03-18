package com.example.calmacar.passenger.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.calmacar.R;
import com.example.calmacar.common.model.Trip;

import java.util.ArrayList;

public class TripsSearchResultAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Trip> trips;

    public TripsSearchResultAdapter(Context context, ArrayList<Trip> trips) {
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
        return trips.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.listview_search_result, null);

        // Hooks
        TextView tv_departCity = view.findViewById(R.id.tv_departCity);
        TextView tv_arrivalCity = view.findViewById(R.id.tv_arrivalCity);
        TextView tv_departTime = view.findViewById(R.id.tv_departTime);
        TextView tv_arrivalTime = view.findViewById(R.id.tv_arrivalTime);
        TextView tv_price = view.findViewById(R.id.tv_price);

        // Set Values
        tv_departCity.setText(trips.get(i).getStartCity());
        tv_arrivalCity.setText(trips.get(i).getEndCity());
        tv_departTime.setText(trips.get(i).getStartTime());
        tv_arrivalTime.setText(trips.get(i).getEndTime());
        tv_price.setText(trips.get(i).getPrice() + "€");

        return view;
    }
}
