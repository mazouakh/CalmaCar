package com.example.calmacar.passenger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.calmacar.R;
import com.example.calmacar.common.Trip;

import java.awt.font.TextAttribute;
import java.util.ArrayList;

public class TripsSearchResultActivity extends AppCompatActivity {

    //TODO remove this temporary code
    TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_search_result);

        //TODO remove this temporary code
        tv_result = findViewById(R.id.tv_result);
        ArrayList<Trip> result = getIntent().getParcelableArrayListExtra("EXTRA_SEARCH_RESULT");
        String resultText = "";
        for (Trip trip : result){
            resultText = resultText + "-> " + trip.toString() + "\n";
        }
        tv_result.setText(resultText);
    }
}