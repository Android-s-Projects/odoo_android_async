package com.example.odoo_android_async.services;

import com.example.odoo_android_async.model.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface GETEmployee {
    @GET("/api/employee/{userId}")
    public Call<User> getEmployeeId(@Header("Cookie") String sessionId,
                                    @Path("userId") int userId);
}
