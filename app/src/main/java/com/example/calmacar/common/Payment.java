package com.example.calmacar.common;

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
        this.id =  formatter.getDateTime() + formatter.generateID(5);
        this.amount = amount;
    }
}
