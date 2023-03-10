package com.example.calmacar.common;

public class Balance {

    private static Balance instance;
    private float value;

    private Balance() {
        value = 0;
    }

    public static Balance getInstance(){
        if (instance == null)
            instance = new Balance();
        return instance;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
