package com.pyozer.tankyou.activity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pyozer.tankyou.CapteurManager;

public class JeuActivity extends AppCompatActivity {

    // l'interface
    MainActivityView mainActivityView;

    // la tank
    Tank tank;

    CapteurManager capteurManager;

    float sensorX;
    float sensorY;
    long sensorTimeStamp;
    long cpuTimeStamp;

    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // creation de l'interface
        mainActivityView = new MainActivityView(this);
        setContentView(mainActivityView);

        // initialile l'accelerometre
        //capteurManager = new CapteurManager(this);

        // creation de la tank
        tank = new Tank(this);

        // initialise le score
        score = 0;

    }

    public void onSensorChanged(SensorEvent event) {

        // si l'évènement vient de l'accéléromètre
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            // sauvegarder les valeurs de l'accélérometre
            sensorX = event.values[0];
            sensorY = -event.values[1];

            // enregistrer la date du sensor
            sensorTimeStamp = event.timestamp;

            // enregistrer la date du téléphone
            cpuTimeStamp = System.nanoTime();
        }
    }

    @Override
    protected void onResume() {
        capteurManager.startAccelerometerSensor();
        capteurManager.startCompassSensor();

        super.onResume();

    }

    @Override
    protected void onPause() {
        capteurManager.stopAccelerometerSensor();
        capteurManager.stopCompassSensor();

        super.onPause();
    }

}