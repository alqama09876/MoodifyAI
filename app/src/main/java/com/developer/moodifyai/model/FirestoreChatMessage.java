package com.developer.moodifyai.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.PropertyName;

public class FirestoreChatMessage {
    private String content;
    private String sender; // "user" or "bot"
    private Timestamp timestamp;

    public FirestoreChatMessage() {}  // Needed for Firestore

    public FirestoreChatMessage(String content, String sender, Timestamp timestamp) {
        this.content = content;
        this.sender = sender;
        this.timestamp = timestamp;
    }

    @PropertyName("content")
    public String getContent() {
        return content;
    }

    @PropertyName("sender")
    public String getSender() {
        return sender;
    }

    @PropertyName("timestamp")
    public Timestamp getTimestamp() {
        return timestamp;
    }
}