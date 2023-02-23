package com.example.calmacar;

public class User {
    private String uid, firstname, lastname;

    public String getUid() {
        return uid;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public User(String uid, String firstname, String lastname){
        this.uid = uid;
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
