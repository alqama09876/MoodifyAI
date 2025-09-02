package com.developer.moodifyai.deep_breathing;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.developer.moodifyai.DashboardScreen;
import com.developer.moodifyai.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DeepBreathingScreen extends AppCompatActivity {

    ImageView btn_back;
    AppCompatButton btn_2min, btn_3min, btn_4min, btn_5min, btn_customMin, btn_start;

    List<AppCompatButton> timeButtons;
    int selectedTimeInSeconds = 120; // Default = 2 minutes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_deep_breathing_screen);

        btn_back = findViewById(R.id.btn_back);
        btn_2min = findViewById(R.id.btn_2min);
        btn_3min = findViewById(R.id.btn_3min);
        btn_4min = findViewById(R.id.btn_4min);
        btn_5min = findViewById(R.id.btn_5min);
        btn_customMin = findViewById(R.id.btn_customMin);
        btn_start = findViewById(R.id.btn_lets_start);

        timeButtons = new ArrayList<>();
        timeButtons.add(btn_2min);
        timeButtons.add(btn_3min);
        timeButtons.add(btn_4min);
        timeButtons.add(btn_5min);
        timeButtons.add(btn_customMin);

        setSelectedButton(btn_2min); // Default

        for (AppCompatButton btn : timeButtons) {
            btn.setOnClickListener(v -> {
                setSelectedButton(btn);
                if (btn == btn_customMin) {
                    openCustomTimePicker();
                } else {
                    selectedTimeInSeconds = getTimeFromButtonText(btn.getText().toString());
                }
            });
        }

        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(DeepBreathingScreen.this, DashboardScreen.class);
            intent.putExtra("navigate_to", "ExploreFragment");
            startActivity(intent);
            finish();
        });

        btn_start.setOnClickListener(v -> {
            if (selectedTimeInSeconds < 30) {
                Toast.makeText(this, "Please select at least 1 minute", Toast.LENGTH_SHORT).show();
                return;
            }

            int oneThird = selectedTimeInSeconds / 3;

            Intent intent = new Intent(DeepBreathingScreen.this, InhaleScreen.class);
            intent.putExtra("inhaleTime", oneThird);
            intent.putExtra("holdTime", oneThird);
            intent.putExtra("exhaleTime", oneThird);
            startActivity(intent);
        });
    }

    private void setSelectedButton(AppCompatButton selectedButton) {
        for (AppCompatButton btn : timeButtons) {
            if (btn == selectedButton) {
                btn.setBackgroundResource(R.drawable.bg_done_button);
                btn.setTextColor(Color.WHITE);
            } else {
                btn.setBackgroundResource(R.drawable.bg_buttons);
                btn.setTextColor(getResources().getColor(R.color.black));
            }
        }
    }

    private int getTimeFromButtonText(String text) {
        // return Integer.parseInt(text.replace("min", "").trim()) * 60;
        try {
            // Extract only digits from the string
            String digitsOnly = text.replaceAll("\\D+", "");
            return Integer.parseInt(digitsOnly) * 60;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 120; // fallback to default 2 min
        }
    }

    private void openCustomTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (TimePicker view, int hourOfDay, int minute) -> {
                    int totalSeconds = (hourOfDay * 60 + minute) * 60;
                    if (totalSeconds < 30) {
                        Toast.makeText(this, "Please select at least 1 minute", Toast.LENGTH_SHORT).show();
                    } else {
                        selectedTimeInSeconds = totalSeconds;
                        btn_customMin.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
                    }
                },
                0, 2, true // default 2 minutes
        );
        timePickerDialog.setTitle("Select Custom Time");
        timePickerDialog.show();
    }
}