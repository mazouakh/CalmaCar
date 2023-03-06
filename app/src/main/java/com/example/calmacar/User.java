package com.example.calmacar;

public class User {
    private String uid, firstname, lastname, phone, userType;

    public String getPhone() {
        return phone;
    }

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

    public User(String uid, String firstname, String lastname, String phone, String userType){
        this.uid = uid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.userType = userType;
        this.phone = phone;
    }
}
