package com.pyozer.tankyou.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.pyozer.tankyou.R;

/**
 * Activity pour afficher le Leaderboard
 */
public class ScoresActivity extends BaseScoreActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView titleText = findViewById(R.id.scores_title);
        titleText.setText(getString(R.string.scores_title));

        TextView dateText = findViewById(R.id.scores_header_1);
        dateText.setText(getString(R.string.scores_user));
    }

    /**
     * Défini notre requete pour récupéré les 50 premiers scores
     */
    @Override
    protected Query getQuery() {
        return FirebaseDatabase.getInstance()
                .getReference()
                .child("leaderboard").orderByChild("score").limitToLast(50);
    }
}
