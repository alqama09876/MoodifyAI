package com.developer.moodifyai.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.developer.moodifyai.GetStartedScreen;
import com.developer.moodifyai.R;
import com.developer.moodifyai.auth.LoginScreen;
import com.developer.moodifyai.notepad.AddNotes;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AccountFragment extends Fragment {

    AppCompatButton btnLogout, btnSetNotificationTime, btn_google_logout;
    TextView tvNotificationTime;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private String userId;

    public AccountFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        btnLogout = view.findViewById(R.id.btn_logout);
        btn_google_logout = view.findViewById(R.id.btn_google_logout);
        btnSetNotificationTime = view.findViewById(R.id.btn_set_notification_time);
        tvNotificationTime = view.findViewById(R.id.tv_notification_time);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getUid();

        btnLogout.setOnClickListener(v -> logoutUser());

        btn_google_logout.setOnClickListener(v -> logoutUserFromGoogle());

        btnSetNotificationTime.setOnClickListener(v -> showTimePickerDialog());

        loadNotificationTime();

        return view;
    }

    private void logoutUserFromGoogle() {
        if (googleSignInClient == null) {
            googleSignInClient = GoogleSignIn.getClient(requireContext(),
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build()
            );
        }

        googleSignInClient.signOut().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                auth.signOut();
                Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), GetStartedScreen.class);
                requireActivity().startActivity(intent);
                requireActivity().finishAffinity();
            } else {
                Toast.makeText(getContext(), "Failed to log out. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();

        SharedPreferences moodifyPrefs = requireActivity().getSharedPreferences("MoodifyAI", MODE_PRIVATE);
        moodifyPrefs.edit().putBoolean("isLoggedIn", false).apply();

        Intent intent = new Intent(getActivity(), LoginScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        requireActivity().startActivity(intent);

        requireActivity().finishAffinity();
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), (TimePicker view, int hourOfDay, int minute) -> {
            String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            tvNotificationTime.setText(time);
            saveNotificationTimeToFirestore(hourOfDay, minute);
        }, 9, 0, true);

        timePickerDialog.show();
    }

    private void saveNotificationTimeToFirestore(int hour, int minute) {
        if (userId == null) return;

        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("hour", hour);
        notificationData.put("minute", minute);

        firestore.collection("notification_preferences")
                .document(userId)
                .set(notificationData)
                .addOnSuccessListener(aVoid -> {
                    showSnackbar(getActivity(), null, "Saved Notification Successfully", false);
                })
                .addOnFailureListener(e -> {
                    showSnackbar(getActivity(), null, "Error: " + e.getMessage(), true);

                });
    }

    private void loadNotificationTime() {
        if (userId == null) return;

        firestore.collection("notification_preferences")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        int hour = documentSnapshot.getLong("hour").intValue();
                        int minute = documentSnapshot.getLong("minute").intValue();

                        String time = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                        tvNotificationTime.setText(time);
                    }
                })
                .addOnFailureListener(e -> {
                    showSnackbar(getActivity(), null, "Notification not loaded" + e.getMessage(), true);
                });
    }

    public static void showSnackbar(Context context, View view, String message, boolean isError) {
        if (view == null) {
            view = ((android.app.Activity) context).findViewById(android.R.id.content);
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
}