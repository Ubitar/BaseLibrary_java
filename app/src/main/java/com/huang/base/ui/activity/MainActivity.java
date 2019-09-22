package com.huang.base.ui.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.common.bean.UserBean;
import com.common.common.IntentRouter;
import com.common.network.DefaultNetObserver;
import com.common.saver.UserInfoSaver;
import com.common.ui.activity.BaseActivity;
import com.common.ui.adapter.DefActionBarAdapter;
import com.common.ui.adapter.FragmentViewPagerAdapter;
import com.common.ui.fragment.BaseSupportFragment;
import com.huang.base.R;
import com.common.bean.BaseResponse;
import com.common.network.NetworkManager;
import com.common.network.ResponseCompose;
import com.huang.base.network.model.UserModel;
import com.huang.base.ui.delegate.MainDelegate;
import com.huang.base.ui.fragment.MainFragment;
import com.huang.lib.network.SchedulerCompose;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

@Route(path = IntentRouter.MAIN_ACITIVTY)
public class MainActivity extends BaseActivity<MainDelegate> {

    private FragmentViewPagerAdapter adapter;

    private UserModel userModel=new UserModel();

    @Override
    protected Class getDelegateClass() {
        return MainDelegate.class;
    }

    @Override
    protected Class getActionBarAdapter() {
        return DefActionBarAdapter.class;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDelegate.setText("点击发起网络请求1");
        initViewPager();
        // 显示隐藏actionbar
        actionBarAdapter.showActionBar();
        //演示点击返回
        actionBarAdapter.setOnClickLeftListener(() -> onBackPressed());

        initShowHideFragment();
    }

    @OnClick(R.id.txt)
    public void onClickTxt() {
        userModel.login("123", "123")
                .compose(ResponseCompose.parseResult())
                .flatMap(new Function<UserBean, ObservableSource<BaseResponse<Object>>>() {
                    @Override
                    public ObservableSource<BaseResponse<Object>> apply(UserBean userBean) throws Exception {
                        UserInfoSaver.saveUserInfo(userBean);
                        return userModel.logout(userBean.getToken());
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

    @OnClick(R.id.btn)
    public void onClickBtn() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        BaseSupportFragment fragment = (BaseSupportFragment) fragmentManager.findFragmentByTag("hidden");
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (fragment.isFragmentVisible()) fragmentTransaction.hide(fragment);
        else fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
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
        viewDelegate.setCurrentAt(3);
    }

    private void initShowHideFragment() {
        MainFragment fragment = MainFragment.newInstance(4);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.layoutContainer, fragment, "hidden");
        fragmentTransaction.commit();
    }
}
