package com.developer.moodifyai.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.developer.moodifyai.DashboardScreen;
import com.developer.moodifyai.R;
import com.developer.moodifyai.affirmation.AffirmationScreen;
import com.developer.moodifyai.chat.TherapistListActivity;
import com.developer.moodifyai.chatbot.ChatbotActivity;
import com.developer.moodifyai.coping_strategies.CopingStrategyScreen;
import com.developer.moodifyai.mood_calender.MoodCalenderScreen;
import com.developer.moodifyai.notepad.NotepadScreen;
import com.developer.moodifyai.quotes.QuotesScreen;
import com.developer.moodifyai.tips.TipsScreen;
import com.developer.moodifyai.user_emotion.HappyScreen;
import com.developer.moodifyai.user_emotion.NeutralScreen;
import com.developer.moodifyai.user_emotion.SadScreen;
import com.developer.moodifyai.user_emotion.VeryHappyScreen;
import com.developer.moodifyai.user_emotion.AngryScreen;

public class HomeFragment extends Fragment {

    ImageView img_angry, img_happy, img_neutral, img_sad, img_very_happy;
    RelativeLayout item_mood_calender, item_coping_strategies, item_tips, item_quotes, item_notepad, item_affirmation;
    LinearLayout talk_with_therapist, chat_with_moodifyai;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        img_angry = view.findViewById(R.id.img_angry);
        img_happy = view.findViewById(R.id.img_happy);
        img_neutral = view.findViewById(R.id.img_neutral);
        img_sad = view.findViewById(R.id.img_sad);
        img_very_happy = view.findViewById(R.id.img_very_happy);
        item_mood_calender = view.findViewById(R.id.item_mood_calender);
        item_coping_strategies = view.findViewById(R.id.item_coping_strategies);
        item_tips = view.findViewById(R.id.item_tips);
        item_quotes = view.findViewById(R.id.item_quotes);
        item_notepad = view.findViewById(R.id.item_notepad);
        item_affirmation = view.findViewById(R.id.item_affirmation);
        chat_with_moodifyai = view.findViewById(R.id.chat_with_moodifyai);
        talk_with_therapist = view.findViewById(R.id.talk_with_therapist);

        img_angry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AngryScreen.class));
                requireActivity().finish();
            }
        });

        img_happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HappyScreen.class));
                requireActivity().finish();
            }
        });

        img_neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NeutralScreen.class));
                requireActivity().finish();
            }
        });

        img_sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SadScreen.class));
                requireActivity().finish();
            }
        });

        img_very_happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), VeryHappyScreen.class));
                requireActivity().finish();
            }
        });

        chat_with_moodifyai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ChatbotActivity.class));
                requireActivity().finish();
            }
        });

        talk_with_therapist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TherapistListActivity.class));
                requireActivity().finish();
            }
        });

        item_mood_calender.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MoodCalenderScreen.class);
            intent.putExtra("cameFromHome", true);
            startActivity(intent);
            requireActivity().finish();
        });

        item_coping_strategies.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CopingStrategyScreen.class);
            intent.putExtra("cameFromHome", true);
            startActivity(intent);
            requireActivity().finish();
        });

        item_tips.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TipsScreen.class);
            intent.putExtra("cameFromHome", true);
            startActivity(intent);
            requireActivity().finish();
        });

        item_quotes.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), QuotesScreen.class);
            intent.putExtra("cameFromHome", true);
            startActivity(intent);
            requireActivity().finish();
        });

        item_notepad.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), NotepadScreen.class);
            intent.putExtra("cameFromHome", true);
            startActivity(intent);
            requireActivity().finish();
        });

        item_affirmation.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AffirmationScreen.class);
            intent.putExtra("cameFromHome", true);
            startActivity(intent);
            requireActivity().finish();
        });

        return view;
    }
}