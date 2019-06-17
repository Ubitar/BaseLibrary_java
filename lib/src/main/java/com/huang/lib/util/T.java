package com.huang.lib.util;

import android.app.Application;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by chenweiqi on 2017/1/6.
 */

public class T {

    private static Application app;

    public static void init(Application app) {
        T.app = app;
    }

    public static Toast show(String message) {
        return showShortCenter(message);
    }

    public static Toast showShort(String message) {
        Toast toast = Toast.makeText(app, message, Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }

    public static Toast showLong(String message) {
        Toast toast = Toast.makeText(app, message, Toast.LENGTH_LONG);
        toast.show();
        return toast;
    }

    public static Toast showShortCenter(String message) {
        Toast toast = Toast.makeText(app, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return toast;
    }

    public static Toast showLongCenter(String message) {
        Toast toast = Toast.makeText(app, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return toast;
    }

    public static Toast showShortTopLeft(String message, int x, int y) {
        Toast toast = Toast.makeText(app, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.LEFT | Gravity.TOP, x, y);
        toast.show();
        return toast;
    }
}
