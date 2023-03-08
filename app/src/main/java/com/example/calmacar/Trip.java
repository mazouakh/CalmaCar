package com.example.calmacar;

import java.security.SecureRandom;

public class Trip {
    String id, date, departCity, arriverCity;

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getDepartCity() {
        return departCity;
    }

    public String getArriverCity() {
        return arriverCity;
    }

    public Trip() {}

    public Trip(String date, String depart_city, String arriver_city) {
        this.id = generateID(5);
        this.date = date;
        this.departCity = depart_city;
        this.arriverCity = arriver_city;
    }

    private static String generateID(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }
}
