package com.pyozer.tankyou;

import android.content.Context;

public class Cible extends Objet {

    public Cible(Context context) {
        super(context);
        mPosX = 800;
        mPosY = 1300;
    }

    @Override
    public void resolveCollisionWithBounds(float mHorizontalBound, float mVerticalBound) {
        //Nothing
    }

}