package com.orders.management.app.models;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderDetail implements Parcelable {
    private String orderId;
    private Product productId;
    private int quantity;
    private float total;

    public OrderDetail() {
        super();
    }

    public OrderDetail(String orderId, Product productId, int quantity, float total) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.total = total;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.orderId);
        dest.writeParcelable(this.productId, flags);
        dest.writeInt(this.quantity);
        dest.writeFloat(this.total);
    }

    public void readFromParcel(Parcel source) {
        this.orderId = source.readString();
        this.productId = source.readParcelable(Product.class.getClassLoader());
        this.quantity = source.readInt();
        this.total = source.readFloat();
    }

    protected OrderDetail(Parcel in) {
        this.orderId = in.readString();
        this.productId = in.readParcelable(Product.class.getClassLoader());
        this.quantity = in.readInt();
        this.total = in.readFloat();
    }

    public static final Parcelable.Creator<OrderDetail> CREATOR = new Parcelable.Creator<OrderDetail>() {
        @Override
        public OrderDetail createFromParcel(Parcel source) {
            return new OrderDetail(source);
        }

        @Override
        public OrderDetail[] newArray(int size) {
            return new OrderDetail[size];
        }
    };
}
