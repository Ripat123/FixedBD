package com.sbitbd.fixedbd.Adapter;

public class checkout_pro_model {
    String product_name,quantity,subtotal,id;

    public checkout_pro_model(String product_name, String quantity, String subtotal,String id) {
        this.product_name = product_name;
        this.quantity = quantity;
        this.subtotal = subtotal;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }
}
