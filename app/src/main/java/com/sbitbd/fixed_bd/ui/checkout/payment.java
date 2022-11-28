package com.sbitbd.fixed_bd.ui.checkout;

public class payment {
    private String bkash,rocket,nagad,bank,other;

    public payment(String bkash, String rocket, String nagad, String bank, String other) {
        this.bkash = bkash;
        this.rocket = rocket;
        this.nagad = nagad;
        this.bank = bank;
        this.other = other;
    }

    public payment() {
    }

    public String getBkash() {
        return bkash;
    }

    public void setBkash(String bkash) {
        this.bkash = bkash;
    }

    public String getRocket() {
        return rocket;
    }

    public void setRocket(String rocket) {
        this.rocket = rocket;
    }

    public String getNagad() {
        return nagad;
    }

    public void setNagad(String nagad) {
        this.nagad = nagad;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
