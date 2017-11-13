package com.pyozer.tankyou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // On redirige directement vers notre menu
        startActivity(new Intent(this, MainActivity.class));
        finish();

    }
}
