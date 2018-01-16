package com.github.conanchen.gedit.ui.my;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseFragment;
import com.github.conanchen.gedit.di.common.Injectable;
import com.github.conanchen.gedit.ui.auth.CurrentSigninViewModel;
import com.github.conanchen.gedit.ui.store.StoreCreateActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Conan Chen on 2018/1/9.
 */

public class MySummaryFragment extends BaseFragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @BindView(R.id.image)
    AppCompatImageView image;
    @BindView(R.id.name)
    AppCompatTextView name;
    @BindView(R.id.me)
    ConstraintLayout me;
    private MySummaryViewModel mySummaryViewModel;
    CurrentSigninViewModel currentSigninViewModel;


    @BindView(R.id.myviewmodeltext)
    AppCompatTextView myviewmodeltext;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    private void setupViewModel() {
        mySummaryViewModel = ViewModelProviders.of(this, viewModelFactory).get(MySummaryViewModel.class);
        currentSigninViewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrentSigninViewModel.class);
        myviewmodeltext.setText(String.format("MySummaryViewModel is injected ? mySummaryViewModel=%s", mySummaryViewModel));
        mySummaryViewModel.setHelloName("set name");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, null);
        ButterKnife.bind(this, view);

        return view;
    }


    @OnClick(R.id.myviewmodeltext)
    public void onViewClicked() {
        startActivity(new Intent(getContext(), StoreCreateActivity.class));
    }


    @OnClick({R.id.me, R.id.my_invest, R.id.my_store, R.id.my_fans, R.id.my_points, R.id.my_extension_stores, R.id.customer_service, R.id.setting})
    public void onViewClicked(View view) {
//        currentSigninViewModel.getCurrentSigninResponse().observe(this,signinResponse -> {
//                    if (io.grpc.Status.Code.OK.name().compareToIgnoreCase(signinResponse.getStatus().getCode()) == 0) {
//
//                    }else{
//                       startActivity(new Intent(this.getActivity(), LoginActivity.class));
//                    }
//        });
        switch (view.getId()) {
            case R.id.me:
                //点击了小花花
                break;
            case R.id.my_invest:
                //投资
                break;
            case R.id.my_store:
                //我的店铺
                ARouter.getInstance().build("/app/MyStoresActivity").navigation();
                break;
            case R.id.my_fans:
                //我的粉丝
                ARouter.getInstance().build("/app/MyFansActivity").navigation();
                break;
            case R.id.my_points:
                //积分
                ARouter.getInstance().build("/app/MyPointsActivity").navigation();
                break;
            case R.id.my_extension_stores:
                //我推广的店铺
                ARouter.getInstance().build("/app/MyIntroducedStoresActivity").navigation();
                break;
            case R.id.customer_service:
                //客服
                ARouter.getInstance().build("/app/CustomerServiceActivity").navigation();
                break;
            case R.id.setting:
                //设置
                ARouter.getInstance().build("/app/SettingActivity").navigation();
                break;
        }
    }

}
