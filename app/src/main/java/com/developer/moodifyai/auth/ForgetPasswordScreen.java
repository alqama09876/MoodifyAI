package com.developer.moodifyai.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.developer.moodifyai.R;
import com.developer.moodifyai.notepad.AddNotes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordScreen extends AppCompatActivity {

    ImageView btn_back;
    AppCompatButton btn_reset_password;
    EditText edt_email;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forget_password_screen);

        btn_back = findViewById(R.id.btn_back);
        btn_reset_password = findViewById(R.id.btn_reset_password);
        edt_email = findViewById(R.id.edt_email);
        auth = FirebaseAuth.getInstance();

        btn_back.setOnClickListener(v -> {
            String sourceScreen = getIntent().getStringExtra("SOURCE_SCREEN");

            if (sourceScreen != null) {
                if (sourceScreen.equals("LoginScreen")) {
                    startActivity(new Intent(ForgetPasswordScreen.this, LoginScreen.class));
                } else if (sourceScreen.equals("TherapistLoginScreen")) {
                    startActivity(new Intent(ForgetPasswordScreen.this, TherapistLoginScreen.class));
                }
            }
            finish();
        });

//        btn_back.setOnClickListener(v -> {
//            finish();
//        });

        btn_reset_password.setOnClickListener(v -> {
            String email = edt_email.getText().toString().trim();

            if (email.isEmpty()) {
                edt_email.setError("Enter your email");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edt_email.setError("Enter a valid email");
            } else {
                resetPassword(email);
            }
        });
    }

    private void resetPassword(String email) {
        btn_reset_password.setEnabled(false);

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                btn_reset_password.setEnabled(true);
                if (task.isSuccessful()) {
                    showSnackbar(ForgetPasswordScreen.this, null, "Reset email sent!", false);
                    edt_email.setText("");
                    startActivity(new Intent(ForgetPasswordScreen.this, ResetPasswordSuccessScreen.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                btn_reset_password.setEnabled(true);
                showSnackbar(ForgetPasswordScreen.this, null, "Error: " + e.getMessage(), true);
            }
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