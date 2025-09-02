package com.developer.moodifyai.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.moodifyai.R;

import java.util.List;
import java.util.Map;

public class MonthlyCalenderAdapter extends RecyclerView.Adapter<MonthlyCalenderAdapter.CalendarViewHolder> {
    List<String> dates;
    Map<String, Integer> moodMap;

    public MonthlyCalenderAdapter(List<String> dates, Map<String, Integer> moodMap) {
        this.dates = dates;
        this.moodMap = moodMap;
    }

    @NonNull
    @Override
    public MonthlyCalenderAdapter.CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar_date, parent, false);
        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthlyCalenderAdapter.CalendarViewHolder holder, int position) {
        String date = dates.get(position);

        if (!date.isEmpty()) {
            Integer moodEmoji = moodMap.get(date);
            if (moodEmoji != null) {
                holder.moodImage.setImageResource(moodEmoji);
                holder.moodImage.setVisibility(View.VISIBLE);
            } else {
                holder.moodImage.setImageResource(R.drawable.default_mood);
                holder.moodImage.setVisibility(View.VISIBLE);
            }
            holder.dateText.setText(date);
        } else {
            holder.dateText.setText("");
            holder.moodImage.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public void updateCalendar(List<String> newDates, Map<String, Integer> newMoodMap) {
        this.dates = newDates;
        this.moodMap = newMoodMap;
        notifyDataSetChanged();
    }

    static class CalendarViewHolder extends RecyclerView.ViewHolder {
        TextView dateText;
        ImageView moodImage;

        public CalendarViewHolder(View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.tv_date);
            moodImage = itemView.findViewById(R.id.iv_mood);
        }
    }
}