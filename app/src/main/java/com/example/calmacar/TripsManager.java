package com.example.calmacar;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TripsManager {

    private static TripsManager instance;
    private static String TAG = "TripsManager";
    FirebaseAuth mAuth;
    FirebaseDatabase mDb;
    DatabaseReference activeTripsReference, completedTripsReference;


    private TripsManager() {
        // Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();
        // Firebase Database instance
        mDb = FirebaseDatabase.getInstance();
        // Database references
        activeTripsReference = mDb.getReference("Active Trips");
        completedTripsReference = mDb.getReference("Completed Trips");
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

    public void updateActiveTripsListView(Context ctx, ListView listView){
        activeTripsReference.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Trip> activeTrips = new ArrayList<>();
                if (snapshot.exists()){
                    // get all trips
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        activeTrips.add(dataSnapshot.getValue(Trip.class));

                        Log.d(TAG, "onDataChange: got the following trip : " + dataSnapshot.getValue(Trip.class));
                    }
                    Log.d(TAG, "Got the following trips : " + activeTrips.toString());

                    // update the list view
                    TripsAdapter activeTripsAdapter = new TripsAdapter(ctx, activeTrips);
                    listView.setAdapter(activeTripsAdapter);

                }else {
                    Log.w(TAG, "Trying to get trips for user ["+ mAuth.getUid() +"] " +
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

                        Log.d(TAG, "onDataChange: got the following completed trip : " + dataSnapshot.getValue(Trip.class));
                    }
                    Log.d(TAG, "Got the following completed trips : " + completedTrips);

                    // update the list view
                    TripsAdapter activeTripsAdapter = new TripsAdapter(ctx, completedTrips);
                    listView.setAdapter(activeTripsAdapter);

                }else {
                    Log.w(TAG, "Trying to get completed trips for user ["+ mAuth.getUid() +"] " +
                            "but none were found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateWalletBalanceBasedOnCompletedTrips(Context ctx, TextView balance){
        completedTripsReference.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float totalBalance = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if (!dataSnapshot.child("price").exists())
                        continue;
                    totalBalance += dataSnapshot.child("price").getValue(Float.TYPE);
                }
                balance.setText(totalBalance + "€");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
