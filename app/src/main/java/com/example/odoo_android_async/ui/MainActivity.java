package com.example.odoo_android_async.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.odoo_android_async.R;
import com.example.odoo_android_async.model.Attendance;
import com.example.odoo_android_async.model.Result;
import com.example.odoo_android_async.model.User;
import com.example.odoo_android_async.services.GETEmployee;
import com.example.odoo_android_async.services.POSTAttendance;
import com.example.odoo_android_async.util.Time;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.tb_logout);
        setSupportActionBar(toolbar);

        final TextView hours = findViewById(R.id.tvHoursToday);
        final TextView infoMain = findViewById(R.id.tvInfoMain);
        final TextView dbName = findViewById(R.id.tvDbName);

        hours.setText(getIntent().getStringExtra("hours"));
        String db = getIntent().getStringExtra("db");
        dbName.setText("Base de Datos: " + db);

        final FloatingActionButton add = findViewById(R.id.btAddHour);
        final EditText begin = findViewById(R.id.txBegin);
        final EditText finish = findViewById(R.id.txFinish);
        final Button validate = findViewById(R.id.btValidate);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHour(begin, finish);
            }
        });

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://54.160.229.76:8069/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                POSTAttendance service = retrofit.create(POSTAttendance.class);
                GETEmployee serviceEmployee = retrofit.create(GETEmployee.class);

                String sessionId = getIntent().getStringExtra("session_id");
                String check_in = Time.formatDate(begin.getText().toString());
                String check_out = Time.formatDate(finish.getText().toString());

                Call<User> reposEmployeeId = serviceEmployee.getEmployeeId("session_id=" + sessionId + ";", Integer.valueOf(getIntent().getStringExtra("uid")));
                reposEmployeeId.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Attendance attendance = new Attendance(check_in, check_out, response.body().employee_id, 1);
                        Call<Result> repos = service.attendance("session_id=" + sessionId + ";", attendance);
                        getResult(repos, retrofit, infoMain, begin, finish);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        infoMain.setText("Error al conseguir el employee Id");
                    }
                });


                //console.setText((CharSequence) reposEmployeeId);
            }

            private void getResult(Call<Result> repos, Retrofit retrofit, TextView infoMain, EditText begin, TextView finish) {
                repos.enqueue(new Callback<Result>() {
                    @Override
                    public void onResponse(Call<Result> call, Response<Result> response) {
                        if (response.isSuccessful()) {
                            begin.setText("");
                            finish.setText("");
                            infoMain.setText("Marcajes del dia correctos");
                        } else {
                            infoMain.setText("Error al introducir marcaje");
                        }
                    }

                    @Override
                    public void onFailure(Call<Result> call, Throwable t) {
                        infoMain.setText("Error al introducir marcaje");
                    }
                });
            }
        });
    }

    private void showHour(EditText begin, EditText finish) {
        Calendar now = Calendar.getInstance();
        String hour = String.valueOf(now.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(now.get(Calendar.MINUTE));

        if (begin.getText().toString().isEmpty()) {
            begin.setText(hour);
            return;
        } else {
            finish.setText(hour);
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}