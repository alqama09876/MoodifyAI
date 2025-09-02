package com.developer.moodifyai.model;

public class CallRequest {
    private String senderId;
    private String receiverId;
    private String callType; // "voice" or "video"
    private long timestamp;
    private String roomId; // ZegoCloud Room ID

    public CallRequest() {}

    public CallRequest(String senderId, String receiverId, String callType, long timestamp, String roomId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.callType = callType;
        this.timestamp = timestamp;
        this.roomId = roomId;
    }

    // Getters and Setters

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}

