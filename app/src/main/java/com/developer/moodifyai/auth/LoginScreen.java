package com.developer.moodifyai.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.developer.moodifyai.DashboardScreen;
import com.developer.moodifyai.GetStartedScreen;
import com.developer.moodifyai.R;
import com.developer.moodifyai.notepad.AddNotes;
import com.developer.moodifyai.user_personal_info.Screen1;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginScreen extends AppCompatActivity {
    EditText edt_email, edt_password;
    AppCompatButton btn_login;
    FirebaseAuth auth;
    private boolean isPasswordVisible = false;
    TextView btn_signup, txt_forgot_password;
    ImageView btn_back;
    GoogleSignInOptions gso;
    FirebaseFirestore db;
    GoogleSignInClient googleSignInClient;
    int RC_SIGN_IN = 100;
    LinearLayout ll_google;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_screen);

        btn_back = findViewById(R.id.btn_back);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);
        btn_signup = findViewById(R.id.btn_signup);
        txt_forgot_password = findViewById(R.id.txt_forgot_password);
        ll_google = findViewById(R.id.ll_google);
        progressBar = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginScreen.this, RegisterScreen.class));
                finish();
            }
        });

        txt_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginScreen.this, ForgetPasswordScreen.class);
                intent.putExtra("SOURCE_SCREEN", "LoginScreen"); // Pass extra data
                startActivity(intent);
                finish();
            }
        });

//        txt_forgot_password.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoginScreen.this, ForgetPasswordScreen.class));
//                finish();
//            }
//        });

        edt_password.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                int drawableRight = 2;
                if (event.getRawX() >= (edt_password.getRight() - edt_password.getCompoundDrawables()[drawableRight].getBounds().width())) {
                    togglePasswordVisibility();
                    return true;
                }
            }
            return false;
        });

        edt_password.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                edt_password.post(() -> {
                    if (isPasswordVisible) {
                        edt_password.clearFocus();
                    }
                });
            }
        });

        btn_login.setOnClickListener(view -> {
            String email = edt_email.getText().toString();
            String password = edt_password.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                edt_email.setError("Required");
                edt_password.setError("Required");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edt_email.setError("Invalid email format");
            } else {
                loginUser(email, password);
            }
        });

        // google auth...
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(LoginScreen.this, gso);

        ll_google.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            signInWithGoogle();
        });
    }

    private void togglePasswordVisibility() {
        int drawableStart = R.drawable.password_icon;

        if (isPasswordVisible) {
            edt_password.setCompoundDrawablesWithIntrinsicBounds(drawableStart, 0, R.drawable.eye_close_icon, 0);
            edt_password.setTransformationMethod(new PasswordTransformationMethod());
        } else {
            edt_password.setCompoundDrawablesWithIntrinsicBounds(drawableStart, 0, R.drawable.eye_open_icon, 0);
            edt_password.setTransformationMethod(null);
        }

        isPasswordVisible = !isPasswordVisible;
        edt_password.setSelection(edt_password.getText().length());
    }

    private void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                SharedPreferences.Editor editor = getSharedPreferences("MoodifyAI", MODE_PRIVATE).edit();
                editor.putBoolean("isLoggedIn", true);
                editor.apply();
                showSnackbar(LoginScreen.this, null, "Login Successfully", false);
                startActivity(new Intent(LoginScreen.this, DashboardScreen.class));
                finish();
            }
        }).addOnFailureListener(e -> {
            showSnackbar(LoginScreen.this, null, "Error: " + e.getMessage(), true);
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

    // google auth...
    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                progressBar.setVisibility(View.GONE);
                Log.e("GoogleSignIn", "Google sign-in failed", e);
                Toast.makeText(this, "Sign-in Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        checkUserInDatabase();
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void checkUserInDatabase() {
        String uid = auth.getCurrentUser().getUid();

        db.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    progressBar.setVisibility(View.GONE);
                    if (documentSnapshot.exists()) {
                        navigateToDashboard();
                    } else {
                        navigateToScreen1();
                        saveNewUserToDatabase(uid);
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, "Error fetching user data", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveNewUserToDatabase(String uid) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) return;

        String email = currentUser.getEmail();
        String photoURL = String.valueOf(currentUser.getPhotoUrl());

        Map<String, Object> userData = new HashMap<>();
        userData.put("uid", uid);
        userData.put("email", email);
        userData.put("photoURL", photoURL);
        userData.put("created_at", System.currentTimeMillis());
        userData.put("role", "user");

        db.collection("users").document(uid).set(userData)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "User added successfully"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error adding user", e));
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(LoginScreen.this, DashboardScreen.class);
        startActivity(intent);
        finish();
    }

    private void navigateToScreen1() {
        Intent intent = new Intent(LoginScreen.this, Screen1.class);
        startActivity(intent);
        finish();
    }
}