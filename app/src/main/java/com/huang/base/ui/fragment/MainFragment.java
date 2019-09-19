package com.huang.base.ui.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.launcher.ARouter;
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
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        ARouter.getInstance().inject(this);
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
