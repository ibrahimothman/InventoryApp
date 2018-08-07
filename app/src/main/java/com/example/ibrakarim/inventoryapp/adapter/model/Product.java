package com.example.ibrakarim.inventoryapp.adapter.model;

public class Product {
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
