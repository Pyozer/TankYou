package com.pyozer.tankyou.model;

import android.content.Context;

/**
 * Classe Missile
 */
public class Missile extends Objet {

    public float mVitesse = 8f;
    public double angleRadian;

    public Missile(Context context, float angleDegreeDirection) {
        super(context);

        // Défini l'angle du missile selon celui donnée en paramètre (tank)
        angleRadian = Math.toRadians(angleDegreeDirection);
    }

    /**
     * On met à jour la position du missile selon son angle et vitesse
     */
    public void updatePosMissile() {
        mPosX += Math.cos(angleRadian) * mVitesse;
        mPosY += Math.sin(angleRadian) * mVitesse;
    }

}