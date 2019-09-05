package com.huang.base.ui.activity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.common.common.IntentRouter;
import com.common.ui.dialog.AlertConfirmDialog;
import com.huang.base.R;
import com.huang.base.bean.BaseResponse;
import com.huang.base.bean.UserBean;
import com.huang.base.common.Constant;
import com.huang.base.network.NetworkManager;
import com.huang.base.network.ResponseCompose;
import com.huang.base.ui.adapter.FragmentViewPagerAdapter;
import com.huang.base.ui.delegate.MainDelegate;
import com.huang.base.ui.fragment.MainFragment;
import com.common.network.DefaultNetObserver;
import com.huang.lib.network.SchedulerCompose;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import butterknife.OnClick;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

@Route(path = IntentRouter.MAIN_ACITIVTY)
public class MainActivity extends BaseActivity<MainDelegate> {

    private FragmentViewPagerAdapter adapter;

    @Override
    protected Class getDelegateClass() {
        return MainDelegate.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDelegate.setText("点击发起网络请求1");
        initViewPager();
    }

    @OnClick(R.id.txt)
    public void onClickTxt() {
        NetworkManager.getRequest().login("123", "123")
                .compose(ResponseCompose.parseResult())
                .flatMap(new Function<UserBean, ObservableSource<BaseResponse<Object>>>() {
                    @Override
                    public ObservableSource<BaseResponse<Object>> apply(UserBean userBean) throws Exception {
                        Constant.saveUserInfo(userBean);
                        return NetworkManager.getRequest().logout(userBean.getToken());
                    }
                })
                .compose(SchedulerCompose.io2main())
                .compose(ResponseCompose.parseResult())
                .as(AutoDispose.<Object>autoDisposable(AndroidLifecycleScopeProvider.from(this)))
                .subscribe(new DefaultNetObserver<Object>() {
                    @Override
                    public void onNext(Object e) {
                        super.onNext(e);
                    }
                });
    }

    @OnClick(R.id.tab1)
    public void onClickTab1() {
        viewDelegate.setCurrentAt(0);
    }

    @OnClick(R.id.tab2)
    public void onClickTab2() {
        viewDelegate.setCurrentAt(1);
    }

    @OnClick(R.id.tab3)
    public void onClickTab3() {
        viewDelegate.setCurrentAt(2);
    }

    @OnClick(R.id.tab4)
    public void onClickTab4() {
        viewDelegate.setCurrentAt(3);
    }

    @OnClick(R.id.txtOption)
    public void onClickTxtOption() {
        new AlertConfirmDialog.Builder()
                .setTitle("标题")
                .setContent("内容")
                .build()
                .showNow(getSupportFragmentManager(), AlertConfirmDialog.TAG);
    }

    private void initViewPager() {
        adapter = new FragmentViewPagerAdapter(getSupportFragmentManager());
        List<Fragment> fragments = new ArrayList<>(4);
        fragments.add(MainFragment.newInstance(0));
        fragments.add(MainFragment.newInstance(1));
        fragments.add(MainFragment.newInstance(2));
        fragments.add(MainFragment.newInstance(3));
        adapter.setFragments(fragments);
        viewDelegate.initViewPager(adapter);
    }
}
