package com.developer.moodifyai.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.moodifyai.R;
import com.developer.moodifyai.model.Therapist;

import java.util.List;

public class TherapistAdapter extends RecyclerView.Adapter<TherapistAdapter.TherapistViewHolder> {
    private List<Therapist> therapistList;
    private OnChatClickListener listener;

    public interface OnChatClickListener {
        void onChatClick(Therapist therapist);
    }

    public TherapistAdapter(List<Therapist> therapistList, OnChatClickListener listener) {
        this.therapistList = therapistList;
        this.listener = listener;
    }

    public void updateData(List<Therapist> newList) {
        this.therapistList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TherapistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_therapist, parent, false);
        return new TherapistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TherapistViewHolder holder, int position) {
        Therapist therapist = therapistList.get(position);

        holder.name.setText(therapist.getName());
        holder.speciality.setText(therapist.getSpecialization());

        // Always show default image
        holder.image.setImageResource(R.drawable.ic_profile_placeholder);

        holder.chatButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onChatClick(therapist);
            }
        });
    }

    @Override
    public int getItemCount() {
        return therapistList != null ? therapistList.size() : 0;
    }

    static class TherapistViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, speciality;
        AppCompatButton chatButton;

        public TherapistViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.therapist_img);
            name = itemView.findViewById(R.id.therapist_name);
            speciality = itemView.findViewById(R.id.therapist_speciality);
            chatButton = itemView.findViewById(R.id.btn_chat);
        }
    }
}


//package com.developer.moodifyai.adapter;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.widget.AppCompatButton;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.developer.moodifyai.R;
//import com.developer.moodifyai.model.Therapist;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//
//import java.util.List;
//
//public class TherapistAdapter extends RecyclerView.Adapter<TherapistAdapter.TherapistViewHolder> {
//    private List<Therapist> therapistList;
//    private OnChatClickListener listener;
//
//    public interface OnChatClickListener {
//        void onChatClick(Therapist therapist);
//    }
//
//    public TherapistAdapter(List<Therapist> therapistList, OnChatClickListener listener) {
//        this.therapistList = therapistList;
//        this.listener = listener;
//    }
//
//    public void updateData(List<Therapist> newList) {
//        this.therapistList = newList;
//        notifyDataSetChanged();
//    }
//
//    @NonNull
//    @Override
//    public TherapistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_therapist, parent, false);
//        return new TherapistViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull TherapistViewHolder holder, int position) {
//        Therapist therapist = therapistList.get(position);
//
//        // Set basic info
//        holder.name.setText(therapist.getName());
//        holder.speciality.setText(therapist.getSpecialization());
//
//        // Load profile image
//        if (therapist.getProfileImageUrl() != null && !therapist.getProfileImageUrl().isEmpty()) {
//            Glide.with(holder.itemView.getContext())
//                    .load(therapist.getProfileImageUrl())
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .placeholder(R.drawable.ic_profile_placeholder)
//                    .error(R.drawable.ic_profile_placeholder)
//                    .into(holder.image);
//        } else {
//            holder.image.setImageResource(R.drawable.ic_profile_placeholder);
//        }
//
//        // Set chat button click listener
//        holder.chatButton.setOnClickListener(v -> {
//            if (listener != null) {
//                listener.onChatClick(therapist);
//            }
//        });
//
//        // Add availability indicator if needed
//        if (therapist.isAvailable()) {
//            holder.chatButton.setEnabled(true);
//            holder.chatButton.setAlpha(1f);
//        } else {
//            holder.chatButton.setEnabled(false);
//            holder.chatButton.setAlpha(0.5f);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return therapistList != null ? therapistList.size() : 0;
//    }
//
//    static class TherapistViewHolder extends RecyclerView.ViewHolder {
//        ImageView image;
//        TextView name, speciality;
//        AppCompatButton chatButton;
//
//        public TherapistViewHolder(@NonNull View itemView) {
//            super(itemView);
//            image = itemView.findViewById(R.id.therapist_img);
//            name = itemView.findViewById(R.id.therapist_name);
//            speciality = itemView.findViewById(R.id.therapist_speciality);
//            chatButton = itemView.findViewById(R.id.btn_chat);
//        }
//    }
//}


//package com.developer.moodifyai.adapter;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.widget.AppCompatButton;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.developer.moodifyai.R;
//import com.developer.moodifyai.model.Therapist;
//
//import java.util.List;
//
//public class TherapistAdapter extends RecyclerView.Adapter<TherapistAdapter.TherapistViewHolder> {
//    private List<Therapist> therapistList;
//    private OnChatClickListener listener;
//
//    public interface OnChatClickListener {
//        void onChatClick(Therapist therapist);
//    }
//
//    public TherapistAdapter(List<Therapist> therapistList, OnChatClickListener listener) {
//        this.therapistList = therapistList;
//        this.listener = listener;
//    }
//
//    @NonNull
//    @Override
//    public TherapistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_therapist, parent, false);
//        return new TherapistViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull TherapistViewHolder holder, int position) {
//        Therapist therapist = therapistList.get(position);
//        holder.name.setText(therapist.getName());
//        holder.speciality.setText(therapist.getSpecialization());
//
//        // TODO: Load image using Glide or Picasso if you have profile URLs
//        // Glide.with(holder.itemView.getContext()).load(therapist.getProfileImageUrl()).into(holder.image);
//
//        holder.chatButton.setOnClickListener(v -> listener.onChatClick(therapist));
//    }
//
//    @Override
//    public int getItemCount() {
//        return therapistList.size();
//    }
//
//    static class TherapistViewHolder extends RecyclerView.ViewHolder {
//        ImageView image;
//        TextView name, speciality;
//        AppCompatButton chatButton;
//
//        public TherapistViewHolder(@NonNull View itemView) {
//            super(itemView);
//            image = itemView.findViewById(R.id.therapist_img);
//            name = itemView.findViewById(R.id.therapist_name);
//            speciality = itemView.findViewById(R.id.therapist_speciality);
//            chatButton = itemView.findViewById(R.id.btn_chat);
//        }
//    }
//}