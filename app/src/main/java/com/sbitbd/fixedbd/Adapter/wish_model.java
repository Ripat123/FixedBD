package com.sbitbd.fixedbd.Adapter;

public class wish_model {
    String pro_name,price;
    int image;

    public wish_model(String pro_name, String price, int image) {
        this.pro_name = pro_name;
        this.price = price;
        this.image = image;
    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
