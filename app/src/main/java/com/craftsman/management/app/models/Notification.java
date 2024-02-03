package com.craftsman.management.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class Notification implements Parcelable {
    private String id;
    private String message;
    private long timestamp;
    private String serviceId;
    public Notification() {
        timestamp = Calendar.getInstance().getTimeInMillis();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String toString() {
        return "Notification{" + "message='" + message + '\'' + ", timestamp=" + timestamp + ", location='" + serviceId + '\'' + '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.message);
        dest.writeLong(this.timestamp);
        dest.writeString(this.serviceId);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.message = source.readString();
        this.timestamp = source.readLong();
        this.serviceId = source.readString();
    }

    protected Notification(Parcel in) {
        this.id = in.readString();
        this.message = in.readString();
        this.timestamp = in.readLong();
        this.serviceId = in.readString();
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel source) {
            return new Notification(source);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };
}