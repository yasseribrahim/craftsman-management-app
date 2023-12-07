package com.orders.management.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Event implements Parcelable {
    private String id;
    private String title;
    private String description;
    private String createdBy;
    private String address;
    private String location;
    private Date date;
    private boolean approved;
    private List<String> registers;
    private List<Review> reviews;

    public Event(String id) {
        this.id = id;
        this.registers = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }

    public Event() {
        this.registers = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getRegisters() {
        return registers;
    }

    public void setRegisters(List<String> registers) {
        this.registers = registers;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Review> getReviews() {
        if (reviews == null) {
            setRegisters(new ArrayList<>());
        }
        return reviews;
    }

    public float getRating() {
        float value = 0;
        if (reviews == null) {
            setRegisters(new ArrayList<>());
        }
        for (var rating : reviews) {
            value += rating.getValue();
        }

        return reviews.isEmpty() ? 0 : value / reviews.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id.equals(event.id);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", address='" + address + '\'' +
                ", location='" + location + '\'' +
                ", date=" + date +
                ", approved=" + approved +
                ", registers=" + registers +
                ", ratings=" + reviews +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.createdBy);
        dest.writeString(this.address);
        dest.writeString(this.location);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeByte(this.approved ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.registers);
        dest.writeTypedList(this.reviews);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.title = source.readString();
        this.description = source.readString();
        this.createdBy = source.readString();
        this.address = source.readString();
        this.location = source.readString();
        long tmpDate = source.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.approved = source.readByte() != 0;
        this.registers = source.createStringArrayList();
        this.reviews = source.createTypedArrayList(Review.CREATOR);
    }

    protected Event(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.createdBy = in.readString();
        this.address = in.readString();
        this.location = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.approved = in.readByte() != 0;
        this.registers = in.createStringArrayList();
        this.reviews = in.createTypedArrayList(Review.CREATOR);
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
