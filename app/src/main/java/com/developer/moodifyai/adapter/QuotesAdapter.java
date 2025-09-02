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
import com.developer.moodifyai.quotes.QuoteDetailsScreen;

import java.util.List;
import java.util.Map;

public class QuotesAdapter extends BaseAdapter {
    private final Context context;
    private final List<Map<String, Object>> quotesList;
    private final ProgressBar progressBar;

    public QuotesAdapter(Context context, List<Map<String, Object>> quotesList, ProgressBar progressBar) {
        this.context = context;
        this.quotesList = quotesList;
        this.progressBar = progressBar;
    }

    @Override
    public int getCount() {
        return quotesList.size();
    }

    @Override
    public Object getItem(int position) {
        return quotesList.get(position);
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

        Map<String, Object> quote = quotesList.get(position);

        String title = (String) quote.get("title");
        String quoteText = (String) quote.get("quote");
        String author = (String) quote.get("author");
        String imageUrl = (String) quote.get("image");

        holder.itemTitle.setText(title);

        Glide.with(context)
                .load(imageUrl)
                .into(holder.itemImage);

        convertView.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            Intent intent = new Intent(context, QuoteDetailsScreen.class);
            intent.putExtra("QUOTE_TITLE", title);
            intent.putExtra("QUOTE_TEXT", quoteText);
            intent.putExtra("QUOTE_AUTHOR", author);
            intent.putExtra("QUOTE_IMAGE", imageUrl);
            context.startActivity(intent);
        });

        return convertView;
    }

    static class ViewHolder {
        TextView itemTitle;
        ImageView itemImage;
    }
}