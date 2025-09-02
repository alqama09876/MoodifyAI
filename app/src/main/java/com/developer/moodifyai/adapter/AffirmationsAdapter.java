package com.developer.moodifyai.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.developer.moodifyai.R;
import com.developer.moodifyai.affirmation.AffirmationDetailsScreen;
import com.developer.moodifyai.quotes.QuoteDetailsScreen;

import java.util.List;
import java.util.Map;

public class AffirmationsAdapter extends BaseAdapter {
    private final Context context;
    private final List<Map<String, Object>> affirmationsList;
    private final ProgressBar progressBar;

    public AffirmationsAdapter(Context context, List<Map<String, Object>> affirmationsList, ProgressBar progressBar) {
        this.context = context;
        this.affirmationsList = affirmationsList;
        this.progressBar = progressBar;
    }

    @Override
    public int getCount() {
        return affirmationsList.size();
    }

    @Override
    public Object getItem(int position) {
        return affirmationsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_quotes_layout, parent, false);
            holder = new ViewHolder();
            holder.itemTitle = convertView.findViewById(R.id.item_title);
            holder.itemImage = convertView.findViewById(R.id.item_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Map<String, Object> affirmation = affirmationsList.get(position);

        String title = (String) affirmation.get("title");
        String affirmationText = (String) affirmation.get("affirmation");
        String imageUrl = (String) affirmation.get("image");

        holder.itemTitle.setText(title);

        Glide.with(context)
                .load(imageUrl)
                .into(holder.itemImage);

        convertView.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            Intent intent = new Intent(context, AffirmationDetailsScreen.class);
            intent.putExtra("AFFIRMATION_TITLE", title);
            intent.putExtra("AFFIRMATION_TEXT", affirmationText);
            intent.putExtra("AFFIRMATION_IMAGE", imageUrl);
            context.startActivity(intent);
        });

        return convertView;
    }

    static class ViewHolder {
        TextView itemTitle;
        ImageView itemImage;
    }
}