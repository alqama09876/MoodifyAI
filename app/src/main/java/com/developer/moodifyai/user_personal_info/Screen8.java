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

public class Screen8 extends AppCompatActivity {
    ImageView btn_back;
    AppCompatButton btn_continue;

    AppCompatButton option_yes_regularly, option_yes_occasionally, option_yes_a_long_time_ago,
            option_no_i_have_never_tried_meditation;

    private static final String PREFS_NAME = "MoodifyAI";
    private static final String KEY_TRIED_MEDITATION = "triedMeditationBefore";

    private String triedMeditation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_screen8);

        btn_back = findViewById(R.id.btn_back);
        btn_continue = findViewById(R.id.btn_continue);

        option_yes_regularly = findViewById(R.id.option_yes_regularly);
        option_yes_occasionally = findViewById(R.id.option_yes_occasionally);
        option_yes_a_long_time_ago = findViewById(R.id.option_yes_a_long_time_ago);
        option_no_i_have_never_tried_meditation = findViewById(R.id.option_no_i_have_never_tried_meditation);

        loadTriedMeditationBefore();

        setupOptionClickListener(option_yes_regularly, "yes_regularly");
        setupOptionClickListener(option_yes_occasionally, "yes_occasionally");
        setupOptionClickListener(option_yes_a_long_time_ago, "yes_a_long_time_ago");
        setupOptionClickListener(option_no_i_have_never_tried_meditation, "no_i_have_never_tried_meditation");

        btn_back.setOnClickListener(v -> {
            startActivity(new Intent(Screen8.this, Screen7.class));
            finish();
        });

        btn_continue.setOnClickListener(v -> {
            if (triedMeditation.isEmpty()) {
                Snackbar.make(v, "Please select the item", Snackbar.LENGTH_SHORT).show();
            } else {
                saveTriedMeditationBefore();
                startActivity(new Intent(Screen8.this, Screen9.class));
                finish();
            }
        });
    }

    private void setupOptionClickListener(AppCompatButton button, String tried) {
        button.setOnClickListener(v -> {
            resetButtonState();

            button.setBackground(ContextCompat.getDrawable(Screen8.this, R.drawable.btn_selector_olive));

            triedMeditation = tried;
        });

        if (triedMeditation.equals(tried)) {
            button.setBackground(ContextCompat.getDrawable(Screen8.this, R.drawable.btn_selector_olive));
        } else {
            button.setBackground(ContextCompat.getDrawable(Screen8.this, R.drawable.btn_selector));
        }
    }

    private void resetButtonState() {
        option_yes_regularly.setBackground(ContextCompat.getDrawable(Screen8.this, R.drawable.btn_selector));
        option_yes_occasionally.setBackground(ContextCompat.getDrawable(Screen8.this, R.drawable.btn_selector));
        option_yes_a_long_time_ago.setBackground(ContextCompat.getDrawable(Screen8.this, R.drawable.btn_selector));
        option_no_i_have_never_tried_meditation.setBackground(ContextCompat.getDrawable(Screen8.this, R.drawable.btn_selector));
    }

    private void loadTriedMeditationBefore() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        triedMeditation = preferences.getString(KEY_TRIED_MEDITATION, "");

        switch (triedMeditation) {
            case "yes_regularly":
                option_yes_regularly.setBackground(ContextCompat.getDrawable(Screen8.this, R.drawable.btn_selector_olive));
                break;
            case "yes_occasionally":
                option_yes_occasionally.setBackground(ContextCompat.getDrawable(Screen8.this, R.drawable.btn_selector_olive));
                break;
            case "yes_a_long_time_ago":
                option_yes_a_long_time_ago.setBackground(ContextCompat.getDrawable(Screen8.this, R.drawable.btn_selector_olive));
                break;
            case "no_i_have_never_tried_meditation":
                option_no_i_have_never_tried_meditation.setBackground(ContextCompat.getDrawable(Screen8.this, R.drawable.btn_selector_olive));
                break;
        }
    }

    private void saveTriedMeditationBefore() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_TRIED_MEDITATION, triedMeditation);
        editor.apply();
    }
}