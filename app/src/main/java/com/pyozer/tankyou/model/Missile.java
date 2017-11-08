package com.pyozer.tankyou.model;

import android.content.Context;

public class Missile extends Objet {

    public float mVitesse = 8f;
    public double angleRadian;

    public Missile(Context context, float angleDegreeDirection) {
        super(context);

        angleRadian = Math.toRadians(angleDegreeDirection);
    }

    public void updatePosMissile() {
        mPosX += Math.cos(angleRadian) * mVitesse;
        mPosY += Math.sin(angleRadian) * mVitesse;
    }

}