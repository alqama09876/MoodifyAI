package com.developer.moodifyai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.moodifyai.R;
import com.developer.moodifyai.model.Notes;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    Context context;
    ArrayList<Notes> notesList = new ArrayList<>();
    OnOptionClickListener optionClickListener;

    public interface OnOptionClickListener {
        void onOptionClick(Notes note);
    }

    public NotesAdapter(Context context, ArrayList<Notes> notesList, OnOptionClickListener optionClickListener) {
        this.context = context;
        this.notesList = notesList;
        this.optionClickListener = optionClickListener;
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_items_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, int position) {
        Notes note = notesList.get(position);
        holder.txtThought.setText(note.getThought());
        holder.txtDay.setText(note.getDay());
        holder.txtTime.setText(note.getTime());
        holder.txtDate.setText(note.getDate());

        holder.imgOption.setOnClickListener(v -> optionClickListener.onOptionClick(note));
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtThought, txtDay, txtTime, txtDate;
        ImageView imgOption;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtThought = itemView.findViewById(R.id.txt_thought);
            txtDay = itemView.findViewById(R.id.txt_day);
            txtTime = itemView.findViewById(R.id.txt_time);
            txtDate = itemView.findViewById(R.id.txt_date);
            imgOption = itemView.findViewById(R.id.img_option);
        }
    }
}