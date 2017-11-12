package com.pyozer.tankyou.util;

import java.util.Random;

/**
 * Classe contenant des fonctions utilisable partout
 */
public class FunctionsUtil {

    /**
     * Génère un entier aléatoire entre 0 et max
     * @param max Valeur maximum
     * @return int - Entier aléatoire
     */
    public static int randInt(int max) {
        return randInt(0, max);
    }

    /**
     * Génère un entier aléatoire entre deux bornes [min, max]
     * @param min Valeur minimale
     * @param max Valeur maximale
     * @return int - Entier aléatoire
     */
    public static int randInt(int min, int max) {
        return min + new Random().nextInt((max - min) + 1);
    }

    /**
     * Génère un float aléatoire entre deux bornes [min, max]
     * @param min Valeur minimale
     * @param max Valeur maximale
     * @return int - Float aléatoire
     */
    public static float randFloat(float min, float max) {
        return min + new Random().nextFloat() * (max - min);
    }

    /**
     * Transforme un timestamp en Il y a x j/h/min/sec
     * @param date Timestamp de la date
     * @return String - Date formaté
     */
    public static String getDateFormated(long date) {
        // On calcule l'écart entre maintenant et la date
        long ecart = (System.currentTimeMillis() - date) / 1000;
        // On vérifie au cas ou que ça soit pas négatif
        if(ecart < 0) ecart = 0;

        String value;
        // Si l'écart est en
        if (ecart < 60) // Secondes
            value = (int) ecart + "sec";
        else if (ecart < 3600) // Minutes
            value = (int) Math.floor(ecart / 60) + "min";
        else if (ecart < 86400) // Heures
            value = (int) Math.floor(ecart / 3600) + "h";
        else // Jours
            value = (int) Math.floor(ecart / 86400) + "j";

        return "Il y a " + value;
    }
}
