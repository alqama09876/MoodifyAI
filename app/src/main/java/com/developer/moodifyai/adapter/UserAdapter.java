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
import com.developer.moodifyai.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;
    private UserAdapter.OnChatClickListener listener;

    public interface OnChatClickListener {
        void onChatClick(User user);
    }

    public UserAdapter(List<User> userList, UserAdapter.OnChatClickListener listener) {
        this.userList = userList;
        this.listener = listener;
    }

    public void updateData(List<User> newList) {
        this.userList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        User user = userList.get(position);

        holder.name.setText(user.getName());

        // Always show default image
        holder.image.setImageResource(R.drawable.ic_profile_placeholder);

        holder.chatButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onChatClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList != null ? userList.size() : 0;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        AppCompatButton chatButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.user_img);
            name = itemView.findViewById(R.id.user_name);
            chatButton = itemView.findViewById(R.id.btn_chat);
        }
    }
}