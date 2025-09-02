package com.developer.moodifyai.deep_breathing;

import android.content.Intent;
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

public class HoldScreen extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView timeCount;
    private ImageView btn_back;
    private ImageView pause, play, vibration, vibrationOff, volumeOn, volumeOff;

    private int holdTime;
    private int exhaleTime;

    private CountDownTimer countDownTimer;
    private boolean isPaused = false;
    private boolean isVibrationOn = true;
    private boolean isVolumeOn = true;

    private long timeRemainingInMillis;
    private Vibrator vibrator;

    private Animation pulseAnimation;

    // Vibration loop handler
    private Handler vibrationHandler = new Handler();
    private Runnable vibrationRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hold_screen);

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

        holdTime = getIntent().getIntExtra("holdTime", 40);
        exhaleTime = getIntent().getIntExtra("exhaleTime", 40);

        progressBar.setMax(holdTime);
        timeRemainingInMillis = holdTime * 1000L;

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

        startCountdown(timeRemainingInMillis);

        startVibrationLoop();
    }

    private void startCountdown(long durationInMillis) {
        countDownTimer = new CountDownTimer(durationInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemainingInMillis = millisUntilFinished;
                int remainingSeconds = (int) (millisUntilFinished / 1000);
                timeCount.setText(String.format(Locale.getDefault(), "%02d:%02d", remainingSeconds / 60, remainingSeconds % 60));
                int progress = holdTime - remainingSeconds;
                progressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                timeCount.setText("00:00");
                progressBar.setProgress(holdTime);
                progressBar.clearAnimation();

                stopVibrationLoop();

                Intent intent = new Intent(HoldScreen.this, ExhaleScreen.class);
                intent.putExtra("exhaleTime", exhaleTime);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    private void pauseCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            isPaused = true;

            pause.setVisibility(View.GONE);
            play.setVisibility(View.VISIBLE);

            stopVibrationLoop();
        }
    }

    private void resumeCountdown() {
        isPaused = false;
        startCountdown(timeRemainingInMillis);

        play.setVisibility(View.GONE);
        pause.setVisibility(View.VISIBLE);

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
        if (countDownTimer != null) countDownTimer.cancel();
        stopVibrationLoop();
        progressBar.clearAnimation();
    }
}