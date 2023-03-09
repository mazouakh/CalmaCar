package com.example.calmacar;

import android.icu.text.DecimalFormat;
import android.icu.text.DecimalFormatSymbols;
import android.icu.text.NumberFormat;

public class Formatter {

    private static Formatter instance;
    private NumberFormat priceFormatter;

    private Formatter(){
        // Creating a price formatter with € currency symbol
        priceFormatter = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) priceFormatter).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol("€");
        ((DecimalFormat) priceFormatter).setDecimalFormatSymbols(decimalFormatSymbols);
    }

    public static Formatter getInstance(){
        if (instance == null)
            instance = new Formatter();
        return instance;
    }

    public String formatPrice(String price){
        return priceFormatter.format(Float.parseFloat(price)/100);
    }
}
