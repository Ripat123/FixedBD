package com.sbitbd.fixed_bd.Adapter;

public class deliveryModel {
    String first_nameS,last_nameS,phoneS,emailS,countryS,district_idS,fulladdressS,session,thisdate;

    public deliveryModel(String first_nameS, String last_nameS, String phoneS, String emailS, String
            countryS, String district_idS, String fulladdressS, String session, String thisdate) {
        this.first_nameS = first_nameS;
        this.last_nameS = last_nameS;
        this.phoneS = phoneS;
        this.emailS = emailS;
        this.countryS = countryS;
        this.district_idS = district_idS;
        this.fulladdressS = fulladdressS;
        this.session = session;
        this.thisdate = thisdate;
    }

    public String getFirst_nameS() {
        return first_nameS;
    }

    public void setFirst_nameS(String first_nameS) {
        this.first_nameS = first_nameS;
    }

    public String getLast_nameS() {
        return last_nameS;
    }

    public void setLast_nameS(String last_nameS) {
        this.last_nameS = last_nameS;
    }

    public String getPhoneS() {
        return phoneS;
    }

    public void setPhoneS(String phoneS) {
        this.phoneS = phoneS;
    }

    public String getEmailS() {
        return emailS;
    }

    public void setEmailS(String emailS) {
        this.emailS = emailS;
    }

    public String getCountryS() {
        return countryS;
    }

    public void setCountryS(String countryS) {
        this.countryS = countryS;
    }

    public String getDistrict_idS() {
        return district_idS;
    }

    public void setDistrict_idS(String district_idS) {
        this.district_idS = district_idS;
    }

    public String getFulladdressS() {
        return fulladdressS;
    }

    public void setFulladdressS(String fulladdressS) {
        this.fulladdressS = fulladdressS;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getThisdate() {
        return thisdate;
    }

    public void setThisdate(String thisdate) {
        this.thisdate = thisdate;
    }
}
