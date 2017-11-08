package com.pyozer.tankyou.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pyozer.tankyou.R;
import com.pyozer.tankyou.model.RankScore;

public class RankScoreHolder extends RecyclerView.ViewHolder {

    private TextView mUserField;
    private TextView mScoreField;

    public RankScoreHolder(View itemView) {
        super(itemView);
        mUserField = itemView.findViewById(R.id.rankscore_user);
        mScoreField = itemView.findViewById(R.id.rankscore_score);
    }

    public void bind(RankScore rankScore) {
        mUserField.setText(rankScore.getUser());
        mScoreField.setText(String.valueOf(rankScore.getScore()));
    }

}