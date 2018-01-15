package com.github.conanchen.gedit.ui.store;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseFragment;
import com.github.conanchen.gedit.di.common.Injectable;
import com.github.conanchen.gedit.room.store.Store;
import com.github.conanchen.gedit.ui.my.RecordSingleListActivity;
import com.github.conanchen.gedit.util.CustomPopWindow;
import com.github.conanchen.gedit.vo.Location;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Conan Chen on 2018/1/9.
 */

public class StoreListFragment extends BaseFragment implements Injectable, StoreListAdapter.OnItemClickListener, CustomPopWindow.OnItemClickListener {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private StoreListViewModel storeListViewModel;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.hellobutton)
    AppCompatButton hellobutton;
    @BindView(R.id.createbutton)
    AppCompatButton createbutton;
    @BindView(R.id.right)
    AppCompatImageButton right;

    private StoreListAdapter mAdapter;
    private CustomPopWindow popWindow;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_list, null);
        ButterKnife.bind(this, view);


        setupPop();
        setupRecyclerView();
        setupViewModel();
        return view;
    }


    /**
     * 设置右上角弹框的内容
     */
    private void setupPop() {
        //设置右上角弹框内容
        List<String> menu = new ArrayList<>();
        menu.add("扫一扫");
        menu.add("收款");
        menu.add("录单");
        popWindow = new CustomPopWindow(getActivity(), menu);
    }

    private void setupViewModel() {
        storeListViewModel = ViewModelProviders.of(this, viewModelFactory).get(StoreListViewModel.class);
        storeListViewModel.getLiveStores().observe(this, stores -> {
            if (stores != null) {
                mAdapter.setList(stores);
            }
        });
        storeListViewModel.updateLocation(Location.builder().setLat(1).setLon(2).build());
    }


    /**
     * 设置recyclerView
     */
    private void setupRecyclerView() {
        mAdapter = new StoreListAdapter();
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);
    }

    @OnClick(R.id.hellobutton)
    public void openHelloButtonClicked() {
        ARouter.getInstance().build("/app/HelloActivity").navigation();
    }

    @OnClick(R.id.createbutton)
    public void openCreateStoreButtonClicked() {
        ARouter.getInstance().build("/app/StoreCreateActivity").navigation();
    }

    /**
     * 列表条目的点击事件
     *
     * @param store
     */
    @Override
    public void OnItemClick(Store store) {
        startActivity(new Intent(getContext(), StoreDetailsActivity.class));
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @OnClick({R.id.right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.right:
                //右上角的弹框
                popWindow.showLocation(R.id.right);
                popWindow.setOnItemClickListener(this);
                break;
        }
    }


    @Override
    public void onClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            //扫一扫
//            startActivity(new Intent(getActivity(), ));
        } else if (position == 1) {
            //收款
            startActivity(new Intent(getActivity(), PaymentActivity.class));
        } else {
            //录单
            startActivity(new Intent(getActivity(), RecordSingleListActivity.class));
        }
    }
}
