package com.example.calmacar.common.model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.calmacar.common.view.TripsAdapter;
import com.example.calmacar.driver.view.PassengerDetailsActivity;
import com.example.calmacar.driver.model.PaymentManager;
import com.example.calmacar.passenger.view.DriverDetailsActivity;
import com.example.calmacar.passenger.view.SuccessfulReservationActivity;
import com.example.calmacar.passenger.view.TripsDetailsActivity;
import com.example.calmacar.passenger.view.TripsSearchResultActivity;
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
    DatabaseReference activeTripsReference, bookedTripsReference, completedTripsReference, archivedTripsReference;


    private TripsManager() {
        // Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();
        // Firebase Database instance
        mDb = FirebaseDatabase.getInstance();
        // Database references
        activeTripsReference = mDb.getReference("Active Trips");
        bookedTripsReference = mDb.getReference("Booked Trips");
        completedTripsReference = mDb.getReference("Completed Trips");
        archivedTripsReference = mDb.getReference("Archived Trips");
    }

    public static TripsManager getInstance(){
        if (instance == null){
            instance = new TripsManager();
        }
        return instance;
    }

    // CRUD API

    // CREATE
    public void createNewTrip(Context ctx, Trip newTrip){
        // IMPORTANT mAuth.getUid() is the driver's ID
        // send the new trip to the database
        activeTripsReference.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // get old values before updating them
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

    // UPDATE
    public void bookTrip(Context ctx, Trip trip) {
        // IMPORTANT mAuth.getUid() is the passenger's ID
        activeTripsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean tripFound = false;
                // Find the trip in the db
                usersLoop:
                for (DataSnapshot driversSnapshot : snapshot.getChildren()) {
                    // prevent booking a trip that is published by the same user
                    if (driversSnapshot.getKey().equals(mAuth.getUid()))
                        continue;
                    for (DataSnapshot tripsSnapshot : driversSnapshot.getChildren()){
                        if (!tripsSnapshot.getKey().equals(trip.getId()))
                            continue;

                        tripFound = true;

                        // Move the trip from active to booked node in db
                        Trip tripToBook = tripsSnapshot.getValue(Trip.class);
                        // add trip to booked node
                        bookedTripsReference.child(driversSnapshot.getKey()).child(mAuth.getUid()).child(tripsSnapshot.getKey()).setValue(tripToBook);
                        // remove trip from active trip (a trip is no longer available after it's booked)
                        activeTripsReference.child(driversSnapshot.getKey()).child(tripsSnapshot.getKey()).removeValue();
                        Toast.makeText(ctx, "Trajet reservé avec succes", Toast.LENGTH_SHORT).show();

                        // open SuccessfulReservation Activity
                        Intent intent = new Intent(ctx, SuccessfulReservationActivity.class);
                        ctx.startActivity(intent);
                        break usersLoop;
                    }
                }

                if (!tripFound)
                    Toast.makeText(ctx, "Trip " + trip.getId() + " not found as an active trip", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void completeTripAndUpdateUI(Context ctx, ListView lv_bookedTrips, String tripID){
        // IMPORTANT mAuth.getUid() is not used because we have both the driver's and passenger's
        // ID in the node hierarchy already

        // remove the trip from active trips and add it to completed trips
        bookedTripsReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean tripFound = false;

                driversLoop:
                for (DataSnapshot driversSnapshot : snapshot.getChildren()){
                    for (DataSnapshot passengersSnapshot : driversSnapshot.getChildren()){
                        for (DataSnapshot tripsSnapshot : passengersSnapshot.getChildren()){
                            if (!tripsSnapshot.getKey().equals(tripID))
                                continue;

                            tripFound = true;

                            // store the trip to remove locally
                            Trip tripToMarkCompleted = tripsSnapshot.getValue(Trip.class);
                            // add trip to Completed Trips table
                            completedTripsReference.child(driversSnapshot.getKey()).child(passengersSnapshot.getKey()).child(tripID).setValue(tripToMarkCompleted);
                            // remove Trip from Booked Trips table
                            bookedTripsReference.child(driversSnapshot.getKey()).child(passengersSnapshot.getKey()).child(tripID).removeValue();
                            Toast.makeText(ctx, "Trajet completé avec succes", Toast.LENGTH_SHORT).show();

                            break driversLoop;
                        }
                    }
                }


                if (!tripFound) {
                    Log.e("TripsManager", "Trying to mark the trip (" + tripID + ") as completed but the trip was not found in BookedTrips db");
                    return;
                }
                //TODO should this be kept of will the trip completion occur elsewhere then in the driver's trips fragment?
                if (lv_bookedTrips != null)
                    displayDriverBookedTrips(ctx, lv_bookedTrips);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void archiveTripsAndSendPayment(Context ctx, ListView lv_payments, ListView lv_completedTrips, TextView tv_balance){
        // IMPORTANT mAuth.getUid() is the driver's ID
        completedTripsReference.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float tripsTotalPrice = 0;
                // First check that the user has completed trips
                if (snapshot.exists()){
                    // iterate through each trip
                    for (DataSnapshot passengerSnapshot : snapshot.getChildren()){
                        for (DataSnapshot tripSnapshot : passengerSnapshot.getChildren()){
                            // store the trip to remove locally
                            Trip tripToMarkPaid = tripSnapshot.getValue(Trip.class);
                            // add trip to Completed Trips table
                            archivedTripsReference.child(mAuth.getUid()).child(passengerSnapshot.getKey()).child(tripToMarkPaid.getId()).setValue(tripToMarkPaid);
                            // remove Trip from Active Trips table
                            completedTripsReference.child(mAuth.getUid()).child(passengerSnapshot.getKey()).child(tripToMarkPaid.getId()).removeValue();
                            // save the trip price
                            tripsTotalPrice += tripToMarkPaid.getPrice();
                        }
                    }
                    // double checking to make sure that the balance to pay is not 0. ie: not trips were found
                    if (tripsTotalPrice == 0)
                        return;

                    // Create a payment to the driver
                    PaymentManager.getInstance().makePayment(ctx, lv_payments, lv_completedTrips, tv_balance, tripsTotalPrice);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * looks for all the trips that have a date less then today's date.
     * If the trip is booked, moves it to completed trips.
     * If the trip is not booked, moves it to archived trips.
     */
    public void handleOutdatedTrips(){
        activeTripsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot driverSnapshot : snapshot.getChildren()){
                    for (DataSnapshot tripSnapshot : driverSnapshot.getChildren()){
                        Trip trip = tripSnapshot.getValue(Trip.class);
                        if (!trip.isOutdated())
                            continue;

                        // move it to archived trips
                        archivedTripsReference.child(driverSnapshot.getKey()).child(trip.getId()).setValue(trip);
                        activeTripsReference.child(driverSnapshot.getKey()).child(trip.getId()).removeValue();

                        Log.d(TAG, "onDataChange: Archived trip : " + trip);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bookedTripsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot driverSnapshot : snapshot.getChildren()){
                    for (DataSnapshot passengerSnapshot : driverSnapshot.getChildren()){
                        for (DataSnapshot tripSnapshot : passengerSnapshot.getChildren()){
                            Trip trip = tripSnapshot.getValue(Trip.class);
                            if (!trip.isOutdated())
                                continue;

                            // move it to completed trips
                            completedTripsReference.child(driverSnapshot.getKey()).child(passengerSnapshot.getKey()).child(trip.getId()).setValue(trip);
                            bookedTripsReference.child(driverSnapshot.getKey()).child(passengerSnapshot.getKey()).child(trip.getId()).removeValue();

                            Log.d(TAG, "onDataChange: Completed trip : " + trip);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // READ
    public void displayActiveTripDetails(Context ctx, Trip trip){
        activeTripsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Search for the driver of this trip
                usersLoop:
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    for (DataSnapshot tripSnapshot : dataSnapshot.getChildren()){
                        Log.d(TAG, "onDataChange: looking for trip " + trip.getId() + " on snapshot " + tripSnapshot.toString());
                        if (!tripSnapshot.getKey().equals(trip.getId()))
                            continue;
                        Log.d(TAG, "onDataChange: found the trip");
                        // send both trip and it's driver to TripDetailActivity
                        String driverID = dataSnapshot.getKey();
                        Intent intent = new Intent(ctx, TripsDetailsActivity.class);
                        intent.putExtra("EXTRA_TRIP", trip);
                        intent.putExtra("EXTRA_DRIVER_ID", driverID);
                        ctx.startActivity(intent);

                        break usersLoop;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void displayPassengerBookedTripDetails(Context ctx, Trip trip){
        // IMPORTANT mAuth.getUid() is the passenger's ID
        bookedTripsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Search for the driver of this trip
                usersLoop:
                for (DataSnapshot driverSnapshot : snapshot.getChildren()){
                    for (DataSnapshot tripSnapshot : driverSnapshot.child(mAuth.getUid()).getChildren()){
                        Log.d(TAG, "onDataChange: looking for trip " + trip.getId() + " on snapshot " + tripSnapshot.toString());
                        if (!tripSnapshot.getKey().equals(trip.getId()))
                            continue;
                        Log.d(TAG, "onDataChange: found the trip");

                        // look for driver


                        // send both trip and it's driver to TripDetailActivity
                        String driverID = driverSnapshot.getKey();
                        Intent intent = new Intent(ctx, DriverDetailsActivity.class);
                        intent.putExtra("EXTRA_TRIP", trip);
                        intent.putExtra("EXTRA_DRIVER_ID", driverID);
                        ctx.startActivity(intent);

                        break usersLoop;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void displayDriverBookedTripDetails(Context ctx, Trip trip){
        // IMPORTANT mAuth.getUid() is the driver's ID
        bookedTripsReference.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Search for the driver of this trip
                usersLoop:
                for (DataSnapshot passengerSnapshot : snapshot.getChildren()){
                    for (DataSnapshot tripSnapshot : passengerSnapshot.getChildren()){
                        Log.d(TAG, "onDataChange: looking for trip " + trip.getId() + " on snapshot " + tripSnapshot.toString());
                        if (!tripSnapshot.getKey().equals(trip.getId()))
                            continue;
                        Log.d(TAG, "onDataChange: found the trip");

                        // TODO try to have one common activity for displaying trip details
                        // send both trip and it's driver to TripDetailActivity
                        String passengerID = passengerSnapshot.getKey();
                        Intent intent = new Intent(ctx, PassengerDetailsActivity.class);
                        intent.putExtra("EXTRA_TRIP", trip);
                        intent.putExtra("EXTRA_PASSENGER_ID", passengerID);
                        ctx.startActivity(intent);

                        break usersLoop;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void displayActiveTrips(Context ctx, ListView lv_activeTrips){
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
                    lv_activeTrips.setAdapter(activeTripsAdapter);

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

    public void displayDriverBookedTrips(Context ctx, ListView lv_bookedTrips){
        // IMPORTANT mAuth.getUid() is the driver's ID
        bookedTripsReference.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    Log.w(TAG, "Trying to get booked trips for user ["+ mAuth.getUid() +"] " +
                            "but no trips have been booked yet.");
                    return;
                }

                ArrayList<Trip> bookedTrips = new ArrayList<>();
                for (DataSnapshot passengersSnapshot : snapshot.getChildren()){
                    for (DataSnapshot tripsSnapshot : passengersSnapshot.getChildren()){
                        bookedTrips.add(tripsSnapshot.getValue(Trip.class));
                    }
                }
                Log.d(TAG, "Got the following booked trips : " + bookedTrips.toString());

                // update the list view
                TripsAdapter bookedTripsAdapter = new TripsAdapter(ctx, bookedTrips);
                lv_bookedTrips.setAdapter(bookedTripsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void displayPassengerBookedTrips(Context ctx, ListView lv_bookedTrips){
        // IMPORTANT mAuth.getUid() is the passenger's ID
        bookedTripsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    Log.w(TAG, "Trying to get booked trips for user ["+ mAuth.getUid() +"] " +
                            "but no trips have been booked yet.");
                    return;
                }

                ArrayList<Trip> bookedTrips = new ArrayList<>();
                for (DataSnapshot driversSnapshot : snapshot.getChildren()){
                    for (DataSnapshot tripsSnapshot : driversSnapshot.child(mAuth.getUid()).getChildren()){
                        if (tripsSnapshot.exists())
                            bookedTrips.add(tripsSnapshot.getValue(Trip.class));
                    }
                }
                if (bookedTrips.isEmpty()){
                    Log.w(TAG, "Trying to get booked trips for user ["+ mAuth.getUid() +"] " +
                            "but no trips have been booked yet.");
                    return;
                }

                Log.d(TAG, "Got the following booked trips : " + bookedTrips);

                // update the list view
                TripsAdapter bookedTripsAdapter = new TripsAdapter(ctx, bookedTrips);
                lv_bookedTrips.setAdapter(bookedTripsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void displayDriverCompletedTrips(Context ctx, ListView listView){
        completedTripsReference.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Trip> completedTrips = new ArrayList<>();
                if (snapshot.exists()){
                    // get all trips
                    for (DataSnapshot passengerSnapshot : snapshot.getChildren()){
                        for (DataSnapshot tripSnapshot : passengerSnapshot.getChildren()){
                            completedTrips.add(tripSnapshot.getValue(Trip.class));
                        }
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

    public void displayPassengerCompletedTrips(Context ctx, ListView lv_completedTrips){
        // IMPORTANT mAuth.getUid() is the passenger's ID
        completedTripsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    return;
                }

                ArrayList<Trip> completedTrips = new ArrayList<>();
                for (DataSnapshot driversSnapshot : snapshot.getChildren()){
                    for (DataSnapshot tripsSnapshot : driversSnapshot.child(mAuth.getUid()).getChildren()){
                        if (tripsSnapshot.exists())
                            completedTrips.add(tripsSnapshot.getValue(Trip.class));
                    }
                }
                if (completedTrips.isEmpty()){
                    Log.w(TAG, "Trying to get completed trips for user ["+ mAuth.getUid() +"] " +
                            "but no trips have been completed yet.");
                    return;
                }

                Log.d(TAG, "Got the following completed trips : " + completedTrips);

                // update the list view
                TripsAdapter completedTripsAdapter = new TripsAdapter(ctx, completedTrips);
                lv_completedTrips.setAdapter(completedTripsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void searchForTripsAndDisplayResult(Context ctx, String startCity, String endCity, String date, String startTime) {
        activeTripsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Trip> result = new ArrayList<>();
                // look for the active trips
                for (DataSnapshot driversSnapshot : snapshot.getChildren()){
                    // skip the trips that are published by the same user
                    if (driversSnapshot.getKey().equals(mAuth.getUid()))
                        continue;
                    for (DataSnapshot tripsSnapshot : driversSnapshot.getChildren()){
                        Trip currentlyFoundTrip = tripsSnapshot.getValue(Trip.class);

                        // check if the trip fits search criteria
                        if (!currentlyFoundTrip.getStartCity().equals(startCity) |
                        !currentlyFoundTrip.getEndCity().equals(endCity) |
                        !currentlyFoundTrip.getDate().equals(date) |
                        !currentlyFoundTrip.isStartTimeAfter(startTime))
                            continue;

                        // add the trip to the result
                        result.add(currentlyFoundTrip);
                    }
                }
                // open search result activity and send to it the result
                Intent intent = new Intent(ctx, TripsSearchResultActivity.class);
                intent.putParcelableArrayListExtra("EXTRA_SEARCH_RESULT", result);
                ctx.startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
