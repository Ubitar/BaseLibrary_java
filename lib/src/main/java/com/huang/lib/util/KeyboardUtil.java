package com.huang.lib.util;

import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class KeyboardUtil {

    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else return true;
        }
        return false;
    }


}
