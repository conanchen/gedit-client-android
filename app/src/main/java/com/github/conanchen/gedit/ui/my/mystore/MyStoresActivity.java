package com.github.conanchen.gedit.ui.my.mystore;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.room.store.MyStores;
import com.github.conanchen.gedit.ui.store.StoreCreateActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的店铺
 */
@Route(path = "/app/MyStoresActivity")
public class MyStoresActivity extends BaseActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    MyStoresViewModel myStoresViewModel;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private MyStoresAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_store);
        ButterKnife.bind(this);

        setupRecyclerView();
        setupViewModel();

    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyStoresAdapter();
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MyStoresAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(MyStores myStores) {
                startActivity(new Intent(MyStoresActivity.this, MyStoreActivity.class));
            }
        });
    }

    private void setupViewModel() {
        myStoresViewModel = ViewModelProviders.of(this, viewModelFactory).get(MyStoresViewModel.class);
        myStoresViewModel.getLiveStores().observe(this, stores -> {
            if (stores != null) {
                mAdapter.setList(stores);
            }
        });
        myStoresViewModel.loadMyStores(System.currentTimeMillis());
    }

    @OnClick({R.id.back, R.id.right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.right:
                startActivity(new Intent(MyStoresActivity.this, StoreCreateActivity.class));
                break;
        }
    }

}
