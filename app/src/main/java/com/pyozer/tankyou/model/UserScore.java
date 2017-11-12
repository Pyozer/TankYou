package com.pyozer.tankyou.model;

import com.google.firebase.database.ServerValue;
import com.pyozer.tankyou.util.FunctionsUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe du score de l'utilisateur
 */
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

    /**
     * Renvoi la date formaté pour l'affichage (Il y a x j/h/min/sec)
     * @return
     */
    public String getDateFormat() {
        return FunctionsUtil.getDateFormated(date);
    }

    public void setDate(long date) {
        this.date = date;
    }

    /**
     * Renvoi un Map avec les valeurs du score, durée et date
     * @return Map Valeur de l'objet
     */
    public Map<String,Object> toUserStatsMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("score", score);
        values.put("duree", duree);
        values.put("date", ServerValue.TIMESTAMP);

        return values;
    }

    /**
     * Renvoi un Map avec les valeurs du score, durée, pseudo et date
     * @return Map Valeur de l'objet
     */
    public Map<String,Object> toLeaderboardMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("score", score);
        values.put("duree", duree);
        values.put("user", user);
        values.put("date", ServerValue.TIMESTAMP);

        return values;
    }
}
