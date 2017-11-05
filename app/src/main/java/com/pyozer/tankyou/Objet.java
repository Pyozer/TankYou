package com.pyozer.tankyou;

import android.content.Context;
import android.view.View;

public abstract class Objet extends View {

    float mPosX;
    float mPosY;

    public Objet(Context context) {
        super(context);
    }

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