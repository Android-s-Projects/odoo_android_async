package com.example.odoo_android_async.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.odoo_android_async.R;
import com.example.odoo_android_async.model.Authentication;
import com.example.odoo_android_async.model.AuthenticationData;
import com.example.odoo_android_async.model.Timetable;
import com.example.odoo_android_async.services.GETHoursDay;
import com.example.odoo_android_async.services.POSTAuthenticate;
import com.example.odoo_android_async.util.Time;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText user = findViewById(R.id.txtUsername);
        final EditText pass = findViewById(R.id.txtPassword);
        final Button btLogin = findViewById(R.id.btLogin);
        final TextView info = findViewById(R.id.tvInfo);
        final TextView dbName = findViewById(R.id.tvDbName);

        btLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://54.160.229.76:8069/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                POSTAuthenticate service = retrofit.create(POSTAuthenticate.class);

                Authentication auth = new Authentication("2.0", new Authentication.Params(
                        "odoo14", user.getText().toString(), pass.getText().toString()));
                Call<AuthenticationData> repos = service.authenticate(auth);

                login(repos, retrofit, info, dbName);
            }
        });
    }

    private void login(Call<AuthenticationData> repos, Retrofit retrofit, TextView info, TextView dbName) {
        repos.enqueue(new Callback<AuthenticationData>() {
            @Override
            public void onResponse(Call<AuthenticationData> call, Response<AuthenticationData> response) {
                if (response.isSuccessful()) {
                    AuthenticationData userData = response.body();

                    if (userData.result != null) {
                        String session_id = response.headers().values("Set-Cookie")
                                .get(0).split(";")[0].split("=")[1];
                        GETHoursDay service = retrofit.create(GETHoursDay.class);
                        Call<List<Timetable>> repos = service.getHours(
                                "session_id=" + session_id + ";",
                                Integer.valueOf(userData.result.uid), Time.getCurrentDayNumber());
                        getHours(repos, session_id, userData, info);
                    } else {
                        info.setText("Usuario o Contraseña incorrectos");
                    }
                } else {
                    info.setText("Usuario o Contraseña incorrectos");
                }
            }

            @Override
            public void onFailure(Call<AuthenticationData> call, Throwable t) {
                info.setText("Error de autenticacion, Inténtelo más tarde");
            }
        });
    }

    private void getHours(Call<List<Timetable>> repos, String session_id, AuthenticationData userData, TextView info) {
        repos.enqueue(new Callback<List<Timetable>>() {
            @Override
            public void onResponse(Call<List<Timetable>> call, Response<List<Timetable>> response) {
                if (response.isSuccessful()) {
                    List<Timetable> times = response.body();
                    if(times != null){
                        String hours = "";
                        for (Timetable t :times) {
                            hours += t.hour_from + "-" + t.hour_to + " y de ";
                        }
                        hours += ".";
                        hours = hours.replace(" y de .", " ");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("session_id", session_id);
                        intent.putExtra("db", userData.result.db);
                        intent.putExtra("uid", userData.result.uid);
                        intent.putExtra("hours", hours);
                        startActivity(intent);

                    }else{
                        info.setText("Error al obtener el horario");
                    }
                } else {
                    info.setText("No ha sido posible obtener el horario");
                }
            }

            @Override
            public void onFailure(Call<List<Timetable>> call, Throwable t) {

            }
        });
    }
}