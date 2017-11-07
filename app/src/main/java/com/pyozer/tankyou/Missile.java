package com.pyozer.tankyou;

import android.content.Context;

public class Missile extends Objet {

    float mVitesse = 7f;
    double angleRadian;

    public Missile(Context context, float angleDegreeDirection) {
        super(context);

        angleRadian = Math.toRadians(angleDegreeDirection);
    }

    public void updatePosMissile() {
        mPosX += Math.cos(angleRadian) * mVitesse;
        mPosY += Math.sin(angleRadian) * mVitesse;
    }

    public boolean isOutOfScreen(float mHorizontalBound, float mVerticalBound) {
        if (mPosX > mHorizontalBound) {
            return true;
        } else if (mPosX < 0) {
            return true;
        }
        if (mPosY > mVerticalBound) {
            return true;
        } else if (mPosY < 0) {
            return true;
        }
        return false;
    }

}