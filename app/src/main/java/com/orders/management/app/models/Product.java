package com.orders.management.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Product implements Parcelable {
    private String id;
    private String userId;
    private String name;
    private String details;
    private float price;
    private int quantity;
    private boolean active;

    public Product() {
    }

    public Product(String userId, String name, String details, float price, int quantity, boolean active) {
        this.userId = userId;
        this.name = name;
        this.details = details;
        this.price = price;
        this.quantity = quantity;
        this.active = active;
    }

    public Product(String id, String userId, String name, String details, float price, int quantity, boolean active) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.details = details;
        this.price = price;
        this.quantity = quantity;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", details='" + details + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", active=" + active +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.userId);
        dest.writeString(this.name);
        dest.writeString(this.details);
        dest.writeFloat(this.price);
        dest.writeInt(this.quantity);
        dest.writeByte(this.active ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.userId = source.readString();
        this.name = source.readString();
        this.details = source.readString();
        this.price = source.readFloat();
        this.quantity = source.readInt();
        this.active = source.readByte() != 0;
    }

    protected Product(Parcel in) {
        this.id = in.readString();
        this.userId = in.readString();
        this.name = in.readString();
        this.details = in.readString();
        this.price = in.readFloat();
        this.quantity = in.readInt();
        this.active = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
