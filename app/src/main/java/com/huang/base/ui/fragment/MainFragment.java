package com.huang.base.ui.fragment;

import android.os.Bundle;

import com.huang.base.R;
import com.huang.base.ui.delegate.MainFragmentDelegate;

import butterknife.OnClick;

public class MainFragment extends BaseFragment<MainFragmentDelegate> {
    private int index;

    @Override
    protected Class getDelegateClass() {
        return MainFragmentDelegate.class;
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        index = getArguments().getInt("index", 0);
        viewDelegate.setText("fragment" + (index + 1));
        viewDelegate.setBackgroundColor(index);
    }

    @OnClick(R.id.txt)
    public void onClickTxt() {
        showLoading("点击外面加载框消失");
    }

    public static MainFragment newInstance(int index) {
        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        return fragment;
    }
}
