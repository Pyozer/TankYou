package com.pyozer.tankyou.util;

import java.util.Random;

public class FunctionsUtil {

    public static int randInt(int max) {
        return randInt(0, max);
    }

    public static int randInt(int min, int max) {
        return min + new Random().nextInt((max - min) + 1);
    }

    public static float randFloat(float min, float max) {
        return min + new Random().nextFloat() * (max - min);
    }

    public static String getDateFormated(long date) {

        long ecart = (System.currentTimeMillis() - date) / 1000;

        if(ecart < 0) ecart = 0;

        String value;

        if (ecart < 60)
            value = (int) ecart + "sec";
        else if (ecart < 3600)
            value = (int) Math.floor(ecart / 60) + "min";
        else if (ecart < 86400)
            value = (int) Math.floor(ecart / 3600) + "h";
        else
            value = (int) Math.floor(ecart / 86400) + "j";

        return "Il y a " + value;
    }
}
