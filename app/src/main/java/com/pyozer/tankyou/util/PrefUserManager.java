package com.pyozer.tankyou.util;

import android.content.Context;
import android.text.TextUtils;

public class PrefUserManager extends PrefManager {

    private final static String PREF_USERNAME = "username";

    public PrefUserManager(Context context) {
        super(context);
    }

    public String getUsername() {
        return mPref.getString(PREF_USERNAME, "");
    }

    public boolean isUserExists() {
        return !(TextUtils.isEmpty(getUsername()));
    }

    public void saveUsername(String username) {
        mEditor.putString(PREF_USERNAME, username);
        mEditor.apply();
    }
}
