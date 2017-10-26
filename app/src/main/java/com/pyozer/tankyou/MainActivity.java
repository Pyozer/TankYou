package com.pyozer.tankyou;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends BaseActivity {

    private EditText mInputPseudo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInputPseudo = (EditText) findViewById(R.id.home_input_pseudo);

        Button mButtonStart = (Button) findViewById(R.id.home_button_start);
        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToGame();
            }
        });
    }

    private void goToGame() {
        mInputPseudo.setError(null);

        String pseudo = mInputPseudo.getText().toString().trim();

        if(TextUtils.isEmpty(pseudo)) {
            mInputPseudo.setError(getString(R.string.field_required));
        } else {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra(AppConfig.EXTRA_PSEUDO, pseudo);
            startActivity(intent);
        }
    }
}
