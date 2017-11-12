package com.pyozer.tankyou.model;

import android.content.Context;

import com.pyozer.tankyou.util.FunctionsUtil;

import java.util.Random;

public class Obstacle extends Objet {

    public final static int[] FROM_TOP = {0, 1};
    public final static int[] FROM_BOTTOM = {0, -1};

    public int[] orientation;
    public int orientationNum;

    public float mVitesse;
    public double angleRadian;

    public Obstacle(Context context) {
        super(context);

        mVitesse = FunctionsUtil.randFloat(1.5f, 2.5f);
        angleRadian = Math.toRadians(FunctionsUtil.randFloat(-45f, 45f));

        orientationNum = FunctionsUtil.randInt(1, 2);
        switch (orientationNum) {
            case 1:
                orientation = FROM_TOP.clone();
                break;
            case 2:
                orientation = FROM_BOTTOM.clone();
                break;
        }
    }

    public void updatePosObstacle() {
        mPosX += orientation[0] * Math.cos(angleRadian) * mVitesse;
        mPosY += orientation[1] * Math.sin(angleRadian) * mVitesse;
    }

}