package com.example.calmacar.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeManager {
    private static TimeManager instance;

    private Calendar mCalendar;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");;
    private TimeManager(){}

    public static TimeManager getInstance(){
        if (instance == null)
            instance = new TimeManager();
        return instance;
    }

    public int getYear(){
        return Integer.parseInt(sdf.format(Calendar.getInstance().getTime()).split("-")[0]);
    }

    public int getMonth(){
        return Integer.parseInt(sdf.format(Calendar.getInstance().getTime()).split("-")[1]) - 1;
    }

    public int getDay(){
        return Integer.parseInt(sdf.format(Calendar.getInstance().getTime()).split("-")[2]);
    }

    public int getHour(){
        return Integer.parseInt(sdf.format(Calendar.getInstance().getTime()).split("-")[3]);
    }

    public int getMinute(){
        return Integer.parseInt(sdf.format(Calendar.getInstance().getTime()).split("-")[4]);
    }

    /**
     * get local date & time in the format : "yyyyMMddHHmm"
     * @return
     */
    public String getDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        return sdf.format(Calendar.getInstance().getTime());
    }
}
