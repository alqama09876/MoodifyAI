package com.developer.moodifyai.user_emotion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.developer.moodifyai.DashboardScreen;
import com.developer.moodifyai.R;
import com.developer.moodifyai.TherapistDashboardScreen;
import com.developer.moodifyai.deep_breathing.FinishBreathingScreen;
import com.developer.moodifyai.meditation.FinishMeditationScreen;
import com.developer.moodifyai.utils.MoodUtils;

public class NeutralScreen extends AppCompatActivity {

    ImageView btn_close;
    AppCompatButton btn_neutral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_neutral_screen);

        btn_close = findViewById(R.id.btn_close);
        btn_neutral = findViewById(R.id.btn_neutral);

        btn_close.setOnClickListener(v -> {
            String cameFrom = getIntent().getStringExtra("cameFrom");

            if ("FinishMeditationScreen".equals(cameFrom)) {
                Intent intent = new Intent(NeutralScreen.this, FinishMeditationScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else if ("FinishBreathingScreen".equals(cameFrom)) {
                Intent intent = new Intent(NeutralScreen.this, FinishBreathingScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else if ("TherapistHomeFragment".equals(cameFrom)) {
                Intent intent = new Intent(NeutralScreen.this, TherapistDashboardScreen.class);
                intent.putExtra("navigate_to", "TherapistHomeFragment");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(NeutralScreen.this, DashboardScreen.class);
                intent.putExtra("navigate_to", "HomeFragment");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        btn_neutral.setOnClickListener(v -> MoodUtils.saveMoodBasedOnFirestoreRole(NeutralScreen.this, "Neutral"));
    }
}

//package com.developer.moodifyai.user_emotion;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.ImageView;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.AppCompatButton;
//
//import com.developer.moodifyai.DashboardScreen;
//import com.developer.moodifyai.R;
//import com.developer.moodifyai.TherapistDashboardScreen;
//import com.developer.moodifyai.deep_breathing.FinishBreathingScreen;
//import com.developer.moodifyai.meditation.FinishMeditationScreen;
//import com.developer.moodifyai.utils.MoodUtils;
//
//public class NeutralScreen extends AppCompatActivity {
//
//    ImageView btn_close;
//    AppCompatButton btn_neutral;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_neutral_screen);
//
//        btn_close = findViewById(R.id.btn_close);
//        btn_neutral = findViewById(R.id.btn_neutral);
//
////        btn_close.setOnClickListener(v -> {
////            Intent intent = new Intent(NeutralScreen.this, DashboardScreen.class);
////            intent.putExtra("navigate_to", "HomeFragment");
////            startActivity(intent);
////            finish();
////        });
//
//        btn_close.setOnClickListener(v -> {
//            String cameFrom = getIntent().getStringExtra("cameFrom");
//
//            if ("FinishMeditationScreen".equals(cameFrom)) {
//                Intent intent = new Intent(NeutralScreen.this, FinishMeditationScreen.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//            } else if ("FinishBreathingScreen".equals(cameFrom)) {
//                Intent intent = new Intent(NeutralScreen.this, FinishBreathingScreen.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//            } else if ("TherapistHomeFragment".equals(cameFrom)){
//                Intent intent = new Intent(NeutralScreen.this, TherapistDashboardScreen.class);
//                intent.putExtra("navigate_to", "TherapistHomeFragment");
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//            } else {
//                Intent intent = new Intent(NeutralScreen.this, DashboardScreen.class);
//                intent.putExtra("navigate_to", "HomeFragment");
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//            }
//        });
//
////        btn_close.setOnClickListener(v -> {
////            String cameFrom = getIntent().getStringExtra("cameFrom");
////
////            if ("FinishMeditationScreen".equals(cameFrom)) {
////                Intent intent = new Intent(NeutralScreen.this, FinishMeditationScreen.class);
////                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                startActivity(intent);
////                finish();
////            } else if ("FinishBreathingScreen".equals(cameFrom)) {
////                Intent intent = new Intent(NeutralScreen.this, FinishBreathingScreen.class);
////                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                startActivity(intent);
////                finish();
////            } else {
////                Intent intent = new Intent(NeutralScreen.this, DashboardScreen.class);
////                intent.putExtra("navigate_to", "HomeFragment");
////                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                startActivity(intent);
////                finish();
////            }
////        });
//
//        btn_neutral.setOnClickListener(v -> MoodUtils.saveMood(NeutralScreen.this, "Neutral"));
//    }
//}