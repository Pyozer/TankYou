package com.pyozer.tankyou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.pyozer.tankyou.R;

/**
 * Activity pour afficher les scores du joueurs
 */
public class StatsActivity extends BaseScoreActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView titleText = findViewById(R.id.scores_title);
        titleText.setText(getString(R.string.stats_title));

        TextView dateText = findViewById(R.id.scores_header_1);
        dateText.setText(getString(R.string.scores_date));

        Button btnReset = findViewById(R.id.scores_reset);
        btnReset.setVisibility(View.VISIBLE);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetScores();
            }
        });
    }

    /**
     * Reset les scores de l'utilisateur
     */
    private void resetScores() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("scores").child(prefUserManager.getUsername()).removeValue();
    }

    /**
     * Défini la requete Firebase pour récupérer les scores du joueur
     * @return Query
     */
    @Override
    public Query getQuery() {
        return FirebaseDatabase.getInstance()
                .getReference()
                .child("scores").child(prefUserManager.getUsername()).orderByChild("date");
    }
}
