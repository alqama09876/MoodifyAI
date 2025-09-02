package com.developer.moodifyai.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.moodifyai.R;
import com.developer.moodifyai.auth.LoginScreen;
import com.developer.moodifyai.auth.TherapistLoginScreen;
import com.google.firebase.auth.FirebaseAuth;

public class TherapistAccountFragment extends Fragment {
    AppCompatButton btnLogout;

    public TherapistAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_therapist_account, container, false);

        btnLogout = view.findViewById(R.id.btn_logout);

        btnLogout.setOnClickListener(v -> logoutUser());

        return view;
    }

    private void logoutUser() {
        // Sign out from Firebase
        FirebaseAuth.getInstance().signOut();

        // Clear SharedPreferences for therapist login session
        SharedPreferences preferences = requireContext().getSharedPreferences("TherapistPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Redirect to TherapistLoginScreen
        Intent intent = new Intent(getActivity(), TherapistLoginScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear backstack
        startActivity(intent);
        requireActivity().finish(); // Finish current activity
    }
}


//    private void logoutUser() {
//        // Sign out from Firebase
//        FirebaseAuth.getInstance().signOut();
//
//        // Clear SharedPreferences if you are storing login session
//        SharedPreferences preferences = requireContext().getSharedPreferences("TherapistPrefs", MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.clear();
//        editor.apply();
//
//        // Send user to TherapistLoginScreen
//        Intent intent = new Intent(getActivity(), com.developer.moodifyai.auth.TherapistLoginScreen.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear backstack
//        startActivity(intent);
//        requireActivity().finish(); // Finish current activity
//    }