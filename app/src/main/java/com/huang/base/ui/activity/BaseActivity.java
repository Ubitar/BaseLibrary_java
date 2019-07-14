package com.huang.base.ui.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.gyf.barlibrary.ImmersionBar;
import com.huang.base.ui.delegate.BaseDelegate;
import com.huang.lib.R;
import com.huang.lib.ui.dialog.LoadingDialog;
import com.huang.lib.util.ActivityRecorder;
import com.huang.lib.util.SoftInputUtil;
import com.huang.lib.util.T;
import com.noober.background.BackgroundLibrary;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by laohuang on 2018/9/9.
 */

public abstract class BaseActivity<S extends BaseDelegate> extends AppCompatActivity {
    protected S viewDelegate;

    protected boolean isDoubleBack;
    protected boolean autoHideKeyBoard;
    protected long curMillsTime;

    protected ImmersionBar immersionBar;
    protected Unbinder unbinder;
    protected ActivityRecorder manager = ActivityRecorder.getManager();

    protected LoadingDialog loadingDialog;


    public BaseActivity() {
        try {
            viewDelegate = this.getDelegateClass().newInstance();
        } catch (InstantiationException var2) {
            throw new RuntimeException("createMainView IDelegate error");
        } catch (IllegalAccessException var3) {
            throw new RuntimeException("createMainView IDelegate error");
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        BackgroundLibrary.inject(this);
        super.onCreate(savedInstanceState);

        createMainBinding(getLayoutInflater(), null, savedInstanceState);

        View rootView = viewDelegate.onCreateView();
        setContentView(rootView);
        unbinder = ButterKnife.bind(this);

        immersionBar = ImmersionBar.with(this);
        immersionBar.statusBarDarkFont(true)
                .navigationBarColor(android.R.color.white).init();

        viewDelegate.initWidget();

        manager.addActivity(this);
    }

    //使onActivityResult能够传到fragment
    @SuppressLint("RestrictedApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onActivityResultForFragment(getSupportFragmentManager().getFragments(), requestCode, resultCode, data);
    }

    @SuppressLint("RestrictedApi")
    private void onActivityResultForFragment(List<Fragment> rootFragmentList, int requestCode, int resultCode, Intent data) {
        if (rootFragmentList != null) {
            for (Fragment fragment : rootFragmentList) {
                if (fragment == null) continue;
                fragment.onActivityResult(requestCode, resultCode, data);
                onActivityResultForFragment(fragment.getChildFragmentManager().getFragments(), requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (isDoubleBack) {
            if (System.currentTimeMillis() - curMillsTime < 1500) super.onBackPressed();
            else {
                curMillsTime = System.currentTimeMillis();
                T.showShort("再点击一次退出");
            }
        } else super.onBackPressed();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_right_out);
    }

    @Override
    public void finish() {
        SoftInputUtil.hideSoftKeyboard(this);
        super.finish();
        overridePendingTransition(R.anim.activity_right_in, R.anim.activity_right_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewDelegate.onResume();
    }

    @Override
    protected void onPause() {
        viewDelegate.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        hideLoading();
        viewDelegate.onDestoryWidget();
        unbinder.unbind();
        unbinder = null;
        viewDelegate = null;
        immersionBar.destroy();
        manager.finishActivity(this);
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (autoHideKeyBoard && ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                SoftInputUtil.hideKeyboardWithView(v);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else return true;
        }
        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        if (this.viewDelegate.getOptionsMenuId() != 0) {
            this.getMenuInflater().inflate(this.viewDelegate.getOptionsMenuId(), menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    protected <D extends ViewDataBinding> D
    createMainBinding(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return this.viewDelegate.createMainView(inflater, container, savedInstanceState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (this.viewDelegate == null) {
            try {
                viewDelegate = this.getDelegateClass().newInstance();
                createMainBinding(this.getLayoutInflater(), null, savedInstanceState);
            } catch (InstantiationException var3) {
                throw new RuntimeException("createMainView IDelegate error");
            } catch (IllegalAccessException var4) {
                throw new RuntimeException("createMainView IDelegate error");
            }
        }
    }

    public boolean isDoubleBack() {
        return isDoubleBack;
    }

    public void setDoubleBack(boolean doubleBack) {
        isDoubleBack = doubleBack;
    }

    public boolean isAutoHideKeyBoard() {
        return autoHideKeyBoard;
    }

    public void setAutoHideKeyBoard(boolean autoHideKeyBoard) {
        this.autoHideKeyBoard = autoHideKeyBoard;
    }

    public void showLoading() {
        showLoading("加载中");
    }

    public void showLoading(String message) {
        showLoading(message, true, null);
    }

    public void showLoading(String message, boolean cancelable, DialogInterface.
            OnCancelListener cancelListener) {
        loadingDialog = new LoadingDialog.Builder()
                .setCancelable(cancelable)
                .setText(message)
                .build();
        loadingDialog.show(getSupportFragmentManager(), LoadingDialog.TAG);
    }

    public void hideLoading() {
        if (loadingDialog != null) loadingDialog.dismiss();
    }

    protected BaseActivity getActivity() {
        return this;
    }

    protected abstract Class<S> getDelegateClass();

}
