package com.example.calmacar.driver.model;

import android.content.Context;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.calmacar.driver.view.PaymentsAdapter;
import com.example.calmacar.common.model.TripsManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaymentManager {
    private static PaymentManager instance;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDb;
    private DatabaseReference completedTripsReference, paymentsReference;

    private Balance mBalance;



    private PaymentManager(){
        // Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();
        // Firebase Database instance
        mDb = FirebaseDatabase.getInstance();
        // Database references
        completedTripsReference = mDb.getReference("Completed Trips");
        paymentsReference = mDb.getReference("Drivers Payments");
        // Balance instance
        mBalance = Balance.getInstance();
    }

    public static PaymentManager getInstance(){
        if (instance == null)
            instance = new PaymentManager();
        return instance;
    }

    public void makePayment(Context ctx, ListView lv_payments, ListView lv_completedTrips, TextView tv_balance, float amount){
        // create a new payment instance
        Payment payment = new Payment(amount);
        // send it to the database
        paymentsReference.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Object> postValues = new HashMap<>();
                // storing old values
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    postValues.put(dataSnapshot.getKey(), dataSnapshot.getValue());
                }
                // adding new value
                postValues.put(payment.getId(), payment);
                // updating the node with new values
                paymentsReference.child(mAuth.getUid()).setValue(postValues);
                // update UI
                updateDriverPaymentsListView(ctx, lv_payments);
                mBalance.setValue(0);
                tv_balance.setText("0.00€");
                TripsManager.getInstance().updateDriverCompletedTripsListView(ctx, lv_completedTrips);
                Toast.makeText(ctx, "Paiement envoyé avec succes.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void updateDriverAvailableBalance(Context ctx, TextView balance){
        // IMPORTANT mAuth.getUid() is the driver's ID
        completedTripsReference.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float totalBalance = 0;
                for (DataSnapshot passengerSnapshot : snapshot.getChildren()){
                    for (DataSnapshot tripSnapshot : passengerSnapshot.getChildren()){
                        if (!tripSnapshot.child("price").exists())
                            continue;
                        totalBalance += tripSnapshot.child("price").getValue(Float.TYPE);
                    }
                }
                mBalance.setValue(totalBalance);
                balance.setText(totalBalance + "€");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void updateDriverPaymentsListView(Context ctx, ListView lv_payments){
        paymentsReference.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Payment> payments = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    payments.add(dataSnapshot.getValue(Payment.class));
                }
                PaymentsAdapter adapter = new PaymentsAdapter(ctx, payments);
                lv_payments.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
