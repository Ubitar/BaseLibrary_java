package com.huang.lib.network;

import android.content.Context;
import android.content.DialogInterface;

import com.huang.lib.ui.dialog.LoadingDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class DefaultNetObserver<T> implements Observer<T> {

    private boolean cancelAfterCloseLoading;

    protected LoadingDialog loadingDialog;
    protected FragmentManager fragmentManager;
    protected Context context;
    protected Disposable disposable;

    public DefaultNetObserver(){

    }

    public DefaultNetObserver(AppCompatActivity activity) {
        this(activity, "加载中", true);
    }

    public DefaultNetObserver(AppCompatActivity activity, String message) {
        this(activity, message, true);
    }

    public DefaultNetObserver(AppCompatActivity activity, String message, boolean cancelable) {
        this.context = activity;
        if (context != null) {
            this.fragmentManager = activity.getSupportFragmentManager();
            loadingDialog = new LoadingDialog.Builder().setCancelable(cancelable).setText(message)
                    .setOnDismissListener(new NetworkLoadingDismissListener()).build();
        }
    }

    public DefaultNetObserver(Fragment fragment) {
        this(fragment, "加载中", true);
    }

    public DefaultNetObserver(Fragment fragment, String message) {
        this(fragment, message, true);
    }

    public DefaultNetObserver(Fragment fragment, String message, boolean cancelable) {
        this.context = fragment.getContext();
        if (fragment != null) {
            this.fragmentManager = fragment.getChildFragmentManager();
            loadingDialog = new LoadingDialog.Builder().setCancelable(cancelable).setText(message)
                    .setOnDismissListener(new NetworkLoadingDismissListener()).build();
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
        if (loadingDialog != null) loadingDialog.show(fragmentManager, LoadingDialog.TAG);
    }

    @Override
    public void onNext(T e) {
        System.out.println(e);
    }

    @Override
    public void onError(Throwable throwable) {
        if (throwable instanceof ApiException) {
            com.huang.lib.util.T.show(((ApiException) throwable).getDisplayMessage());
        }
        if (loadingDialog != null) loadingDialog.dismiss();
    }

    @Override
    public void onComplete() {
        if (loadingDialog != null) loadingDialog.dismiss();
        this.fragmentManager = null;
        this.context = null;
        this.loadingDialog = null;
    }

    private class NetworkLoadingDismissListener implements DialogInterface.OnDismissListener {
        @Override
        public void onDismiss(DialogInterface dialog) {
            if (cancelAfterCloseLoading) disposable.dispose();
        }
    }
}
