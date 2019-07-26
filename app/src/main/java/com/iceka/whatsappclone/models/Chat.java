package com.iceka.whatsappclone.models;

public class Chat {

    private String username;
    private String message;
    private int imageResourceId;

    public Chat(String username, String message, int imageResourceId) {
        this.username = username;
        this.message = message;
        this.imageResourceId = imageResourceId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
}
