package com.example.calmacar.common;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserManager {
    private static final String TAG = "UserManager";
    private static UserManager instance;

    private DatabaseReference registeredUsersReference;

    private UserManager(){
        registeredUsersReference = FirebaseDatabase.getInstance().getReference("Registered Users");
    }

    public static UserManager getInstance(){
        if (instance == null)
            instance = new UserManager();
        return instance;
    }



    public void displayUserFirstName(String id, TextView tv_firstName){
        registeredUsersReference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    Log.d(TAG, "displayUserFirstName: user with ID " + id + " not found");
                    return;
                }

                User driver = snapshot.getValue(User.class);
                tv_firstName.setText(driver.getFirstname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void displayDriverDetails(String driverID, TextView tv_driverFirstName, TextView tv_driverNumber){
        registeredUsersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()){
                    if (!userSnapshot.getKey().equals(driverID))
                        continue;

                    User driver = userSnapshot.getValue(User.class);
                    tv_driverFirstName.setText(driver.getFirstname());
                    tv_driverNumber.setText(driver.getPhone());
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void displayPassengerDetails(String passengerID, TextView tv_passengerName, TextView tv_passengerPhone) {
        registeredUsersReference.child(passengerID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    Log.d(TAG, "onDataChange: Passenger with ID " + passengerID + " not found");
                    return;
                }

                User passenger = snapshot.getValue(User.class);
                tv_passengerName.setText(passenger.getFirstname());
                tv_passengerPhone.setText(passenger.getPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
