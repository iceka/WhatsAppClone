package com.iceka.whatsappclone.models;

import java.util.List;

public class Chat {

    private String username;
    private String message;
    private String senderUid;
    private String receiverUid;
    private int imageResourceId;

    public Chat() {
    }

    public Chat(String username, String message, int imageResourceId) {
        this.username = username;
        this.message = message;
        this.imageResourceId = imageResourceId;
    }

    public Chat(String message, String senderUid, String receiverUid) {
        this.message = message;
        this.senderUid = senderUid;
        this.receiverUid = receiverUid;
    }

    public Chat(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
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

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

}
