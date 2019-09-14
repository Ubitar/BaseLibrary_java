package com.common.saver;

import com.blankj.utilcode.util.StringUtils;
import com.common.bean.UserBean;
import com.orhanobut.hawk.Hawk;

public class UserInfoSaver {
    private final static String USER_INFO = "USER_INFO";
    private static UserBean userBean;
    private static String token;

    public static UserBean getUserInfo() {
        if (UserInfoSaver.userBean == null) UserInfoSaver.userBean = Hawk.get(USER_INFO);
        return UserInfoSaver.userBean;
    }

    public static void saveUserInfo(UserBean userBean) {
        UserInfoSaver.userBean = userBean;
        UserInfoSaver.token = userBean.getToken();
        Hawk.put(USER_INFO, userBean);
    }

    public static boolean isLogined() {
        return !StringUtils.isSpace(userBean.getToken());
    }

    public static String getToken() {
        if (token == null) {
            userBean = getUserInfo();
            if (userBean == null) return null;
            else return token = userBean.getToken();
        } else return token;
    }
}
