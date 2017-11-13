package com.pyozer.tankyou.activity;

import android.app.Dialog;
import android.content.Intent;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pyozer.tankyou.R;
import com.pyozer.tankyou.model.UserScore;
import com.pyozer.tankyou.util.PrefUserManager;
import com.pyozer.tankyou.view.GameView;

/**
 * Activity du jeu
 */
public class GameActivity extends BaseActivity {

    private GameView mSimulationView;
    public SensorManager mSensorManager;

    public PowerManager mPowerManager;
    public WindowManager mWindowManager;
    public Display mDisplay;
    private WakeLock mWakeLock;

    private PrefUserManager prefUserManager;

    public SoundPool soundPool;
    public int soundIds[];

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
        mWakeLock = mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());

        prefUserManager = new PrefUserManager(this);

        // Création des paramètres pour le sons
        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        // Création de la gestion des sons
        soundPool = new SoundPool.Builder()
                .setMaxStreams(10)
                .setAudioAttributes(attrs)
                .build();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Définition des sons
        soundIds = new int[3];
        soundIds[0] = soundPool.load(this, R.raw.sf_explosion_01, 1);
        soundIds[1] = soundPool.load(this, R.raw.sf_canon_01, 1);

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

    /**
     * Affiche l'interface de fin de jeu
     */
    public void showGameEnd(int time, int score) {
        // Envoi les scores à Firebase
        saveScore(time, score);
        // Créer notre dialog fullscreen
        final Dialog dialog = new Dialog(this, R.style.AppTheme_NoActionBar);
        View view = LayoutInflater.from(this).inflate(R.layout.end_game_dialog, null);

        TextView textScore = view.findViewById(R.id.end_game_score);
        String scoreDisplay = getString(R.string.score) + "\n" + score;
        textScore.setText(scoreDisplay);

        TextView textDuration = view.findViewById(R.id.end_game_duration);
        String durationDisplay = getString(R.string.duree) + "\n" + time + "sec";
        textDuration.setText(durationDisplay);

        view.findViewById(R.id.end_game_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GameActivity.this, GameActivity.class));
                finish();
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.end_game_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GameActivity.this, MainActivity.class));
                finish();
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.setCancelable(false);

        dialog.show();
    }

    /**
     * Enregistre les scores sur Firebase
     * @param duree int - Durée de la partie
     * @param scoreUser int - score de la partie
     */
    private void saveScore(int duree, int scoreUser) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        String userPseudo = prefUserManager.getUsername();

        UserScore score = new UserScore(userPseudo, duree, scoreUser);

        mDatabase.child("leaderboard").child(userPseudo).setValue(score.toLeaderboardMap());

        String key = mDatabase.child("scores").child(userPseudo).push().getKey();
        mDatabase.child("scores").child(userPseudo).child(key).setValue(score.toUserStatsMap());
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop the simulation
        mSimulationView.stopSimulation();

        // and release our wake-lock
        if(mWakeLock.isHeld())
            mWakeLock.release();
    }
}
