package com.github.conanchen.gedit.ui.my;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseFragment;
import com.github.conanchen.gedit.di.common.Injectable;
import com.github.conanchen.gedit.ui.store.StoreCreateActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Conan Chen on 2018/1/9.
 */

public class MyFragment extends BaseFragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @BindView(R.id.mine)
    LinearLayout mine;
    @BindView(R.id.my_invest)
    LinearLayout myInvest;
    @BindView(R.id.my_store)
    LinearLayout myStore;
    @BindView(R.id.my_fans)
    LinearLayout myFans;
    @BindView(R.id.my_integral)
    LinearLayout myIntegral;
    @BindView(R.id.my_extension_store)
    LinearLayout myExtensionStore;
    @BindView(R.id.customer_service)
    LinearLayout customerService;
    @BindView(R.id.setting)
    LinearLayout setting;

    private MyViewModel viewModel;

    @BindView(R.id.myviewmodeltext)
    AppCompatTextView myviewmodeltext;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViewModel();
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MyViewModel.class);
        myviewmodeltext.setText(String.format("MyViewModel is injected ? viewModel=%s", viewModel));
        viewModel.setHelloName("set name");
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

    @OnClick({R.id.mine, R.id.my_invest, R.id.my_store, R.id.my_fans, R.id.my_integral, R.id.my_extension_store, R.id.customer_service, R.id.setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine:
                //点击了小花花

                break;
            case R.id.my_invest:
                //投资
                break;
            case R.id.my_store:
                //我的店铺
                ARouter.getInstance().build("/app/MyStoreActivity").navigation();
                break;
            case R.id.my_fans:
                //我的粉丝
                ARouter.getInstance().build("/app/MyFansListActivity").navigation();
                break;
            case R.id.my_integral:
                //积分
                ARouter.getInstance().build("/app/PointsActivity").navigation();
                break;
            case R.id.my_extension_store:
                //我推广的店铺
                ARouter.getInstance().build("/app/MyExtensionStoreActivity").navigation();
                break;
            case R.id.customer_service:
                //客服
                break;
            case R.id.setting:
                //设置
                break;
        }
    }
}
