package com.developer.moodifyai.meditation;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.developer.moodifyai.R;

public class StartMeditationScreen extends AppCompatActivity {

    ImageView btn_back, btn_pause, btn_play;
    TextView txt_timer, tv_current_time, tv_total_time;
    SeekBar seekBar;

    private boolean isPlaying = false;
    private int currentPosition = 0;
    private final int totalDuration = 525;
    private Handler handler;
    private Runnable runnable;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start_meditation_screen);

        btn_back = findViewById(R.id.btn_back);
        btn_pause = findViewById(R.id.btn_pause);
        btn_play = findViewById(R.id.btn_play);
        txt_timer = findViewById(R.id.txt_timer);
        tv_current_time = findViewById(R.id.tv_current_time);
        tv_total_time = findViewById(R.id.tv_total_time);
        seekBar = findViewById(R.id.seekBar);

        seekBar.setMax(totalDuration);
        tv_total_time.setText(formatTime(totalDuration));
        txt_timer.setText(formatTime(totalDuration));

        handler = new Handler();

        mediaPlayer = MediaPlayer.create(this, R.raw.ocean_waves);
        mediaPlayer.setLooping(true);

        btn_back.setOnClickListener(v -> {
            finish();
        });

        btn_play.setOnClickListener(v -> {
            togglePlayback(true);
        });

        btn_pause.setOnClickListener(v -> {
            togglePlayback(false);
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    currentPosition = progress;
                    tv_current_time.setText(formatTime(currentPosition));
                    txt_timer.setText(formatTime(totalDuration - currentPosition));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        boolean shouldStart = getIntent().getBooleanExtra("startTimer", false);
        if (shouldStart) {
            togglePlayback(true);
        }
    }

    private void togglePlayback(boolean play) {
        isPlaying = play;
        btn_play.setVisibility(play ? View.GONE : View.VISIBLE);
        btn_pause.setVisibility(play ? View.VISIBLE : View.GONE);

        if (play) {
            startTimer();
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start(); // 🎵 Start music
            }
        } else {
            pauseTimer();
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause(); // 🎵 Pause music
            }
        }
    }

    private void startTimer() {
        runnable = new Runnable() {
            @Override
            public void run() {
                if (currentPosition < totalDuration) {
                    currentPosition++;
                    seekBar.setProgress(currentPosition);
                    tv_current_time.setText(formatTime(currentPosition));
                    txt_timer.setText(formatTime(totalDuration - currentPosition));
                    handler.postDelayed(this, 1000);
                } else {
                    togglePlayback(false);

                    Intent intent = new Intent(StartMeditationScreen.this, FinishMeditationScreen.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }


    private void pauseTimer() {
        handler.removeCallbacks(runnable);
    }

    private String formatTime(int seconds) {
        int min = seconds / 60;
        int sec = seconds % 60;
        return String.format("%02d:%02d", min, sec);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}