package com.developer.moodifyai.meditation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developer.moodifyai.DashboardScreen;
import com.developer.moodifyai.R;
import com.developer.moodifyai.quotes.QuotesScreen;

public class MeditationScreen extends AppCompatActivity {

    ImageView btnBack;
    AppCompatButton btnLetsStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_meditation_screen);

        btnBack = findViewById(R.id.btn_back);
        btnLetsStart = findViewById(R.id.btn_lets_start);

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(MeditationScreen.this, DashboardScreen.class);
            boolean cameFromHome = getIntent().getBooleanExtra("cameFromHome", false);
            if (cameFromHome) {
                intent.putExtra("navigate_to", "HomeFragment");
            } else {
                intent.putExtra("navigate_to", "ExploreFragment");
            }
            startActivity(intent);
            finish();
        });

        btnLetsStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeditationScreen.this, StartMeditationScreen.class);
                intent.putExtra("startTimer", true);
                startActivity(intent);
            }
        });
    }
}