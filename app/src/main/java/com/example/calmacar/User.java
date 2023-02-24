package com.example.calmacar;

public class User {
    private String uid, firstname, lastname, userType;

    public String getUid() {
        return uid;
    }

    public String getUserType() {
        return userType;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public User(String uid, String firstname, String lastname, String userType){
        this.uid = uid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.userType = userType;
    }
}
