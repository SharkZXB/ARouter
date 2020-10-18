package com.sharkz.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.sharkz.annotation.Route;
import com.sharkz.annotation_api.Arouter;

@Route(path = "/login/login")
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Arouter.getInstance().jumpActivity("/logout/logout");
            }
        });
    }
}