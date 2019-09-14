package com.huang.base.ui.delegate;

import android.graphics.Color;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.ui.delegate.BaseDelegate;
import com.huang.base.R;

import butterknife.BindView;

public class MainFragmentDelegate extends BaseDelegate {
    @BindView(R.id.bg)
    RelativeLayout bg;
    @BindView(R.id.txt)
    TextView txt;

    @Override
    protected int getMainLayoutId() {
        return R.layout.activity_main_fragment;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        txt.setText("测试");
    }

    public void setBackgroundColor(int index) {
        if (index <= 0) bg.setBackgroundColor(Color.RED);
        else if (index == 1) bg.setBackgroundColor(Color.GREEN);
        else if (index == 2) bg.setBackgroundColor(Color.BLUE);
        else if (index == 3) bg.setBackgroundColor(Color.GRAY);
    }

    public void setText(String text) {
        txt.setText(text);
    }
}
