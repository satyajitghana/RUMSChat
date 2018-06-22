package com.msruas.debug.rumschat.model;

public class User {
    private String regNo;
    private String name;
    private String email;
    private String password;
    private String created_at;
    private String newPassword;
    private String token;

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRegNo() {
        return this.regNo;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
