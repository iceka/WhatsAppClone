package com.iceka.whatsappclone.models;

public class User {

    private String uid;
    private String username;
    private String phone;
    private String photoUrl;
    private String about;

    public User() {
    }

    public User(String uid, String username, String phone, String photoUrl, String about) {
        this.uid = uid;
        this.username = username;
        this.phone = phone;
        this.photoUrl = photoUrl;
        this.about = about;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
