package com.pyozer.tankyou.model;

import android.content.Context;
import android.util.Log;

import com.pyozer.tankyou.util.FunctionsUtil;

import java.util.Random;

/**
 * Classe Obstacle
 */
public class Obstacle extends Objet {

    public final static int FROM_TOP = 1;
    public final static int FROM_BOTTOM = -1;

    public boolean isFromTop;
    public int orientation;

    public float mVitesse;
    public double angleRadian;

    public Obstacle(Context context) {
        super(context);

        // On défini une vitesse et angle aléatoire
        mVitesse = FunctionsUtil.randFloat(1.5f, 2.5f);
        angleRadian = Math.toRadians(FunctionsUtil.randFloat(70f, 110f));

        // On défini l'origine (vers le bas ou vers le haut)
        isFromTop = new Random().nextBoolean();
        if(isFromTop)
            orientation = FROM_TOP;
        else
            orientation = FROM_BOTTOM;
    }

    /**
     * Met à jour la position de l'obstacle selon son origine, angle et vitesse
     */
    public void updatePosObstacle() {
        mPosX += Math.cos(angleRadian) * mVitesse;
        mPosY += orientation * Math.sin(angleRadian) * mVitesse;
    }

}