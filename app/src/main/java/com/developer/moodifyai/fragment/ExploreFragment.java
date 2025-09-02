package com.developer.moodifyai.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.developer.moodifyai.R;
import com.developer.moodifyai.adapter.GridAdapter;
import com.developer.moodifyai.affirmation.AffirmationScreen;
import com.developer.moodifyai.coping_strategies.CopingStrategyScreen;
import com.developer.moodifyai.deep_breathing.DeepBreathingScreen;
import com.developer.moodifyai.meditation.MeditationScreen;
import com.developer.moodifyai.mood_calender.MoodCalenderScreen;
import com.developer.moodifyai.notepad.NotepadScreen;
import com.developer.moodifyai.quotes.QuotesScreen;
import com.developer.moodifyai.tips.TipsScreen;

public class ExploreFragment extends Fragment {
    GridView gridView;
    GridAdapter adapter;

    public ExploreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        gridView = view.findViewById(R.id.gridView);
        adapter = new GridAdapter(getContext());
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedItem = adapter.getItem(position).toString();
            if ("Notepad".equals(selectedItem)) {
                Intent intent = new Intent(getContext(), NotepadScreen.class);
                startActivity(intent);
            } else if ("Tips".equals(selectedItem)) {
                Intent intent = new Intent(getContext(), TipsScreen.class);
                startActivity(intent);
            } else if ("Affirmation".equals(selectedItem)) {
                Intent intent = new Intent(getContext(), AffirmationScreen.class);
                startActivity(intent);
            } else if ("Quotes".equals(selectedItem)) {
                Intent intent = new Intent(getContext(), QuotesScreen.class);
                startActivity(intent);
            } else if ("Mood Calender".equals(selectedItem)) {
                Intent intent = new Intent(getContext(), MoodCalenderScreen.class);
                startActivity(intent);
            } else if ("Coping Strategies".equals(selectedItem)) {
                Intent intent = new Intent(getContext(), CopingStrategyScreen.class);
                startActivity(intent);
            } else if ("Meditation".equals(selectedItem)) {
                Intent intent = new Intent(getContext(), MeditationScreen.class);
                startActivity(intent);
            } else if ("Breathing".equals(selectedItem)) {
                Intent intent = new Intent(getContext(), DeepBreathingScreen.class);
                startActivity(intent);
            }
        });

        return view;
    }
}