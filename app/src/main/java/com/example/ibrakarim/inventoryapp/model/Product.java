package com.example.ibrakarim.inventoryapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private String name;
    private String price;
    private String desc;
    private String quantity;

    public Product(String name, String price, String desc, String quantity) {
        this.name = name;
        this.price = price;
        this.desc = desc;
        this.quantity = quantity;
    }


    public Product() {
    }

    protected Product(Parcel in) {
        name = in.readString();
        price = in.readString();
        desc = in.readString();
        quantity = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(price);
        parcel.writeString(desc);
        parcel.writeString(quantity);
    }
}
