package com.common.network;

import android.app.Activity;
import android.content.DialogInterface;

import com.common.ui.dialog.LoadingDialog;
import com.huang.lib.network.ApiException;
import com.huang.lib.util.ActivityRecorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class DefaultNetObserver<T> implements Observer<T> {

    private boolean cancelAfterCloseLoading;

    protected LoadingDialog loadingDialog;
    protected FragmentManager fragmentManager;
    protected Disposable disposable;

    public DefaultNetObserver() {
        this(true, "加载中", true);
    }

    public DefaultNetObserver(String message) {
        this(true, message, true);
    }

    public DefaultNetObserver(boolean showLoading) {
        this(showLoading, "加载中", true);
    }

    public DefaultNetObserver(boolean showLoading, String message, boolean cancelable) {
        Activity activity = ActivityRecorder.getManager().currentActivity();
        if (activity != null) {
            if (activity instanceof FragmentActivity) {
                this.fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
                if (showLoading)
                    loadingDialog = new LoadingDialog.Builder().setCancelable(cancelable).setText(message)
                            .setOnDismissListener(new NetworkLoadingDismissListener()).build();
            }

        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
        if (loadingDialog != null) loadingDialog.show(fragmentManager);
    }

    @Override
    public void onNext(T e) {
    }

    @Override
    public void onError(Throwable throwable) {
        if (throwable instanceof ApiException) {
            com.huang.lib.util.T.show(((ApiException) throwable).getDisplayMessage());
        }
        if (loadingDialog != null) loadingDialog.dismissAllowingStateLoss();
        this.fragmentManager = null;
        this.loadingDialog = null;
    }

    @Override
    public void onComplete() {
        if (loadingDialog != null) loadingDialog.dismissAllowingStateLoss();
        this.fragmentManager = null;
        this.loadingDialog = null;
    }

    private class NetworkLoadingDismissListener implements DialogInterface.OnDismissListener {
        @Override
        public void onDismiss(DialogInterface dialog) {
            if (cancelAfterCloseLoading) disposable.dispose();
        }
    }
}
