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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by laohuang on 2018/9/9.
 */

public abstract class BaseFragment<S extends BaseDelegate> extends BaseSwipeBackFragment {

    private HashSet<Integer> postRefreshEvent = new HashSet<>(10);

    private boolean isLazyLoaded = false;

    protected S viewDelegate;

    protected Unbinder unbinder;

    protected BaseActionBarAdapter actionBarAdapter;

    protected LoadingDialog loadingDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createViewDelegate();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (viewDelegate.getRootView() == null) {
            createMainViewBinding(inflater, container, savedInstanceState);
            ViewGroup view = viewDelegate.onCreateView();
            unbinder = ButterKnife.bind(this, view);
            initActionBarAdapter(view);
            viewDelegate.initWidget();
            return viewDelegate.getRootView();
        }
        return viewDelegate.getRootView();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        isLazyLoaded = true;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        viewDelegate.onSupportVisible();
        if (isLazyLoaded) {
            for (int type : postRefreshEvent) refresh(type);
            postRefreshEvent.clear();
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        viewDelegate.onSupportInvisible();
    }

    @Override
    public void onDestroyView() {
        if (actionBarAdapter != null) actionBarAdapter.release();
        actionBarAdapter = null;
        viewDelegate.onDestroyWidget();
        if (unbinder != null) unbinder.unbind();
        unbinder = null;
        initFragmentStatus();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        this.viewDelegate = null;
        super.onDestroy();
    }

    protected boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    protected boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    protected <D extends ViewDataBinding> D createMainViewBinding(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.viewDelegate.createMainView(inflater, container, savedInstanceState);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (this.viewDelegate.getOptionsMenuId() != 0) {
            inflater.inflate(this.viewDelegate.getOptionsMenuId(), menu);
        }
    }

    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (viewDelegate == null) createViewDelegate();
    }

    private void createViewDelegate() {
        try {
            viewDelegate = getDelegateClass().newInstance();
        } catch (IllegalAccessException | java.lang.InstantiationException e) {
            e.printStackTrace();
        }
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

    private void initFragmentStatus() {
        viewDelegate.setRootView(null);
    }

    public void showLoading() {
        showLoading("加载中");
    }

    public void showLoading(String message) {
        showLoading(message, true, null);
    }

    public void showLoading(String message, boolean cancelable, DialogInterface.OnDismissListener cancelListener) {
        if (isDetached() || isInLayout() || isRemoving() || loadingDialog != null) return;
        loadingDialog = new LoadingDialog.Builder()
                .setCancelable(cancelable)
                .setText(message)
                .setOnDismissListener(cancelListener)
                .build();
        loadingDialog.show(getChildFragmentManager(), LoadingDialog.TAG);
    }

    public void hideLoading() {
        if (loadingDialog != null) loadingDialog.dismissAllowingStateLoss();
        loadingDialog = null;
    }

    /**
     * 在fragment有空(出现在用于面前)的时候再刷新
     */
    public void postRefresh(int type) {
        if (isSupportVisible()) refresh(type);
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

    protected abstract Class<S> getDelegateClass();

}
