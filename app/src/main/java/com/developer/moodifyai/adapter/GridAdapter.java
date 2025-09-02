package com.developer.moodifyai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.moodifyai.R;

public class GridAdapter extends BaseAdapter {
    private Context context;
    private String[] titles = {"Meditation", "Breathing", "Music", "Articles", "Quotes", "Podcast", "Affirmation", "Tips", "Notepad", "Mood Calender", "Coping Strategies"};
    private int[] images = {R.drawable.ic_meditation, R.drawable.ic_breathing, R.drawable.ic_music,
            R.drawable.ic_articles, R.drawable.ic_quotes, R.drawable.ic_podcast,
            R.drawable.ic_affirmation, R.drawable.ic_tips, R.drawable.ic_notepad, R.drawable.ic_m_calender, R.drawable.coping_startegies};

    public GridAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return titles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.item_image);
        TextView textView = convertView.findViewById(R.id.item_text);

        imageView.setImageResource(images[position]);
        textView.setText(titles[position]);

        return convertView;
    }
}