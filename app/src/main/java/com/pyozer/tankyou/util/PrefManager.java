package com.pyozer.tankyou.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public abstract class PrefManager {

    static SharedPreferences mPref;
    static SharedPreferences.Editor mEditor;
    Context mContext;

    public PrefManager(Context context) {
        this.mContext = context;

        PrefManager.mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEditor = mPref.edit();
    }

}