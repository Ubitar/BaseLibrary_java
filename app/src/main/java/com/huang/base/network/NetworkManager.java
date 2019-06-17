package com.huang.base.network;

import com.baronzhang.retrofit2.converter.FastJsonConverterFactory;
import com.huang.lib.network.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class NetworkManager {
    private static NetworkManager mInstance;
    private static Retrofit retrofit;
    private static volatile ApiService request = null;

    public static NetworkManager init() {
        if (mInstance == null) {
            synchronized (NetworkManager.class) {
                if (mInstance == null) {
                    mInstance = new NetworkManager();
                }
            }
        }
        return mInstance;
    }

    private NetworkManager() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new LoggerInterceptor(LoggerInterceptor.TAG))
                .addInterceptor(new TokenInterceptor())
                .build();
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(ApiService.HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .build();
    }

    public static ApiService getRequest() {
        if (request == null) {
            synchronized (ApiService.class) {
                request = retrofit.create(ApiService.class);
            }
        }
        return request;
    }
}
