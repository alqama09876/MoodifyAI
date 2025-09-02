package com.developer.moodifyai.user_personal_info;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.developer.moodifyai.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashSet;
import java.util.Set;

public class Screen4 extends AppCompatActivity {
    ImageView btn_back;
    AppCompatButton btn_continue;

    AppCompatButton option_reduce_stress, option_improve_mood, option_manage_anxiety,
            option_improve_sleep, option_enhance_relationship, option_boost_confidence;

    private static final String PREFS_NAME = "MoodifyAI";
    private static final String KEY_SELECTED_GOALS = "selected_goals";

    private Set<String> selectedGoals = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_screen4);

        btn_back = findViewById(R.id.btn_back);
        btn_continue = findViewById(R.id.btn_continue);

        option_reduce_stress = findViewById(R.id.option_reduce_stress);
        option_improve_mood = findViewById(R.id.option_improve_mood);
        option_manage_anxiety = findViewById(R.id.option_manage_anxiety);
        option_improve_sleep = findViewById(R.id.option_improve_sleep);
        option_enhance_relationship = findViewById(R.id.option_enhance_relationship);
        option_boost_confidence = findViewById(R.id.option_boost_confidence);

        loadSelectedGoals();

        setupOptionClickListener(option_reduce_stress, "reduce_stress");
        setupOptionClickListener(option_improve_mood, "improve_mood");
        setupOptionClickListener(option_manage_anxiety, "manage_anxiety");
        setupOptionClickListener(option_improve_sleep, "improve_sleep");
        setupOptionClickListener(option_enhance_relationship, "enhance_relationship");
        setupOptionClickListener(option_boost_confidence, "boost_confidence");

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Screen4.this, Screen3.class));
                finish();
            }
        });

        btn_continue.setOnClickListener(v -> {
            if (selectedGoals.isEmpty()) {
                Snackbar.make(v, "Please select at least one goal", Snackbar.LENGTH_SHORT).show();
            } else {
                saveSelectedGoals();
                startActivity(new Intent(Screen4.this, Screen5.class));
                finish();
            }
        });
    }

    private void setupOptionClickListener(AppCompatButton button, String goal) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedGoals.contains(goal)) {
                    selectedGoals.remove(goal);
                    button.setBackground(ContextCompat.getDrawable(Screen4.this, R.drawable.btn_selector)); // Default background
                } else {
                    selectedGoals.add(goal);
                    button.setBackground(ContextCompat.getDrawable(Screen4.this, R.drawable.btn_selector_olive));
                }
            }
        });

        if (selectedGoals.contains(goal)) {
            button.setBackground(ContextCompat.getDrawable(Screen4.this, R.drawable.btn_selector_olive));
        }
    }

    private void loadSelectedGoals() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        selectedGoals = preferences.getStringSet(KEY_SELECTED_GOALS, new HashSet<>());

        updateOptionUI(option_reduce_stress, "reduce_stress");
        updateOptionUI(option_improve_mood, "improve_mood");
        updateOptionUI(option_manage_anxiety, "manage_anxiety");
        updateOptionUI(option_improve_sleep, "improve_sleep");
        updateOptionUI(option_enhance_relationship, "enhance_relationship");
        updateOptionUI(option_boost_confidence, "boost_confidence");
    }

    private void saveSelectedGoals() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(KEY_SELECTED_GOALS, selectedGoals);
        editor.apply();
    }

    private void updateOptionUI(AppCompatButton button, String goal) {
        if (selectedGoals.contains(goal)) {
            button.setBackground(ContextCompat.getDrawable(Screen4.this, R.drawable.btn_selector_olive));
        } else {
            button.setBackground(ContextCompat.getDrawable(Screen4.this, R.drawable.btn_selector));
        }
    }
}