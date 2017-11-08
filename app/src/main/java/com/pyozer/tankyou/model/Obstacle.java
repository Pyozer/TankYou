package com.pyozer.tankyou.model;

import android.content.Context;

import java.util.Random;

public class Obstacle extends Objet {

    public final static int[] FROM_TOP = {0, 1};
    public final static int[] FROM_BOTTOM = {0, -1};
    public final static int[] FROM_LEFT = {1, 0};
    public final static int[] FROM_RIGHT = {-1, 0};

    public int[] orientation;
    public int orientationNum;

    public float mVitesse;
    public double angleRadian;

    public Obstacle(Context context) {
        super(context);

        mVitesse = 1.5f + (new Random().nextFloat()) * (2.5f - 1.5f);
        angleRadian = Math.toRadians(45 + (new Random().nextFloat()) * (135 - 45));

        orientationNum = (int) (1 + (new Random().nextFloat()) * (4 - 1));
        switch (orientationNum) {
            case 1:
                orientation = FROM_TOP.clone();
                break;
            case 2:
                orientation = FROM_RIGHT.clone();
                break;
            case 3:
                orientation = FROM_BOTTOM.clone();
                break;
            case 4:
                orientation = FROM_LEFT.clone();
                break;
        }
    }

    public void updatePosObstacle() {
        mPosX += orientation[0] * Math.cos(angleRadian) * mVitesse;
        mPosY += orientation[1] * Math.sin(angleRadian) * mVitesse;
    }

}