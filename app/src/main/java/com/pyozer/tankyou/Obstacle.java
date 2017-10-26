package com.pyozer.tankyou;

import android.content.Context;

import java.util.Random;

public class Obstacle extends Objet {

    float mVitesse;

    public Obstacle(Context context) {
        super(context);

        mVitesse = new Random().nextFloat();
    }

}