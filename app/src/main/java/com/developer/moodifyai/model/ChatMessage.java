package com.developer.moodifyai.model;

import java.util.Date;

public class ChatMessage {
    public static final int TYPE_BOT = 0;
    public static final int TYPE_USER = 1;

    private String content;
    private int messageType;
    private Date timestamp;

    public ChatMessage(String content, int messageType) {
        this.content = content;
        this.messageType = messageType;
        this.timestamp = new Date();
    }

    public String getContent() { return content; }
    public int getMessageType() { return messageType; }
    public String getFormattedTime() {
        return android.text.format.DateFormat.format("hh:mm a", timestamp).toString();
    }
}