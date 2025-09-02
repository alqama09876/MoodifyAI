package com.developer.moodifyai.user_personal_info;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.developer.moodifyai.R;
import com.google.android.material.snackbar.Snackbar;

public class Screen9 extends AppCompatActivity {
    ImageView btn_back;
    AppCompatButton btn_continue;

    AppCompatButton option_very_poor, option_poor, option_average,
            option_good, option_excellent;

    private static final String PREFS_NAME = "MoodifyAI";
    private static final String KEY_SLEEP_LEVEL = "sleepQualityLevel";

    private String sleepLevel = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_screen9);

        btn_back = findViewById(R.id.btn_back);
        btn_continue = findViewById(R.id.btn_continue);

        option_very_poor = findViewById(R.id.option_very_poor);
        option_poor = findViewById(R.id.option_poor);
        option_average = findViewById(R.id.option_average);
        option_good = findViewById(R.id.option_good);
        option_excellent = findViewById(R.id.option_excellent);

        loadSleepLevel();

        setupOptionClickListener(option_very_poor, "very_poor");
        setupOptionClickListener(option_poor, "poor");
        setupOptionClickListener(option_average, "average");
        setupOptionClickListener(option_good, "good");
        setupOptionClickListener(option_excellent, "excellent");

        btn_back.setOnClickListener(v -> {
            startActivity(new Intent(Screen9.this, Screen8.class));
            finish();
        });

        btn_continue.setOnClickListener(v -> {
            if (sleepLevel.isEmpty()) {
                Snackbar.make(v, "Please select the item", Snackbar.LENGTH_SHORT).show();
            } else {
                saveSleepLevel();
                startActivity(new Intent(Screen9.this, Screen10.class));
                finish();
            }
        });
    }

    private void setupOptionClickListener(AppCompatButton button, String sleep) {
        button.setOnClickListener(v -> {
            resetButtonState();

            button.setBackground(ContextCompat.getDrawable(Screen9.this, R.drawable.btn_selector_olive));

            sleepLevel = sleep;
        });

        if (sleepLevel.equals(sleep)) {
            button.setBackground(ContextCompat.getDrawable(Screen9.this, R.drawable.btn_selector_olive));
        } else {
            button.setBackground(ContextCompat.getDrawable(Screen9.this, R.drawable.btn_selector));
        }
    }

    private void resetButtonState() {
        option_very_poor.setBackground(ContextCompat.getDrawable(Screen9.this, R.drawable.btn_selector));
        option_poor.setBackground(ContextCompat.getDrawable(Screen9.this, R.drawable.btn_selector));
        option_average.setBackground(ContextCompat.getDrawable(Screen9.this, R.drawable.btn_selector));
        option_good.setBackground(ContextCompat.getDrawable(Screen9.this, R.drawable.btn_selector));
        option_excellent.setBackground(ContextCompat.getDrawable(Screen9.this, R.drawable.btn_selector));
    }

    private void loadSleepLevel() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        sleepLevel = preferences.getString(KEY_SLEEP_LEVEL, "");

        switch (sleepLevel) {
            case "very_poor":
                option_very_poor.setBackground(ContextCompat.getDrawable(Screen9.this, R.drawable.btn_selector_olive));
                break;
            case "poor":
                option_poor.setBackground(ContextCompat.getDrawable(Screen9.this, R.drawable.btn_selector_olive));
                break;
            case "average":
                option_average.setBackground(ContextCompat.getDrawable(Screen9.this, R.drawable.btn_selector_olive));
                break;
            case "good":
                option_good.setBackground(ContextCompat.getDrawable(Screen9.this, R.drawable.btn_selector_olive));
                break;
            case "excellent":
                option_excellent.setBackground(ContextCompat.getDrawable(Screen9.this, R.drawable.btn_selector_olive));
                break;
        }
    }

    private void saveSleepLevel() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_SLEEP_LEVEL, sleepLevel);
        editor.apply();
    }
}