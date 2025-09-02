package com.developer.moodifyai.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.developer.moodifyai.DashboardScreen;
import com.developer.moodifyai.R;

public class ResetPasswordSuccessScreen extends AppCompatActivity {
    AppCompatButton btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password_success_screen);

        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPasswordSuccessScreen.this, LoginScreen.class));
                finish();
            }
        });
    }
}