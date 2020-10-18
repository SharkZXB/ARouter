package com.sharkz.logout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sharkz.annotation.Route;

@Route(path = "/logout/logout")
public class LogoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
    }
}