package com.developer.moodifyai.user_personal_info;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.developer.moodifyai.DashboardScreen;
import com.developer.moodifyai.R;
import com.developer.moodifyai.model.UserData;
import com.developer.moodifyai.notepad.AddNotes;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PreparingPlanScreen extends AppCompatActivity {
    AppCompatButton btn_continue;
    ProgressBar circularProgressBar;
    TextView progressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_preparing_plan_screen);

        btn_continue = findViewById(R.id.btn_continue);
        circularProgressBar = findViewById(R.id.circularProgressBar);
        progressText = findViewById(R.id.progressText);

        animateProgressBar(75);

        btn_continue.setOnClickListener(v -> {
            SharedPreferences moodifyPrefs = getSharedPreferences("MoodifyAI", MODE_PRIVATE);

            String userName = moodifyPrefs.getString("user_name", "");
            String userGender = moodifyPrefs.getString("user_gender", "");
            int userAge = moodifyPrefs.getInt("user_age", 18);

            Set<String> selectedGoalsSet = moodifyPrefs.getStringSet("selected_goals", new HashSet<>());
            Set<String> mentalHealthCausesSet = moodifyPrefs.getStringSet("mentalHealth_cause", new HashSet<>());
            List<String> selectedGoals = new ArrayList<>(selectedGoalsSet);
            List<String> mentalHealthCauses = new ArrayList<>(mentalHealthCausesSet);

            String stressFrequency = moodifyPrefs.getString("stressFrequency", "");
            String healthyEatingHabit = moodifyPrefs.getString("healthyEatingHabit", "");
            String triedMeditationBefore = moodifyPrefs.getString("triedMeditationBefore", "");
            String sleepQualityLevel = moodifyPrefs.getString("sleepQualityLevel", "");
            String happinessLevel = moodifyPrefs.getString("happinessLevel", "");

            Log.d("PreparingPlanScreen", "Fetched Goals: " + selectedGoals);
            Log.d("PreparingPlanScreen", "Fetched Causes: " + mentalHealthCauses);

            UserData userData = new UserData(userName, userGender, userAge, selectedGoals, mentalHealthCauses, stressFrequency, healthyEatingHabit, triedMeditationBefore, sleepQualityLevel, happinessLevel);

            saveDataToFirebase(userData);

            SharedPreferences.Editor editor = moodifyPrefs.edit();
            editor.putBoolean("isSetupComplete", true);
            editor.putBoolean("isLoggedIn", true);
            editor.apply();

            startActivity(new Intent(PreparingPlanScreen.this, DashboardScreen.class));
            finish();
        });
    }

    private void saveDataToFirebase(UserData userData) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = auth.getCurrentUser().getUid();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userName", userData.getUserName());
        userMap.put("userGender", userData.getUserGender());
        userMap.put("userAge", userData.getUserAge());
        userMap.put("selectedGoals", userData.getMainGoal());
        userMap.put("mentalHealthCauses", userData.getMentalHealthCause());
        userMap.put("stressFrequency", userData.getStressFrequency());
        userMap.put("healthyEatingHabit", userData.getHealthyEatingHabit());
        userMap.put("triedMeditationBefore", userData.getTriedMeditationBefore());
        userMap.put("sleepQualityLevel", userData.getSleepQualityLevel());
        userMap.put("happinessLevel", userData.getHappinessLevel());

        db.collection("user_personal_data").document(uid).set(userMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showSnackbar(PreparingPlanScreen.this, findViewById(R.id.rl_main), "User Data saved successfully", false);
                    } else {
                        showSnackbar(PreparingPlanScreen.this, findViewById(R.id.rl_main), "Error: " + task.getException(), true);
                    }
                })
                .addOnFailureListener(e -> {
                    showSnackbar(PreparingPlanScreen.this, findViewById(R.id.rl_main), "Error: " + e.getMessage(), true);
                });
    }

    public static void showSnackbar(Context context, View view, String message, boolean isError) {
        if (view == null) {
            view = ((android.app.Activity) context).findViewById(R.id.rl_main);
        }

        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);

        if (isError) {
            snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.red));
            snackbar.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.olive_green));
            snackbar.setTextColor(ContextCompat.getColor(context, R.color.white));
        }

        snackbar.setAction("DISMISS", v -> snackbar.dismiss());
        snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.white));

        snackbar.show();
    }

    private void animateProgressBar(int targetProgress) {
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(circularProgressBar, "progress", 0, targetProgress);
        progressAnimator.setDuration(1500);
        progressAnimator.start();

        progressAnimator.addUpdateListener(animation -> {
            int progress = (int) animation.getAnimatedValue();
            progressText.setText(progress + "%");
        });
    }
}