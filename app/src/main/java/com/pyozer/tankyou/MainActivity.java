package com.pyozer.tankyou;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.jouer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });
        findViewById(R.id.scores).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ScoresActivity.class));
            }
        });
        findViewById(R.id.rules).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RulesActivity.class));
            }
        });
        findViewById(R.id.about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAboutDialog();
            }
        });
    }

    private void showAboutDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.about_dialog);
        dialog.show();
    }

}
