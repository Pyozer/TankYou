package com.pyozer.tankyou;

import android.content.Context;
import android.view.View;

public abstract class Objet extends View {

    float mPosX = 500;
    float mPosY = 500;

    public Objet(Context context) {
        super(context);
    }

    /*
     * Resolving constraints and collisions with the Verlet integrator
     * can be very simple, we simply need to move a colliding or
     * constrained particle in such way that the constraint is
     * satisfied.
     */
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