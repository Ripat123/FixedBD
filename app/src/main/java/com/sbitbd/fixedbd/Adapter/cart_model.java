package com.sbitbd.fixedbd.Adapter;

public class cart_model {
    String pro_name,price_quant,quant,min_quant,proID,image,color,size;
    double total_price;

    public cart_model(String pro_name, String price_quant, String quant, double total_price, String
            image,String min_quant,String proID,String color,String size) {
        this.pro_name = pro_name;
        this.price_quant = price_quant;
        this.quant = quant;
        this.total_price = total_price;
        this.image = image;
        this.proID = proID;
        this.min_quant = min_quant;
        this.color = color;
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMin_quant() {
        return min_quant;
    }

    public void setMin_quant(String min_quant) {
        this.min_quant = min_quant;
    }

    public String getProID() {
        return proID;
    }

    public void setProID(String proID) {
        this.proID = proID;
    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public String getPrice_quant() {
        return price_quant;
    }

    public void setPrice_quant(String price_quant) {
        this.price_quant = price_quant;
    }

    public String getQuant() {
        return quant;
    }

    public void setQuant(String quant) {
        this.quant = quant;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
