package com.example.odoo_android_async.model;

public class AuthenticationData {
    public String jsonrpc;
    public Object id;
    public Result result;

    public static class Result{
        public String uid;
        public String name;
        public String username;
        public String db;
    }
}
