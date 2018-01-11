package com.github.conanchen.gedit.ui.store;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseFragment;
import com.github.conanchen.gedit.di.common.Injectable;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Conan Chen on 2018/1/9.
 */

public class StoreListFragment extends BaseFragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @BindView(R.id.title)
    AppCompatTextView title;
    @BindView(R.id.hellobutton)
    AppCompatButton hellobutton;
    @BindView(R.id.createbutton)
    AppCompatButton createbutton;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.login)
    AppCompatButton login;
    @BindView(R.id.registe)
    AppCompatButton registe;

    private StoreListViewModel storeListViewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        storeListViewModel = ViewModelProviders.of(this, viewModelFactory).get(StoreListViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_list, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.hellobutton)
    public void openHelloButtonClicked() {
        // 1. 应用内简单的跳转(通过URL跳转在'进阶用法'中)
        ARouter.getInstance().build("/app/HelloActivity").navigation();
//        startActivity(new Intent(this.getContext(), StoreCreateActivity.class));
    }

    @OnClick(R.id.createbutton)
    public void openCreateStoreButtonClicked() {
        // 1. 应用内简单的跳转(通过URL跳转在'进阶用法'中)
        ARouter.getInstance().build("/app/StoreCreateActivity").navigation();
//        startActivity(new Intent(this.getContext(), StoreCreateActivity.class));
    }


    @OnClick({R.id.login, R.id.registe})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login:
                //登录界面
                ARouter.getInstance().build("/app/LoginActivity").navigation();
                break;
            case R.id.registe:
                //注册界面
                ARouter.getInstance().build("/app/RegisterActivity").navigation();
                break;
        }
    }
}
