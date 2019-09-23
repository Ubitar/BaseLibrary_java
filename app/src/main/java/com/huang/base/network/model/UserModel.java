package com.huang.base.network.model;


import com.common.bean.BaseResponse;
import com.common.bean.UserBean;
import com.common.network.model.BaseModel;
import com.huang.base.network.api.UserApi;

import io.reactivex.Observable;

/**
 * XXModel类中的方法主要是对网络请求做部分准备
 * <p>
 * 例如：你传入了一个Bean类，希望通过该Model类进行对类的拆解，取出Bean类中相应的值放入Api接口中作为参数
 * <p>
 * 例如2：你的传入参数需要进行排序签名，可以将代码放置到此处
 */

public class UserModel extends BaseModel<UserApi> {

    @Override
    protected Class getApi() {
        return UserApi.class;
    }

    public Observable<BaseResponse<UserBean>> login(String account, String password) {
        return model.login(account, password);
    }

    public Observable<BaseResponse<Object>> logout(String token) {
        return model.logout(token);
    }

}