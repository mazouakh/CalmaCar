package com.example.calmacar.driver.model;

import com.example.calmacar.utils.Formatter;
import com.example.calmacar.utils.TimeManager;

public class Payment {
    private String id;
    private float amount;

    public String getId() {
        return id;
    }

    public float getAmount() {
        return amount;
    }

    public Payment(){}

    public Payment(float amount) {
        Formatter formatter = Formatter.getInstance();
        TimeManager timeManager = TimeManager.getInstance();

        this.id =  timeManager.getDateTime() + formatter.generateID(5);
        this.amount = amount;
    }
}
