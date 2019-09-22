package com.common.network;

import com.baronzhang.retrofit2.converter.FastJsonConverterFactory;
import com.huang.lib.network.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class NetworkManager {
    private static NetworkManager mInstance;
    private static Retrofit retrofit;

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
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new LoggerInterceptor(LoggerInterceptor.TAG))
                .addInterceptor(new TokenInterceptor())
                .build();
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(Host.DEFAULT_HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .build();
    }

    public static <T> T getRequest(Class<T> c) {
        synchronized (c) {
            return retrofit.create(c);
        }
    }
}
