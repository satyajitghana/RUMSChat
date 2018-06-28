package com.msruas.debug.rumschat.model;

public class ChatDM {
    private String chatID;
    private String regNo;
    private String name;

    public ChatDM() {

    }

    public ChatDM(String chatID, String regNo, String name) {
        this.chatID = chatID;
        this.regNo = regNo;
        this.name = name;
    }

    public void setChatID(String chatID){
        this.chatID = chatID;
    }
    public void setRegNo(String regNo){
        this.regNo = regNo;
    }
    public void setName(String name){
        this.name= name;
    }

    public String getChatID() {
        return this.chatID;
    }
    public String getRegNo() {
        return this.regNo;
    }
    public String getName() {
        return this.name;
    }

}
