package com.example.odoo_android_async.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Time {
    public static int getCurrentDayNumber(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.DAY_OF_WEEK) - 2;
    }

    public static String formatDate(String hour){
        try{
            String [] parts = hour.split(":");
            return new SimpleDateFormat("yyyy-MM-dd").format(new Date())
                    + " " + (parts[0].length() == 1 ? "0" + parts[0] : parts[0])
                    + ":" + (parts[1].length() == 1 ? "0" + parts[1] : parts[1]) + ":00";
        }catch (Exception e){
            return "Error en el formato de la fecha";
        }
    }
}
