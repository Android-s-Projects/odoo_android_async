package com.example.odoo_android_async.services;

import com.example.odoo_android_async.model.Authentication;
import com.example.odoo_android_async.model.AuthenticationData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface POSTAuthenticate {
    @POST("web/session/authenticate")
    public Call<AuthenticationData> authenticate(@Body Authentication authentication);
}
