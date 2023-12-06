package com.example.appcontacts;

public class ModelContact {
    String id, contact_name, contact_number, contact_email, uid, service, img_uri;
    int favourite;
    int recommended;

    public int getRecommended() {
        return recommended;
    }

    public void setRecommended(int recommended) {
        this.recommended = recommended;
    }


    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favorite) {
        this.favourite = favorite;
    }

    public ModelContact() {
    }

    public ModelContact(String id, String contact_name, String contact_number, String contact_email, String uid, String service, String img_uri) {
        this.id = id;
        this.contact_name = contact_name;
        this.contact_number = contact_number;
        this.contact_email = contact_email;
        this.uid = uid;
        this.service = service;
        this.img_uri = img_uri;

    }

    public String getImg_uri() {
        return img_uri;
    }

    public void setImg_uri(String img_uri) {
        this.img_uri = img_uri;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getService() {
        return service;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public void setContact_email(String contact_email) {
        this.contact_email = contact_email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public String getContact_name() {
        return contact_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public String getContact_email() {
        return contact_email;
    }

    public String getUid() {
        return uid;
    }
}
