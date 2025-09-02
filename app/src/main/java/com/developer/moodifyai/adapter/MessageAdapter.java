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
import com.developer.moodifyai.model.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private Context context;
    private List<Message> messageList;
    private String currentUserId;

    public MessageAdapter(List<Message> messageList, String currentUserId) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        return message.getSenderId().equals(currentUserId) ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);

        if (holder instanceof SentMessageViewHolder) {
            SentMessageViewHolder sentHolder = (SentMessageViewHolder) holder;
            sentHolder.textMessage.setText(message.getMessage());

            // Format timestamp
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            sentHolder.textTime.setText(sdf.format(new Date(message.getTimestamp())));

            // Set status icon
            switch(message.getStatus()) {
                case READ:
                    sentHolder.statusIndicator.setImageResource(R.drawable.read);
                    break;
                case DELIVERED:
                    sentHolder.statusIndicator.setImageResource(R.drawable.delievered);
                    break;
                default:
                    sentHolder.statusIndicator.setImageResource(R.drawable.sent);
            }
        } else {
            ReceivedMessageViewHolder receivedHolder = (ReceivedMessageViewHolder) holder;
            receivedHolder.textMessage.setText(message.getMessage());

            // Format timestamp
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            receivedHolder.textTime.setText(sdf.format(new Date(message.getTimestamp())));
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage, textTime;
        ImageView statusIndicator;

        public SentMessageViewHolder(View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.text_sent_message);
            textTime = itemView.findViewById(R.id.text_sent_time);
            statusIndicator = itemView.findViewById(R.id.img_status);
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage, textTime;

        public ReceivedMessageViewHolder(View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.text_received_message);
            textTime = itemView.findViewById(R.id.text_received_time);
        }
    }
}