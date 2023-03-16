package com.example.calmacar.common;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserManager {
    private static UserManager instance;
    private UserManager(){}

    public static UserManager getInstance(){
        if (instance == null)
            instance = new UserManager();
        return instance;
    }

    public void displayUserFirstName(String driverID, TextView tv_driverFirstName){
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("Registered Users");
        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()){
                    if (!userSnapshot.getKey().equals(driverID))
                        continue;

                    User driver = userSnapshot.getValue(User.class);
                    tv_driverFirstName.setText(driver.getFirstname());
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
