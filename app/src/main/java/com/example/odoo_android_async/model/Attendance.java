package com.example.odoo_android_async.model;

public class Attendance {
    public String check_in;
    public String check_out;
    public int employee_id;
    public int department_id;

    public Attendance(String check_in, String check_out, int employee_id, int department_id){
        this.check_in = check_in;
        this.check_out = check_out;
        this.employee_id = employee_id;
        this.department_id = department_id;
    }
}
