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

public class Screen10 extends AppCompatActivity {
    ImageView btn_back;
    AppCompatButton btn_continue;

    AppCompatButton option_angry, option_sad, option_neutral,
            option_happy, option_very_happy;

    private static final String PREFS_NAME = "MoodifyAI";
    private static final String KEY_HAPPINESS_LEVEL = "happinessLevel";

    private String happyLevel = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_screen10);

        btn_back = findViewById(R.id.btn_back);
        btn_continue = findViewById(R.id.btn_continue);

        option_angry = findViewById(R.id.option_angry);
        option_sad = findViewById(R.id.option_sad);
        option_neutral = findViewById(R.id.option_neutral);
        option_happy = findViewById(R.id.option_happy);
        option_very_happy = findViewById(R.id.option_very_happy);

        loadHappyLevel();

        setupOptionClickListener(option_angry, "angry");
        setupOptionClickListener(option_sad, "sad");
        setupOptionClickListener(option_neutral, "neutral");
        setupOptionClickListener(option_happy, "happy");
        setupOptionClickListener(option_very_happy, "very_happy");

        btn_back.setOnClickListener(v -> {
            startActivity(new Intent(Screen10.this, Screen9.class));
            finish();
        });

        btn_continue.setOnClickListener(v -> {
            if (happyLevel.isEmpty()) {
                Snackbar.make(v, "Please select the item", Snackbar.LENGTH_SHORT).show();
            } else {
                saveHappyLevel();
                startActivity(new Intent(Screen10.this, PreparingPlanScreen.class));
                finish();
            }
        });
    }

    private void setupOptionClickListener(AppCompatButton button, String happy) {
        button.setOnClickListener(v -> {
            resetButtonState();

            button.setBackground(ContextCompat.getDrawable(Screen10.this, R.drawable.btn_selector_olive));

            happyLevel = happy;
        });

        if (happyLevel.equals(happy)) {
            button.setBackground(ContextCompat.getDrawable(Screen10.this, R.drawable.btn_selector_olive));
        } else {
            button.setBackground(ContextCompat.getDrawable(Screen10.this, R.drawable.btn_selector));
        }
    }

    private void resetButtonState() {
        option_angry.setBackground(ContextCompat.getDrawable(Screen10.this, R.drawable.btn_selector));
        option_sad.setBackground(ContextCompat.getDrawable(Screen10.this, R.drawable.btn_selector));
        option_neutral.setBackground(ContextCompat.getDrawable(Screen10.this, R.drawable.btn_selector));
        option_happy.setBackground(ContextCompat.getDrawable(Screen10.this, R.drawable.btn_selector));
        option_very_happy.setBackground(ContextCompat.getDrawable(Screen10.this, R.drawable.btn_selector));
    }

    private void loadHappyLevel() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        happyLevel = preferences.getString(KEY_HAPPINESS_LEVEL, "");

        switch (happyLevel) {
            case "angry":
                option_angry.setBackground(ContextCompat.getDrawable(Screen10.this, R.drawable.btn_selector_olive));
                break;
            case "sad":
                option_sad.setBackground(ContextCompat.getDrawable(Screen10.this, R.drawable.btn_selector_olive));
                break;
            case "neutral":
                option_neutral.setBackground(ContextCompat.getDrawable(Screen10.this, R.drawable.btn_selector_olive));
                break;
            case "happy":
                option_happy.setBackground(ContextCompat.getDrawable(Screen10.this, R.drawable.btn_selector_olive));
                break;
            case "very_happy":
                option_very_happy.setBackground(ContextCompat.getDrawable(Screen10.this, R.drawable.btn_selector_olive));
                break;
        }
    }

    private void saveHappyLevel() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_HAPPINESS_LEVEL, happyLevel);
        editor.apply();
    }
}