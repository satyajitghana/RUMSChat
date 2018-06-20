package com.msruas.debug.rumschat.network;

import com.msruas.debug.rumschat.model.ResponseMessage;
import com.msruas.debug.rumschat.model.User;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RUMSAPI {
    @POST("users")
    //@Headers({ "Content-type: application/json" })
    Observable<ResponseMessage> register(@Body User user);

    @POST("authenticate")
    Observable<ResponseMessage> login();
}
