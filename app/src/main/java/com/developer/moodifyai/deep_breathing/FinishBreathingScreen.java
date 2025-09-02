package com.developer.moodifyai.deep_breathing;

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

import com.airbnb.lottie.LottieAnimationView;
import com.developer.moodifyai.DashboardScreen;
import com.developer.moodifyai.R;
import com.developer.moodifyai.user_emotion.AngryScreen;
import com.developer.moodifyai.user_emotion.HappyScreen;
import com.developer.moodifyai.user_emotion.NeutralScreen;
import com.developer.moodifyai.user_emotion.SadScreen;
import com.developer.moodifyai.user_emotion.VeryHappyScreen;

public class FinishBreathingScreen extends AppCompatActivity {

    AppCompatButton btn_repeat, btn_done;
    LottieAnimationView animationView;
    ImageView img_angry, img_happy, img_neutral, img_sad, img_very_happy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_finish_breathing_screen);

        animationView = findViewById(R.id.lottie_finish);
        btn_repeat = findViewById(R.id.btn_repeat);
        btn_done = findViewById(R.id.btn_done);
        img_angry = findViewById(R.id.img_angry);
        img_happy = findViewById(R.id.img_happy);
        img_neutral = findViewById(R.id.img_neutral);
        img_sad = findViewById(R.id.img_sad);
        img_very_happy = findViewById(R.id.img_very_happy);

        img_angry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishBreathingScreen.this, AngryScreen.class);
                intent.putExtra("cameFrom", "FinishBreathingScreen");
                startActivity(intent);
                finish();
            }
        });

        img_happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishBreathingScreen.this, HappyScreen.class);
                intent.putExtra("cameFrom", "FinishBreathingScreen");
                startActivity(intent);
                finish();
            }
        });

        img_neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishBreathingScreen.this, NeutralScreen.class);
                intent.putExtra("cameFrom", "FinishBreathingScreen");
                startActivity(intent);
                finish();
            }
        });

        img_sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishBreathingScreen.this, SadScreen.class);
                intent.putExtra("cameFrom", "FinishBreathingScreen");
                startActivity(intent);
                finish();
            }
        });

        img_very_happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinishBreathingScreen.this, VeryHappyScreen.class);
                intent.putExtra("cameFrom", "FinishBreathingScreen");
                startActivity(intent);
                finish();
            }
        });

        btn_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FinishBreathingScreen.this, DeepBreathingScreen.class);
                startActivity(intent);
                finish();
            }
        });

        btn_done.setOnClickListener(view -> {
            Intent intent = new Intent(FinishBreathingScreen.this, DashboardScreen.class);
            intent.putExtra("navigate_to", "ExploreFragment");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }
}