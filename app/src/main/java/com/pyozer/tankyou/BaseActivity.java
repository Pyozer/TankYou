package com.pyozer.tankyou;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
