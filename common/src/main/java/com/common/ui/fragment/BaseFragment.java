package com.common.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.ui.adapter.BaseActionBarAdapter;
import com.common.ui.dialog.LoadingDialog;
import com.common.ui.delegate.BaseDelegate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import java.util.HashSet;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by laohuang on 2018/9/9.
 */

public abstract class BaseFragment<S extends BaseDelegate> extends BaseSupportFragment<S> {

    private HashSet<Integer> postRefreshEvent = new HashSet<>(10);

    protected Unbinder unbinder;

    protected BaseActionBarAdapter actionBarAdapter;

    protected LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initActionBarAdapter(view);
        viewDelegate.initWidget();
        return view;
    }

    @Override
    public void onDestroyView() {
        if (actionBarAdapter != null) actionBarAdapter.release();
        actionBarAdapter = null;
        super.onDestroyView();
        if (unbinder != null) unbinder.unbind();
        unbinder = null;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    private void initActionBarAdapter(ViewGroup viewGroup) {
        Class<BaseActionBarAdapter> adapterClass = getActionBarAdapter();
        if (adapterClass != null) {
            try {
                actionBarAdapter = adapterClass.newInstance();
                actionBarAdapter.injectView(viewGroup);
            } catch (IllegalAccessException | java.lang.InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    public void showLoading() {
        showLoading("加载中");
    }

    public void showLoading(String message) {
        showLoading(message, true, null);
    }

    public void showLoading(String message, boolean cancelable, DialogInterface.
            OnDismissListener cancelListener) {
        new LoadingDialog.Builder()
                .setCancelable(cancelable)
                .setText(message)
                .setOnDismissListener(cancelListener)
                .build()
                .show(getChildFragmentManager(), LoadingDialog.TAG);
    }

    public void hideLoading() {
        if (loadingDialog != null) loadingDialog.dismiss();
    }

    @Override
    protected void onFragmentVisible() {
        super.onFragmentVisible();
        if (!isFirstVisible) {
            for (int type : postRefreshEvent) refresh(type);
            postRefreshEvent.clear();
        }
    }

    /**
     * 在fragment有空(出现在用于面前)的时候再刷新
     */
    public void postRefresh(int type) {
        if (isFragmentVisible) refresh(type);
        else postRefreshEvent.add(type);
    }

    /**
     * 立即刷新 ，推荐使用PostRefresh
     */
    public void refresh(int type) {
    }

    /**
     * 设置头部适配器
     */
    protected Class getActionBarAdapter() {
        return null;
    }


}
