package com.example.odoo_android_async.model;

public class Authentication {
    public String jsonrpc;
    public Params params;

    public Authentication(String jsonrpc, Params params){
        this.jsonrpc = jsonrpc;
        this.params = params;
    }

    public static class Params {
        public String db;
        public String login;
        public String password;

        public Params(String db, String login, String password){
            this.db = db;
            this.login = login;
            this.password = password;
        }
    }
}
