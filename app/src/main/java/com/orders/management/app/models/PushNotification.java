package com.orders.management.app.models;

import com.google.firebase.database.ServerValue;

import java.util.Map;

public class PushNotification {
    private String eventId;
    private String message;
    private Map<String, String> timestamp;

    public PushNotification() {
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public PushNotification(String eventId, String message) {
        this.eventId = eventId;
        this.message = message;
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Map<String, String> timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "PushNotification{" +
                "location='" + eventId + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
