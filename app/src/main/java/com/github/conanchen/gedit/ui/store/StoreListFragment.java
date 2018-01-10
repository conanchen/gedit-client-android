package com.github.conanchen.gedit.ui.store;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.github.conanchen.gedit.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Conan Chen on 2018/1/9.
 */

public class StoreListFragment extends Fragment {
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
}
