package com.pyozer.tankyou.model;

import android.content.Context;
import android.view.View;

public abstract class Objet extends View {

    // Position de l'objet
    public float mPosX;
    public float mPosY;

    public Objet(Context context) {
        super(context);
        setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    /**
     * Méthode pour défini la position X de notre objet
     * @param x
     */
    @Override
    public void setX(float x) {
      mPosX = x;
      super.setX(mPosX);
    }

    /**
     * Méthode pour défini la position Y de notre objet
     * @param y
     */
    @Override
    public void setY(float y) {
        mPosY = y;
        super.setY(mPosY);
    }

    /**
     * Vérifie si l'objet sort de l'écran
     * @param mHorizontalBound Taille horizontale de l'écran
     * @param mVerticalBound Taille vertical de l'écran
     * @return boolean
     */
    public boolean isOutOfScreen(float mHorizontalBound, float mVerticalBound) {
        if (mPosX > mHorizontalBound) { // Si il est sortie à droite
            return true;
        } else if (mPosX < 0) { // Si il est sortie à gauche
            return true;
        }
        if (mPosY > mVerticalBound) { // Si il est sortie en bas
            return true;
        } else if (mPosY < 0) { // Si il est sortie en haut
            return true;
        }
        return false;
    }
}