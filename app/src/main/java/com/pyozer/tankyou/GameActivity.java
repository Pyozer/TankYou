package com.pyozer.tankyou;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Display;
import android.view.WindowManager;

public class GameActivity extends BaseActivity {

    private GameView mSimulationView;
    public SensorManager mSensorManager;
    public PowerManager mPowerManager;
    public WindowManager mWindowManager;
    public Display mDisplay;
    private WakeLock mWakeLock;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get an instance of the SensorManager
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Get an instance of the PowerManager
        mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);

        // Get an instance of the WindowManager
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();

        // Create a bright wake lock
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, getClass().getName());

        // instantiate our simulation view and set it as the activity's content
        mSimulationView = new GameView(this);
        mSimulationView.setBackgroundResource(R.drawable.background_game);
        setContentView(mSimulationView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
         * when the activity is resumed, we acquire a wake-lock so that the
         * screen stays on, since the user will likely not be fiddling with the
         * screen or buttons.
         */
        mWakeLock.acquire(10*60*1000L /*10 minutes*/);

        // Start the simulation
        mSimulationView.startSimulation();
    }

    public void showGameWin() {
        showGameEnd(true);
    }

    public void showGameLose() {
        showGameEnd(false);
    }
    private void showGameEnd(boolean isGameWin) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);

        builder.setTitle("Vous avez " + ((isGameWin) ? "gagné" : "perdu") + " !")
                .setMessage("Voulez-vous recommencer ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*
         * When the activity is paused, we make sure to stop the simulation,
         * release our sensor resources and wake locks
         */

        // Stop the simulation
        mSimulationView.stopSimulation();

        // and release our wake-lock
        mWakeLock.release();
    }
}
