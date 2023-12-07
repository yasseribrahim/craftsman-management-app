package com.orders.management.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.orders.management.app.Constants;

import java.util.ArrayList;
import java.util.List;

public class User2 implements Parcelable {
    private String id;
    private String username;
    private String password;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String imageProfile;
    private int type;
    private String token;
    private List<String> events;

    public User2() {
        this(null, null, null, null, null, null, null, null, 0);
    }

    public User2(String id) {
        this(id, null, null, null, null, null, null, null, 0);
    }

    public User2(String username, String password) {
        this(null, username, password, null, null, username, null, null, 0);
    }

    public User2(String username, String password, String fullName, String phone) {
        this(null, username, password, fullName, phone, username, null, null, 0);
    }

    public User2(String id, String username, String password, String fullName, String phone, String email, String address, String imageProfile, int type) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.imageProfile = imageProfile;
        this.type = type;
        this.events = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getEvents() {
        return events != null ? events : new ArrayList<>();
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public boolean isMerchant() {
        return Constants.USER_TYPE_CRAFTSMAN == type;
    }

    public boolean isClient() {
        return Constants.USER_TYPE_CLIENT == type;
    }

    public boolean isAdmin() {
        return Constants.USER_TYPE_ADMIN == type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User2 user = (User2) o;
        return id.equals(user.id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.fullName);
        dest.writeString(this.phone);
        dest.writeString(this.email);
        dest.writeString(this.address);
        dest.writeString(this.imageProfile);
        dest.writeInt(this.type);
        dest.writeString(this.token);
        dest.writeStringList(this.events);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.username = source.readString();
        this.password = source.readString();
        this.fullName = source.readString();
        this.phone = source.readString();
        this.email = source.readString();
        this.address = source.readString();
        this.imageProfile = source.readString();
        this.type = source.readInt();
        this.token = source.readString();
        this.events = source.createStringArrayList();
    }

    protected User2(Parcel in) {
        this.id = in.readString();
        this.username = in.readString();
        this.password = in.readString();
        this.fullName = in.readString();
        this.phone = in.readString();
        this.email = in.readString();
        this.address = in.readString();
        this.imageProfile = in.readString();
        this.type = in.readInt();
        this.token = in.readString();
        this.events = in.createStringArrayList();
    }

    public static final Creator<User2> CREATOR = new Creator<User2>() {
        @Override
        public User2 createFromParcel(Parcel source) {
            return new User2(source);
        }

        @Override
        public User2[] newArray(int size) {
            return new User2[size];
        }
    };
}
