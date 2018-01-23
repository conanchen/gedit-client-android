package com.github.conanchen.gedit.ui.my.myworkinstore;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.common.grpc.Location;
import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.room.store.Store;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我工作的店铺列表
 */
@Route(path = "/app/MyWorkStoresActivity")
public class MyWorkStoresActivity extends BaseActivity {
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    MyWorkStoresViewModel myWorkStoresViewModel;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private MyWorkStoresAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_workin_stores);
        ButterKnife.bind(this);

        setupRecyclerView();
        setupViewModel();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyWorkStoresAdapter();
        recyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new MyWorkStoresAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Store store) {
                startActivity(new Intent(MyWorkStoresActivity.this, MyWorkStoreActivity.class));
            }
        });
    }

    private void setupViewModel() {
        myWorkStoresViewModel = ViewModelProviders.of(this, viewModelFactory).get(MyWorkStoresViewModel.class);
        myWorkStoresViewModel.getLiveStores().observe(this, stores -> {
            if (stores != null) {
                mAdapter.setList(stores);
            }
        });
        myWorkStoresViewModel.updateLocation(Location.newBuilder().setLat(1).setLon(2).build());
    }

    @OnClick({R.id.back, R.id.right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.right:
                //增加
                break;
        }
    }
}
