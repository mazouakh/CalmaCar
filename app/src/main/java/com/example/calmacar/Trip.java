package com.example.calmacar;

import android.content.Intent;

import java.security.SecureRandom;

public class Trip {
    String id, startCity, endCity, date, startTime, endTime, description;
    float price;

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getStartCity() {
        return startCity;
    }

    public String getEndCity() {
        return endCity;
    }

    public Trip() {}


    public Trip(String startCity, String endCity, String date, String startTime, String endTime, String price, String description) {
        this.id = generateID(5);
        this.startCity = startCity;
        this.endCity = endCity;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = Float.parseFloat(price.replace("€", ""));
        this.description = description;
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

    @Override
    public String toString() {
        return "Trip{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", startCity='" + startCity + '\'' +
                ", endCity='" + endCity + '\'' +
                '}';
    }
}
