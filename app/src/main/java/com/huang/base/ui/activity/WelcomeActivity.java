package com.huang.base.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.common.bean.UserBean;
import com.common.common.IntentRouter;
import com.common.ui.activity.BaseActivity;
import com.common.ui.dialog.AlertConfirmDialog;
import com.huang.base.ui.delegate.WelcomeDelegate;
import com.huang.lib.helper.IntentHelper;
import com.qw.soul.permission.SoulPermission;
import com.qw.soul.permission.bean.Permission;
import com.qw.soul.permission.bean.Permissions;
import com.qw.soul.permission.callbcak.CheckRequestPermissionsListener;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class WelcomeActivity extends BaseActivity<WelcomeDelegate> {
    @Override
    protected Class<WelcomeDelegate> getDelegateClass() {
        return WelcomeDelegate.class;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        immersionBar.reset()
                .transparentBar()
                .init();
        checkPermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        checkPermission();
    }

    private void checkPermission() {
        SoulPermission.getInstance().checkAndRequestPermissions(
                Permissions.build(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                new CheckRequestPermissionsListener() {
                    @Override
                    public void onAllPermissionOk(Permission[] allPermissions) {
                        Observable.timer(2, TimeUnit.SECONDS)
                                .as(AutoDispose.<Long>autoDisposable(AndroidLifecycleScopeProvider.from(getActivity())))
                                .subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(Long aLong) throws Exception {
                                        UserBean testBean = new UserBean();
                                        testBean.setName("123321");
                                        IntentRouter.build(IntentRouter.MAIN_ACITIVTY)
                                                .navigation();
                                        finish();
                                    }
                                });
                    }

                    @Override
                    public void onPermissionDenied(Permission[] refusedPermissions) {
                        new AlertConfirmDialog.Builder()
                                .setTitle("缺少必要权限")
                                .setContent("当前应用缺少必要权限,请点击\"设置\"-\"权限\"-打开所需权限。")
                                .setSubmitText("设置").setCancelText("退出")
                                .setCancelable(false)
                                .setOnClickSubmitListener(new AlertConfirmDialog.OnClickSubmitListener() {
                                    @Override
                                    public void onClick(AlertConfirmDialog dialog) {
                                        IntentHelper.startSystemPermissionActivity(getActivity());
                                    }
                                })
                                .setOnClickCancelListener(new AlertConfirmDialog.OnClickCancelListener() {
                                    @Override
                                    public void onClick(AlertConfirmDialog dialog) {
                                        finish();
                                    }
                                })
                                .build()
                                .showNow(getSupportFragmentManager(), AlertConfirmDialog.TAG);
                    }
                });
    }
}
