package com.pyozer.tankyou;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import java.util.Random;

public class Obstacle extends Objet {

    float mVitesse;

    public Obstacle(Context context) {
        super(context);
        mVitesse = 1 + (new Random().nextFloat()) * (2.5f - 1);
    }

    public boolean isOutOfScreen( float mVerticalBound) {
        if (mPosY + getHeight() > mVerticalBound) {
            Log.e("OUT SCREEN", "TRUE");
            return true;
        }
        return false;
    }

}