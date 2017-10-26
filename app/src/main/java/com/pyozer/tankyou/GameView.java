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

public class GameView extends FrameLayout implements SensorEventListener {

    private final GameActivity mContext;
    private Sensor mAccelerometer;
    private Sensor mCompass;

    private float mSensorY;
    private float mSensorDegree;
    private float mHorizontalMax;
    private float mVerticalMax;

    private final Cible mCible;
    private final Tank mTank;

    private boolean tankAlreadyOnTarget = false;

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

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inDither = true;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        mCible.setX(mCible.mPosX);
        mCible.setY(mCible.mPosY);

        mCible.setBackgroundResource(R.drawable.cible);
        mTank.setBackgroundResource(R.drawable.tanks_sprites_blue);
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
        if (tankAlreadyOnTarget || isTankOnTarget(mTank.mPosX, mTank.mPosY)) {
            stopSimulation();
            mContext.showAlertDialog("Bravo", "Tu as atteint ta cible !");
        } else {

            float oldDegre = mTank.mDegre;

            mTank.computePhysics(mSensorY, mSensorDegree);

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

    public boolean isTankOnTarget(float tankX, float tankY) {
        if (mCible.mPosX < tankX + mTank.getWidth() / 2
                && mCible.mPosX + mCible.getWidth() > tankX + mTank.getWidth() / 2
                && mCible.mPosY < tankY + mTank.getHeight() / 2
                && mCible.mPosY + mCible.getHeight() > tankY + mTank.getHeight() / 2) {

            // si le tank vient de rentrer dans la cible
            tankAlreadyOnTarget = true;
            return true;
        }
        return false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}