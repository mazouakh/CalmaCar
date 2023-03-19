package com.example.calmacar.common.model;

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

    /**
     * Empty constructor for Firebase
     * */
    public User(){};

    public User(String uid, String firstname, String lastname, String phone, String userType){
        this.uid = uid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.userType = userType;
        this.phone = phone;
    }

    /**
     * Updated version with no user type since a user can be both passenger and driver with
     * the same account
     * @param uid
     * @param firstname
     * @param lastname
     * @param phone
     */
    public User(String uid, String firstname, String lastname, String phone){
        this.uid = uid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.userType = userType;
        this.phone = phone;
    }
}
