package com.example.calmacar.driver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.calmacar.R;
import com.example.calmacar.common.Trip;
import com.example.calmacar.common.TripsManager;
import com.example.calmacar.common.UserManager;

public class PassengerDetailsActivity extends AppCompatActivity {

    TextView tv_tripDate, tv_departCity, tv_arrivalCity, tv_departTime, tv_arrivalTime, tv_price,
            tv_tripDescription, tv_passengerName, tv_passengerPhone;
    Button btn_return;
    ImageView imgv_passengerPhoto;

    Trip trip;

    // References
    TripsManager mTripsManager;
    UserManager mUserManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_details);

        // References
        mTripsManager = TripsManager.getInstance();
        mUserManager = UserManager.getInstance();

        // Hooks
        tv_tripDate = findViewById(R.id.tv_tripDate);
        tv_departCity = findViewById(R.id.tv_departCity);
        tv_arrivalCity = findViewById(R.id.tv_arrivalCity);
        tv_departTime = findViewById(R.id.tv_departTime);
        tv_arrivalTime = findViewById(R.id.tv_arrivalTime);
        tv_price = findViewById(R.id.tv_price);
        tv_tripDescription = findViewById(R.id.tv_tripDescription);
        tv_passengerName = findViewById(R.id.tv_passengerName);
        tv_passengerPhone = findViewById(R.id.tv_passengerPhone);
        btn_return = findViewById(R.id.btn_return);
        imgv_passengerPhoto = findViewById(R.id.imgv_passengerPhoto);

        // Button Listeners
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Get data from Intent
        Intent incoming = getIntent();
        trip = (Trip) incoming.getParcelableExtra("EXTRA_TRIP");
        String passengerID = incoming.getStringExtra("EXTRA_PASSENGER_ID");

        // update trip details
        tv_tripDate.setText(trip.getDate());
        tv_departCity.setText(trip.getStartCity());
        tv_arrivalCity.setText(trip.getEndCity());
        tv_departTime.setText(trip.getStartTime());
        tv_arrivalTime.setText(trip.getEndTime());
        tv_price.setText(String.valueOf(trip.getPrice()));
        tv_tripDescription.setText(trip.getDescription());

        // update driver details
        mUserManager.updatePassengerDetails(passengerID, tv_passengerName, tv_passengerPhone);
    }
}