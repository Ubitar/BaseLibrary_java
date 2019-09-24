package com.common.network;

import com.huang.lib.network.ApiException;
import com.huang.lib.network.DefaultNetExceptionParser;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class RetryWhenFunction implements Function<Observable<? extends Throwable>, Observable<?>> {

    //重试延迟时间
    private int delayTime = 3000;
    //重试次数
    private int maxRetryCount = 3;
    private int currentTime = 0;

    public RetryWhenFunction() {

    }

    public RetryWhenFunction(int delayTime, int maxRetryCount) {
        this.delayTime = delayTime;
        this.maxRetryCount = maxRetryCount;
    }

    @Override
    public Observable<?> apply(Observable<? extends Throwable> observable) throws Exception {
        return observable.flatMap(new Function<Throwable, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Throwable throwable) throws Exception {
                if (throwable instanceof ApiException) {
                    ApiException exception = (ApiException) throwable;
                    if (exception.getCode() == DefaultNetExceptionParser.NETWORK_ERROR) {
                        if (currentTime++ < maxRetryCount) {
                            return Observable.just(currentTime).delay(delayTime, TimeUnit.MILLISECONDS);
                        }
                    }
                }
                return Observable.error(throwable);
            }
        });
    }
}