package com.pyozer.tankyou.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Activity de base
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static boolean isInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // On met l'activity en mode Fullscreen
        View decorView = getWindow().getDecorView();
        // Cache la status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // On défini le mode persistant de Firebase
        try{
            if(!isInitialized){
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                isInitialized = true;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
