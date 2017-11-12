package com.pyozer.tankyou.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;
import com.pyozer.tankyou.R;
import com.pyozer.tankyou.model.UserScore;
import com.pyozer.tankyou.util.PrefUserManager;
import com.pyozer.tankyou.view.ScoreHolder;

/**
 * Activity de base pour afficher les scores
 */
public abstract class BaseScoreActivity extends BaseActivity {

    // Elements de la recyclerview
    private FirebaseRecyclerAdapter mAdapter;
    private RecyclerView mRecycler;

    private ProgressBar mLoader;

    protected PrefUserManager prefUserManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        findViewById(R.id.scores_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mRecycler = findViewById(R.id.scores_list);
        mLoader = findViewById(R.id.scores_loader);

        prefUserManager = new PrefUserManager(this);

        // Charges les scores
        populateScoreData();
    }

    // Permet aux activity de score de définir leur propre requete pour Firebase
    protected abstract Query getQuery();

    // Charge les scores de Firebase dans la recyclerview
    private void populateScoreData() {
        FirebaseRecyclerOptions<UserScore> options = new FirebaseRecyclerOptions.Builder<UserScore>()
                .setQuery(getQuery(), UserScore.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<UserScore, ScoreHolder>(options) {
            @Override
            public ScoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.rankscore_row, parent, false);

                return new ScoreHolder(view);
            }

            @Override
            protected void onBindViewHolder(ScoreHolder holder, int position, UserScore model) {
                holder.bind(model);
            }

            @Override
            public void onDataChanged() {
                mLoader.setVisibility(View.GONE);
            }
        };

        // Ajoute les élément par le haut pour trier de manière décroissant
        // Firebase ne permet pas le trie descroissant)
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}
