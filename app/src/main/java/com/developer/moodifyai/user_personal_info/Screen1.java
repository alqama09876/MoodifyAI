package com.developer.moodifyai.user_personal_info;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developer.moodifyai.DashboardScreen;
import com.developer.moodifyai.R;
import com.developer.moodifyai.auth.RegisterScreen;

public class Screen1 extends AppCompatActivity {
    ImageView btn_back;
    AppCompatButton btn_continue;
    EditText edt_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_screen1);

        btn_back = findViewById(R.id.btn_back);
        btn_continue = findViewById(R.id.btn_continue);
        edt_name = findViewById(R.id.edt_name);

        SharedPreferences prefs = getSharedPreferences("MoodifyAI", MODE_PRIVATE);
        String storedName = prefs.getString("user_name", "");
        edt_name.setText(storedName);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edt_name.getText().toString().trim();
                if (name.isEmpty()) {
                    edt_name.setError("Required! Name is Mandatory");
                    return;
                }

                SharedPreferences.Editor editor = getSharedPreferences("MoodifyAI", MODE_PRIVATE).edit();
                editor.putString("user_name", name);
                editor.apply();

                startActivity(new Intent(Screen1.this, Screen2.class));
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("MoodifyAI", MODE_PRIVATE);
        boolean isSetupComplete = prefs.getBoolean("isSetupComplete", false);

        if (isSetupComplete) {
            startActivity(new Intent(Screen1.this, DashboardScreen.class));
            finish();
        }
    }
}