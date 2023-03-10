package com.example.calmacar.common;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class PickerManager {

    private static PickerManager instance;

    private PickerManager(){
    }

    public static PickerManager getInstance(){
        if (instance == null){
            instance = new PickerManager();
        }
        return instance;
    }

    public void displayDatePicker(Context ctx, Button btn_datePicker){
        DatePickerDialog dialog = new DatePickerDialog(
                ctx,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        btn_datePicker.setText(formatDate(year, month, day));
                    }
                },
                2023, 10-1, 19);
        dialog.show();
    }

    public void displayTimePicker(Context ctx, Button btn_timePicker){
        TimePickerDialog dialog = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                btn_timePicker.setText(formatTime(hours, minutes));
            }
        }, 00, 00, true);
        dialog.show();
    }

    private String formatTime(int hours, int minutes) {
        String hoursText = hours < 10 ? "0" + hours : String.valueOf(hours);
        String minutesText = minutes < 10 ? "0" + minutes : String.valueOf(minutes);

        return hoursText + ":" + minutesText;
    }

    private String formatDate(int year, int month, int day){

        String yearText = String.valueOf(year);
        String monthText = monthIndexToText(month);
        String dayText = day < 10 ? "0" + day : String.valueOf(day);


        return dayText + " " + monthText + " " + yearText;
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
}
