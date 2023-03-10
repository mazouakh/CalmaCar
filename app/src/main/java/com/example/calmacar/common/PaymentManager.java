package com.example.calmacar.common;

import android.content.Context;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PaymentManager {
    private static PaymentManager instance;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDb;
    private DatabaseReference completedTripsReference, paymentsReference;



    private PaymentManager(){
        // Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();
        // Firebase Database instance
        mDb = FirebaseDatabase.getInstance();
        // Database references
        completedTripsReference = mDb.getReference("Completed Trips");
        paymentsReference = mDb.getReference("Drivers Payments");
    }

    public static PaymentManager getInstance(){
        if (instance == null)
            instance = new PaymentManager();
        return instance;
    }

    public void makePayment(Context ctx, ListView lv_payments, float amount){
        // get all completed trips

        // move them to Archived Trips
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
                Payment[] payments = (Payment[]) postValues.values().toArray();
                PaymentsAdapter adapter = new PaymentsAdapter(ctx, new ArrayList<>(Arrays.asList(payments)));
                lv_payments.setAdapter(adapter);

                Toast.makeText(ctx, "Paiement envoyé avec succes.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void updateDriverAvailableBalance(Context ctx, TextView balance){
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
