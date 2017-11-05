package com.pyozer.tankyou;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends FrameLayout implements SensorEventListener {

    private final GameActivity mContext;
    private Sensor mAccelerometer;
    private Sensor mCompass;

    private float mSensorY;
    private float mSensorDegree;
    private float mHorizontalMax;
    private float mVerticalMax;

    private Cible mCible;
    private Tank mTank;
    private List<Obstacle> mObstacles;

    private boolean tankAlreadyOnTarget = false;
    private boolean tankAlreadyOnObstacle = false;
    private boolean alreadyShowWin = false;
    private boolean alreadyShowLose = false;

    private long lastTime = System.currentTimeMillis();

    public void startSimulation() {
        mContext.mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mContext.mSensorManager.registerListener(this, mCompass, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stopSimulation() {
        mContext.mSensorManager.unregisterListener(this, mAccelerometer);
        mContext.mSensorManager.unregisterListener(this, mCompass);
    }

    public GameView(GameActivity context) {
        super(context);
        this.mContext = context;

        mAccelerometer = mContext.mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mCompass = mContext.mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        DisplayMetrics metrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        mHorizontalMax = metrics.widthPixels;
        mVerticalMax = metrics.heightPixels;

        mCible = new Cible(getContext());
        mCible.setLayerType(LAYER_TYPE_HARDWARE, null);
        addView(mCible, new ViewGroup.LayoutParams(200, 200));

        mTank = new Tank(getContext());
        mTank.setLayerType(LAYER_TYPE_HARDWARE, null);
        addView(mTank, new ViewGroup.LayoutParams(175, 150));

        mObstacles = new ArrayList<>();

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inDither = true;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
    }

    private void createNewObstacle() {
        Obstacle obstacle = new Obstacle(getContext());
        obstacle.setLayerType(LAYER_TYPE_HARDWARE, null);
        addView(obstacle, new ViewGroup.LayoutParams(110, 110));
        obstacle.setX(randInt((int) mHorizontalMax - obstacle.getWidth()));
        obstacle.setY(0);
        obstacle.setBackgroundResource(R.drawable.obstacle);
        mObstacles.add(obstacle);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        mCible.setX(randInt((int) mHorizontalMax - mCible.getWidth()));
        mCible.setY(mCible.getHeight() / 2);

        mTank.setX(randInt((int) mHorizontalMax - mTank.getWidth()));
        mTank.setY(mVerticalMax - mTank.getHeight() / 2);

        mCible.setBackgroundResource(R.drawable.cible);
        mTank.setBackgroundResource(R.drawable.tanks_sprites_blue);
    }

    private int randInt(int max) {
        return new Random().nextInt(max);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            mSensorDegree = event.values[0];
        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            switch (mContext.mDisplay.getRotation()) {
                case Surface.ROTATION_0:
                    mSensorY = -event.values[1];
                    break;
                case Surface.ROTATION_90:
                    mSensorY = -event.values[0];
                    break;
                case Surface.ROTATION_180:
                    mSensorY = +event.values[1];
                    break;
                case Surface.ROTATION_270:
                    mSensorY = +event.values[0];
                    break;
            }

            if (mSensorY > 1) mSensorY = 5;
            else if (mSensorY < -1) mSensorY = -5;
            else mSensorY = 0;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (tankAlreadyOnTarget || isTankOnTarget()) {
            if(!alreadyShowWin) {
                stopSimulation();
                mContext.showAlertDialog("Bravo", "Tu as atteint ta cible !");
                alreadyShowWin = true;
            }
        } else {
            // On vérifie si le tank ne touche pas un des obstacles
            boolean touchObstacle = false;
            for(Obstacle obstacle : mObstacles) {
                obstacle.setTranslationY(++obstacle.mPosY);
                touchObstacle = isTankOnObstacle(obstacle);
                if(touchObstacle)
                    break;
            }
            // Si le tank touche un obstacle
            if (tankAlreadyOnObstacle || touchObstacle) {
                if(!alreadyShowLose) {
                    stopSimulation();
                    mContext.showAlertDialog("Perdu", "Vous avez toucher un obstacle :/");
                    alreadyShowLose = true;
                }
            } else {
                // Sinon si il touche rien
                long currentTime = System.currentTimeMillis();
                float oldDegre = mTank.mDegre;

                mTank.computePhysics(mSensorY, mSensorDegree);

                long deltaTime = (currentTime - lastTime);
                if(deltaTime >= 1500) { // On créer un obstacle toute les 1.5sec
                    createNewObstacle();
                    lastTime = currentTime;
                }

                mTank.resolveCollisionWithBounds(mHorizontalMax, mVerticalMax);

                mTank.setTranslationX(mTank.mPosX);
                mTank.setTranslationY(mTank.mPosY);

                updateTankOrientation(oldDegre, mTank.mDegre);

                // and make sure to redraw asap
                invalidate();
            }
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

    public boolean isTankOnTarget() {
        if (mCible.mPosX < mTank.mPosX + mTank.getWidth() / 2
                && mCible.mPosX + mCible.getWidth() > mTank.mPosX + mTank.getWidth() / 2
                && mCible.mPosY < mTank.mPosY + mTank.getHeight() / 2
                && mCible.mPosY + mCible.getHeight() > mTank.mPosY + mTank.getHeight() / 2) {

            // si le tank vient de rentrer dans la cible
            tankAlreadyOnTarget = true;
            return true;
        }
        return false;
    }

    public boolean isTankOnObstacle(Obstacle obstacle) {
        if(mTank.mPosX < obstacle.mPosX + obstacle.getWidth()
                && mTank.mPosX + mTank.getWidth() > obstacle.mPosX
                && mTank.mPosY < obstacle.mPosY + obstacle.getHeight()
                && mTank.getHeight() + mTank.mPosY > obstacle.mPosY) {

            Log.e("COLLISION", "COLLISION DETECTE");
            tankAlreadyOnObstacle = true;
            return true;
        }
        return false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}