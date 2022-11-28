package com.sbitbd.fixed_bd.Adapter;

import java.io.Serializable;

public class size_model implements Serializable {
    private String name;
    private boolean active;

    public size_model(String name) {
        this.name = name;
    }

    public size_model(String name, boolean active) {
        this.name = name;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
