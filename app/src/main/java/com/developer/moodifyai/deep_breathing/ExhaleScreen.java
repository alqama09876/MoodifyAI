package com.developer.moodifyai.deep_breathing;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.developer.moodifyai.R;

import java.util.Locale;

public class ExhaleScreen extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView timeCount;
    private ImageView btn_back;
    private ImageView pause, play, vibration, vibrationOff, volumeOn, volumeOff;

    private int exhaleTime;
    private long timeRemainingInMillis;
    private int progress = 0;

    private boolean isPaused = false;
    private boolean isVibrationOn = true;
    private boolean isVolumeOn = true;

    private CountDownTimer countDownTimer;
    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;

    private Animation pulseAnimation;

    // Vibration loop handler
    private Handler vibrationHandler = new Handler();
    private Runnable vibrationRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_exhale_screen);

        // Bind Views
        progressBar = findViewById(R.id.progressBar);
        timeCount = findViewById(R.id.time_count);
        btn_back = findViewById(R.id.btn_back);
        pause = findViewById(R.id.pause);
        play = findViewById(R.id.play);
        vibration = findViewById(R.id.vibration);
        vibrationOff = findViewById(R.id.vibrationOff);
        volumeOn = findViewById(R.id.volumeOn);
        volumeOff = findViewById(R.id.volumeOff);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        exhaleTime = getIntent().getIntExtra("exhaleTime", 40);
        timeRemainingInMillis = exhaleTime * 1000L;
        progressBar.setMax(exhaleTime);

        btn_back.setOnClickListener(v -> finish());

        pause.setOnClickListener(v -> pauseCountdown());
        play.setOnClickListener(v -> resumeCountdown());
        vibration.setOnClickListener(v -> toggleVibration(false));
        vibrationOff.setOnClickListener(v -> toggleVibration(true));
        volumeOn.setOnClickListener(v -> toggleVolume(false));
        volumeOff.setOnClickListener(v -> toggleVolume(true));

        // Setup pulse animation for progress bar
        pulseAnimation = new ScaleAnimation(
                1f, 1.2f, 1f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        pulseAnimation.setDuration(1000);
        pulseAnimation.setRepeatMode(Animation.REVERSE);
        pulseAnimation.setRepeatCount(Animation.INFINITE);
        progressBar.startAnimation(pulseAnimation);

        if (isVolumeOn) startMedia();

        startCountdown(timeRemainingInMillis);

        startVibrationLoop();
    }

    private void startCountdown(long durationInMillis) {
        countDownTimer = new CountDownTimer(durationInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemainingInMillis = millisUntilFinished;
                int remaining = (int) (millisUntilFinished / 1000);
                timeCount.setText(String.format(Locale.getDefault(), "%02d:%02d", remaining / 60, remaining % 60));
                progressBar.setProgress(exhaleTime - remaining);

                // No direct vibrate here, vibration is handled by loop now
            }

            @Override
            public void onFinish() {
                timeCount.setText("00:00");
                progressBar.setProgress(exhaleTime);
                stopMedia();
                stopVibrationLoop();

                Intent intent = new Intent(ExhaleScreen.this, FinishBreathingScreen.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    private void pauseCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }

        pause.setVisibility(View.GONE);
        play.setVisibility(View.VISIBLE);
        isPaused = true;

        stopVibrationLoop();
    }

    private void resumeCountdown() {
        startCountdown(timeRemainingInMillis);

        if (mediaPlayer != null && !mediaPlayer.isPlaying() && isVolumeOn) {
            mediaPlayer.start();
        }

        play.setVisibility(View.GONE);
        pause.setVisibility(View.VISIBLE);
        isPaused = false;

        startVibrationLoop();
    }

    private void toggleVibration(boolean turnOn) {
        isVibrationOn = turnOn;
        vibration.setVisibility(turnOn ? View.VISIBLE : View.GONE);
        vibrationOff.setVisibility(turnOn ? View.GONE : View.VISIBLE);

        if (turnOn) {
            startVibrationLoop();
        } else {
            stopVibrationLoop();
        }
    }

    private void toggleVolume(boolean turnOn) {
        isVolumeOn = turnOn;
        volumeOn.setVisibility(turnOn ? View.VISIBLE : View.GONE);
        volumeOff.setVisibility(turnOn ? View.GONE : View.VISIBLE);

        if (turnOn) {
            startMedia();
        } else {
            stopMedia();
        }
    }

    private void startMedia() {
        stopMedia(); // prevent overlapping
        mediaPlayer = MediaPlayer.create(this, R.raw.exhale); // your exhale sound
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    private void stopMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    // Vibration loop every 1 second with strong 300ms pulse
    private void startVibrationLoop() {
        stopVibrationLoop(); // To avoid duplicates

        vibrationRunnable = new Runnable() {
            @Override
            public void run() {
                if (isVibrationOn && !isPaused && vibrator != null) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(300);
                    }
                    vibrationHandler.postDelayed(this, 1000);
                }
            }
        };
        vibrationHandler.post(vibrationRunnable);
    }

    private void stopVibrationLoop() {
        if (vibrationRunnable != null) {
            vibrationHandler.removeCallbacks(vibrationRunnable);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMedia();
        if (countDownTimer != null) countDownTimer.cancel();
        stopVibrationLoop();
        progressBar.clearAnimation();
    }
}