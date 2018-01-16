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

import com.alibaba.android.arouter.launcher.ARouter;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseFragment;
import com.github.conanchen.gedit.di.common.Injectable;
import com.github.conanchen.gedit.room.store.Store;
import com.github.conanchen.gedit.vo.Location;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Conan Chen on 2018/1/9.
 */

public class StoreListFragment extends BaseFragment implements Injectable, StoreListAdapter.OnItemClickListener {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private StoreListViewModel storeListViewModel;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.hellobutton)
    AppCompatButton hellobutton;
    @BindView(R.id.createbutton)
    AppCompatButton createbutton;

    private StoreListAdapter mAdapter;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_list, null);
        ButterKnife.bind(this, view);

        setupRecyclerView();
        setupViewModel();
        return view;
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
        ARouter.getInstance().build("/app/RegisterActivity").navigation();
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

}
