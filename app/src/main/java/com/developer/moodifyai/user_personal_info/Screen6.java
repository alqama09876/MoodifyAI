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

public class Screen6 extends AppCompatActivity {
    ImageView btn_back;
    AppCompatButton btn_continue;

    AppCompatButton option_almost_daily, option_frequently, option_occasionally,
            option_rarely, option_never;

    private static final String PREFS_NAME = "MoodifyAI";
    private static final String KEY_STRESS_FREQUENCY = "stressFrequency";

    private String selectedFrequency = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_screen6);

        btn_back = findViewById(R.id.btn_back);
        btn_continue = findViewById(R.id.btn_continue);

        option_almost_daily = findViewById(R.id.option_almost_daily);
        option_frequently = findViewById(R.id.option_frequently);
        option_occasionally = findViewById(R.id.option_occasionally);
        option_rarely = findViewById(R.id.option_rarely);
        option_never = findViewById(R.id.option_never);

        loadSelectedFrequency();

        setupOptionClickListener(option_almost_daily, "almost_daily");
        setupOptionClickListener(option_frequently, "frequently");
        setupOptionClickListener(option_occasionally, "occasionally");
        setupOptionClickListener(option_rarely, "rarely");
        setupOptionClickListener(option_never, "never");

        btn_back.setOnClickListener(v -> {
            startActivity(new Intent(Screen6.this, Screen5.class));
            finish();
        });

        btn_continue.setOnClickListener(v -> {
            if (selectedFrequency.isEmpty()) {
                Snackbar.make(v, "Please select the item", Snackbar.LENGTH_SHORT).show();
            } else {
                saveSelectedFrequency();
                startActivity(new Intent(Screen6.this, Screen7.class));
                finish();
            }
        });
    }

    private void setupOptionClickListener(AppCompatButton button, String frequency) {
        button.setOnClickListener(v -> {
            resetButtonState();

            button.setBackground(ContextCompat.getDrawable(Screen6.this, R.drawable.btn_selector_olive));

            selectedFrequency = frequency;
        });

        if (selectedFrequency.equals(frequency)) {
            button.setBackground(ContextCompat.getDrawable(Screen6.this, R.drawable.btn_selector_olive));
        } else {
            button.setBackground(ContextCompat.getDrawable(Screen6.this, R.drawable.btn_selector));
        }
    }

    private void resetButtonState() {
        option_almost_daily.setBackground(ContextCompat.getDrawable(Screen6.this, R.drawable.btn_selector));
        option_frequently.setBackground(ContextCompat.getDrawable(Screen6.this, R.drawable.btn_selector));
        option_occasionally.setBackground(ContextCompat.getDrawable(Screen6.this, R.drawable.btn_selector));
        option_rarely.setBackground(ContextCompat.getDrawable(Screen6.this, R.drawable.btn_selector));
        option_never.setBackground(ContextCompat.getDrawable(Screen6.this, R.drawable.btn_selector));
    }

    private void loadSelectedFrequency() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        selectedFrequency = preferences.getString(KEY_STRESS_FREQUENCY, "");

        switch (selectedFrequency) {
            case "almost_daily":
                option_almost_daily.setBackground(ContextCompat.getDrawable(Screen6.this, R.drawable.btn_selector_olive));
                break;
            case "frequently":
                option_frequently.setBackground(ContextCompat.getDrawable(Screen6.this, R.drawable.btn_selector_olive));
                break;
            case "occasionally":
                option_occasionally.setBackground(ContextCompat.getDrawable(Screen6.this, R.drawable.btn_selector_olive));
                break;
            case "rarely":
                option_rarely.setBackground(ContextCompat.getDrawable(Screen6.this, R.drawable.btn_selector_olive));
                break;
            case "never":
                option_never.setBackground(ContextCompat.getDrawable(Screen6.this, R.drawable.btn_selector_olive));
                break;
        }
    }

    private void saveSelectedFrequency() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_STRESS_FREQUENCY, selectedFrequency);
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
//public class Screen6 extends AppCompatActivity {
//    ImageView btn_back;
//    AppCompatButton btn_continue;
//
//    AppCompatButton option_almost_daily, option_frequently, option_occasionally,
//            option_rarely, option_never;
//
//    private static final String PREFS_NAME = "UserPreferences";
//    private static final String KEY_STRESS_FREQUENCIES = "stressFrequency";
//
//    private Set<String> Frequencies = new HashSet<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_screen6);
//
//        btn_back = findViewById(R.id.btn_back);
//        btn_continue = findViewById(R.id.btn_continue);
//
//        option_almost_daily = findViewById(R.id.option_almost_daily);
//        option_frequently = findViewById(R.id.option_frequently);
//        option_occasionally = findViewById(R.id.option_occasionally);
//        option_rarely = findViewById(R.id.option_rarely);
//        option_never = findViewById(R.id.option_never);
//
//        loadFrequencies();
//
//        setupOptionClickListener(option_almost_daily, "almost_daily");
//        setupOptionClickListener(option_frequently, "frequently");
//        setupOptionClickListener(option_occasionally, "occasionally");
//        setupOptionClickListener(option_rarely, "rarely");
//        setupOptionClickListener(option_never, "never");
//
//        btn_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Screen6.this, Screen5.class));
//                finish();
//            }
//        });
//
//        btn_continue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveStressFrequency();
//                startActivity(new Intent(Screen6.this, Screen7.class));
//                finish();
//            }
//        });
//    }
//
//    private void setupOptionClickListener(AppCompatButton button, String feel) {
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Frequencies.contains(feel)) {
//                    Frequencies.remove(feel);
//                    button.setBackground(ContextCompat.getDrawable(Screen6.this, R.drawable.btn_selector)); // Default background
//                } else {
//                    Frequencies.add(feel);
//                    button.setBackground(ContextCompat.getDrawable(Screen6.this, R.drawable.btn_selector_olive));
//                }
//            }
//        });
//
//        if (Frequencies.contains(feel)) {
//            button.setBackground(ContextCompat.getDrawable(Screen6.this, R.drawable.btn_selector_olive));
//        }
//    }
//
//    private void loadFrequencies() {
//        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//        Frequencies = preferences.getStringSet(KEY_STRESS_FREQUENCIES, new HashSet<>());
//
//        updateOptionUI(option_almost_daily, "almost_daily");
//        updateOptionUI(option_frequently, "frequently");
//        updateOptionUI(option_occasionally, "occasionally");
//        updateOptionUI(option_rarely, "rarely");
//        updateOptionUI(option_never, "never");
//    }
//
//    private void saveStressFrequency() {
//        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putStringSet(KEY_STRESS_FREQUENCIES, Frequencies);
//        editor.apply();
//    }
//
//    private void updateOptionUI(AppCompatButton button, String feel) {
//        if (Frequencies.contains(feel)) {
//            button.setBackground(ContextCompat.getDrawable(Screen6.this, R.drawable.btn_selector_olive));
//        } else {
//            button.setBackground(ContextCompat.getDrawable(Screen6.this, R.drawable.btn_selector));
//        }
//    }
//}