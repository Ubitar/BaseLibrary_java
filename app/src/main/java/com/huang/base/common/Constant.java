package com.huang.base.common;

import com.huang.base.bean.UserBean;
import com.orhanobut.hawk.Hawk;

public class Constant extends com.huang.lib.common.Constant {
    public final static String USER_INFO = "USER_INFO";
    private static UserBean userBean;
    private static String token;

    public static UserBean getUserInfo() {
        if (Constant.userBean == null) Constant.userBean = Hawk.get(USER_INFO);
        return Constant.userBean;
    }

    public static void saveUserInfo(UserBean userBean) {
        Constant.userBean = userBean;
        Constant.token = userBean.getToken();
        Hawk.put(USER_INFO, userBean);
    }

    public static String getToken() {
        if (token == null) {
            userBean = getUserInfo();
            if (userBean == null) return null;
            else return token = userBean.getToken();
        } else return token;
    }
}
