package com.craftsman.management.app.models;

import com.google.firebase.database.ServerValue;

import java.util.Map;

public class PushNotification {
    private String serviceId;
    private String message;
    private Map<String, String> timestamp;

    public PushNotification() {
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public PushNotification(String serviceId, String message) {
        this.serviceId = serviceId;
        this.message = message;
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
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
                "location='" + serviceId + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
