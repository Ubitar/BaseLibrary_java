package com.huang.base.ui.delegate;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huang.base.R;
import com.huang.base.R2;
import com.huang.lib.util.ScreenUtil;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by laohuang on 2018/9/7.
 */

public abstract class BaseDelegate extends AppDelegate {

    protected LinearLayout rootView;
    protected Unbinder unbinder;

    @BindView(R2.id.imgBaseBack)
    ImageView imgBaseBack;
    @BindView(R2.id.txtBaseBack)
    TextView txtBaseBack;
    @BindView(R2.id.txtBaseTitle)
    TextView txtBaseTitle;
    @BindView(R2.id.txtBaseFunction)
    TextView txtBaseFunction;
    @BindView(R2.id.imgBaseFunction)
    ImageView imgBaseFunction;
    @BindView(R2.id.layoutBaseStatusBar)
    RelativeLayout layoutStatusBar;
    @BindView(R2.id.layoutBaseActionBar)
    ConstraintLayout layoutActionBar;

    public BaseDelegate() {
    }

    public View onCreateView() {
        //把状态栏和主布局放到一起
        rootView = new LinearLayout(getActivity());
        rootView.setOrientation(LinearLayout.VERTICAL);
        rootView.addView(LayoutInflater.from(getActivity()).inflate(R.layout.activity_base_head, null));
        rootView.addView(getMainView(), LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        return rootView;
    }

    @Override
    public void initWidget() {
        unbinder = ButterKnife.bind(this, rootView);

        //初始化状态栏高度
        initStatusBar();

        //设置标题
        if (!TextUtils.isEmpty(getActivity().getTitle()))
            txtBaseTitle.setText(getActivity().getTitle());
    }

    public void onPause(){

    }

    public void onResume(){

    }

    @Override
    public void onDestoryWidget() {
        if (unbinder != null) unbinder.unbind();
        unbinder=null;
    }

    @OnClick({R2.id.imgBaseBack, R2.id.txtBaseBack})
    public void onBackClick() {
        getActivity().onBackPressed();
    }

    @OnClick({R2.id.txtBaseFunction, R2.id.imgBaseFunction})
    public void onFunctionClick() {

    }

    public TextView getTxtBaseFunction() {
        return txtBaseFunction;
    }

    public void setTxtBaseTitle(String text) {
        txtBaseTitle.setText(text);
    }

    public void setTextColor(int id) {
        txtBaseTitle.setTextColor(getActivity().getResources().getColor(id));
    }

    public void showBackBtn() {
        imgBaseBack.setVisibility(View.VISIBLE);
    }

    public void setFunctionText(String text) {
        txtBaseFunction.setText(text);
    }

    public void showFunctionText() {
        txtBaseFunction.setVisibility(View.VISIBLE);
    }

    public void setFuncationTextColor(int color) {
        txtBaseFunction.setTextColor(color);
    }

    public void showFunctionImage() {
        imgBaseFunction.setVisibility(View.VISIBLE);
    }

    public void hideFunctionImage() {
        imgBaseFunction.setVisibility(View.GONE);
    }

    public void hideFunctionText() {
        txtBaseFunction.setVisibility(View.INVISIBLE);
    }

    public void setBackText(String text) {
        txtBaseBack.setText(text);
    }

    public void showBackImage() {
        imgBaseBack.setVisibility(View.VISIBLE);
    }

    public void hideBackImage() {
        imgBaseBack.setVisibility(View.GONE);
    }

    public void setFunctionImage(int resId) {
        imgBaseFunction.setImageDrawable(getActivity().getResources().getDrawable(resId));
    }

    public void hideActionBar() {
        layoutActionBar.setVisibility(View.GONE);
    }

    public void showActionBar() {
        layoutActionBar.setVisibility(View.VISIBLE);
    }

    public void hideStatusBar() {
        layoutStatusBar.setVisibility(View.GONE);
    }

    public void showStatusBar() {
        layoutStatusBar.setVisibility(View.VISIBLE);
    }

    public LinearLayout getRootView() {
        return rootView;
    }

    public void setRootView(LinearLayout rootView) {
        this.rootView = rootView;
    }

    private void initStatusBar() {
        int statusHeight = ScreenUtil.getStatusHeight(getActivity());
        layoutStatusBar.getLayoutParams().height = statusHeight;
    }
}
