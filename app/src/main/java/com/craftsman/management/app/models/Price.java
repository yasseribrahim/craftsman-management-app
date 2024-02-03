package com.craftsman.management.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.Objects;

public class Price implements Parcelable {
    private String id;
    private String craftsmanId;
    private float price;
    private Date date;
    private boolean accepted;

    public Price() {
    }

    public Price(String id, String craftsmanId, float price, Date date, boolean accepted) {
        this.id = id;
        this.craftsmanId = craftsmanId;
        this.price = price;
        this.date = date;
        this.accepted = accepted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCraftsmanId() {
        return craftsmanId;
    }

    public void setCraftsmanId(String craftsmanId) {
        this.craftsmanId = craftsmanId;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return craftsmanId.equals(price.craftsmanId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(craftsmanId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.craftsmanId);
        dest.writeFloat(this.price);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeByte(this.accepted ? (byte) 1 : (byte) 0);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.craftsmanId = source.readString();
        this.price = source.readFloat();
        long tmpDate = source.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.accepted = source.readByte() != 0;
    }

    protected Price(Parcel in) {
        this.id = in.readString();
        this.craftsmanId = in.readString();
        this.price = in.readFloat();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.accepted = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Price> CREATOR = new Parcelable.Creator<Price>() {
        @Override
        public Price createFromParcel(Parcel source) {
            return new Price(source);
        }

        @Override
        public Price[] newArray(int size) {
            return new Price[size];
        }
    };
}
