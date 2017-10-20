package com.pyozer.tankyou.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.pyozer.tankyou.R;

public class MainActivityView extends View {

    // pointe vers JeuActivity
    JeuActivity context;

    // variables qui permettent de convertir les valeurs en px en metre
    float metersToPixelsX;
    float metersToPixelsY;

    // Bipmap qui servira pour dessiner la tank sur l'écran
    Bitmap imageDeBille;

    // Bipmap qui servira pour dessiner la cible sur l'écran
    Bitmap imageDeCible;

    // diameter de la cible (en pixel)
    static int DIAMETRE_CIBLE = 200;
    float cibleX;
    float cibleY;

    // valeurs min et max pour éviter aux billes de sortir de l'écran
    float xMin;
    float xMax;
    float yMin;
    float yMax;

    // contient les informations sur l'affichage de la fenêtre
    DisplayMetrics fenetreMetrics;

    Boolean billeDejaSurCible;

    /*
     * Constructeur
     */
    public MainActivityView(JeuActivity pcontext) {
        super(pcontext);

        // initialiser le context
        context = pcontext;

        // initialiter les informations sur l'affichage de la fenêtre
        fenetreMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(fenetreMetrics);

        // trouver la taille réelle de l'écran en metre
        metersToPixelsX = fenetreMetrics.xdpi / 0.0254f; // 1 inch == 0.0254 metre
        metersToPixelsY = fenetreMetrics.ydpi / 0.0254f;

        // creer l'image de la tank
        // et redimentionner la pour qu'elle ait le bon diametre sur n'importe quel écran
        // il faut faire cette étape avant le onDraw qui n'aura pas le temps de créer un BitMapImage
        Bitmap billeBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.tanks_sprites_blue);
        int w = (int) (Tank.DIAMETRE_BILLE);
        int h = (int) (Tank.DIAMETRE_BILLE);
        imageDeBille = Bitmap.createScaledBitmap(billeBitMap, w, h, true);

        // créer l'image de la cible
        Bitmap cibleBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.cible);
        imageDeCible = Bitmap.createScaledBitmap(cibleBitMap, DIAMETRE_CIBLE, DIAMETRE_CIBLE, true);
        // calculer le X et Y de la cible maintenant
        // cela ne sert à rien de surcharger la fonction onDraw()
        cibleX = fenetreMetrics.widthPixels / 2 - DIAMETRE_CIBLE / 2;
        cibleY = fenetreMetrics.heightPixels / 2 - DIAMETRE_CIBLE / 2 - 100; // -100 pour la barre de tache

        billeDejaSurCible = false;
    }


    /*
     * Cette fonction est appelée à chaque fois que la taille de l'écran change
     * elle est donc appellée la toute première fois que l'appli s'ouvre
     */
    @Override
    protected void onSizeChanged(int wEcran, int hEcran, int oldwEcran, int oldhEcran) {

        // mettre à jouer les x et y min et max
        // ces valeurs sont utilisées dans Tank.resourdreCollisionAvecLimites()
        // pour éviter que les billes ne sortent de l'écran
        xMin = 0;
        xMax = wEcran;
        yMin = 0;
        yMax = hEcran;
    }


    /*
     * Cette fonction est appelée dès que la classe View est crée
     * elle se répète indéfiniement dès que possible, puisque nous
     * avons ajouter un invalidate(); à la fin
     */
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

        // desine la cible au milieu de l'écran
        canvas.drawBitmap(imageDeCible, cibleX, cibleY, null);

        // trouve la date actuelle
        long timestamp = context.sensorTimeStamp + (System.nanoTime() - context.cpuTimeStamp);

        // met à jour la position de la tank avec les données de l'accélérometre et la date actuelle
        context.tank.updatePosition(context.sensorX, context.sensorY, timestamp);

        // desinne l'image au bon endroit
        float x = context.tank.posX * metersToPixelsX;
        float y = context.tank.posY * metersToPixelsY;
        canvas.drawBitmap(imageDeBille, x, y, null);

        // si la tank est sur la cible, icrémente le score
        if (billeSurCible(x, y)) {
            context.score++;
        }

        // affiche le score en haut à gauche
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(60);
        canvas.drawText("score = " + context.score, 100, 100, paint);

        // le fait d'invalider l'interface tout de suite fait qu'elle va être
        // redessiner aussi tôt que possible
        invalidate();
    }


    /*
     * return true si la tank est sur la cible
     */
    public boolean billeSurCible(float billeX, float billeY) {
        // si la tank est sur la cible
        if (cibleX < billeX
                && (billeX + Tank.DIAMETRE_BILLE) < (cibleX + DIAMETRE_CIBLE)
                && cibleY < billeY
                && (billeY + Tank.DIAMETRE_BILLE) < (cibleY + DIAMETRE_CIBLE)) {

            // si la tank vient de rentrer dans la cible
            if (!billeDejaSurCible) {
                billeDejaSurCible = true;
                return true;
            } else {
                return false;
            }
        } else {
            billeDejaSurCible = false;
            return false;
        }
    }
}