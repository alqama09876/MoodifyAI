package com.developer.moodifyai.deep_breathing;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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

public class InhaleScreen extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView timeCount;
    private ImageView btn_back;
    private int inhaleTime;
    private ImageView pause, vibration, volumeOn, play, vibrationOff, volumeOff;
    private boolean isPaused = false;
    private boolean isVibrationOn = true;
    private boolean isVolumeOn = true;

    private MediaPlayer inhaleSound;
    private Vibrator vibrator;
    private CountDownTimer countDownTimer;
    private Animation pulseAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inhale_screen);

        progressBar = findViewById(R.id.progressBar);
        timeCount = findViewById(R.id.time_count);
        btn_back = findViewById(R.id.btn_back);
        pause = findViewById(R.id.pause);
        play = findViewById(R.id.play);
        vibration = findViewById(R.id.vibration);
        vibrationOff = findViewById(R.id.vibrationOff);
        volumeOn = findViewById(R.id.volumeOn);
        volumeOff = findViewById(R.id.volumeOff);

        inhaleTime = getIntent().getIntExtra("inhaleTime", 40);
        int holdTime = getIntent().getIntExtra("holdTime", 40);
        int exhaleTime = getIntent().getIntExtra("exhaleTime", 40);

        progressBar.setMax(inhaleTime);

        btn_back.setOnClickListener(v -> finish());

        startCountdown(inhaleTime, holdTime, exhaleTime);

        // ...
        play.setVisibility(View.GONE);
        vibrationOff.setVisibility(View.GONE);
        volumeOff.setVisibility(View.GONE);

        inhaleSound = MediaPlayer.create(this, R.raw.inhale);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        pause.setOnClickListener(v -> {
            isPaused = true;
            pause.setVisibility(View.GONE);
            play.setVisibility(View.VISIBLE);
            countDownTimer.cancel();
            if (inhaleSound.isPlaying()) inhaleSound.pause();
        });

        play.setOnClickListener(v -> {
            isPaused = false;
            pause.setVisibility(View.VISIBLE);
            play.setVisibility(View.GONE);
            startCountdown(inhaleTime, holdTime, exhaleTime);
            inhaleSound.start();
        });

        vibration.setOnClickListener(v -> {
            isVibrationOn = false;
            vibration.setVisibility(View.GONE);
            vibrationOff.setVisibility(View.VISIBLE);
        });

        vibrationOff.setOnClickListener(v -> {
            isVibrationOn = true;
            vibration.setVisibility(View.VISIBLE);
            vibrationOff.setVisibility(View.GONE);
        });

        volumeOn.setOnClickListener(v -> {
            isVolumeOn = false;
            volumeOn.setVisibility(View.GONE);
            volumeOff.setVisibility(View.VISIBLE);
            inhaleSound.setVolume(0f, 0f);
        });

        volumeOff.setOnClickListener(v -> {
            isVolumeOn = true;
            volumeOn.setVisibility(View.VISIBLE);
            volumeOff.setVisibility(View.GONE);
            inhaleSound.setVolume(1f, 1f);
        });

        // animation...
        pulseAnimation = new ScaleAnimation(
                1f, 1.2f, 1f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        pulseAnimation.setDuration(1000);
        pulseAnimation.setRepeatMode(Animation.REVERSE);
        pulseAnimation.setRepeatCount(Animation.INFINITE);

        progressBar.startAnimation(pulseAnimation);
    }

    private void startCountdown(int seconds, int holdTime, int exhaleTime) {
        countDownTimer = new CountDownTimer(seconds * 1000L, 1000) {
            int progress = 0;

            @Override
            public void onTick(long millisUntilFinished) {
                if (isPaused) {
                    cancel();
                    return;
                }

                int remaining = (int) (millisUntilFinished / 1000);
                timeCount.setText(String.format(Locale.getDefault(), "%02d:%02d", remaining / 60, remaining % 60));
                progressBar.setProgress(++progress);

                if (isVibrationOn) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
                    }
                }

                if (isVolumeOn && !inhaleSound.isPlaying()) {
                    inhaleSound.start();
                }
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(seconds);
                timeCount.setText("00:00");

                if (inhaleSound.isPlaying()) inhaleSound.stop();
                inhaleSound.release();
                progressBar.clearAnimation();

                Intent intent = new Intent(InhaleScreen.this, HoldScreen.class);
                intent.putExtra("holdTime", holdTime);
                intent.putExtra("exhaleTime", exhaleTime);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (inhaleSound != null) {
            inhaleSound.release();
            inhaleSound = null;
        }
    }
}