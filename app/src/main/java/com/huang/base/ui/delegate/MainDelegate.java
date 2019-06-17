package com.huang.base.ui.delegate;

import android.widget.TextView;

import com.huang.base.R;
import com.huang.base.ui.adapter.FragmentViewPagerAdapter;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

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
        showStatusBar();
        showActionBar();
    }

    public void setText(String text) {
        txt.setText(text);
    }

    public void initViewPager(
            FragmentViewPagerAdapter adapter
    ) {
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
    }

    public void setCurrentAt(int index) {
        viewPager.setCurrentItem(index,false);
    }
}
