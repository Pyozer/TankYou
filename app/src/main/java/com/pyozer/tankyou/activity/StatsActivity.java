package com.pyozer.tankyou.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.pyozer.tankyou.R;
import com.pyozer.tankyou.util.PrefUserManager;

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
    }

    /**
     * Défini la requete Firebase pour récupérer les scores du joueur
     * @return Query
     */
    @Override
    public Query getQuery() {
        return FirebaseDatabase.getInstance()
                .getReference()
                .child("scores").child(new PrefUserManager(this).getUsername()).orderByChild("date");
    }
}
