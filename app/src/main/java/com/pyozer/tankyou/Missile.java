package com.pyozer.tankyou;

import android.content.Context;

public class Missile extends Objet {

    float mVitesse = 8f;
    double angleRadian;

    public Missile(Context context, float angleDegreeDirection) {
        super(context);

        angleRadian = Math.toRadians(angleDegreeDirection);
    }

    public void updatePosMissile() {
        mPosX += Math.cos(angleRadian) * mVitesse;
        mPosY += Math.sin(angleRadian) * mVitesse;
    }

}