package com.developer.moodifyai.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.moodifyai.R;
import com.developer.moodifyai.tips.TipDetailScreen;

public class CategoryAdapter extends BaseAdapter {
    private Context context;
    private String[] titles;
    private int[] images;
    private String[] descriptions;
    private String[] examples;

    public CategoryAdapter(Context context, String[] titles, int[] images, String[] descriptions, String[] examples) {
        this.context = context;
        this.titles = titles;
        this.images = images;
        this.descriptions = descriptions;
        this.examples = examples;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_tips_layout, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.item_image);
        TextView textView = convertView.findViewById(R.id.item_text);

        imageView.setImageResource(images[position]);
        textView.setText(titles[position]);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TipDetailScreen.class);
                intent.putExtra("TIP_IMAGE", images[position]);
                intent.putExtra("TIP_TITLE", titles[position]);
                intent.putExtra("TIP_DESCRIPTION", descriptions[position]);
                intent.putExtra("TIP_EXAMPLE", examples[position]);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}