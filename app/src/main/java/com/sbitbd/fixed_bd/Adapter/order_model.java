package com.sbitbd.fixed_bd.Adapter;

public class order_model {
    String order_id,pro_name,date,status;

    public order_model(String order_id, String pro_name, String date, String status) {
        this.order_id = order_id;
        this.pro_name = pro_name;
        this.date = date;
        this.status = status;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
