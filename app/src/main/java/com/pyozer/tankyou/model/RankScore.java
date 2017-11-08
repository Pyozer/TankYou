package com.pyozer.tankyou.model;

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
}
