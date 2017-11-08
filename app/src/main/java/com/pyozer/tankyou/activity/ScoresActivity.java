package com.pyozer.tankyou.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.pyozer.tankyou.R;
import com.pyozer.tankyou.model.RankScore;
import com.pyozer.tankyou.view.RankScoreHolder;

public class ScoresActivity extends BaseActivity {

    private FirebaseRecyclerAdapter mAdapter;
    private RecyclerView mRecycler;

    private ProgressBar mLoader;

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

        populateScoreData();
    }

    private void populateScoreData() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("scores").orderByChild("score").limitToLast(50);

        FirebaseRecyclerOptions<RankScore> options = new FirebaseRecyclerOptions.Builder<RankScore>()
                .setQuery(query, RankScore.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<RankScore, RankScoreHolder>(options) {
            @Override
            public RankScoreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.rankscore_row, parent, false);

                return new RankScoreHolder(view);
            }

            @Override
            protected void onBindViewHolder(RankScoreHolder holder, int position, RankScore model) {
                holder.bind(model);
            }

            @Override
            public void onDataChanged() {
                mLoader.setVisibility(View.GONE);
            }
        };

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
