package com.example.calmacar.common;

import com.google.firebase.auth.FirebaseAuth;

public class Auth {


    FirebaseAuth mAuth;

    public Auth(){
        // Firebase
        mAuth = FirebaseAuth.getInstance();
    }

    static Auth instance;
    public static Auth getInstance(){
        if (instance == null){
            instance = new Auth();
        }
        return instance;
    }

    public void logout() {
        mAuth.signOut();
    }


}
