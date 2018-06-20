package com.msruas.debug.rumschat.model;

public class ResponseMessage {
    private String message;
    private String token;

    public String getMessage() {
        return this.message;
    }

    public String getToken() {
        return this.token;
    }

    @Override
    public String toString() {
        String ret = "";
        ret = ret + this.message + " " + this.token;
        return ret;
    }
}
