package com.pyozer.tankyou.model;

import java.util.HashMap;
import java.util.Map;

public class RankScore {

    private int score;
    private String user;

    public RankScore() {}

    public RankScore(int score, String user) {
        this.score = score;
        this.user = user;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Map<String,Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("score", score);
        values.put("user", user);

        return values;
    }
}
