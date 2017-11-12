package com.pyozer.tankyou.view;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.pyozer.tankyou.R;
import com.pyozer.tankyou.model.UserScore;

public class ScoreHolder extends RecyclerView.ViewHolder {

    private TextView mUserField;
    private TextView mDureeField;
    private TextView mScoreField;

    public ScoreHolder(View itemView) {
        super(itemView);
        mUserField = itemView.findViewById(R.id.rankscore_user);
        mDureeField = itemView.findViewById(R.id.rankscore_duree);
        mScoreField = itemView.findViewById(R.id.rankscore_score);
    }

    public void bind(UserScore rankScore) {
        if(rankScore.getUser() == null || TextUtils.isEmpty(rankScore.getUser())) {
            mUserField.setText(rankScore.getDateFormat());
        } else {
            mUserField.setText(rankScore.getUser());
        }
        mDureeField.setText(String.valueOf(rankScore.getDuree()));
        mScoreField.setText(String.valueOf(rankScore.getScore()));
    }

}