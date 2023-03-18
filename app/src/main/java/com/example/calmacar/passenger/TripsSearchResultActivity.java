package com.example.calmacar.passenger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.calmacar.R;
import com.example.calmacar.common.Trip;
import com.example.calmacar.common.TripsManager;
import com.example.calmacar.common.TripsSearchResultAdapter;

import java.util.ArrayList;
import java.util.Collections;

public class TripsSearchResultActivity extends AppCompatActivity {

    RadioGroup r_grp_orderBy;
    RadioButton r_btn_priceAsc, r_btn_priceDsc, r_btn_departAsc, r_btn_departDsc;
    ListView lv_searchResult;
    ArrayList<Trip> searchResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_search_result);

        // Get data from intent
        searchResult = getIntent().getParcelableArrayListExtra("EXTRA_SEARCH_RESULT");

        // Hooks
        r_grp_orderBy = findViewById(R.id.r_grp_orderBy);
        r_btn_priceAsc = findViewById(R.id.r_btn_priceAsc);
        r_btn_priceDsc = findViewById(R.id.r_btn_priceDsc);
        r_btn_departAsc = findViewById(R.id.r_btn_departAsc);
        r_btn_departDsc = findViewById(R.id.r_btn_departDsc);
        lv_searchResult = findViewById(R.id.lv_searchResult);

        // Order by radio btn
        r_grp_orderBy.clearCheck();
        // Listeners
        r_grp_orderBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.r_btn_priceAsc:
                        orderByPrice(true);
                        break;
                    case R.id.r_btn_priceDsc:
                        orderByPrice(false);

                        break;
                    case R.id.r_btn_departAsc:
                        orderByStartTime(true);
                        break;
                    case R.id.r_btn_departDsc:
                        orderByStartTime(false);
                        break;
                }
                // update UI
                updateUI();
            }
        });
        r_btn_departAsc.setChecked(true);

        // Trip clicked listener
        lv_searchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("TSRA", "onItemClick: item " + i + " clicked");
                Trip trip = (Trip) lv_searchResult.getAdapter().getItem(i);
                TripsManager.getInstance().displayActiveTripDetails(TripsSearchResultActivity.this, trip);
            }
        });

        // Finally display the listview
        updateUI();
    }

    private void orderByPrice(Boolean Ascending){
        // sort by price
        if (Ascending)
            Collections.sort(searchResult, Trip.PriceAscComparator);
        else
            Collections.sort(searchResult, Trip.PriceDscComparator);
    }

    private void orderByStartTime(Boolean Ascending){
        // sort by start time
        if (Ascending)
            Collections.sort(searchResult, Trip.StartTimeAscComparator);
        else
            Collections.sort(searchResult, Trip.StartTimeDscComparator);
    }

   //TODO remove this temporary code
    private void updateUI(){
        TripsSearchResultAdapter adapter = new TripsSearchResultAdapter(this, searchResult);
        lv_searchResult.setAdapter(adapter);
    }
}