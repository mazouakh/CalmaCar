package com.example.calmacar.common;

import android.icu.text.DecimalFormat;
import android.icu.text.DecimalFormatSymbols;
import android.icu.text.NumberFormat;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    public String formatDate(int year, int month, int day){

        String yearText = String.valueOf(year);
        String monthText = monthIndexToText(month);
        String dayText = day < 10 ? "0" + day : String.valueOf(day);


        return dayText + " " + monthText + " " + yearText;
    }

    public String formatTime(int hours, int minutes) {
        String hoursText = hours < 10 ? "0" + hours : String.valueOf(hours);
        String minutesText = minutes < 10 ? "0" + minutes : String.valueOf(minutes);

        return hoursText + ":" + minutesText;
    }

    public String monthIndexToText(int idx){
        switch (idx){
            case 0:
                return "JAN";
            case 1:
                return "FEV";
            case 2:
                return "MAR";
            case 3:
                return "AVR";
            case 4:
                return "MA";
            case 5:
                return "JUN";
            case 6:
                return "JUL";
            case 7:
                return "AUG";
            case 8:
                return "SEP";
            case 9:
                return "OCT";
            case 10:
                return "NOV";
            case 11:
                return "DEC";
            default:
                return "N/A";
        }
    }

    public int timeToInt(String time){
        String timeText = time.replace(":", "");
        return Integer.parseInt(timeText);
    }
}
