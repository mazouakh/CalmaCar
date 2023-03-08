package com.example.calmacar;

import android.util.Log;
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
import java.util.Map;
import java.util.Set;

public class TripsManager {

    private static TripsManager instance;
    FirebaseAuth mAuth;
    FirebaseDatabase mDb;
    DatabaseReference activeTripsReference, completedTripsReference;


    private TripsManager() {
        // Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();
        // Firebase Database instance
        mDb = FirebaseDatabase.getInstance();
        // Database references
        DatabaseReference activeTripsReference = mDb.getReference("Active Trips");
        DatabaseReference completedTripsReference = mDb.getReference("Completed Trips");
    }

    public static TripsManager getInstance(){
        if (instance == null){
            instance = new TripsManager();
        }
        return instance;
    }

    public void createNewTrip(String uid, String date, String depart_city, String arriver_city){
        // Create a new trip object
        Trip newTrip = new Trip(date, depart_city, arriver_city);

        // send it to the database

        // get old values before updating them
        activeTripsReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
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
                activeTripsReference.child(uid).setValue(postValues);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public void markTripAsCompleted(String tripID){
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
                }
                else {
                    Log.e("TripsManager", "Trying to mark the trip ("+ tripID +") as completed but the trip was not found in ActiveTrips db");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public ArrayList<Trip> getActiveTripsForUser(String userID){
        ArrayList<Trip> trips = new ArrayList<>();

        return trips;
    }

    public ArrayList<Trip> getCompletedTripsForUser(String userID){
        ArrayList<Trip> trips = new ArrayList<>();

        return trips;
    }
}
