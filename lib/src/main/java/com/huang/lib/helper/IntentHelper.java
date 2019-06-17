package com.huang.lib.helper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

/**
 * Created by laohuang on 2018/11/10.
 */
public class IntentHelper {

    //跳转至系统自带的权限控制界面
    public static void startSystemPermissionActivity(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(intent, 0);
    }

}
