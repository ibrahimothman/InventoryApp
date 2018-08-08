package com.example.ibrakarim.inventoryapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product   {
    private int id;
    private String name;
    private String price;
    private String desc;
    private String quantity;
    private byte[] image;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }



    public Product(String name, String price, String desc, String quantity, int id, byte[]image) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.desc = desc;
        this.quantity = quantity;
        this.id = id;
    }


    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



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




}
