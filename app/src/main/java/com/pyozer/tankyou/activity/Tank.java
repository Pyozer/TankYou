package com.pyozer.tankyou.activity;

public class Tank {

    // pointe vers JeuActivity
    JeuActivity context;

    // la position de la tank (en metre)
    float posX;
    float posY;

    // la dernière position de la tank (en metre)
    float lastPosX;
    float lastPosY;

    // l'accélération de la tank
    float accelX;
    float accelY;

    // la date à laquelle la dernière position de la tank
    // à été enregistré
    long lastT;
    float lastDeltaT;

    // friction de la tank avec la table et l'air
    float friction;

    // diameter de la tank (en metre)
    static float DIAMETRE_BILLE = 1f;

    /*
     * Constructeur
     */
    Tank(JeuActivity pcontext) {

        // intialise le context
        context = pcontext;

        // initialise la friction entre 0 et 1
        friction = 0.5f;

        // position intialle de la tank (en metre)
        posX = 0f;
        posY = 0f;

    }


    /*
     * Met à jour la position du tank en fonction des données de l'accélérometre
     */
    public void updatePosition(float pSensorX, float pSensorY, long pTimestamp) {

        if (lastT != 0) {
            // trouver l'interval de temps entre la date actuelle et la date de la dernière updatePosition
            float deltaT = (float) (pTimestamp - lastT) * (1.0f / 1000000000.0f);

            if (lastDeltaT != 0) {
                // calculer le rapport entre le delta et le dernier delta
                float rapportDelta = deltaT / lastDeltaT;

                // Utilisation de l'équation Verlet pour trouver la nouvelle position de la tank
                // x(t+dt) = x(t) + (1-f) * (x(t) - x(t-dt)) * (dt/dt_prev) + a(t) * t^2
                final float x = posX * context.mainActivityView.metersToPixelsX + (1.0f - friction) * rapportDelta * (posX - lastPosX) * context.mainActivityView.metersToPixelsX + accelX * deltaT * deltaT;
                final float y = posY * context.mainActivityView.metersToPixelsY + (1.0f - friction) * rapportDelta * (posY - lastPosY) * context.mainActivityView.metersToPixelsY + accelY * deltaT * deltaT;

                // enregistrer la position et l'accélération
                lastPosX = posX;
                lastPosY = posY;
                accelX = -pSensorX;
                accelY = -pSensorY;

                // mise à jour de la nouvelle position
                posX = x;
                posY = y;
            }
            lastDeltaT = deltaT;
        }
        lastT = pTimestamp;


        // vérifier que la tank ne sort pas des limites
        resourdreCollisionAvecBordEcran();
    }


    /*
     * Resoudre les collisions avec les bords de l'écran
     */
    public void resourdreCollisionAvecBordEcran() {
        if (posX > context.mainActivityView.xMax - DIAMETRE_BILLE) {
            posX = context.mainActivityView.xMax - DIAMETRE_BILLE;
        } else if (posX < context.mainActivityView.xMin) {
            posX = context.mainActivityView.xMin;
        }
        if (posY > context.mainActivityView.yMax - DIAMETRE_BILLE) {
            posY = context.mainActivityView.yMax - DIAMETRE_BILLE;
        } else if (posY < context.mainActivityView.yMin) {
            posY = context.mainActivityView.yMin;
        }
    }


}