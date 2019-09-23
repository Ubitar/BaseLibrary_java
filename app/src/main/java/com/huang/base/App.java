package com.huang.base;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.common.network.NetworkManager;
import com.huang.lib.common.Constant;
import com.huang.lib.util.L;
import com.huang.lib.util.T;
import com.orhanobut.hawk.Hawk;
import com.qw.soul.permission.SoulPermission;

public class App extends Application {
    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        T.init(this);
        L.init(this);
        Utils.init(this);
        SoulPermission.init(this);
        NetworkManager.init();
        Hawk.init(this).build();

        if (Constant.isDebug) {           // These two lines must be written before init, otherwise these configurations will be invalid in the init process
            ARouter.openLog();     // Print log
            ARouter.openDebug();   // Turn on debugging mode (If you are running in InstantRun mode, you must turn on debug mode! Online version needs to be closed, otherwise there is a security risk)
        }
        ARouter.init(this); // As earl

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).onLowMemory();
    }

    public static App getInstance() {
        return app;
    }
}
