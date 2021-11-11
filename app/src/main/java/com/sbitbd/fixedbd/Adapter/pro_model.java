package com.sbitbd.fixedbd.Adapter;

public class pro_model {
    String proName,size,price,dis_val,image,id,dis_price;

    public pro_model(String image, String proName, String size, String price,String dis_val,String id,String dis_price) {
        this.image = image;
        this.proName = proName;
        this.size = size;
        this.price = price;
        this.dis_val = dis_val;
        this.id = id;
        this.dis_price = dis_price;
    }

    public String getDis_price() {
        return dis_price;
    }

    public void setDis_price(String dis_price) {
        this.dis_price = dis_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDis_val() {
        return dis_val;
    }

    public void setDis_val(String dis_val) {
        this.dis_val = dis_val;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
