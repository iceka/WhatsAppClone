package com.iceka.whatsappclone.models;

import android.os.Parcel;
import android.os.Parcelable;


public class User implements Parcelable {

    private String uid;
    private String username;
    private String phone;
    private String photoUrl;
    private String about;
    private Boolean online;
    private long lastSeen;

    public User() {
    }

    public User(String uid, String username, String phone, String photoUrl, String about, boolean online, long lastSeen) {
        this.uid = uid;
        this.username = username;
        this.phone = phone;
        this.photoUrl = photoUrl;
        this.about = about;
        this.online = online;
        this.lastSeen = lastSeen;
    }

    protected User(Parcel in) {
        this.uid = in.readString();
        this.username = in.readString();
        this.phone = in.readString();
        this.photoUrl = in.readString();
        this.about = in.readString();
        this.online = in.readInt() == 1;
        this.lastSeen = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.uid);
        parcel.writeString(this.username);
        parcel.writeString(this.phone);
        parcel.writeString(this.photoUrl);
        parcel.writeString(this.about);
        parcel.writeInt(this.online ? 1 : 0);
        parcel.writeLong(this.lastSeen);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUid() {
        return uid;
    }


    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }


    public String getPhotoUrl() {
        return photoUrl;
    }


    public String getAbout() {
        return about;
    }


    public Boolean isOnline() {
        return online;
    }


    public long getLastSeen() {
        return lastSeen;
    }


}
