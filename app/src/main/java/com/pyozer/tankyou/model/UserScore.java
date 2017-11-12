package com.pyozer.tankyou.model;

import com.google.firebase.database.ServerValue;
import com.pyozer.tankyou.util.FunctionsUtil;

import java.util.HashMap;
import java.util.Map;

public class UserScore {

    private String user;
    private int duree;
    private int score;
    private long date;

    public UserScore() {}

    public UserScore(String user, int duree, int score) {
        this(user, duree, score, 0);
    }

    public UserScore(String user, int duree, int score, long date) {
        this.user = user;
        this.score = score;
        this.duree = duree;
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public long getDate() {
        return date;
    }

    public String getDateFormat() {
        return FunctionsUtil.getDateFormated(date);
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Map<String,Object> toUserStatsMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("score", score);
        values.put("duree", duree);
        values.put("date", ServerValue.TIMESTAMP);

        return values;
    }

    public Map<String,Object> toLeaderboardMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("score", score);
        values.put("duree", duree);
        values.put("user", user);
        values.put("date", ServerValue.TIMESTAMP);

        return values;
    }
}
