package com.developer.moodifyai.user_personal_info;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.developer.moodifyai.R;

public class Screen2 extends AppCompatActivity {
    ImageView btn_back, img_male, img_female;
    TextView txt_male, txt_female, txt_transgender;
    AppCompatButton btn_continue;
    String selectedGender = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_screen2);

        btn_back = findViewById(R.id.btn_back);
        btn_continue = findViewById(R.id.btn_continue);
        img_male = findViewById(R.id.img_male);
        txt_male = findViewById(R.id.txt_male);
        img_female = findViewById(R.id.img_female);
        txt_female = findViewById(R.id.txt_female);
        txt_transgender = findViewById(R.id.txt_transgender);

        SharedPreferences prefs = getSharedPreferences("MoodifyAI", MODE_PRIVATE);
        selectedGender = prefs.getString("user_gender", null);

        if (selectedGender != null) {
            updateGenderUI(selectedGender);
        }

        btn_back.setOnClickListener(v -> {
            startActivity(new Intent(Screen2.this, Screen1.class));
            finish();
        });

        img_male.setOnClickListener(v -> selectGender("Male"));

        img_female.setOnClickListener(v -> selectGender("Female"));

        txt_transgender.setOnClickListener(v -> selectGender("Transgender"));

        btn_continue.setOnClickListener(v -> {
            if (selectedGender != null) {
                SharedPreferences.Editor editor = getSharedPreferences("MoodifyAI", MODE_PRIVATE).edit();
                editor.putString("user_gender", selectedGender);
                editor.apply();

                startActivity(new Intent(Screen2.this, Screen3.class));
                finish();
            } else {
                Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectGender(String gender) {
        selectedGender = gender;
        updateGenderUI(gender);
    }

    private void updateGenderUI(String gender) {
        resetGenderUI();

        selectedGender = gender;
        switch (gender) {
            case "Male":
                img_male.setBackground(ContextCompat.getDrawable(this, R.drawable.selected_bg));
                img_male.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
                txt_male.setTextColor(ContextCompat.getColor(this, R.color.olive_green));
                break;

            case "Female":
                img_female.setBackground(ContextCompat.getDrawable(this, R.drawable.selected_bg));
                img_female.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
                txt_female.setTextColor(ContextCompat.getColor(this, R.color.olive_green));
                break;

            case "Transgender":
                txt_transgender.setBackground(ContextCompat.getDrawable(this, R.drawable.txt_selected_bg));
                txt_transgender.setTextColor(ContextCompat.getColor(this, R.color.white));
                break;
        }
    }

    private void resetGenderUI() {
        img_male.setBackground(ContextCompat.getDrawable(this, R.drawable.img_female_bg));
        img_male.setColorFilter(null);
        txt_male.setTextColor(ContextCompat.getColor(this, R.color.black));

        img_female.setBackground(ContextCompat.getDrawable(this, R.drawable.img_female_bg));
        img_female.setColorFilter(null);
        txt_female.setTextColor(ContextCompat.getColor(this, R.color.black));

        txt_transgender.setBackground(ContextCompat.getDrawable(this, R.drawable.txt_bg));
        txt_transgender.setTextColor(ContextCompat.getColor(this, R.color.black));
    }
}