package com.developer.moodifyai.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.moodifyai.R;
import com.developer.moodifyai.model.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_BOT = 2;
    private static final int VIEW_TYPE_TYPING = 3;

    private List<ChatMessage> messages;

    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messages.get(position);
        if (message.getMessageType() == ChatMessage.TYPE_USER) {
            return VIEW_TYPE_USER;
        } else if (message.getMessageType() == ChatMessage.TYPE_BOT) {
            if ("typing".equals(message.getContent())) {
                return VIEW_TYPE_TYPING;
            }
            return VIEW_TYPE_BOT;
        }
        return VIEW_TYPE_BOT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_USER) {
            View view = inflater.inflate(R.layout.item_message_user, parent, false);
            return new UserMessageViewHolder(view);
        } else if (viewType == VIEW_TYPE_BOT) {
            View view = inflater.inflate(R.layout.item_message_bot, parent, false);
            return new BotMessageViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_typing_indicator, parent, false);
            return new TypingIndicatorViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_USER:
                ((UserMessageViewHolder) holder).bind(message);
                break;

            case VIEW_TYPE_BOT:
                ((BotMessageViewHolder) holder).bind(message);
                break;

            case VIEW_TYPE_TYPING:
                // No data binding needed for typing indicator
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    public void removeMessage(int position) {
        if (position >= 0 && position < messages.size()) {
            messages.remove(position);
            notifyItemRemoved(position);
        }
    }

    static class UserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;

        UserMessageViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.text_sent_message);
        }

        void bind(ChatMessage message) {
            tvMessage.setText(message.getContent());
        }
    }

    static class BotMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;

        BotMessageViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.text_received_message);
        }

        void bind(ChatMessage message) {
            tvMessage.setText(message.getContent());
        }
    }

    static class TypingIndicatorViewHolder extends RecyclerView.ViewHolder {
        ProgressBar typingIndicator;
        TextView typingText;

        TypingIndicatorViewHolder(View itemView) {
            super(itemView);
            typingIndicator = itemView.findViewById(R.id.typing_indicator);
            typingText = itemView.findViewById(R.id.typing_text);
        }
    }
}

//package com.developer.moodifyai.adapter;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.developer.moodifyai.R;
//import com.developer.moodifyai.model.ChatMessage;
//
//import java.util.List;
//
//public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    private static final int VIEW_TYPE_USER = 1;
//    private static final int VIEW_TYPE_BOT = 2;
//    private static final int VIEW_TYPE_TYPING = 3;
//
//    private List<ChatMessage> messages;
//
//    public ChatAdapter(List<ChatMessage> messages) {
//        this.messages = messages;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        ChatMessage message = messages.get(position);
//        if (message.getMessageType() == ChatMessage.TYPE_USER) {
//            return VIEW_TYPE_USER;
//        } else if (message.getMessageType() == ChatMessage.TYPE_BOT) {
//            if ("typing".equals(message.getContent())) {
//                return VIEW_TYPE_TYPING;
//            }
//            return VIEW_TYPE_BOT;
//        }
//        return VIEW_TYPE_BOT;
//    }
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (viewType == VIEW_TYPE_USER) {
//            View view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.item_message_user, parent, false);
//            return new UserMessageViewHolder(view);
//        } else if (viewType == VIEW_TYPE_BOT) {
//            View view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.item_message_bot, parent, false);
//            return new BotMessageViewHolder(view);
//        } else {
//            View view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.item_typing_indicator, parent, false);
//            return new TypingIndicatorViewHolder(view);
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        ChatMessage message = messages.get(position);
//
//        if (holder.getItemViewType() == VIEW_TYPE_USER) {
//            ((UserMessageViewHolder) holder).bind(message);
//        } else {
//            ((BotMessageViewHolder) holder).bind(message);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return messages.size();
//    }
//
//    public void addMessage(ChatMessage message) {
//        messages.add(message);
//        notifyItemInserted(messages.size() - 1);
//    }
//
//    static class UserMessageViewHolder extends RecyclerView.ViewHolder {
//        TextView tvMessage;
//
//        UserMessageViewHolder(View itemView) {
//            super(itemView);
//            tvMessage = itemView.findViewById(R.id.text_sent_message);
//        }
//
//        void bind(ChatMessage message) {
//            tvMessage.setText(message.getContent());
//        }
//    }
//
//    static class BotMessageViewHolder extends RecyclerView.ViewHolder {
//        TextView tvMessage;
//
//        BotMessageViewHolder(View itemView) {
//            super(itemView);
//            tvMessage = itemView.findViewById(R.id.text_received_message);
//        }
//
//        void bind(ChatMessage message) {
//            tvMessage.setText(message.getContent());
//        }
//    }
//
//    static class TypingIndicatorViewHolder extends RecyclerView.ViewHolder {
//        ProgressBar typingIndicator;
//
//        TypingIndicatorViewHolder(View itemView) {
//            super(itemView);
//            typingIndicator = itemView.findViewById(R.id.typing_indicator);
//        }
//    }
//}