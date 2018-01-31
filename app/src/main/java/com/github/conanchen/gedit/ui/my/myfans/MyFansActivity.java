package com.github.conanchen.gedit.ui.my.myfans;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.ui.auth.CurrentSigninViewModel;
import com.github.conanchen.gedit.user.auth.grpc.SigninResponse;
import com.github.conanchen.gedit.user.fans.grpc.FanshipResponse;
import com.github.conanchen.utils.vo.MyFansBean;
import com.github.conanchen.utils.vo.VoAccessToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的粉丝列表界面
 */
@Route(path = "/app/MyFansActivity")
public class MyFansActivity extends BaseActivity {
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    MyFansViewModel myFansViewModel;
    private CurrentSigninViewModel currentSigninViewModel;

    @BindView(R.id.refresh)
    SmartRefreshLayout mSmartRefresh;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private MyFansAdapter mAdapter;
    private VoAccessToken voAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fans_list);
        ButterKnife.bind(this);

        setupRecyclerView();
        setupViewModel();
        setupRefresh();
    }

    private void setupRefresh() {
        mSmartRefresh.setRefreshHeader(new ClassicsHeader(this));
        mSmartRefresh.setHeaderHeight(60);

        mSmartRefresh.setOnRefreshListener((view) -> {
            if (voAccessToken != null) {
                myFansViewModel.refresh(MyFansBean.builder().setVoAccessToken(voAccessToken).build());
            }
        });
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyFansAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    private void setupViewModel() {
        myFansViewModel = ViewModelProviders.of(this, viewModelFactory).get(MyFansViewModel.class);
        currentSigninViewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrentSigninViewModel.class);
        myFansViewModel.getMyFanshipPagedListLiveData().observe(this, stores -> {
            if (stores != null) {
                mAdapter.setList(stores);
                if (mSmartRefresh != null) {
                    mSmartRefresh.finishRefresh();
                }
            }
        });


        currentSigninViewModel.getCurrentSigninResponse().observe(this, new Observer<SigninResponse>() {
            @Override
            public void onChanged(@Nullable SigninResponse signinResponse) {
                if (Status.Code.OK == signinResponse.getStatus().getCode()) {
                    voAccessToken = VoAccessToken.builder()
                            .setAccessToken(signinResponse.getAccessToken())
                            .setExpiresIn(signinResponse.getExpiresIn())
                            .build();
                    MyFansBean myFansBean = MyFansBean.builder()
                            .setVoAccessToken(voAccessToken)
                            .build();
                    myFansViewModel.refresh(myFansBean);
                }
            }
        });

        myFansViewModel.addPagedListLiveData().observe(this, new Observer<FanshipResponse>() {
            @Override
            public void onChanged(@Nullable FanshipResponse fanshipResponse) {
                if (Status.Code.OK == fanshipResponse.getStatus().getCode()) {
                    Log.i("-=-=", "add fans  success");
                }
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
                if (voAccessToken != null) {
                    MyFansBean myFansBean = MyFansBean.builder()
                            .setVoAccessToken(voAccessToken)
                            .setFanUuid("111111111111")//这个是添加粉丝的uuid
                            .build();
                    myFansViewModel.add(myFansBean);
                }
                break;
        }
    }
}
