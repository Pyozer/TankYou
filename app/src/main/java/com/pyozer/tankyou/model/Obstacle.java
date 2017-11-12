package com.pyozer.tankyou.model;

import android.content.Context;

import com.pyozer.tankyou.util.FunctionsUtil;

/**
 * Classe Obstacle
 */
public class Obstacle extends Objet {

    public final static int[] FROM_TOP = {0, 1};
    public final static int[] FROM_BOTTOM = {0, -1};

    public int[] orientation;
    public int orientationNum;

    public float mVitesse;
    public double angleRadian;

    public Obstacle(Context context) {
        super(context);

        // On défini une vitesse et angle aléatoire
        mVitesse = FunctionsUtil.randFloat(1.5f, 2.5f);
        angleRadian = Math.toRadians(FunctionsUtil.randFloat(-45f, 45f));

        // On défini l'origine (vers le bas ou vers le haut)
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

    /**
     * Met à jour la position de l'obstacle selon son origine, angle et vitesse
     */
    public void updatePosObstacle() {
        mPosX += orientation[0] * Math.cos(angleRadian) * mVitesse;
        mPosY += orientation[1] * Math.sin(angleRadian) * mVitesse;
    }

}