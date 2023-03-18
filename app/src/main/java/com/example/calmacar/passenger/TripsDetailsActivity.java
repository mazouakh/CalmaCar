package com.example.calmacar.passenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.calmacar.R;
import com.example.calmacar.common.Trip;
import com.example.calmacar.common.TripsManager;
import com.example.calmacar.common.User;
import com.example.calmacar.common.UserManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class TripsDetailsActivity extends AppCompatActivity {

    TextView tv_tripDate, tv_departCity, tv_arrivalCity, tv_departTime, tv_arrivalTime, tv_price,
            tv_tripDescription, tv_driverName;
    Button btn_bookTrip;
    ImageView imgv_driverPhoto;

    Trip trip;

    // References
    TripsManager mTripsManager;
    UserManager mUserManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_details);

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
        tv_driverName = findViewById(R.id.tv_driverName);
        btn_bookTrip = findViewById(R.id.btn_bookTrip);
        imgv_driverPhoto = findViewById(R.id.imgv_driverPhoto);

        // Button Listeners
        btn_bookTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTripsManager.bookTrip(TripsDetailsActivity.this, trip);
            }
        });

        // Get data from Intent
        Intent incoming = getIntent();
        trip = (Trip) incoming.getParcelableExtra("EXTRA_TRIP");
        String driverID = incoming.getStringExtra("EXTRA_DRIVER_ID");

        // update trip details
        tv_tripDate.setText(trip.getDate());
        tv_departCity.setText(trip.getStartCity());
        tv_arrivalCity.setText(trip.getEndCity());
        tv_departTime.setText(trip.getStartTime());
        tv_arrivalTime.setText(trip.getEndTime());
        tv_price.setText(String.valueOf(trip.getPrice()));
        tv_tripDescription.setText(trip.getDescription());

        // update driver details
        mUserManager.displayUserFirstName(driverID, tv_driverName);
    }
}