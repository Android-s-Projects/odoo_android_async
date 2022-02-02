package com.example.odoo_android_async.services;

import com.example.odoo_android_async.model.Timetable;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;;

public interface GETHoursDay {

    @GET("/api/attendance/hours/{userId}/{dayWeek}")
    public Call<List<Timetable>> getHours(@Header("Cookie") String sessionId,
                                          @Path("userId") int userId,
                                          @Path("dayWeek") int dayweek);
}
