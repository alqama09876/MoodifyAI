package com.developer.moodifyai.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developer.moodifyai.R;
import com.developer.moodifyai.TherapistDashboardScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class TherapistLoginScreen extends AppCompatActivity {

    EditText edt_email, edt_password;
    AppCompatButton btn_login;
    FirebaseAuth auth;
    private boolean isPasswordVisible = false;
    TextView btn_signup, txt_forgot_password;
    FirebaseFirestore db;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_therapist_login_screen);

        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);
        txt_forgot_password = findViewById(R.id.txt_forgot_password);
        progressBar = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TherapistLoginScreen.this, TherapistRegisterScreen.class));
                finish();
            }
        });

        txt_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TherapistLoginScreen.this, ForgetPasswordScreen.class);
                intent.putExtra("SOURCE_SCREEN", "TherapistLoginScreen"); // Pass extra data
                startActivity(intent);
                finish();
            }
        });

        edt_password.setOnTouchListener(this::handlePasswordToggle);

        btn_login.setOnClickListener(v -> validateAndSubmit());

    }

    private boolean handlePasswordToggle(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int drawableRight = 2;
            if (event.getRawX() >= (edt_password.getRight() - edt_password.getCompoundDrawables()[drawableRight].getBounds().width())) {
                togglePasswordVisibility();
                return true;
            }
        }
        return false;
    }

    private void togglePasswordVisibility() {
        int drawableStart = R.drawable.password_icon;
        edt_password.setTransformationMethod(isPasswordVisible ? new PasswordTransformationMethod() : null);
        edt_password.setCompoundDrawablesWithIntrinsicBounds(drawableStart, 0,
                isPasswordVisible ? R.drawable.eye_close_icon : R.drawable.eye_open_icon, 0);
        isPasswordVisible = !isPasswordVisible;
        edt_password.setSelection(edt_password.getText().length());
    }

    private void validateAndSubmit() {
        if (!validateEmail() || !validatePassword()) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btn_signup.setEnabled(false);

        loginTherapist(
                edt_email.getText().toString().trim(),
                edt_password.getText().toString().trim()
        );
    }

    private void loginTherapist(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    btn_signup.setEnabled(true);

                    SharedPreferences therapistPrefs = getSharedPreferences("TherapistPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = therapistPrefs.edit();
                    editor.putBoolean("isTherapistLoggedIn", true);
                    editor.apply();

                    startActivity(new Intent(TherapistLoginScreen.this, TherapistDashboardScreen.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                btn_signup.setEnabled(true);
            }
        });
    }

    private boolean validateEmail() {
        String email = edt_email.getText().toString().trim();
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edt_email.setError("Enter a valid email");
            return false;
        }
        return true;
    }

    private boolean validatePassword() {
        String password = edt_password.getText().toString();
        if (TextUtils.isEmpty(password) || password.length() < 6 || !password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{6,}$")) {
            edt_password.setError("Password must be at least 6 characters and contain at least one uppercase letter, one lowercase letter, one digit, and one special character");
            return false;
        }
        return true;
    }

    private static View getRootView(Context context) {
        try {
            return ((android.app.Activity) context).findViewById(R.id.ll_main);
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

    @Override
    protected void onStart() {
        super.onStart();
        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), TherapistDashboardScreen.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}