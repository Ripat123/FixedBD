package com.sbitbd.fixed_bd.Adapter;

import android.graphics.Bitmap;

public class image_model {
    private Bitmap image;
    private String image_name;

    public image_model(Bitmap image, String image_name) {
        this.image = image;
        this.image_name = image_name;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }
}
