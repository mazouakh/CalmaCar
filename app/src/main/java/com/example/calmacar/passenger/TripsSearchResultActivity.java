package com.example.calmacar.passenger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calmacar.R;
import com.example.calmacar.common.Trip;

import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class TripsSearchResultActivity extends AppCompatActivity {

    //TODO remove this temporary code
    TextView tv_result;
    RadioGroup r_grp_orderBy;
    RadioButton r_btn_priceAsc, r_btn_priceDsc, r_btn_departAsc, r_btn_departDsc;
    ArrayList<Trip> searchResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_search_result);

        //TODO remove this temporary code
        tv_result = findViewById(R.id.tv_result);
        searchResult = getIntent().getParcelableArrayListExtra("EXTRA_SEARCH_RESULT");
        updateUI();

        // Hooks
        r_grp_orderBy = findViewById(R.id.r_grp_orderBy);
        r_btn_priceAsc = findViewById(R.id.r_btn_priceAsc);
        r_btn_priceDsc = findViewById(R.id.r_btn_priceDsc);
        r_btn_departAsc = findViewById(R.id.r_btn_departAsc);
        r_btn_departDsc = findViewById(R.id.r_btn_departDsc);

        // Order by radio btn
        r_grp_orderBy.clearCheck();
        // Listeners
        r_grp_orderBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Toast.makeText(TripsSearchResultActivity.this, "Radio button " + i + " clicked", Toast.LENGTH_SHORT).show();
                switch (i){
                    case R.id.r_btn_priceAsc:
                        orderByPrice(true);
                        Log.d("TSRActivity", "onCheckedChanged: ordering by price asc");
                        break;
                    case R.id.r_btn_priceDsc:
                        orderByPrice(false);
                        Log.d("TSRActivity", "onCheckedChanged: ordering by price dsc");

                        break;
                    case R.id.r_btn_departAsc:
                        orderByStartTime(true);
                        break;
                    case R.id.r_btn_departDsc:
                        orderByStartTime(false);
                        break;
                }
            }
        });
        r_btn_departAsc.setChecked(true);
    }

    private void orderByPrice(Boolean Ascending){
        // sort by price
        if (Ascending)
            Collections.sort(searchResult, Trip.PriceAscComparator);
        else
            Collections.sort(searchResult, Trip.PriceDscComparator);
        // update UI
        updateUI();
    }

    private void orderByStartTime(Boolean Ascending){
        // sort by start time
        if (Ascending)
            Collections.sort(searchResult, Trip.StartTimeAscComparator);
        else
            Collections.sort(searchResult, Trip.StartTimeDscComparator);
        // update UI
        updateUI();
    }

   //TODO remove this temporary code
    private void updateUI(){
        String resultText = "";
        for (Trip trip : searchResult){
            resultText = resultText + "-> " + trip.toString() + "\n";
        }
        tv_result.setText(resultText);
    }
}