package com.pyozer.tankyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Toast;

public class RoomActivity extends BaseActivity {

    private String pseudoUser;

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        Intent intent = getIntent();
        pseudoUser = intent.getStringExtra(AppConfig.EXTRA_PSEUDO);

        if(TextUtils.isEmpty(pseudoUser))
            returnToHome();

        Toast.makeText(this, "Pseudo: " + pseudoUser, Toast.LENGTH_LONG).show();


    }

    private void returnToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(AppConfig.EXTRA_ERROR, getString(R.string.no_pseudo_get));
        startActivity(intent);
        finish();
    }
}
