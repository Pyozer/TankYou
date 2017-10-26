package com.pyozer.tankyou;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AlertDialog;

public class CapteurManager implements SensorEventListener {

    private GameActivity context;
    public SensorManager sensorManager;

    private Sensor compassSensor;
    private Sensor accelerometerSensor;

    private float degree = 0;
    private static float valueRef = 0;

    public CapteurManager(GameActivity pContext) {
        context = pContext;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    public void startCompassSensor() {
        compassSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        if (compassSensor == null) {
            showAlertNoCompass();
            return;
        }
        sensorManager.registerListener(this, compassSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void stopCompassSensor() {
        sensorManager.unregisterListener(this, compassSensor);
    }

    public void startAccelerometerSensor() {
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerSensor == null) {
            showAlertNoAccelerometer();
            return;
        }
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void stopAccelerometerSensor() {
        sensorManager.unregisterListener(this, accelerometerSensor);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float [] values = sensorEvent.values;
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {

            float newDegree = Math.round(values[0]);

            //context.updateTankOrientation(degree, newDegree);

            degree = newDegree;
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float speed = values[1];
            if(values[1] > valueRef + 1)
                speed = 2;
            else if(values[1] < valueRef - 1)
                speed = -2;

            //context.updateTankPosition(-speed, degree);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    private void showAlertNoCompass() {
        showAlertDialog(context.getString(R.string.alert_no_sensor_title), context.getString(R.string.alert_no_compass_message));
    }

    private void showAlertNoAccelerometer() {
        showAlertDialog(context.getString(R.string.alert_no_sensor_title), context.getString(R.string.alert_no_accelerometer_message));
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setCancelable(false);

        String positiveText = context.getString(android.R.string.ok);
        builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.finish();
            }
        });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }
}
