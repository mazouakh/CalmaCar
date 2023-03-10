package com.example.calmacar.common;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TripsManager {

    private static TripsManager instance;
    private static String TAG = "TripsManager";
    FirebaseAuth mAuth;
    FirebaseDatabase mDb;
    DatabaseReference activeTripsReference, completedTripsReference, archivedTripsReference;


    private TripsManager() {
        // Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();
        // Firebase Database instance
        mDb = FirebaseDatabase.getInstance();
        // Database references
        activeTripsReference = mDb.getReference("Active Trips");
        completedTripsReference = mDb.getReference("Completed Trips");
        archivedTripsReference = mDb.getReference("Archived Trips");
    }

    public static TripsManager getInstance(){
        if (instance == null){
            instance = new TripsManager();
        }
        return instance;
    }

    public void createNewTrip(Context ctx, Trip newTrip){
        // send the new trip to the database

        // get old values before updating them
        activeTripsReference.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Object> postValues = new HashMap<String, Object>();
                // storing old values
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    postValues.put(dataSnapshot.getKey(), dataSnapshot.getValue());
                }
                // adding new value
                postValues.put(newTrip.getId(), newTrip);
                // updating the node with new values
                activeTripsReference.child(mAuth.getUid()).setValue(postValues);
                // Success toast
                Toast.makeText(ctx, "Trajet ajouter avec success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Could not add trip, an error occured", error.toException());
                Toast.makeText(ctx, "Une erreur c'est produit, veuillez réessayer.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void markTripAsCompletedAndUpdateUI(Context ctx, ListView listView, String tripID){
        // remove the trip from active trips and add it to completed trips
        activeTripsReference.child(mAuth.getUid()).child(tripID).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // First check that the trip exists
                if (snapshot.exists()){
                    // store the trip to remove locally
                    Trip tripToMarkCompleted = snapshot.getValue(Trip.class);
                    // add trip to Completed Trips table
                    completedTripsReference.child(mAuth.getUid()).child(tripID).setValue(tripToMarkCompleted);
                    // remove Trip from Active Trips table
                    activeTripsReference.child(mAuth.getUid()).child(tripID).removeValue();
                    Toast.makeText(ctx, "Trajet completé avec succes", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.e("TripsManager", "Trying to mark the trip ("+ tripID +") as completed but the trip was not found in ActiveTrips db");
                }
                updateActiveTripsListView(ctx, listView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void archiveTripsAndSendPayment(Context ctx, ListView lv_payments, ListView lv_completedTrips, TextView tv_balance){
        completedTripsReference.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float tripsTotalPrice = 0;
                // First check that the user has completed trips
                if (snapshot.exists()){
                    // iterate through each trip
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        // store the trip to remove locally
                        Trip tripToMarkPaid = dataSnapshot.getValue(Trip.class);
                        // add trip to Completed Trips table
                        archivedTripsReference.child(mAuth.getUid()).child(tripToMarkPaid.getId()).setValue(tripToMarkPaid);
                        // remove Trip from Active Trips table
                        completedTripsReference.child(mAuth.getUid()).child(tripToMarkPaid.getId()).removeValue();
                        // save the trip price
                        tripsTotalPrice += tripToMarkPaid.getPrice();
                    }
                    // Create a payment to the driver
                    PaymentManager.getInstance().makePayment(ctx, lv_payments, lv_completedTrips, tv_balance, tripsTotalPrice);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void markTripAsPaidToDriverAndUpdateUI(Context ctx, ListView listView, String tripID){
        // remove the trip from completed trips and add it to archived trips
        completedTripsReference.child(mAuth.getUid()).child(tripID).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // First check that the trip exists
                if (snapshot.exists()){
                    // store the trip to remove locally
                    Trip tripToMarkPaid = snapshot.getValue(Trip.class);
                    // add trip to Completed Trips table
                    archivedTripsReference.child(mAuth.getUid()).child(tripID).setValue(tripToMarkPaid);
                    // remove Trip from Active Trips table
                    completedTripsReference.child(mAuth.getUid()).child(tripID).removeValue();
                    Log.d(TAG, "onDataChange: Trajet " + tripID + " marqué comme payé au conducteur avec succes.");
                }
                else {
                    Log.e(TAG, "Trying to mark the trip "+ tripID +" as paid but the trip was not found in Completed Trips db");
                }
                updateActiveTripsListView(ctx, listView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateActiveTripsListView(Context ctx, ListView listView){
        activeTripsReference.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Trip> activeTrips = new ArrayList<>();
                if (snapshot.exists()){
                    // get all trips
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        activeTrips.add(dataSnapshot.getValue(Trip.class));
                    }
                    Log.d(TAG, "Got the following active trips : " + activeTrips.toString());

                    // update the list view
                    TripsAdapter activeTripsAdapter = new TripsAdapter(ctx, activeTrips);
                    listView.setAdapter(activeTripsAdapter);

                }else {
                    Log.w(TAG, "Trying to get active trips for user ["+ mAuth.getUid() +"] " +
                            "but no trips has been added yet.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateCompletedTripsListView(Context ctx, ListView listView){
        completedTripsReference.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Trip> completedTrips = new ArrayList<>();
                if (snapshot.exists()){
                    // get all trips
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        completedTrips.add(dataSnapshot.getValue(Trip.class));
                    }
                    Log.d(TAG, "Got the following completed trips : " + completedTrips);

                }else {
                    Log.w(TAG, "Trying to get completed trips for user ["+ mAuth.getUid() +"] " +
                            "but none were found.");
                }
                // update the list view
                TripsAdapter activeTripsAdapter = new TripsAdapter(ctx, completedTrips);
                listView.setAdapter(activeTripsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateArchivedTripsListView(Context ctx, ListView lv_archivedTrips) {
        archivedTripsReference.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Trip> archivedTrips = new ArrayList<>();
                if (snapshot.exists()){
                    // get all trips
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        archivedTrips.add(dataSnapshot.getValue(Trip.class));
                    }
                    Log.d(TAG, "Got the following archived trips : " + archivedTrips.toString());

                    // update the list view
                    TripsAdapter activeTripsAdapter = new TripsAdapter(ctx, archivedTrips);
                    lv_archivedTrips.setAdapter(activeTripsAdapter);

                }else {
                    Log.w(TAG, "Trying to get archived trips for user ["+ mAuth.getUid() +"] " +
                            "but no trips has been added yet.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
