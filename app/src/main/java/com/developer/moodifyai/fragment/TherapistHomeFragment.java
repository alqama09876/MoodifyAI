package com.developer.moodifyai.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.developer.moodifyai.R;
import com.developer.moodifyai.chat.UserListActivity;
import com.developer.moodifyai.user_emotion.AngryScreen;
import com.developer.moodifyai.user_emotion.HappyScreen;
import com.developer.moodifyai.user_emotion.NeutralScreen;
import com.developer.moodifyai.user_emotion.SadScreen;
import com.developer.moodifyai.user_emotion.VeryHappyScreen;

public class TherapistHomeFragment extends Fragment {

    ImageView img_angry, img_happy, img_neutral, img_sad, img_very_happy;
    LinearLayout talk_with_patient;

    public TherapistHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_therapist_home, container, false);

        img_angry = view.findViewById(R.id.img_angry);
        img_happy = view.findViewById(R.id.img_happy);
        img_neutral = view.findViewById(R.id.img_neutral);
        img_sad = view.findViewById(R.id.img_sad);
        img_very_happy = view.findViewById(R.id.img_very_happy);
        talk_with_patient = view.findViewById(R.id.talk_with_patient);

        img_angry.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AngryScreen.class);
            intent.putExtra("cameFrom", "TherapistHomeFragment");
            startActivity(intent);
        });

        img_happy.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HappyScreen.class);
            intent.putExtra("cameFrom", "TherapistHomeFragment");
            startActivity(intent);
        });

        img_neutral.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NeutralScreen.class);
            intent.putExtra("cameFrom", "TherapistHomeFragment");
            startActivity(intent);
        });

        img_sad.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SadScreen.class);
            intent.putExtra("cameFrom", "TherapistHomeFragment");
            startActivity(intent);
        });

        img_very_happy.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), VeryHappyScreen.class);
            intent.putExtra("cameFrom", "TherapistHomeFragment");
            startActivity(intent);
        });

        talk_with_patient.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), UserListActivity.class));
            requireActivity().finish();
        });

        return view;
    }
}