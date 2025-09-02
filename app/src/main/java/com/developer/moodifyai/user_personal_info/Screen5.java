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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developer.moodifyai.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashSet;
import java.util.Set;

public class Screen5 extends AppCompatActivity {
    ImageView btn_back;
    AppCompatButton btn_continue;

    AppCompatButton option_work_college, option_relationship, option_finances,
            option_health_concern, option_life_changes, option_other;

    private static final String PREFS_NAME = "MoodifyAI";
    private static final String KEY_MENTAL_HEALTH_CAUSES = "mentalHealth_cause";

    private Set<String> Causes = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_screen5);

        btn_back = findViewById(R.id.btn_back);
        btn_continue = findViewById(R.id.btn_continue);

        option_work_college = findViewById(R.id.option_work_college);
        option_relationship = findViewById(R.id.option_relationship);
        option_finances = findViewById(R.id.option_finances);
        option_health_concern = findViewById(R.id.option_health_concern);
        option_life_changes = findViewById(R.id.option_life_changes);
        option_other = findViewById(R.id.option_other);

        loadCauses();

        setupOptionClickListener(option_work_college, "work_college");
        setupOptionClickListener(option_relationship, "relationship");
        setupOptionClickListener(option_finances, "finances");
        setupOptionClickListener(option_health_concern, "health_concern");
        setupOptionClickListener(option_life_changes, "life_changes");
        setupOptionClickListener(option_other, "other");


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Screen5.this, Screen4.class));
                finish();
            }
        });

        btn_continue.setOnClickListener(v -> {
            if (Causes.isEmpty()) {
                Snackbar.make(v, "Please select at least one cause", Snackbar.LENGTH_SHORT).show();
            } else {
                saveMentalHealthCauses();
                startActivity(new Intent(Screen5.this, Screen6.class));
                finish();
            }
        });
    }

    private void setupOptionClickListener(AppCompatButton button, String cause) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Causes.contains(cause)) {
                    Causes.remove(cause);
                    button.setBackground(ContextCompat.getDrawable(Screen5.this, R.drawable.btn_selector)); // Default background
                } else {
                    Causes.add(cause);
                    button.setBackground(ContextCompat.getDrawable(Screen5.this, R.drawable.btn_selector_olive));
                }
            }
        });

        if (Causes.contains(cause)) {
            button.setBackground(ContextCompat.getDrawable(Screen5.this, R.drawable.btn_selector_olive));
        }
    }

    private void loadCauses() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Causes = preferences.getStringSet(KEY_MENTAL_HEALTH_CAUSES, new HashSet<>());

        updateOptionUI(option_work_college, "work_college");
        updateOptionUI(option_relationship, "relationship");
        updateOptionUI(option_finances, "finances");
        updateOptionUI(option_health_concern, "health_concern");
        updateOptionUI(option_life_changes, "life_changes");
        updateOptionUI(option_other, "other");
    }

    private void saveMentalHealthCauses() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet(KEY_MENTAL_HEALTH_CAUSES, Causes);
        editor.apply();
    }

    private void updateOptionUI(AppCompatButton button, String cause) {
        if (Causes.contains(cause)) {
            button.setBackground(ContextCompat.getDrawable(Screen5.this, R.drawable.btn_selector_olive));
        } else {
            button.setBackground(ContextCompat.getDrawable(Screen5.this, R.drawable.btn_selector));
        }
    }
}