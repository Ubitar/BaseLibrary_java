package com.huang.lib.util;

import android.app.Application;
import android.util.Log;

import com.huang.lib.common.Constant;

public class L {


    private static Application app;

    public static void init(Application app) {
        L.app = app;
    }

    public static void i(String msg) {
        i(L.app.getPackageName(), msg);
    }

    public static void d(String msg) {
        d(L.app.getPackageName(), msg);
    }

    public static void e(String msg) {
        e(L.app.getPackageName(), msg);
    }

    public static void v(String msg) {
        v(L.app.getPackageName(), msg);
    }

    public static void i(String tag, String msg) {
        if (Constant.isDebug)
            Log.i(tag,msg);
    }

    public static void d(String tag, String msg) {
        if (Constant.isDebug)
            Log.d(tag,msg);
    }

    public static void e(String tag, String msg) {
        if (Constant.isDebug)
            Log.e(tag,msg);
    }

    public static void v(String tag, String msg) {
        if (Constant.isDebug)
            Log.v(tag,msg);
    }
}
