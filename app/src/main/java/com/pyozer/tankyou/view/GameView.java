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
import android.util.Log;
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

    public void startSimulation() {
        mContext.mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stopSimulation() {
        mContext.mSensorManager.unregisterListener(this, mAccelerometer);
    }

    public GameView(GameActivity context) {
        super(context);
        this.mContext = context;

        mAccelerometer = mContext.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        DisplayMetrics metrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        mHorizontalMax = metrics.widthPixels;
        mVerticalMax = metrics.heightPixels;

        mTank = new Tank(getContext());
        addView(mTank, new ViewGroup.LayoutParams(175, 150));

        mObstacles = new ArrayList<>();
        mMissiles = new ArrayList<>();

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inDither = true;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;

        paintWhite = new Paint();
        paintWhite.setColor(Color.WHITE);
        paintWhite.setTextSize(60);

        startGameTime = System.currentTimeMillis();
    }

    private void createNewObstacle() {
        Obstacle obstacle = new Obstacle(getContext());
        addView(obstacle, new ViewGroup.LayoutParams(110, 110));

        int xRand = FunctionsUtil.randInt((int) mHorizontalMax - obstacle.getWidth());

        if (obstacle.orientationNum == 1) { // FROM_TOP
            obstacle.setX(xRand);
            obstacle.setY(-obstacle.getHeight());
        } else if (obstacle.orientationNum == 3) { // FROM_BOTTOM
            obstacle.setX(xRand);
            obstacle.setY(mVerticalMax);
        }
        obstacle.setBackgroundResource(R.drawable.obstacle);
        mObstacles.add(obstacle);
    }

    private void createNewRocket() {
        Missile missile = new Missile(getContext(), mSensorDegree);
        addView(missile, new ViewGroup.LayoutParams(50, 50));

        missile.setBackgroundResource(R.drawable.missile);
        missile.setX(mTank.mPosX + mTank.getWidth() / 2 - 25);
        missile.setY(mTank.mPosY);
        mMissiles.add(missile);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (!alreadyWindowFocus) {
            alreadyWindowFocus = true;
            mTank.setX(mHorizontalMax / 2 - mTank.getWidth() / 2);
            mTank.setY(mVerticalMax / 2 - mTank.getHeight() / 2);

            mTank.setBackgroundResource(R.drawable.tanks_sprites_blue);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mSensorY = -event.values[1];

            if (mSensorY > 1.1)
                mSensorY = 6.5f;
            else if (mSensorY < -1.6)
                mSensorY = -6.5f;
            else
                mSensorY = 0;

            float mSensorX = -event.values[0];

            if(mSensorX > 2)
                mSensorDegree += 2;
            else if(mSensorX < -2)
                mSensorDegree -= 2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (alreadyShowEndGame)
            return;

        long now = System.currentTimeMillis();

        // On vérifie si le tank ne touche pas un des obstacles
        boolean touchObstacle = false;
        for (Iterator<Obstacle> iteratorObstacle = mObstacles.iterator(); iteratorObstacle.hasNext(); ) {
            Obstacle obstacle = iteratorObstacle.next();
            obstacle.updatePosObstacle();
            obstacle.setTranslationX(obstacle.mPosX);
            obstacle.setTranslationY(obstacle.mPosY);

            if (obstacle.isOutOfScreen(mHorizontalMax, mVerticalMax)) {
                removeView(obstacle);
                iteratorObstacle.remove();
            } else {
                touchObstacle = isTankOnObstacle(obstacle);
                if (touchObstacle)
                    break;
                // On vérifie si un missile touche l'obstacle
                for (Iterator<Missile> iteratorMissile = mMissiles.iterator(); iteratorMissile.hasNext(); ) {
                    Missile missile = iteratorMissile.next();
                    missile.updatePosMissile();
                    missile.setTranslationX(missile.mPosX);
                    missile.setTranslationY(missile.mPosY);

                    if (missile.isOutOfScreen(mHorizontalMax, mVerticalMax)) {
                        removeView(missile);
                        iteratorMissile.remove();
                    } else {
                        if (isMissileOnObstacle(missile, obstacle)) {
                            removeView(obstacle);
                            iteratorObstacle.remove();
                            removeView(missile);
                            iteratorMissile.remove();

                            score++;
                        }
                    }
                }
            }
        }

        canvas.drawText("Score: " + score, 10, 60, paintWhite);
        int gameDuration = Math.round((now - startGameTime) / 1000);
        canvas.drawText("Time: " + gameDuration + "sec", 10, 130, paintWhite);

        // Si le tank touche un obstacle
        if ((tankAlreadyOnObstacle || touchObstacle) && !alreadyShowEndGame) {
            alreadyShowEndGame = true;
            MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.explosion);
            mp.start();
            stopSimulation();
            mContext.showGameEnd(gameDuration, score);
        } else {
            // Sinon si il touche rien
            float oldDegre = mTank.mDegre;

            mTank.computePhysics(mSensorY, mSensorDegree);

            long deltaTime = (now - lastTimeObstacle);
            if (deltaTime >= 1500) { // On créer un obstacle toute les 1.5sec
                createNewObstacle();
                lastTimeObstacle = now;
            }

            mTank.resolveCollisionWithBounds(mHorizontalMax, mVerticalMax);

            mTank.setTranslationX(mTank.mPosX);
            mTank.setTranslationY(mTank.mPosY);

            updateTankOrientation(oldDegre, mTank.mDegre);

            // and make sure to redraw asap
            invalidate();
        }
    }

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

    public double findDistance(double fromX, double fromY, double toX, double toY) {
        double a = Math.abs(fromX - toX);
        double b = Math.abs(fromY - toY);

        return Math.sqrt((a * a) + (b * b));
    }

    public boolean isMissileOnObstacle(Missile missile, Obstacle obstacle) {
        float missileRadius = missile.getWidth() / 2;
        float obstacleRadius = obstacle.getWidth() / 2;

        float missileCenterX = missile.mPosX + missileRadius;
        float missileCenterY = missile.mPosY + missileRadius;
        float obstacleCenterX = obstacle.mPosX + obstacleRadius;
        float obstacleCenterY = obstacle.mPosY + obstacleRadius;

        float dx = missileCenterX - obstacleCenterX;
        float dy = missileCenterY - obstacleCenterY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        return distance < missileRadius + obstacleRadius;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastRocketFired > 1500) {
            createNewRocket();
            lastRocketFired = currentTime;
        }
        return true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}