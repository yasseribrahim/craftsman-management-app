package com.orders.management.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.orders.management.app.Constants;

public class User implements Parcelable {
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
    private String description;

    public User() {
        this(null, null, null, null, null, null, null, null, 0, "");
    }

    public User(String id) {
        this(id, null, null, null, null, null, null, null, 0, "");
    }

    public User(String username, String password) {
        this(null, username, password, null, null, username, null, null, 0, "");
    }

    public User(String username, String password, String fullName, String phone) {
        this(null, username, password, fullName, phone, username, null, null, 0, "");
    }

    public User(String id, String username, String password, String fullName, String phone, String email, String address, String imageProfile, int type, String description) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.imageProfile = imageProfile;
        this.type = type;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public boolean isCraftsman() {
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
        User user = (User) o;
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
        dest.writeString(this.description);
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
        this.description = source.readString();
    }

    protected User(Parcel in) {
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
        this.description = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
