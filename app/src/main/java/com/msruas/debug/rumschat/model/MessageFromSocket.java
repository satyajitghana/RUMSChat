package com.msruas.debug.rumschat.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageFromSocket {
    @SerializedName("chatID")
    @Expose
    private String chatID;

    @SerializedName("message")
    @Expose
    private Message message;

    public MessageFromSocket(String chatID, Message message) {
        this.chatID = chatID;
        this.message = message;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public String getChatID() {
        return chatID;
    }
}
