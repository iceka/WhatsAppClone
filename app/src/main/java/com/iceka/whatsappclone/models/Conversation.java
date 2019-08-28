package com.iceka.whatsappclone.models;

public class Conversation {

    private String userUid;
    private String username;
    private String chatWithId;
    private String chatId;
    private User user;

    public Conversation() {
    }

    public Conversation(String userUid, String chatWithId) {
        this.userUid = userUid;
        this.chatWithId = chatWithId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getChatWithId() {
        return chatWithId;
    }

    public void setChatWithId(String chatWith) {
        this.chatWithId = chatWith;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
