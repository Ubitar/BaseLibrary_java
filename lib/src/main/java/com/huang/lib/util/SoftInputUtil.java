package com.huang.lib.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by chenweiqi on 2017/6/20.
 */

public class SoftInputUtil {
    public static void showSoftKeyboard(Context context, View inputView) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputView.requestFocus();
        imm.showSoftInput(inputView, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    //隐藏软键盘，这个更有效
    public static void hideSoftKeyboard(Activity activity) {
        View currentFocus = activity.getCurrentFocus();
        if (currentFocus != null)
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    // 隐藏键盘
    public static void hideKeyboardWithView(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext()
                .getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
