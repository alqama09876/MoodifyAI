package com.developer.moodifyai.user_personal_info;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.developer.moodifyai.R;
import com.shawnlin.numberpicker.NumberPicker;

public class Screen3 extends AppCompatActivity {
    ImageView btn_back;
    AppCompatButton btn_continue;
    NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_screen3);

        btn_back = findViewById(R.id.btn_back);
        btn_continue = findViewById(R.id.btn_continue);
        numberPicker = findViewById(R.id.number_picker);

        SharedPreferences sharedPreferences = getSharedPreferences("MoodifyAI", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int savedAge = sharedPreferences.getInt("user_age", 18);
        numberPicker.setMinValue(18);
        numberPicker.setMaxValue(100);
        numberPicker.setValue(savedAge);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Screen3.this, Screen2.class));
                finish();
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedAge = numberPicker.getValue();
                editor.putInt("user_age", selectedAge);
                editor.apply();

                startActivity(new Intent(Screen3.this, Screen4.class));
                finish();
            }
        });
    }
}