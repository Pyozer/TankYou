package com.pyozer.tankyou.model;

import android.content.Context;

public class Tank extends Objet {

    public float mDegre = 0;

    public Tank(Context context) {
        super(context);
    }

    public void computePhysics(float sy, float angle) {
        mDegre = angle;

        mPosX += Math.cos(Math.toRadians(mDegre)) * sy;
        mPosY += Math.sin(Math.toRadians(mDegre)) * sy;
    }

    public void resolveCollisionWithBounds(float mHorizontalBound, float mVerticalBound) {
        if (mPosX > mHorizontalBound - getWidth()) {
            mPosX = mHorizontalBound - getWidth();
        } else if (mPosX < 0) {
            mPosX = 0;
        }
        if (mPosY > mVerticalBound - getHeight()) {
            mPosY = mVerticalBound - getHeight();
        } else if (mPosY < 0) {
            mPosY = 0;
        }
    }
}