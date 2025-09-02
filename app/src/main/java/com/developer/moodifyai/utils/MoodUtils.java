package com.developer.moodifyai.utils;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.developer.moodifyai.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MoodUtils {

    public static void saveMoodBasedOnFirestoreRole(Context context, String mood) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = auth.getCurrentUser().getUid();

        // Check role in users collection
        db.collection("users").document(uid).get().addOnSuccessListener(userDoc -> {
            if (userDoc.exists() && "user".equalsIgnoreCase(userDoc.getString("role"))) {
                saveMood(context, mood);
            } else {
                // If not a user, check therapists collection
                db.collection("therapists").document(uid).get().addOnSuccessListener(therapistDoc -> {
                    if (therapistDoc.exists() && "therapist".equalsIgnoreCase(therapistDoc.getString("role"))) {
                        saveTherapistMood(context, mood);
                    } else {
                        showSnackbar(context, getRootView(context), "Unable to determine role. Mood not saved.", true);
                    }
                }).addOnFailureListener(e -> {
                    showSnackbar(context, getRootView(context), "Error: " + e.getMessage(), true);
                });
            }
        }).addOnFailureListener(e -> {
            showSnackbar(context, getRootView(context), "Error: " + e.getMessage(), true);
        });
    }

    public static void saveMood(Context context, String mood) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userID = auth.getCurrentUser().getUid();
        Calendar calendar = Calendar.getInstance();

        String year = new SimpleDateFormat("yyyy").format(calendar.getTime());
        String month = new SimpleDateFormat("MMMM").format(calendar.getTime());
        String week = "Week " + calendar.get(Calendar.WEEK_OF_MONTH);
        String date = new SimpleDateFormat("dd").format(calendar.getTime());

        HashMap<String, Object> moodData = new HashMap<>();
        moodData.put("mood", mood);
        moodData.put("timestamp", System.currentTimeMillis());

        db.collection("mood_tracker")
                .document(userID)
                .collection(year)
                .document(month)
                .collection(week)
                .document(date)
                .set(moodData)
                .addOnSuccessListener(aVoid -> {
                    View rootview = getRootView(context);
                    showSnackbar(context, rootview, "Mood saved successfully!", false);
                })
                .addOnFailureListener(e -> {
                    View rootview = getRootView(context);
                    showSnackbar(context, rootview, "Failed to save mood: " + e.getMessage(), true);
                });
    }

    public static void saveTherapistMood(Context context, String mood) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String therapistID = auth.getCurrentUser().getUid();
        Calendar calendar = Calendar.getInstance();

        String year = new SimpleDateFormat("yyyy").format(calendar.getTime());
        String month = new SimpleDateFormat("MMMM").format(calendar.getTime());
        String week = "Week " + calendar.get(Calendar.WEEK_OF_MONTH);
        String date = new SimpleDateFormat("dd").format(calendar.getTime());

        HashMap<String, Object> therapistMoodData = new HashMap<>();
        therapistMoodData.put("mood", mood);
        therapistMoodData.put("timestamp", System.currentTimeMillis());

        db.collection("therapist_mood_tracker")
                .document(therapistID)
                .collection(year)
                .document(month)
                .collection(week)
                .document(date)
                .set(therapistMoodData)
                .addOnSuccessListener(aVoid -> {
                    View rootview = getRootView(context);
                    showSnackbar(context, rootview, "Mood saved successfully!", false);
                })
                .addOnFailureListener(e -> {
                    View rootview = getRootView(context);
                    showSnackbar(context, rootview, "Failed to save mood: " + e.getMessage(), true);
                });
    }

    private static View getRootView(Context context) {
        try {
            return ((android.app.Activity) context).findViewById(R.id.rl_main);
        } catch (Exception e) {
            Toast.makeText(context, "Unable to find root view", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public static void showSnackbar(Context context, View view, String message, boolean isError) {
        if (view == null) {
            view = getRootView(context);
        }

        if (view != null) {
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
        } else {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }
}


//package com.developer.moodifyai.utils;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.core.content.ContextCompat;
//
//import com.developer.moodifyai.DashboardScreen;
//import com.developer.moodifyai.R;
//import com.google.android.material.snackbar.Snackbar;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.HashMap;
//
//public class MoodUtils {
//
//    public static void saveMood(Context context, String mood) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        String userID = auth.getCurrentUser().getUid();
//        Calendar calendar = Calendar.getInstance();
//
//        String year = new SimpleDateFormat("yyyy").format(calendar.getTime());
//        String month = new SimpleDateFormat("MMMM").format(calendar.getTime());
//        String week = "Week " + calendar.get(Calendar.WEEK_OF_MONTH);
//        String date = new SimpleDateFormat("dd").format(calendar.getTime());
//
//        HashMap<String, Object> moodData = new HashMap<>();
//        moodData.put("mood", mood);
//        moodData.put("timestamp", System.currentTimeMillis());
//
//        db.collection("mood_tracker")
//                .document(userID)
//                .collection(year)
//                .document(month)
//                .collection(week)
//                .document(date)
//                .set(moodData)
//                .addOnSuccessListener(aVoid -> {
//                    View rootview = getRootView(context);
//                    showSnackbar(context, rootview, "Mood saved successfully!", false);
////                    Intent intent = new Intent(context, DashboardScreen.class);
////                    intent.putExtra("navigate_to", "HomeFragment");
////                    context.startActivity(intent);
//                })
//                .addOnFailureListener(e -> {
//                    View rootview = getRootView(context);
//                    showSnackbar(context, rootview, "Failed to save mood: " + e.getMessage(), true);
//                });
//    }
//
//    public static void saveTherapistMood(Context context, String mood) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        String therapistID = auth.getCurrentUser().getUid();
//        Calendar calendar = Calendar.getInstance();
//
//        String year = new SimpleDateFormat("yyyy").format(calendar.getTime());
//        String month = new SimpleDateFormat("MMMM").format(calendar.getTime());
//        String week = "Week " + calendar.get(Calendar.WEEK_OF_MONTH);
//        String date = new SimpleDateFormat("dd").format(calendar.getTime());
//
//        HashMap<String, Object> therapistMoodData = new HashMap<>();
//        therapistMoodData.put("mood", mood);
//        therapistMoodData.put("timestamp", System.currentTimeMillis());
//
//        db.collection("therapist_mood_tracker")
//                .document(therapistID)
//                .collection(year)
//                .document(month)
//                .collection(week)
//                .document(date)
//                .set(therapistMoodData)
//                .addOnSuccessListener(aVoid -> {
//                    View rootview = getRootView(context);
//                    showSnackbar(context, rootview, "Mood saved successfully!", false);
////                    Intent intent = new Intent(context, DashboardScreen.class);
////                    intent.putExtra("navigate_to", "HomeFragment");
////                    context.startActivity(intent);
//                })
//                .addOnFailureListener(e -> {
//                    View rootview = getRootView(context);
//                    showSnackbar(context, rootview, "Failed to save mood: " + e.getMessage(), true);
//                });
//    }
//
//    private static View getRootView(Context context) {
//        try {
//            return ((android.app.Activity) context).findViewById(R.id.rl_main);
//        } catch (Exception e) {
//            Toast.makeText(context, "Unable to find root view", Toast.LENGTH_SHORT).show();
//            return null;
//        }
//    }
//
//    public static void showSnackbar(Context context, View view, String message, boolean isError) {
//        if (view == null) {
//            view = getRootView(context);
//        }
//
//        if (view != null) {
//            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
//
//            if (isError) {
//                snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.red));
//                snackbar.setTextColor(ContextCompat.getColor(context, R.color.white));
//            } else {
//                snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.olive_green));
//                snackbar.setTextColor(ContextCompat.getColor(context, R.color.white));
//            }
//
//            snackbar.setAction("DISMISS", v -> snackbar.dismiss());
//            snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.white));
//
//            snackbar.show();
//        } else {
//            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
//        }
//    }
//}