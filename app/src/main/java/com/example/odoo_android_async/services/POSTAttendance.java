package com.example.odoo_android_async.services;

import com.example.odoo_android_async.model.Attendance;
import com.example.odoo_android_async.model.AuthenticationData;
import com.example.odoo_android_async.model.Result;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface POSTAttendance {
    @POST("api/attendance")
    public Call<Result> attendance(@Header("Cookie") String session_id, @Body Attendance attendance);
}
