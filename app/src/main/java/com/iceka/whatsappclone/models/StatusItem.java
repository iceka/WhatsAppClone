package com.iceka.whatsappclone.models;

import java.util.Map;

public class StatusItem {

    private String id;
    private String type;
    private int duration;
    private String url;
    private String caption;
    private String text;
    private long timestamp;
    private long expireTime;
    private int backgroundColor;
    private Viewed viewed;
    private String thumbnail;

    public StatusItem() {
    }

    // For Text
    public StatusItem(String id, String type, String text, long timestamp, long expireTime, int backgroundColor, String thumbnail, Viewed viewed) {
        this.id = id;
        this.type = type;
        this.text = text;
        this.timestamp = timestamp;
        this.expireTime = expireTime;
        this.backgroundColor = backgroundColor;
        this.thumbnail = thumbnail;
        this.viewed = viewed;
    }

    // For Videos
    public StatusItem(String id, String type, int duration, String url, String caption, long timestamp, long expireTime, Viewed viewed) {
        this.id = id;
        this.type = type;
        this.duration = duration;
        this.url = url;
        this.caption = caption;
        this.timestamp = timestamp;
        this.expireTime = expireTime;
        this.viewed = viewed;
    }

    // For Images
    public StatusItem(String id, String type, String url, String caption, long timestamp, long expireTime, String thumbnail, Viewed viewed) {
        this.id = id;
        this.type = type;
        this.url = url;
        this.caption = caption;
        this.timestamp = timestamp;
        this.expireTime = expireTime;
        this.thumbnail = thumbnail;
        this.viewed = viewed;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public int getDuration() {
        return duration;
    }

    public String getUrl() {
        return url;
    }

    public String getCaption() {
        return caption;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public Viewed getViewed() {
        return viewed;
    }
}
