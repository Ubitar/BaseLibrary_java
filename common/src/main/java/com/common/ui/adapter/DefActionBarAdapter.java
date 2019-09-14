package com.common.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.StringUtils;
import com.common.R;
import com.common.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DefActionBarAdapter implements BaseActionBarAdapter {

    @BindView(R2.id.layoutBaseStatusBar)
    RelativeLayout layoutBaseStatusBar;
    @BindView(R2.id.layoutBaseActionBar)
    ConstraintLayout layoutBaseActionBar;
    @BindView(R2.id.txtBaseTitle)
    TextView txtBaseTitle;

    private Unbinder unbinder;

    private OnClickLeftListener onClickLeftListener;
    private OnClickRightListener onClickRightListener;

    @Override
    public void injectView(ViewGroup root) {
        View head = LayoutInflater.from(root.getContext()).inflate(R.layout.activity_base_head, null);
        root.addView(head, 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        unbinder = ButterKnife.bind(this, head);

        //初始化状态栏高度
        layoutBaseStatusBar.getLayoutParams().height = BarUtils.getStatusBarHeight();

        //设置标题
        Activity activity = (Activity) root.getContext();
        if (!StringUtils.isSpace(activity.getTitle().toString()))
            txtBaseTitle.setText(activity.getTitle());
    }

    @OnClick({R2.id.imgBaseBack, R2.id.txtBaseBack})
    void onClickLeft(View view) {
        if (onClickLeftListener == null) {
            Activity activity = (Activity) view.getContext();
            activity.onBackPressed();
        } else {
            onClickLeftListener.onClick();
        }
    }

    @OnClick({R2.id.imgBaseFunction, R2.id.txtBaseFunction})
    void onClickRight() {
        if (onClickRightListener != null)
            onClickRightListener.onClick();
    }

    @Override
    public void showActionBar() {
        layoutBaseActionBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideActionBar() {
        layoutBaseActionBar.setVisibility(View.GONE);
    }

    @Override
    public void setOnClickLeftListener(OnClickLeftListener listener) {
        this.onClickLeftListener = listener;
    }

    @Override
    public void setOnClickRightListener(OnClickRightListener listener) {
        this.onClickRightListener = listener;
    }

    @Override
    public void release() {
        unbinder.unbind();
        unbinder = null;
    }


}
