package com.pyozer.tankyou.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

/**
 * Classe pour gérer les préférences utilisateur
 */
public class PrefUserManager{

    // Clé pour savuegarder dans les SharedPreferences le pseudo
    private final static String PREF_USERNAME = "username";

    private static SharedPreferences mPref;
    private static SharedPreferences.Editor mEditor;

    public PrefUserManager(Context context) {
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPref.edit();
    }

    /**
     * Renvoi le pseudo sauvegardé
     * @return String - Pseudo
     */
    public String getUsername() {
        return mPref.getString(PREF_USERNAME, "");
    }

    /**
     * Vérifie si un utilisateur est déjà sauvegardé
     * @return boolean - Existe ou pas
     */
    public boolean isUserExists() {
        return !(TextUtils.isEmpty(getUsername()));
    }

    /**
     * Sauvgearde le pseudo de l'utilisateur
     * @param username Pseudo a sauvegarder
     */
    public void saveUsername(String username) {
        mEditor.putString(PREF_USERNAME, username);
        mEditor.apply();
    }
}
