package com.iceka.whatsappclone.models;

import android.view.View;

import java.util.List;

public class StatusText {

    private String text;
    private long timestamp;
    private long expireTime;
    private int backgroundColor;
    private Viewed viewed;
    private String id;

    public StatusText() {
    }

    public StatusText(String text, long timestamp, long expireTime, int backgroundColor, Viewed viewed, String id) {
        this.text = text;
        this.timestamp = timestamp;
        this.expireTime = expireTime;
        this.backgroundColor = backgroundColor;
        this.viewed = viewed;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public Viewed getViewed() {
        return viewed;
    }

    public String getId() {
        return id;
    }
}
