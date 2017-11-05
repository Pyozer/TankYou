package com.pyozer.tankyou;

import android.content.Context;
import android.view.ViewGroup;

import java.util.Random;

public class Obstacle extends Objet {

    float mVitesse;

    public Obstacle(Context context) {
        super(context);

        mVitesse = new Random().nextFloat();
    }

    @Override
    public void resolveCollisionWithBounds(float mHorizontalBound, float mVerticalBound) {
        if (mPosY > mVerticalBound - getHeight()) {
            ((ViewGroup) getParent()).removeView(this);
        }
    }

}