package com.example.proyectodammiguel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class IntroActivity extends AppCompatActivity {

    Button buttonLogin;
    Button buttonRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}