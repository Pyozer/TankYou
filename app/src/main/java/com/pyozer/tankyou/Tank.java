package com.pyozer.tankyou;

import android.content.Context;

public class Tank extends Objet {

    float mDegre = 0;

    public Tank(Context context) {
        super(context);
    }

    public void computePhysics(float sy, float angle) {
        mDegre = angle;

        mPosX += Math.cos(Math.toRadians(mDegre)) * sy;
        mPosY += Math.sin(Math.toRadians(mDegre)) * sy;
    }
}