package com.github.conanchen.gedit.ui.my;

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
import com.github.conanchen.gedit.room.store.Store;
import com.github.conanchen.gedit.vo.Location;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我推广的店铺界面
 */
@Route(path = "/app/MyIntroducedStoresActivity")
public class MyIntroducedStoresActivity extends BaseActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    MyIntroducedStoresViewModel myIntroduceStoresViewModel;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private MyIntroducedStoresAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_introduce_stores);
        ButterKnife.bind(this);

        setupRecyclerView();
        setupViewModel();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyIntroducedStoresAdapter();
        recyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new MyIntroducedStoresAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(Store store) {
                startActivity(new Intent(MyIntroducedStoresActivity.this, MyIntroducedStoreActivity.class));
            }
        });
    }

    private void setupViewModel() {
        myIntroduceStoresViewModel = ViewModelProviders.of(this, viewModelFactory).get(MyIntroducedStoresViewModel.class);
        myIntroduceStoresViewModel.getLiveStores().observe(this, stores -> {
            if (stores != null) {
                mAdapter.setList(stores);
            }
        });
        myIntroduceStoresViewModel.updateLocation(Location.builder().setLat(1).setLon(2).build());
    }

    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }
}
