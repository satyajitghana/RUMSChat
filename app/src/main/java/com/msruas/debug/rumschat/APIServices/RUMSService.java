package com.msruas.debug.rumschat.APIServices;

import android.util.Base64;

import com.msruas.debug.rumschat.network.RUMSAPI;
import com.msruas.debug.rumschat.utils.Constants;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RUMSService {
    public static RUMSAPI getAPI() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(RUMSAPI.class);
    }

    public static RUMSAPI getAPI(String regNo, String password) {
        String credentials = regNo + ":" + password;
        String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder builder = original.newBuilder()
                    .addHeader("Authorization", basic)
                    .method(original.method(), original.body());
            return chain.proceed(builder.build());
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(RUMSAPI.class);
    }
}
