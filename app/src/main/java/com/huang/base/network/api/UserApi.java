package com.huang.base.network.api;

import com.common.bean.BaseResponse;
import com.common.bean.UserBean;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 用户相关接口路由
 */
public interface UserApi {

    @FormUrlEncoded
    @POST("student/login")
    Observable<BaseResponse<UserBean>> login(
            @Field("account") String account,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("student/logout")
    Observable<BaseResponse<Object>> logout(@Field("token") String token);
}
