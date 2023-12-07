package com.orders.management.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Order implements Parcelable {
    private String id;
    private String userId;
    private String clientId;
    private float total;
    private Date date;
    private List<OrderDetail> details;

    public Order() {
        super();
    }

    public Order(String userId, String clientId, float total, Date date, List<OrderDetail> details) {
        this.userId = userId;
        this.clientId = clientId;
        this.total = total;
        this.date = date;
        this.details = details;
    }

    public Order(String id, String userId, String clientId, float total, Date date, List<OrderDetail> details) {
        this.id = id;
        this.userId = userId;
        this.clientId = clientId;
        this.total = total;
        this.date = date;
        this.details = details;
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

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<OrderDetail> getDetails() {
        return details;
    }

    public void setDetails(List<OrderDetail> details) {
        this.details = details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", clientId='" + clientId + '\'' +
                ", total=" + total +
                ", date=" + date +
                ", details=" + details +
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
        dest.writeString(this.clientId);
        dest.writeFloat(this.total);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeList(this.details);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.userId = source.readString();
        this.clientId = source.readString();
        this.total = source.readFloat();
        long tmpDate = source.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.details = new ArrayList<>();
        source.readList(this.details, OrderDetail.class.getClassLoader());
    }

    protected Order(Parcel in) {
        this.id = in.readString();
        this.userId = in.readString();
        this.clientId = in.readString();
        this.total = in.readFloat();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.details = new ArrayList<OrderDetail>();
        in.readList(this.details, OrderDetail.class.getClassLoader());
    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
