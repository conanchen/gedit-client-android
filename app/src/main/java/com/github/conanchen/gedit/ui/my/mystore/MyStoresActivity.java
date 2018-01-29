package com.github.conanchen.gedit.ui.my.mystore;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.room.my.store.MyStore;
import com.github.conanchen.gedit.ui.auth.CurrentSigninViewModel;
import com.github.conanchen.utils.vo.StoreCreateInfo;
import com.github.conanchen.utils.vo.VoAccessToken;
import com.google.common.base.Strings;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

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
    private CurrentSigninViewModel currentSigninViewModel;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout mSmartRefresh;
    private MyStoresAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stores);
        ButterKnife.bind(this);

        setupRecyclerView();
        setupViewModel();
        setupRefresh();

    }

    private void setupRefresh() {
        mSmartRefresh.setRefreshHeader(new ClassicsHeader(this));
        mSmartRefresh.setHeaderHeight(60);

        mSmartRefresh.setOnRefreshListener((refreshLayout) -> {
            currentSigninViewModel.getCurrentSigninResponse().observe(this, signinResponse -> {
                if (Status.Code.OK.getNumber() == signinResponse.getStatus().getCode().getNumber()) {
                    StoreCreateInfo storeCreateInfo = StoreCreateInfo.builder()
                            .setVoAccessToken(VoAccessToken.builder()
                                    .setAccessToken(signinResponse.getAccessToken())
                                    .setExpiresIn(signinResponse.getExpiresIn())
                                    .build())
                            .build();
                    myStoresViewModel.loadMyStores(storeCreateInfo);
                }
                mSmartRefresh.finishRefresh();
            });
        });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyStoresAdapter();
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MyStoresAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(MyStore myStore) {
                ARouter.getInstance().build("/app/MyStoreActivity")
                        .withString("uuid", Strings.isNullOrEmpty(myStore.storeUuid) ? System.currentTimeMillis() + "" : myStore.storeUuid)
                        .navigation();
            }
        });
    }

    private void setupViewModel() {
        myStoresViewModel = ViewModelProviders.of(this, viewModelFactory).get(MyStoresViewModel.class);
        myStoresViewModel.getMyStorePagedListLiveData().observe(this, stores -> {
            if (stores != null) {
                mAdapter.setList(stores);
            }
        });

        currentSigninViewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrentSigninViewModel.class);
        currentSigninViewModel.getCurrentSigninResponse().observe(this, signinResponse -> {
            if (Status.Code.OK.getNumber() == signinResponse.getStatus().getCode().getNumber()) {
                StoreCreateInfo storeCreateInfo = StoreCreateInfo.builder()
                        .setVoAccessToken(VoAccessToken.builder()
                                .setAccessToken(signinResponse.getAccessToken())
                                .setExpiresIn(signinResponse.getExpiresIn())
                                .build())
                        .build();
                myStoresViewModel.loadMyStores(storeCreateInfo);
            }
        });
    }

    @OnClick({R.id.back, R.id.right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.right:
                ARouter.getInstance().build("/app/StoreCreateActivity").navigation();
                break;
        }
    }

}
