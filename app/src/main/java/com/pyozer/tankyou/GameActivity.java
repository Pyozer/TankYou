package com.pyozer.tankyou;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends BaseActivity {

    private CapteurManager capteurManager;

    private ImageView tank;
    private ImageView cible;
    private RelativeLayout gameView;

    private float tankAngle = 0;
    private float tankPosX = 0;
    private float tankPosY = 0;

    private float ciblePosX;
    private float ciblePosY;

    private float screenWidth;
    private float screenHeight;

    private final static int DIAMETRE_TANK = 40;
    private final static int DIAMETRE_CIBLE = 40;

    private boolean tankAlreadyOnTarget = false;
    private boolean isWindowLoad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        capteurManager = new CapteurManager(this);

        gameView = (RelativeLayout) findViewById(R.id.gameView);
        tank = new ImageView(this);
        tank.setImageResource(R.drawable.tanks_sprites_blue);
        ViewGroup.LayoutParams paramsTank = new ViewGroup.LayoutParams(200, 200);
        tank.setLayoutParams(paramsTank);
        cible = new ImageView(this);
        cible.setImageResource(R.drawable.cible);
        ViewGroup.LayoutParams paramsCible = new ViewGroup.LayoutParams(200, 200);
        cible.setLayoutParams(paramsCible);

        gameView.addView(tank);
        gameView.addView(cible);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        screenWidth = gameView.getWidth();
        screenHeight = gameView.getHeight();

        cible.setX(screenWidth / 2);
        cible.setY(screenHeight / 2);

        ciblePosX = cible.getX();
        ciblePosY = cible.getY();

        isWindowLoad = true;
    }

    public void updateTankOrientation(float oldDegree, float newDegree) {
        RotateAnimation rotateAnimation = new RotateAnimation(
                oldDegree,
                newDegree,
                tankPosX + tank.getWidth() / 2,
                tankPosY + tank.getHeight() / 2);
        rotateAnimation.setDuration(210);
        rotateAnimation.setFillAfter(true);
        tank.startAnimation(rotateAnimation);
        tankAngle = newDegree;
    }

    public void updateTankPosition(float speed) {
        if (isWindowLoad) {
            if (!tankAlreadyOnTarget && !isTankOnTarget(tank.getX(), tank.getY())) {
                tankPosX = tankPosX + (int) (Math.cos(Math.toRadians(tankAngle)) * speed);
                tankPosY = tankPosY + (int) (Math.sin(Math.toRadians(tankAngle)) * speed);

                tank.setX(tankPosX);
                tank.setY(tankPosY);

                if (tank.getX() < 0)
                    tank.setX(1);
                else if (tank.getX() + tank.getWidth() > screenWidth)
                    tank.setX(screenWidth - tank.getWidth());
                if (tank.getY() < 0)
                    tank.setY(1);
                else if (tank.getY() + tank.getHeight() > screenHeight)
                    tank.setY(screenHeight - tank.getHeight());

                tankPosX = tank.getX();
                tankPosY = tank.getY();
            } else {
                capteurManager.stopAccelerometerSensor();
                capteurManager.stopCompassSensor();
                showAlertDialog("Bravo", "Tu as atteint ta cible !");
            }
        }
    }

    public boolean isTankOutScreen(float tankX, float tankY) {
        return (tankX < 0 || tankX > screenWidth || tankY < 0 || tankY > screenHeight);
    }

    public boolean isTankOnTarget(float tankX, float tankY) {
        if (ciblePosX < tankX + tank.getWidth() / 2
                && ciblePosX + cible.getWidth() > tankX + tank.getWidth() / 2
                && ciblePosY < tankY + tank.getHeight() / 2
                && ciblePosY + cible.getHeight() > tankY + tank.getHeight() / 2) {

            // si le tank vient de rentrer dans la cible
            tankAlreadyOnTarget = true;
            return true;
        }
        return false;
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setCancelable(false);

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(GameActivity.this, GameActivity.class));
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    @Override
    protected void onPause() {
        // désenregistrer notre écoute du capteur
        capteurManager.stopCompassSensor();
        capteurManager.stopAccelerometerSensor();

        super.onPause();
    }

    @Override
    protected void onResume() {
        // enregistrer notre écoute du capteur
        capteurManager.startCompassSensor();
        capteurManager.startAccelerometerSensor();
        super.onResume();
    }
}