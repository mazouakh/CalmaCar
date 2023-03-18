package com.example.calmacar.utils;

import android.icu.text.DecimalFormat;
import android.icu.text.DecimalFormatSymbols;
import android.icu.text.NumberFormat;

import java.security.SecureRandom;

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

    /**
     * convert a date in the format "01 JAN 2023" to ints
     * @param date
     * @return an array where
     * element 0 is the year
     * element 1 is the month
     * element 2 is the day
     */
    public int[] splitDateToInts(String date){
        String[] dateElements= date.split(" ");
        int[] dateInts = new int[dateElements.length];

        // year
        dateInts[0] = Integer.parseInt(dateElements[2]);
        // month
        dateInts[1] = monthTextToIndex(dateElements[1]);
        // day
        dateInts[2] = Integer.parseInt(dateElements[0]);

        return  dateInts;
    }

    public String formatTime(int hours, int minutes) {
        String hoursText = hours < 10 ? "0" + hours : String.valueOf(hours);
        String minutesText = minutes < 10 ? "0" + minutes : String.valueOf(minutes);

        return hoursText + ":" + minutesText;
    }

    public int[] splitTimeToInts(String time) {
        String[] timeElements= time.split(":");
        int[] timeInts = new int[timeElements.length];

        // hours
        timeInts[0] = Integer.parseInt(timeElements[0]);
        // minutes
        timeInts[1] = Integer.parseInt(timeElements[1]);

        return  timeInts;
    }

    public int timeToInt(String time){
        String timeText = time.replace(":", "");
        return Integer.parseInt(timeText);
    }

    public String capitalize(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private String monthIndexToText(int idx){
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

    private int monthTextToIndex(String month){
        switch (month){
            case "JAN":
                return 0;
            case "FEV":
                return 1;
            case "MAR":
                return 2;
            case "AVR":
                return 3;
            case "MAI":
                return 4;
            case "JUN":
                return 5;
            case "JUL":
                return 6;
            case "AUG":
                return 7;
            case "SEP":
                return 8;
            case "OCT":
                return 9;
            case "NOV":
                return 10;
            case "DEC":
                return 11;
            default:
                return -1;
        }
    }
}
