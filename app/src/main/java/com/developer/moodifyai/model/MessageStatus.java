package com.developer.moodifyai.model;

public enum MessageStatus {
    SENT(0),       // Message has been sent but not yet delivered
    DELIVERED(1),  // Message has been delivered to recipient's device
    READ(2);       // Message has been read by the recipient

    private final int value;

    MessageStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static MessageStatus fromValue(int value) {
        for (MessageStatus status : MessageStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        return SENT; // Default if not found
    }

    public static MessageStatus fromString(String status) {
        if (status == null) {
            return SENT;
        }
        switch (status.toLowerCase()) {
            case "delivered":
                return DELIVERED;
            case "read":
                return READ;
            default:
                return SENT;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case DELIVERED:
                return "delivered";
            case READ:
                return "read";
            default:
                return "sent";
        }
    }
}