package com.pyozer.tankyou.model;

import android.content.Context;

/**
 * Classe Tank
 */
public class Tank extends Objet {

    // Orientation du tank
    public float mDegre = 0;

    public Tank(Context context) {
        super(context);
    }

    /**
     * Met à jour la position du tank selon son orientation et si il doit avancer ou reculer
     * @param sy
     * @param angle
     */
    public void computePhysics(float sy, float angle) {
        mDegre = angle;

        mPosX += Math.cos(Math.toRadians(mDegre)) * sy;
        mPosY += Math.sin(Math.toRadians(mDegre)) * sy;
    }

    /**
     * On ajuste la position du tank si celui-ci sort de l'ecran
     * @param mHorizontalBound Taille max horizontale
     * @param mVerticalBound Taille max vertical
     */
    public void resolveCollisionWithBounds(float mHorizontalBound, float mVerticalBound) {
        // On prend en compte la taille du tank comme sa position est celle du coin en haut à gauche
        if (mPosX > mHorizontalBound - getWidth()) { // Si il est sortie à droite
            mPosX = mHorizontalBound - getWidth();
        } else if (mPosX < 0) { // Si il est sortie à gauche
            mPosX = 0;
        }
        if (mPosY > mVerticalBound - getHeight()) { // Si il est sortie en bas
            mPosY = mVerticalBound - getHeight();
        } else if (mPosY < 0) { // Si il est sortie en haut
            mPosY = 0;
        }
    }
}