package com.developer.moodifyai.user_emotion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class SadScreen extends AppCompatActivity {

    ImageView btn_close;
    AppCompatButton btn_unhappy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sad_screen);

        btn_close = findViewById(R.id.btn_close);
        btn_unhappy = findViewById(R.id.btn_unhappy);

        btn_close.setOnClickListener(v -> {
            String cameFrom = getIntent().getStringExtra("cameFrom");

            if ("FinishMeditationScreen".equals(cameFrom)) {
                Intent intent = new Intent(SadScreen.this, FinishMeditationScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else if ("FinishBreathingScreen".equals(cameFrom)) {
                Intent intent = new Intent(SadScreen.this, FinishBreathingScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else if ("TherapistHomeFragment".equals(cameFrom)) {
                Intent intent = new Intent(SadScreen.this, TherapistDashboardScreen.class);
                intent.putExtra("navigate_to", "TherapistHomeFragment");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(SadScreen.this, DashboardScreen.class);
                intent.putExtra("navigate_to", "HomeFragment");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        btn_unhappy.setOnClickListener(v -> MoodUtils.saveMoodBasedOnFirestoreRole(SadScreen.this, "Sad"));
    }
}

//package com.developer.moodifyai.user_emotion;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
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
//public class SadScreen extends AppCompatActivity {
//
//    ImageView btn_close;
//    AppCompatButton btn_unhappy;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_sad_screen);
//
//        btn_close = findViewById(R.id.btn_close);
//        btn_unhappy = findViewById(R.id.btn_unhappy);
//
////        btn_close.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent intent = new Intent(SadScreen.this, DashboardScreen.class);
////                intent.putExtra("navigate_to", "HomeFragment");
////                startActivity(intent);
////                finish();
////            }
////        });
//
//        btn_close.setOnClickListener(v -> {
//            String cameFrom = getIntent().getStringExtra("cameFrom");
//
//            if ("FinishMeditationScreen".equals(cameFrom)) {
//                Intent intent = new Intent(SadScreen.this, FinishMeditationScreen.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//            } else if ("FinishBreathingScreen".equals(cameFrom)) {
//                Intent intent = new Intent(SadScreen.this, FinishBreathingScreen.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//            } else if ("TherapistHomeFragment".equals(cameFrom)) {
//                Intent intent = new Intent(SadScreen.this, TherapistDashboardScreen.class);
//                intent.putExtra("navigate_to", "TherapistHomeFragment");
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//            } else {
//                Intent intent = new Intent(SadScreen.this, DashboardScreen.class);
//                intent.putExtra("navigate_to", "HomeFragment");
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//
////        btn_close.setOnClickListener(v -> {
////            String cameFrom = getIntent().getStringExtra("cameFrom");
////
////            if ("FinishMeditationScreen".equals(cameFrom)) {
////                Intent intent = new Intent(SadScreen.this, FinishMeditationScreen.class);
////                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                startActivity(intent);
////                finish();
////            } else if ("FinishBreathingScreen".equals(cameFrom)) {
////                Intent intent = new Intent(SadScreen.this, FinishBreathingScreen.class);
////                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                startActivity(intent);
////                finish();
////            } else {
////                Intent intent = new Intent(SadScreen.this, DashboardScreen.class);
////                intent.putExtra("navigate_to", "HomeFragment");
////                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                startActivity(intent);
////                finish();
////            }
////        });
//
//        btn_unhappy.setOnClickListener(v -> MoodUtils.saveMood(SadScreen.this, "Sad"));
//
//    }
//}