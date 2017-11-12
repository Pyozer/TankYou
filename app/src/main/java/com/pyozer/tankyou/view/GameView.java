package com.pyozer.tankyou.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;

import com.pyozer.tankyou.R;
import com.pyozer.tankyou.activity.GameActivity;
import com.pyozer.tankyou.model.Missile;
import com.pyozer.tankyou.model.Obstacle;
import com.pyozer.tankyou.model.Tank;
import com.pyozer.tankyou.util.FunctionsUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Vue du jeu
 * Tout le jeu est géré ici
 */
public class GameView extends FrameLayout implements SensorEventListener {

    private final GameActivity mContext;
    private Sensor mAccelerometer;

    private float mSensorY;
    private float mSensorDegree = -90;
    private float mHorizontalMax;
    private float mVerticalMax;

    private Tank mTank;
    private List<Obstacle> mObstacles;
    private List<Missile> mMissiles;

    private boolean tankAlreadyOnObstacle = false;
    private boolean alreadyShowEndGame = false;

    private long lastTimeObstacle = 0;
    private long lastRocketFired;
    private long startGameTime;

    private Paint paintWhite;
    private int score = 0;
    private boolean alreadyWindowFocus = false;

    public GameView(GameActivity context) {
        super(context);
        this.mContext = context;

        mAccelerometer = mContext.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // On récupère les données liés à l'écran
        DisplayMetrics metrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // On défini les valeur maximales de l'écran
        mHorizontalMax = metrics.widthPixels;
        mVerticalMax = metrics.heightPixels;

        // On créer note objet de Tank
        mTank = new Tank(getContext());
        addView(mTank, new ViewGroup.LayoutParams(175, 150));

        // On créer notre liste d'Obstacle et de Missile
        mObstacles = new ArrayList<>();
        mMissiles = new ArrayList<>();

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inDither = true;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;

        // On créer notre couleur pour le Texte
        paintWhite = new Paint();
        paintWhite.setColor(Color.WHITE);
        paintWhite.setTextSize(60);

        // On défini le moment ou le jeu commence
        startGameTime = System.currentTimeMillis();
    }

    /**
     * Créer un nouvelle obstacle
     */
    private void createNewObstacle() {
        Obstacle obstacle = new Obstacle(getContext());
        addView(obstacle, new ViewGroup.LayoutParams(110, 110));

        // On défini la position initiale horizontale où notre obstacle sera
        int xRand = FunctionsUtil.randInt((int) mHorizontalMax - obstacle.getWidth());

        // On défini la position en Y selon si notre obstacle commence en haut ou en bas
        if (obstacle.isFromTop) { // FROM_TOP
            obstacle.setY(-obstacle.getHeight());
        } else { // FROM_BOTTOM
            obstacle.setY(mVerticalMax);
        }
        obstacle.setX(xRand); // On défini la position en X de notre obstacle

        obstacle.setBackgroundResource(R.drawable.obstacle);
        mObstacles.add(obstacle);
    }

    /**
     * Créer un nouveau missile
     */
    private void createNewRocket() {
        // On créer notre Missile qui aura comme angle de direction celui du tank
        Missile missile = new Missile(getContext(), mSensorDegree);
        addView(missile, new ViewGroup.LayoutParams(50, 50));

        missile.setBackgroundResource(R.drawable.missile);
        // On défini la position du missile au niveau du canon du tank
        missile.setX(mTank.mPosX + mTank.getWidth() / 2 - 25);
        missile.setY(mTank.mPosY);
        mMissiles.add(missile);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // On vérifie que la fenetre n'a pas déjà été créer (pour éviter que le tank revienne au centre
        if (!alreadyWindowFocus) {
            alreadyWindowFocus = true;
            // On place le Tank au centre de l'écran
            mTank.setX(mHorizontalMax / 2 - mTank.getWidth() / 2);
            mTank.setY(mVerticalMax / 2 - mTank.getHeight() / 2);

            mTank.setBackgroundResource(R.drawable.tanks_sprites_blue);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mSensorY = -event.values[1];

            // On fait avancer ou reculer le Tank si l'inclisaison en avant ou arrière
            // est supérieur a une certaine valeur
            if (mSensorY > 1.1)
                mSensorY = 6.5f;
            else if (mSensorY < -1.6)
                mSensorY = -6.5f;
            else
                mSensorY = 0;

            float mSensorX = -event.values[0];

            // On défini l'angle du Tank si l'inclinaison gauche/droite
            // est supérieur à une certaine valeur
            if(mSensorX > 2)
                mSensorDegree += 2;
            else if(mSensorX < -2)
                mSensorDegree -= 2;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // On récupère le timestamp actuelle
        long currentTime = System.currentTimeMillis();
        // On vérifie que le dernier missile a été lancé il y a plus de 1.5sec
        if (currentTime - lastRocketFired > 1500) {
            // On créer notre requete et réinitialise le timer
            createNewRocket();
            lastRocketFired = currentTime;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (alreadyShowEndGame)
            return;

        // On récupère le timestamp actuelle
        long now = System.currentTimeMillis();

        // On part du principe que le tank ne touche aucun obstacle
        boolean touchObstacle = false;
        // Pour chaque obstacle on le déplace et vérifie qu'il ne touche aucun missile ou tank
        for (Iterator<Obstacle> iteratorObstacle = mObstacles.iterator(); iteratorObstacle.hasNext(); ) {
            Obstacle obstacle = iteratorObstacle.next();
            // On met à jour la position de l'obstacle
            obstacle.updatePosObstacle();
            // On déplace l'obstacle selon les nouvelles positions
            obstacle.setTranslationX(obstacle.mPosX);
            obstacle.setTranslationY(obstacle.mPosY);

            // Si l'obstacle est en dehors de l'écran on le supprime
            if (obstacle.isOutOfScreen(mHorizontalMax, mVerticalMax)) {
                removeView(obstacle);
                iteratorObstacle.remove();
            } else {
                // On vérifie si le tank est sur l'obstacle en question
                touchObstacle = isTankOnObstacle(obstacle);
                if (touchObstacle) // Si c'est le cas ou quitte la boucle
                    break;
                // Pour chaque missile, on vérifie si il touche l'obstacle
                for (Iterator<Missile> iteratorMissile = mMissiles.iterator(); iteratorMissile.hasNext(); ) {
                    Missile missile = iteratorMissile.next();
                    // On met à jour la position du missile
                    missile.updatePosMissile();
                    // On bouge le missile selon les nouvelle position
                    missile.setTranslationX(missile.mPosX);
                    missile.setTranslationY(missile.mPosY);

                    // Si le missile est en dehors de l'écran on le supprime
                    if (missile.isOutOfScreen(mHorizontalMax, mVerticalMax)) {
                        removeView(missile);
                        iteratorMissile.remove();
                    } else {
                        // Si le missile touche un obstacle on le supprime lui et l'obstacle
                        if (isMissileOnObstacle(missile, obstacle)) {
                            removeView(obstacle);
                            iteratorObstacle.remove();
                            removeView(missile);
                            iteratorMissile.remove();
                            // On inscrémente le score du joueur
                            score++;
                        }
                    }
                }
            }
        }

        // Affiche le score en haut à gauche de l'écran
        canvas.drawText("Score: " + score, 10, 60, paintWhite);
        // Récupère le temps de jeu actuel
        int gameDuration = Math.round((now - startGameTime) / 1000);
        // Affiche le temps de jeu en dessous du score
        canvas.drawText("Time: " + gameDuration + "sec", 10, 130, paintWhite);

        // Si le tank touche un obstacle et que l'on a pas déjà afficher le gameover
        if ((tankAlreadyOnObstacle || touchObstacle) && !alreadyShowEndGame) {
            alreadyShowEndGame = true;
            // On fait un bruit d'explosion car le tank a touché un obstacle
            MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.explosion);
            mp.start();
            // Stop l'écoute du sensor
            stopSimulation();
            // Affiche l'interface de fin de jeu
            mContext.showGameEnd(gameDuration, score);
        } else {
            // Défini la rotation actuelle du tank
            float oldDegre = mTank.mDegre;

            // On met à jour la position du tank selon l'inclinaison du téléphone
            mTank.computePhysics(mSensorY, mSensorDegree);

            // Calcule la durée entre maintenant et le dernier obstacle lancé
            long deltaTime = (now - lastTimeObstacle);
            if (deltaTime >= 1500) { // Créer un obstacle toute les 1.5sec
                createNewObstacle();
                lastTimeObstacle = now;
            }

            // Ajuste la position du tank si celui-ci sort de l'écran
            mTank.resolveCollisionWithBounds(mHorizontalMax, mVerticalMax);
            // Fait bouger le tank
            mTank.setTranslationX(mTank.mPosX);
            mTank.setTranslationY(mTank.mPosY);

            // Oriente le Tank
            updateTankOrientation(oldDegre, mTank.mDegre);

            // Force le onDraw()
            invalidate();
        }
    }

    /**
     * Fait faire une rotation au Tank vers une nouvelle valeur de rotation
     * @param oldDegree Valeur en degré actuel
     * @param newDegree Nouvelle valeur en degré
     */
    public void updateTankOrientation(float oldDegree, float newDegree) {
        RotateAnimation rotateAnimation = new RotateAnimation(
                oldDegree,
                newDegree,
                mTank.mPosX + mTank.getWidth() / 2,
                mTank.mPosY + mTank.getHeight() / 2);
        rotateAnimation.setDuration(210);
        rotateAnimation.setFillAfter(true);
        mTank.startAnimation(rotateAnimation);
    }

    /**
     * Vérifie si le Tank touche un obstacle
     * Utilisation de l'algorithme
     * @param obstacle Obstacle à vérifier
     * @return boolean - Le tank touche ou pas l'obstacle
     */
    public boolean isTankOnObstacle(Obstacle obstacle) {
        // Rotate circle's center point back
        float obstacleCenterX = obstacle.mPosX + obstacle.getWidth() / 2;
        float obstacleCenterY = obstacle.mPosY + obstacle.getHeight() / 2;

        float tankCenterX = mTank.mPosX + mTank.getWidth() / 2;
        float tankCenterY = mTank.mPosY + mTank.getHeight() / 2;

        double unrotatedCircleX = Math.cos(Math.toRadians(mTank.mDegre)) * (obstacleCenterX - tankCenterX) -
                Math.sin(Math.toRadians(mTank.mDegre)) * (obstacleCenterY - tankCenterY) + tankCenterX;
        double unrotatedCircleY = Math.sin(Math.toRadians(mTank.mDegre)) * (obstacleCenterX - tankCenterX) +
                Math.cos(Math.toRadians(mTank.mDegre)) * (obstacleCenterY - tankCenterY) + tankCenterY;

        // Closest point in the rectangle to the center of circle rotated backwards(unrotated)
        double closestX, closestY;

        // Find the unrotated closest x point from center of unrotated circle
        if (unrotatedCircleX < mTank.mPosX)
            closestX = mTank.mPosX;
        else if (unrotatedCircleX > mTank.mPosX + mTank.getWidth())
            closestX = mTank.mPosX + mTank.getWidth();
        else
            closestX = unrotatedCircleX;

        // Find the unrotated closest y point from center of unrotated circle
        if (unrotatedCircleY < mTank.mPosY)
            closestY = mTank.mPosY;
        else if (unrotatedCircleY > mTank.mPosY + mTank.getHeight())
            closestY = mTank.mPosY + mTank.getHeight();
        else
            closestY = unrotatedCircleY;

        double distance = findDistance(unrotatedCircleX, unrotatedCircleY, closestX, closestY);
        if (distance < obstacle.getWidth() / 2) {
            tankAlreadyOnObstacle = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Renvoi la distance entre deux point
     * @param fromX Point 1 x
     * @param fromY Point 1 y
     * @param toX Point 2 x
     * @param toY Point 2 y
     * @return double - Distance entre Point 1 et Point 2
     */
    public double findDistance(double fromX, double fromY, double toX, double toY) {
        double a = Math.abs(fromX - toX);
        double b = Math.abs(fromY - toY);

        return Math.sqrt((a * a) + (b * b));
    }

    /**
     * Vérifie si un missile est sur un obstacle
     * @param missile Missile
     * @param obstacle Obstacle
     * @return boolean - Si le missile touche l'obstacle
     */
    public boolean isMissileOnObstacle(Missile missile, Obstacle obstacle) {
        // Récupère le rayon du missile
        float missileRadius = missile.getWidth() / 2;
        // Récupère le rayon de l'obstacle
        float obstacleRadius = obstacle.getWidth() / 2;

        // Récupère le centre du missile
        float missileCenterX = missile.mPosX + missileRadius;
        float missileCenterY = missile.mPosY + missileRadius;
        // Récupère le centre de l'obstacle
        float obstacleCenterX = obstacle.mPosX + obstacleRadius;
        float obstacleCenterY = obstacle.mPosY + obstacleRadius;
        // Calcule la distance entre les deux centre
        double distance = findDistance(missileCenterX, missileCenterY, obstacleCenterX, obstacleCenterY);

        return (distance < missileRadius + obstacleRadius);
    }

    /**
     * On enregistre le sensor
     */
    public void startSimulation() {
        mContext.mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * On retire l'enregistrement du sensor
     */
    public void stopSimulation() {
        mContext.mSensorManager.unregisterListener(this, mAccelerometer);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}