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
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.di.common.BaseFragment;
import com.github.conanchen.gedit.di.common.Injectable;
import com.github.conanchen.gedit.ui.auth.CurrentSigninViewModel;
import com.github.conanchen.gedit.ui.auth.LoginActivity;
import com.github.conanchen.gedit.ui.store.StoreCreateActivity;
import com.github.conanchen.utils.vo.VoAccessToken;

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
    private MySummaryViewModel mySummaryViewModel;
    private CurrentSigninViewModel currentSigninViewModel;


    @BindView(R.id.image)
    AppCompatImageView image;
    @BindView(R.id.qrcode)
    AppCompatImageView mIvQRCode;
    @BindView(R.id.name)
    AppCompatTextView name;
    @BindView(R.id.me_login)
    ConstraintLayout me;
    @BindView(R.id.myviewmodeltext)
    AppCompatTextView myviewmodeltext;

    boolean isLogin = false;//判断是否登录

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    private void setupViewModel() {
        mySummaryViewModel = ViewModelProviders.of(this, viewModelFactory).get(MySummaryViewModel.class);

        currentSigninViewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrentSigninViewModel.class);
        currentSigninViewModel.getCurrentSigninResponse().observe(this, signinResponse -> {
            if (Status.Code.OK == signinResponse.getStatus().getCode()) {
                isLogin = true;
                //登录成功之后才可以去数据库获取个人资料
                currentSigninViewModel.getMyProfile().observe(this, userProfileResponse -> {
                    if (Status.Code.OK == userProfileResponse.getStatus().getCode()) {
                        //如果登录就显示名字  没有显示登录或注册
                        name.setText("小花花");
                        mIvQRCode.setVisibility(View.VISIBLE);
                        myviewmodeltext.setText(String.format("MySummaryViewModel is injected ? mySummaryViewModel=%s", mySummaryViewModel));
                    } else {
                        //设置默认的名字
                        name.setText("默认的名字,userProfile没加载出来");
                        mIvQRCode.setVisibility(View.VISIBLE);
                    }
                });

                mySummaryViewModel.userProfile(VoAccessToken.builder()
                        .setAccessToken(signinResponse.getAccessToken())
                        .setExpiresIn(signinResponse.getExpiresIn())
                        .build());
            } else {
                isLogin = false;
                name.setText("登录/注册");
                mIvQRCode.setVisibility(View.GONE);
                myviewmodeltext.setText(String.format("MySummaryViewModel is injected ? mySummaryViewModel=%s", mySummaryViewModel));
            }
        });


        mySummaryViewModel.getMySummaryLiveData().observe(this, userProfileResponse -> {
            if (Status.Code.OK == userProfileResponse.getStatus().getCode()) {
                name.setText("小花花");
                mIvQRCode.setVisibility(View.VISIBLE);
            }
        });


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

    @OnClick({R.id.me_login, R.id.my_invest, R.id.my_store, R.id.my_fans, R.id.my_points,
            R.id.my_extension_stores, R.id.qrcode,
            R.id.my_works_stores, R.id.my_invest_payments, R.id.my_payerpayments,
            R.id.my_payeepayments, R.id.customer_service, R.id.setting, R.id.jifenbao})
    public void onViewClicked(View view) {
//        if (isLogin) {
        switch (view.getId()) {
            case R.id.me_login:
                //点击了小花花
                if (isLogin) {
                    Toast.makeText(getContext(), "个人详情", Toast.LENGTH_LONG).show();
                } else {
                    startActivity(new Intent(this.getActivity(), LoginActivity.class));
                }
                break;
            case R.id.qrcode:
                //跳转到自己的二维码界面
                ARouter.getInstance().build("/app/MyQRCodeActivity").navigation();
                break;
            case R.id.my_invest:
                //我的积分
                ARouter.getInstance().build("/app/MyPointsActivity").navigation();
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
            case R.id.my_invest_payments:
                //我的投资录单
                ARouter.getInstance().build("/app/MyInvestPaymentsActivity").navigation();
                break;
            case R.id.my_payerpayments:
                //我的付款记录
                ARouter.getInstance().build("/app/MyPayerPaymentActivity").navigation();
                break;
            case R.id.my_payeepayments:
                //我的收款记录
                ARouter.getInstance().build("/app/MyPayeePaymentActivity").navigation();
                break;
            case R.id.my_works_stores:
                //我工作的店铺
                ARouter.getInstance().build("/app/MyWorkStoresActivity").navigation();
                break;
            case R.id.customer_service:
                //客服
                ARouter.getInstance().build("/app/CustomerServiceActivity").navigation();
                break;
            case R.id.setting:
                //设置
                ARouter.getInstance().build("/app/SettingActivity").navigation();
                break;
            case R.id.jifenbao:
                ARouter.getInstance().build("/app/MyInvestTreasureActivity").navigation();
                break;
        }
//        } else {
//            startActivity(new Intent(this.getActivity(), LoginActivity.class));
//        }
    }

}
