package com.craftsman.management.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Service implements Parcelable {
    private String id;
    private String categoryId;
    private String categoryName;
    private String title;
    private String description;
    private String imageUrl;
    private String createdBy;
    private Date date;
    private List<Price> prices;
    private Price acceptedPrices;

    public Service(String id) {
        this.id = id;
        this.prices = new ArrayList<>();
    }

    public Service() {
        this.prices = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
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

    public void setAcceptedPrices(Price acceptedPrices) {
        this.acceptedPrices = acceptedPrices;
    }

    public Price getAcceptedPrices() {
        return acceptedPrices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return id.equals(service.id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.categoryId);
        dest.writeString(this.categoryName);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.imageUrl);
        dest.writeString(this.createdBy);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeTypedList(this.prices);
        dest.writeParcelable(this.acceptedPrices, flags);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readString();
        this.categoryId = source.readString();
        this.categoryName = source.readString();
        this.title = source.readString();
        this.description = source.readString();
        this.imageUrl = source.readString();
        this.createdBy = source.readString();
        long tmpDate = source.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.prices = source.createTypedArrayList(Price.CREATOR);
        this.acceptedPrices = source.readParcelable(Price.class.getClassLoader());
    }

    protected Service(Parcel in) {
        this.id = in.readString();
        this.categoryId = in.readString();
        this.categoryName = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.imageUrl = in.readString();
        this.createdBy = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.prices = in.createTypedArrayList(Price.CREATOR);
        this.acceptedPrices = in.readParcelable(Price.class.getClassLoader());
    }

    public static final Creator<Service> CREATOR = new Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel source) {
            return new Service(source);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };
}
