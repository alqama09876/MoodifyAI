package com.developer.moodifyai;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.developer.moodifyai.auth.LoginScreen;
import com.developer.moodifyai.user_personal_info.Screen1;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreen extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(() -> {

            // Therapist login check using separate SharedPreferences (or any other flag you use)
            SharedPreferences therapistPrefs = getSharedPreferences("TherapistPrefs", MODE_PRIVATE);
            boolean isTherapistLoggedIn = therapistPrefs.getBoolean("isTherapistLoggedIn", false);

            if (isTherapistLoggedIn) {
                // Therapist logged in — directly go to Therapist Dashboard
                startActivity(new Intent(SplashScreen.this, TherapistDashboardScreen.class));
                finish();
                return;  // Don't execute user logic below
            }

            // User login check - unchanged
            SharedPreferences prefs = getSharedPreferences("MoodifyAI", MODE_PRIVATE);
            boolean isSetupComplete = prefs.getBoolean("isSetupComplete", false);
            boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

            if (auth.getCurrentUser() != null) {
                String uid = auth.getCurrentUser().getUid();
                db.collection("users").document(uid).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        if (isSetupComplete) {
                            startActivity(new Intent(SplashScreen.this, DashboardScreen.class));
                        } else {
                            startActivity(new Intent(SplashScreen.this, Screen1.class));
                        }
                    } else {
                        startActivity(new Intent(SplashScreen.this, RolesScreen.class));
                    }
                    finish();
                });
            } else {
                if (isLoggedIn) {
                    startActivity(new Intent(SplashScreen.this, DashboardScreen.class));
                } else {
                    startActivity(new Intent(SplashScreen.this, RolesScreen.class));
                }
                finish();
            }
        }, 2000);
    }
}


//package com.developer.moodifyai;
//
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.os.Handler;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.developer.moodifyai.auth.LoginScreen;
//import com.developer.moodifyai.user_personal_info.Screen1;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//public class SplashScreen extends AppCompatActivity {
//
//    FirebaseAuth auth = FirebaseAuth.getInstance();
//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_splash_screen);
//
//        new Handler().postDelayed(() -> {
//            SharedPreferences prefs = getSharedPreferences("MoodifyAI", MODE_PRIVATE);
//            boolean isSetupComplete = prefs.getBoolean("isSetupComplete", false);
//            boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
//
//            if (auth.getCurrentUser() != null) {
//                String uid = auth.getCurrentUser().getUid();
//                db.collection("users").document(uid).get().addOnCompleteListener(task -> {
//                    if (task.isSuccessful() && task.getResult().exists()) {
//                        if (isSetupComplete) {
//                            startActivity(new Intent(SplashScreen.this, DashboardScreen.class));
//                        } else {
//                            startActivity(new Intent(SplashScreen.this, Screen1.class));
//                        }
//                    } else {
//                        startActivity(new Intent(SplashScreen.this, RolesScreen.class));
//                    }
//                    finish();
//                });
//            } else {
//                if (isLoggedIn) {
//                    startActivity(new Intent(SplashScreen.this, DashboardScreen.class));
//                } else {
//                    startActivity(new Intent(SplashScreen.this, RolesScreen.class));
//                }
//                finish();
//            }
//        }, 2000);
//    }
//}