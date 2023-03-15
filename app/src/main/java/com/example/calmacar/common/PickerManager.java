package com.example.calmacar.common;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PickerManager {

    private static PickerManager instance;
    private final TimeManager mTimeManager;
    private Formatter mFormatter;

    private PickerManager(){
        mFormatter = Formatter.getInstance();
        mTimeManager = TimeManager.getInstance();
    }

    public static PickerManager getInstance(){
        if (instance == null){
            instance = new PickerManager();
        }
        return instance;
    }

    /**
     * Initialize the text of the date picker button to the current local date
     * @param btn_datePicker
     */
    public void initializeDatePickerButton(Button btn_datePicker){
        // get current date
        int year = mTimeManager.getYear();
        int month = mTimeManager.getMonth();
        int day = mTimeManager.getDay();

        // set the btn text
        btn_datePicker.setText(mFormatter.formatDate(year, month, day));
    }

    /**
     * Initialize the text of the time picker button to the current local time
     * @param btn_timePicker
     */
    public void initializeTimePickerButton(Button btn_timePicker){
        // get current date
        int hours = mTimeManager.getHour();
        int minutes = mTimeManager.getMinute();

        // set the btn text
        btn_timePicker.setText(mFormatter.formatTime(hours, minutes));
    }

    public void displayDatePicker(Context ctx, Button btn_datePicker){
        DatePickerDialog dialog = new DatePickerDialog(
                ctx,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        btn_datePicker.setText(mFormatter.formatDate(year, month, day));
                    }
                },
                mTimeManager.getYear(), mTimeManager.getMonth(), mTimeManager.getDay());

        dialog.show();
    }

    public void displayTimePicker(Context ctx, Button btn_timePicker){
        TimePickerDialog dialog = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {
                btn_timePicker.setText(mFormatter.formatTime(hours, minutes));
            }
        }, mTimeManager.getHour(), mTimeManager.getMinute(), true);
        dialog.show();
    }
}
