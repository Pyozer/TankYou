package com.pyozer.tankyou.model;

import android.content.Context;
import android.view.View;

public abstract class Objet extends View {

    public float mPosX;
    public float mPosY;

    public Objet(Context context) {
        super(context);
        setLayerType(LAYER_TYPE_HARDWARE, null);
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