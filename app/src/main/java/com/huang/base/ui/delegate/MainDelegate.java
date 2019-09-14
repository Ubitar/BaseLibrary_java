package com.huang.base.ui.delegate;

import android.widget.TextView;

import com.common.ui.delegate.BaseDelegate;
import com.huang.base.R;
import com.common.ui.adapter.FragmentViewPagerAdapter;
import com.common.ui.adapter.DefActionBarAdapter;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public class MainDelegate extends BaseDelegate {
    @BindView(R.id.txt)
    TextView txt;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @Override
    protected int getMainLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initWidget() {
        super.initWidget();
    }

    public void setText(String text) {
        txt.setText(text);
    }

    public void initViewPager(FragmentViewPagerAdapter adapter) {
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
    }

    public void setCurrentAt(int index) {
        viewPager.setCurrentItem(index,false);
    }
}
