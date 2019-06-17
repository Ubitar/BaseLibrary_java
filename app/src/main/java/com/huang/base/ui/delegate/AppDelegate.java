package com.huang.base.ui.delegate;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * Created by laohuang on 2018/9/9.
 */

public abstract class AppDelegate {
    protected final SparseArray<View> mViews = new SparseArray();
    protected View mainView;

    public AppDelegate() {
    }

    public <D extends ViewDataBinding> D createMainView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int rootLayoutId = getMainLayoutId();
        D binding = DataBindingUtil.inflate(inflater, rootLayoutId, container, false);
        if (binding == null) {
            this.mainView = inflater.inflate(rootLayoutId, container, false);
        } else {
            this.mainView = binding.getRoot();
        }

        return binding;
    }

    public int getOptionsMenuId() {
        return 0;
    }

    public View getMainView() {
        return this.mainView;
    }

    public void setMainView(View mainView) {
        this.mainView = mainView;
    }

    public void initWidget() {

    }

    public void onDestoryWidget() {

    }

    public <S extends View> S bindView(int id) {
        S view = (S) this.mViews.get(id);
        if (view == null) {
            view = (S) this.mainView.findViewById(id);
            this.mViews.put(id, view);
        }

        return view;
    }

    public <S extends View> S get(int id) {
        return this.bindView(id);
    }

    public <S extends AppCompatActivity> S getActivity() {
        return (S) this.mainView.getContext();
    }

    protected abstract int getMainLayoutId();
}
