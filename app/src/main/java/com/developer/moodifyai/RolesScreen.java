package com.developer.moodifyai;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developer.moodifyai.auth.AdminLoginScreen;
import com.developer.moodifyai.auth.TherapistLoginScreen;
import com.developer.moodifyai.auth.TherapistRegisterScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class RolesScreen extends AppCompatActivity {
    ImageView img_user, img_therapist, img_admin;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_roles_screen);

        img_user = findViewById(R.id.img_user);
        img_therapist = findViewById(R.id.img_therapist);
        img_admin = findViewById(R.id.img_admin);

        db = FirebaseFirestore.getInstance();

        img_user.setOnClickListener(v -> {
            startActivity(new Intent(RolesScreen.this, GetStartedScreen.class));
            finish();
        });

        img_therapist.setOnClickListener(v -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                checkTherapistStatus(currentUser.getUid());
            } else {
                startActivity(new Intent(RolesScreen.this, TherapistLoginScreen.class));
                finish();
            }
        });

        img_admin.setOnClickListener(v -> {
            startActivity(new Intent(RolesScreen.this, AdminLoginScreen.class));
            finish();
        });
    }

    private void checkTherapistStatus(String uid) {
        // First check approved therapists
        db.collection("therapists").document(uid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        startActivity(new Intent(RolesScreen.this, TherapistDashboardScreen.class));
                        finish();
                    } else {
                        // Check pending therapists
                        checkPendingStatus(uid);
                    }
                });
    }

    private void checkPendingStatus(String uid) {
        db.collection("pending_therapists").document(uid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        DocumentSnapshot document = task.getResult();
                        Boolean verified = document.getBoolean("verified");
                        Boolean rejected = document.getBoolean("rejected");

                        if (verified != null && verified) {
                            moveToApprovedTherapists(uid, document.getData());
                        } else if (rejected != null && rejected) {
                            showRejectionDialog();
                        } else {
                            showPendingDialog();
                        }
                    } else {
                        // Not found in any collection - go to login
                        startActivity(new Intent(RolesScreen.this, TherapistLoginScreen.class));
                        finish();
                    }
                });
    }

    private void moveToApprovedTherapists(String uid, Map<String, Object> data) {
        // Remove verification-specific fields
        data.remove("verified");
        data.remove("rejected");

        // Add to therapists collection
        db.collection("therapists").document(uid)
                .set(data)
                .addOnSuccessListener(aVoid -> {
                    // Remove from pending
                    db.collection("pending_therapists").document(uid).delete()
                            .addOnSuccessListener(aVoid1 -> {
                                startActivity(new Intent(RolesScreen.this, TherapistDashboardScreen.class));
                                finish();
                            });
                });
    }

    private void showRejectionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Request Rejected")
                .setMessage("Your therapist request has been rejected by admin.")
                .setPositiveButton("OK", (dialog, which) -> {
                    finishAffinity(); // Close app
                })
                .setCancelable(false)
                .show();
    }

    private void showPendingDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Request Pending")
                .setMessage("Your request is still under review. Please wait for admin approval.")
                .setPositiveButton("OK", (dialog, which) -> {
                    finishAffinity(); // Close app
                })
                .setCancelable(false)
                .show();
    }
}