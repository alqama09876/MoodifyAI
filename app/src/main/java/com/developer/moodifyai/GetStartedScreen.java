package com.developer.moodifyai;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.developer.moodifyai.auth.LoginScreen;
import com.developer.moodifyai.auth.RegisterScreen;
import com.developer.moodifyai.user_personal_info.Screen1;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class GetStartedScreen extends AppCompatActivity {
    AppCompatButton btn_signup, btn_signin;
    GoogleSignInOptions gso;
    FirebaseFirestore db;
    FirebaseAuth auth;
    GoogleSignInClient googleSignInClient;
    int RC_SIGN_IN = 100;
    RelativeLayout btn_google;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get_started_screen);

        btn_signup = findViewById(R.id.btn_signup);
        btn_signin = findViewById(R.id.btn_signin);
        btn_google = findViewById(R.id.btn_google);
        progressBar = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        btn_signup.setOnClickListener(v -> {
            startActivity(new Intent(GetStartedScreen.this, RegisterScreen.class));
            finish();
        });

        btn_signin.setOnClickListener(v -> {
            startActivity(new Intent(GetStartedScreen.this, LoginScreen.class));
            finish();
        });

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(GetStartedScreen.this, gso);

        btn_google.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            signInWithGoogle();
        });
    }

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

        db.collection("users").document(uid).set(userData)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "User added successfully"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error adding user", e));
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(GetStartedScreen.this, DashboardScreen.class);
        startActivity(intent);
        finish();
    }

    private void navigateToScreen1() {
        Intent intent = new Intent(GetStartedScreen.this, Screen1.class);
        startActivity(intent);
        finish();
    }
}