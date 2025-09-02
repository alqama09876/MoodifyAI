package com.developer.moodifyai.model;

import java.util.List;

public class Chat {
    private String chatId;
    private List<String> participants; // UIDs of user and therapist
    private String lastMessage;
    private long timestamp; // Unix time in millis

    public Chat() {
        // Required empty constructor for Firebase
    }

    public Chat(String chatId, List<String> participants, String lastMessage, long timestamp) {
        this.chatId = chatId;
        this.participants = participants;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
    }

    // Getters and Setters

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}