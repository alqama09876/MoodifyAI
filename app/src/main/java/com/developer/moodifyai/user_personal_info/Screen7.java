package com.developer.moodifyai.user_personal_info;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.developer.moodifyai.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Set;

public class Screen7 extends AppCompatActivity {
    ImageView btn_back;
    AppCompatButton btn_continue;

    AppCompatButton option_always, option_most_of_the_time, option_sometimes,
            option_rarely, option_never;

    private static final String PREFS_NAME = "MoodifyAI";
    private static final String KEY_HEALTHY_EATING = "healthyEatingHabit";

    private String selectedHabit = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_screen7);

        btn_back = findViewById(R.id.btn_back);
        btn_continue = findViewById(R.id.btn_continue);

        option_always = findViewById(R.id.option_always);
        option_most_of_the_time = findViewById(R.id.option_most_of_the_time);
        option_sometimes = findViewById(R.id.option_sometimes);
        option_rarely = findViewById(R.id.option_rarely);
        option_never = findViewById(R.id.option_never);

        loadSelectedHabit();

        setupOptionClickListener(option_always, "always");
        setupOptionClickListener(option_most_of_the_time, "most_of_the_time");
        setupOptionClickListener(option_sometimes, "sometimes");
        setupOptionClickListener(option_rarely, "rarely");
        setupOptionClickListener(option_never, "never");

        btn_back.setOnClickListener(v -> {
            startActivity(new Intent(Screen7.this, Screen6.class));
            finish();
        });

        btn_continue.setOnClickListener(v -> {
            if (selectedHabit.isEmpty()) {
                Snackbar.make(v, "Please select the item", Snackbar.LENGTH_SHORT).show();
            } else {
                saveSelectedHabit();
                startActivity(new Intent(Screen7.this, Screen8.class));
                finish();
            }
        });
    }

    private void setupOptionClickListener(AppCompatButton button, String habit) {
        button.setOnClickListener(v -> {
            resetButtonState();
            button.setBackground(ContextCompat.getDrawable(Screen7.this, R.drawable.btn_selector_olive));
            selectedHabit = habit;
        });

        if (selectedHabit.equals(habit)) {
            button.setBackground(ContextCompat.getDrawable(Screen7.this, R.drawable.btn_selector_olive));
        } else {
            button.setBackground(ContextCompat.getDrawable(Screen7.this, R.drawable.btn_selector));
        }
    }

    private void resetButtonState() {
        option_always.setBackground(ContextCompat.getDrawable(Screen7.this, R.drawable.btn_selector));
        option_most_of_the_time.setBackground(ContextCompat.getDrawable(Screen7.this, R.drawable.btn_selector));
        option_sometimes.setBackground(ContextCompat.getDrawable(Screen7.this, R.drawable.btn_selector));
        option_rarely.setBackground(ContextCompat.getDrawable(Screen7.this, R.drawable.btn_selector));
        option_never.setBackground(ContextCompat.getDrawable(Screen7.this, R.drawable.btn_selector));
    }

    private void loadSelectedHabit() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Try to get the value as a String
        try {
            selectedHabit = preferences.getString(KEY_HEALTHY_EATING, "");
        } catch (ClassCastException e) {
            // If it's a Set<String>, handle migration
            Set<String> oldSet = preferences.getStringSet(KEY_HEALTHY_EATING, null);
            if (oldSet != null && !oldSet.isEmpty()) {
                selectedHabit = oldSet.iterator().next(); // Get the first value
            }
            // Migrate old data to new format
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove(KEY_HEALTHY_EATING); // Remove old entry
            editor.putString(KEY_HEALTHY_EATING, selectedHabit); // Save new entry
            editor.apply();
        }

        // Update the button state
        switch (selectedHabit) {
            case "always":
                option_always.setBackground(ContextCompat.getDrawable(Screen7.this, R.drawable.btn_selector_olive));
                break;
            case "most_of_the_time":
                option_most_of_the_time.setBackground(ContextCompat.getDrawable(Screen7.this, R.drawable.btn_selector_olive));
                break;
            case "sometimes":
                option_sometimes.setBackground(ContextCompat.getDrawable(Screen7.this, R.drawable.btn_selector_olive));
                break;
            case "rarely":
                option_rarely.setBackground(ContextCompat.getDrawable(Screen7.this, R.drawable.btn_selector_olive));
                break;
            case "never":
                option_never.setBackground(ContextCompat.getDrawable(Screen7.this, R.drawable.btn_selector_olive));
                break;
        }
    }

    private void saveSelectedHabit() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_HEALTHY_EATING, selectedHabit); // Save as String
        editor.apply();
    }
}


//package com.developer.moodifyai.user_personal_info;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.AppCompatButton;
//import androidx.core.content.ContextCompat;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.developer.moodifyai.R;
//
//import java.util.HashSet;
//import java.util.Set;
//
//public class Screen7 extends AppCompatActivity {
//    ImageView btn_back;
//    AppCompatButton btn_continue;
//
//    AppCompatButton option_always, option_most_of_the_time, option_sometimes,
//            option_rarely, option_never;
//
//    private static final String PREFS_NAME = "UserPreferences";
//    private static final String KEY_EATING_HEALTHY = "healthyEatingHabit";
//
//    private Set<String> HealthyEating = new HashSet<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_screen7);
//
//        btn_back = findViewById(R.id.btn_back);
//        btn_continue = findViewById(R.id.btn_continue);
//
//        option_always = findViewById(R.id.option_always);
//        option_most_of_the_time = findViewById(R.id.option_most_of_the_time);
//        option_sometimes = findViewById(R.id.option_sometimes);
//        option_rarely = findViewById(R.id.option_rarely);
//        option_never = findViewById(R.id.option_never);
//
//        loadHealthyEating();
//
//        setupOptionClickListener(option_always, "always");
//        setupOptionClickListener(option_most_of_the_time, "most_of_the_time");
//        setupOptionClickListener(option_sometimes, "sometimes");
//        setupOptionClickListener(option_rarely, "rarely");
//        setupOptionClickListener(option_never, "never");
//
//        btn_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Screen7.this, Screen6.class));
//                finish();
//            }
//        });
//
//        btn_continue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveHealthyEating();
//                startActivity(new Intent(Screen7.this, Screen8.class));
//                finish();
//            }
//        });
//    }
//
//    private void setupOptionClickListener(AppCompatButton button, String eating) {
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (HealthyEating.contains(eating)) {
//                    HealthyEating.remove(eating);
//                    button.setBackground(ContextCompat.getDrawable(Screen7.this, R.drawable.btn_selector)); // Default background
//                } else {
//                    HealthyEating.add(eating);
//                    button.setBackground(ContextCompat.getDrawable(Screen7.this, R.drawable.btn_selector_olive));
//                }
//            }
//        });
//
//        if (HealthyEating.contains(eating)) {
//            button.setBackground(ContextCompat.getDrawable(Screen7.this, R.drawable.btn_selector_olive));
//        }
//    }
//
//    private void loadHealthyEating() {
//        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//        HealthyEating = preferences.getStringSet(KEY_EATING_HEALTHY, new HashSet<>());
//
//        updateOptionUI(option_always, "always");
//        updateOptionUI(option_most_of_the_time, "most_of_the_time");
//        updateOptionUI(option_sometimes, "sometimes");
//        updateOptionUI(option_rarely, "rarely");
//        updateOptionUI(option_never, "never");
//    }
//
//    private void saveHealthyEating() {
//        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putStringSet(KEY_EATING_HEALTHY, HealthyEating);
//        editor.apply();
//    }
//
//    private void updateOptionUI(AppCompatButton button, String eating) {
//        if (HealthyEating.contains(eating)) {
//            button.setBackground(ContextCompat.getDrawable(Screen7.this, R.drawable.btn_selector_olive));
//        } else {
//            button.setBackground(ContextCompat.getDrawable(Screen7.this, R.drawable.btn_selector));
//        }
//    }
//}