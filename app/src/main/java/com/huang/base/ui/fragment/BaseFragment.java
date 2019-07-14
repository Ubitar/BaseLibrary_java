package com.huang.base.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huang.base.ui.delegate.BaseDelegate;
import com.huang.lib.ui.dialog.LoadingDialog;
import com.huang.lib.util.L;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by laohuang on 2018/9/9.
 */

public abstract class BaseFragment<S extends BaseDelegate> extends Fragment {

    protected boolean isFragmentVisible;
    protected boolean isFirstVisible = true;
    protected boolean postRefresh;

    protected S viewDelegate;

    protected Unbinder unbinder;

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
            View view = viewDelegate.onCreateView();
            Log.e("xxx", "onBind   1");
            unbinder = ButterKnife.bind(this, view);
            if (isFirstVisible) viewDelegate.initWidget();
            if (getUserVisibleHint()) {
                if (isFirstVisible) {
                    onFragmentFirstVisible();
                    isFirstVisible = false;
                }
                onFragmentVisibleChange(true);
                isFragmentVisible = true;
            }
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
        if (isFirstVisible && isVisibleToUser) {
            onFragmentFirstVisible();
            isFirstVisible = false;
        }
        if (isVisibleToUser) {
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
            return;
        }
        if (isFragmentVisible) {
            isFragmentVisible = false;
            onFragmentVisibleChange(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        viewDelegate.onResume();
    }

    @Override
    public void onPause() {
        viewDelegate.onPause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        viewDelegate.onDestoryWidget();
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
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
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void initFragmentStatus() {
        isFirstVisible = true;
        isFragmentVisible = false;
        viewDelegate.setRootView(null);
    }

    public void showLoading() {
        showLoading("加载中");
    }

    public void showLoading(String message) {
        showLoading(message, true, null);
    }

    public void showLoading(String message, boolean cancelable, DialogInterface.
            OnCancelListener cancelListener) {
        new LoadingDialog.Builder()
                .setCancelable(cancelable)
                .setText(message)
                .build()
                .show(getChildFragmentManager(), LoadingDialog.TAG);
    }

    public void hideLoading() {
        if (loadingDialog != null) loadingDialog.dismiss();
    }


    /**
     * 去除setUserVisibleHint()多余的回调场景，保证只有当fragment可见状态发生变化时才回调
     * 回调时机在view创建完后，所以支持ui操作，解决在setUserVisibleHint()里进行ui操作有可能报null异常的问题
     * <p>
     * 可在该回调方法里进行一些ui显示与隐藏，比如加载框的显示和隐藏
     *
     * @param isVisible true  不可见 -> 可见
     *                  false 可见  -> 不可见
     */
    protected void onFragmentVisibleChange(boolean isVisible) {

    }

    /**
     * 在fragment首次可见时回调，可在这里进行加载数据，保证只在第一次打开Fragment时才会加载数据，
     * 这样就可以防止每次进入都重复加载数据
     * 该方法会在 onFragmentVisibleChange() 之前调用，所以第一次打开时，可以用一个全局变量表示数据下载状态，
     * 然后在该方法内将状态设置为下载状态，接着去执行下载的任务
     * 最后在 onFragmentVisibleChange() 里根据数据下载状态来控制下载进度ui控件的显示与隐藏
     */
    protected void onFragmentFirstVisible() {
        //todo 继承使用
    }

    public void postRefresh() {
        if (!isFirstVisible) postRefresh = true;
    }

    public void refresh() {
        postRefresh = false;
    }

    protected abstract Class<S> getDelegateClass();

}
