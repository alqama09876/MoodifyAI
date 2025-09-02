package com.developer.moodifyai;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.developer.moodifyai.fragment.TherapistAccountFragment;
import com.developer.moodifyai.fragment.TherapistAppointmentFragment;
import com.developer.moodifyai.fragment.TherapistHomeFragment;
import com.developer.moodifyai.fragment.TherapistNotificationFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

public class TherapistDashboardScreen extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_therapist_dashboard_screen);

        bottomNavigationView = findViewById(R.id.bn_view);

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new TherapistHomeFragment());
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                int id = item.getItemId();

                if (id == R.id.bnav_home) {
                    selectedFragment = new TherapistHomeFragment();
                } else if (id == R.id.bnav_appointment) {
                    selectedFragment = new TherapistAppointmentFragment();
                } else if (id == R.id.bnav_notification) {
                    selectedFragment = new TherapistNotificationFragment();
                } else if (id == R.id.bnav_account) {
                    selectedFragment = new TherapistAccountFragment();
                }

                return loadFragment(selectedFragment);
            }
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}