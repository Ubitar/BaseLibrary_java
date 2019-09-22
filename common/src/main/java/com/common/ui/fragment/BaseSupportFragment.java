package com.common.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.common.ui.delegate.BaseDelegate;

public abstract class BaseSupportFragment<S extends BaseDelegate> extends Fragment {

    protected boolean isFragmentVisible;
    protected boolean isFirstVisible = true;

    protected S viewDelegate;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createViewDelegate();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (viewDelegate.getRootView() == null) {
            createMainViewBinding(inflater, container, savedInstanceState);
            viewDelegate.onCreateView();
            return viewDelegate.getRootView();
        }
        return viewDelegate.getRootView();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //setUserVisibleHint()有可能在fragment的生命周期外被调用
        if (viewDelegate == null || viewDelegate.getRootView() == null) {
            return;
        }
        if (isVisibleToUser) {
            onFragmentVisible();
            isFragmentVisible = true;
        }
        if (isFirstVisible && isVisibleToUser) {
            onFragmentFirstVisible();
            isFirstVisible = false;
            return;
        }
        if (isFragmentVisible) {
            onFragmentInVisible();
            isFragmentVisible = false;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isFragmentVisible = !hidden;
        if (hidden) onFragmentInVisible();
        else onFragmentVisible();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            viewDelegate.onVisibleWidget();
            onFragmentVisible();
            isFragmentVisible = true;
            if (isFirstVisible) {
                onFragmentFirstVisible();
                isFirstVisible = false;
            }
        }
    }

    @Override
    public void onPause() {
        if (getUserVisibleHint()) {
            viewDelegate.onInVisibleWidget();
            onFragmentInVisible();
        }
        super.onPause();
    }


    @Override
    public void onDestroyView() {
        viewDelegate.onDestroyWidget();
        initFragmentStatus();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        this.viewDelegate = null;
        super.onDestroy();
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

    private void initFragmentStatus() {
        isFirstVisible = true;
        isFragmentVisible = false;
        viewDelegate.setRootView(null);
    }

    //fragment为可见状态  onResume+setUserVisibleHint
    protected void onFragmentVisible() {
    }

    //fragment为可见状态  setUserVisibleHint+onPause
    protected void onFragmentInVisible() {
    }

    // 判断可见性，对手动显示与PagerAdapter方式均有效，且跟随父fragment可见性状态
    public boolean isFragmentVisible() {
        if (isFragmentVisible ) {
            if (getParentFragment() == null) {
                return true;
            }
            if (getParentFragment() instanceof BaseSupportFragment) {
                return ((BaseFragment) getParentFragment()).isFragmentVisible();
            } else {
                return getParentFragment().isVisible();
            }
        }
        return false;
    }

    //懒加载
    protected void onFragmentFirstVisible() {
        // 继承使用
    }

    protected abstract Class<S> getDelegateClass();

}
