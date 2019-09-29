package com.huang.base.ui.fragment;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
import com.common.common.IntentRouter;
import com.common.ui.adapter.DefActionBarAdapter;
import com.common.ui.fragment.BaseFragment;
import com.huang.base.R;
import com.huang.base.ui.delegate.MainFragmentDelegate;

import butterknife.OnClick;

public class MainFragment extends BaseFragment<MainFragmentDelegate> {
    @Autowired(name = "index")
    int index;

    @Override
    protected Class getDelegateClass() {
        return MainFragmentDelegate.class;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        viewDelegate.setText("点击fragment" + (index + 1)+"\n打开新的界面");
        viewDelegate.setBackgroundColor(index);
    }

    @Override
    protected void onFragmentVisible() {
        super.onFragmentVisible();
    }

    @Override
    protected void onFragmentInVisible() {
        super.onFragmentInVisible();
    }

    @OnClick(R.id.txt)
    public void onClickTxt() {
        IntentRouter.build(IntentRouter.MAIN_ACITIVTY).navigation(getContext());
    }

    public static MainFragment newInstance(int index) {
        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        return fragment;
    }
}
