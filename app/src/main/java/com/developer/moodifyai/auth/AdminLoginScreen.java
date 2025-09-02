package com.developer.moodifyai.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developer.moodifyai.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminLoginScreen extends AppCompatActivity {
    EditText edt_email, edt_password;
    AppCompatButton btn_login;
    FirebaseAuth auth;
    private boolean isPasswordVisible = false;
    FirebaseFirestore db;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_login_screen);

        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


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

        loginAdmin(
                edt_email.getText().toString().trim(),
                edt_password.getText().toString().trim()
        );
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

    private void loginAdmin(String email, String password) {
    }
}