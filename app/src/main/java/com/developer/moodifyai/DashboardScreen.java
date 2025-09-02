package com.developer.moodifyai;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.developer.moodifyai.fragment.AccountFragment;
import com.developer.moodifyai.fragment.ExploreFragment;
import com.developer.moodifyai.fragment.HomeFragment;
import com.developer.moodifyai.fragment.InsightsFragment;
import com.developer.moodifyai.notify.NotifyManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DashboardScreen extends AppCompatActivity {
    BottomNavigationView bn_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard_screen);

        bn_view = findViewById(R.id.bn_view);

        scheduleNotificationFromPreferences();

        if (getIntent().hasExtra("navigate_to")) {
            String destination = getIntent().getStringExtra("navigate_to");
            if ("ExploreFragment".equals(destination) && !isFragmentLoaded("ExploreFragment")) {
                clearBackStack();
                loadFragment(new ExploreFragment(), 0);
            } else if ("HomeFragment".equals(destination) && !isFragmentLoaded("HomeFragment")) {
                clearBackStack();
                loadFragment(new HomeFragment(), 0);
            }
        } else {
            if (!isFragmentLoaded("HomeFragment")) {
                loadFragment(new HomeFragment(), 0);
            }
        }

//        // HomeFragment...
//        if (getIntent().hasExtra("navigate_to")) {
//            String destination = getIntent().getStringExtra("navigate_to");
//            if ("HomeFragment".equals(destination) && !isFragmentLoaded("HomeFragment")) {
//                clearBackStack();
//                loadFragment(new HomeFragment(), 0);
//            }
//        } else {
//            if (!isFragmentLoaded("HomeFragment")) {
//                loadFragment(new HomeFragment(), 0);
//            }
//        }

        bn_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.bnav_home) {
                    loadFragment(new HomeFragment(), 1);
                } else if (id == R.id.bnav_explore) {
                    loadFragment(new ExploreFragment(), 1);
                } else if (id == R.id.bnav_insights) {
                    loadFragment(new InsightsFragment(), 1);
                } else if (id == R.id.bnav_account) {
                    loadFragment(new AccountFragment(), 1);
                }
                return true;
            }
        });
    }

    private boolean isFragmentLoaded(String fragmentTag) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        return fragment != null && fragment.getClass().getSimpleName().equals(fragmentTag);
    }

    private void clearBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        while (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();
        }
    }

    private void loadFragment(Fragment fragment, int flag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        String fragmentTag = fragment.getClass().getSimpleName();

        if (flag == 0) {
            fragmentTransaction.add(R.id.container, fragment, fragmentTag);
        } else {
            fragmentTransaction.replace(R.id.container, fragment, fragmentTag);
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();

//        if (fragment instanceof HomeFragment) {
//            bn_view.setSelectedItemId(R.id.bnav_home);
//        } else if (fragment instanceof ExploreFragment) {
//            bn_view.setSelectedItemId(R.id.bnav_explore);
//        } else if (fragment instanceof InsightsFragment) {
//            bn_view.setSelectedItemId(R.id.bnav_insights);
//        } else if (fragment instanceof AccountFragment) {
//            bn_view.setSelectedItemId(R.id.bnav_account);
//        }

    }

    private void scheduleNotificationFromPreferences() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getUid();

        if (userId != null) {
            DocumentReference docRef = db.collection("notification_preferences").document(userId);
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    int hour = documentSnapshot.getLong("hour").intValue();
                    int minute = documentSnapshot.getLong("minute").intValue();
                    NotifyManager.scheduleAlarm(this, hour, minute);
                } else {
                    NotifyManager.scheduleAlarm(this, 18, 50);
                }
            }).addOnFailureListener(e -> {
                e.printStackTrace();
                NotifyManager.scheduleAlarm(this, 18, 50);
            });
        }
    }
}


//---------------------------------------------

//package com.developer.moodifyai;
//
//import android.os.Bundle;
//import android.view.MenuItem;
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//
//import com.developer.moodifyai.fragment.AccountFragment;
//import com.developer.moodifyai.fragment.ExploreFragment;
//import com.developer.moodifyai.fragment.HomeFragment;
//import com.developer.moodifyai.fragment.InsightsFragment;
//import com.developer.moodifyai.notify.NotifyManager;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//public class DashboardScreen extends AppCompatActivity {
//    BottomNavigationView bn_view;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_dashboard_screen);
//
//        NotifyManager.scheduleDailyReminder(this);
//
//        bn_view = findViewById(R.id.bn_view);
//
//        if (getIntent().hasExtra("navigate_to")) {
//            String destination = getIntent().getStringExtra("navigate_to");
//            if ("HomeFragment".equals(destination) && !isFragmentLoaded("HomeFragment")) {
//                clearBackStack();
//                loadFragment(new HomeFragment(), 0);
//            }
//        } else {
//            if (!isFragmentLoaded("HomeFragment")) {
//                loadFragment(new HomeFragment(), 0);
//            }
//        }
//
//        bn_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                int id = item.getItemId();
//                if (id == R.id.bnav_home) {
//                    loadFragment(new HomeFragment(), 1);
//                } else if (id == R.id.bnav_explore) {
//                    loadFragment(new ExploreFragment(), 1);
//                } else if (id == R.id.bnav_insights) {
//                    loadFragment(new InsightsFragment(), 1);
//                } else if (id == R.id.bnav_account) {
//                    loadFragment(new AccountFragment(), 1);
//                }
//                return true;
//            }
//        });
//    }
//
//    private boolean isFragmentLoaded(String fragmentTag) {
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
//        return fragment != null && fragment.getClass().getSimpleName().equals(fragmentTag);
//    }
//
//    private void clearBackStack() {
//        FragmentManager fm = getSupportFragmentManager();
//        while (fm.getBackStackEntryCount() > 0) {
//            fm.popBackStackImmediate();
//        }
//    }
//
//    private void loadFragment(Fragment fragment, int flag) {
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        String fragmentTag = fragment.getClass().getSimpleName();
//
//        if (flag == 0) {
//            fragmentTransaction.add(R.id.container, fragment, fragmentTag);
//        } else {
//            fragmentTransaction.replace(R.id.container, fragment, fragmentTag);
//            fragmentTransaction.addToBackStack(null);
//        }
//        fragmentTransaction.commit();
//    }
//}