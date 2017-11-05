package com.pyozer.tankyou;

import android.content.Context;
import android.view.View;

public abstract class Objet extends View {

    float mPosX;
    float mPosY;

    public Objet(Context context) {
        super(context);
    }

    /*
     * Resolving constraints and collisions with the Verlet integrator
     * can be very simple, we simply need to move a colliding or
     * constrained particle in such way that the constraint is
     * satisfied.
     */
    public abstract void resolveCollisionWithBounds(float mHorizontalBound, float mVerticalBound);

    @Override
    public void setX(float x) {
      mPosX = x;
      super.setX(mPosX);
    }

    @Override
    public void setY(float y) {
        mPosY = y;
        super.setY(mPosY);
    }
}