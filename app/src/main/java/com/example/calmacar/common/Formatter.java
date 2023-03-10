package com.example.calmacar.common;

import android.icu.text.DecimalFormat;
import android.icu.text.DecimalFormatSymbols;
import android.icu.text.NumberFormat;
import android.util.Log;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

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


    public String generateID(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    public String getDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        String currentTime = sdf.format(Calendar.getInstance().getTime());
        return currentTime;
    }
}
