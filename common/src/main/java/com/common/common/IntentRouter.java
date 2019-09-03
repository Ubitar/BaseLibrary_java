package com.common.common;

import androidx.core.app.ActivityOptionsCompat;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import com.common.R;
import com.common.provider.AppProvider;

public class IntentRouter {

    /** 构建路由对象 */
    public static final Postcard build(String router){
        return ARouter.getInstance().build(router)
                .withOptionsCompat(ActivityOptionsCompat.makeCustomAnimation(AppProvider.getInstance(), R.anim.anim_to_left_open, R.anim.anim_no));
    }

    /** 设置dialog的弹出动画 */
    public static final ActivityOptionsCompat getDialogAnimationCompat() {
        return ActivityOptionsCompat.makeCustomAnimation(AppProvider.getInstance(), R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    public static final String MAIN_ACITIVTY = "/app/main";

}
