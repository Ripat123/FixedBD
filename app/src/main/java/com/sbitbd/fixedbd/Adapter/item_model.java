package com.sbitbd.fixedbd.Adapter;

public class item_model {
    String itemID,item_img;

    public item_model(String itemID, String item_img) {
        this.itemID = itemID;
        this.item_img = item_img;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItem_img() {
        return item_img;
    }

    public void setItem_img(String item_img) {
        this.item_img = item_img;
    }
}
