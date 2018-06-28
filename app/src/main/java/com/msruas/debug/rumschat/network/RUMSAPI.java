package com.msruas.debug.rumschat.network;

import com.msruas.debug.rumschat.model.ChatDM;
import com.msruas.debug.rumschat.model.Message;
import com.msruas.debug.rumschat.model.ResponseMessage;
import com.msruas.debug.rumschat.model.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RUMSAPI {
    @POST("users")
    //@Headers({ "Content-type: application/json" })
    Observable<ResponseMessage> register(@Body User user);

    @POST("authenticate")
    Observable<ResponseMessage> login();

    @POST("send_message/{chatID}")
    Observable<ResponseMessage> sendMessage(@Path("chatID") String chatID, @Body Message message);

    @GET("users")
    Observable<List<User>> getUsers();

    @GET("users/{regNo}")
    Observable<ResponseMessage> getUser(@Path("regNo") String regNo);

    @GET("get_chats/{regNo}")
    Observable<List<ChatDM>> getChatDMs(@Path("regNo") String regNo);

    @GET("get_messages/{chatID}")
    Observable<List<Message>> getMessages(@Path("chatID") String chatID);
}
