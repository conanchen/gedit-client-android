package com.github.conanchen.gedit.ui.store;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.hello.grpc.StoreUpdateInfo;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改头像的界面
 */
public class StoreUpdateHeadPortraitActivity extends BaseActivity {

    @BindView(R.id.updatebutton)
    AppCompatButton updatebutton;

    private String TAG = StoreUpdateActivity.class.getSimpleName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    StoreUpdateViewModel storeUpdateViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_update_head_portrait);
        ButterKnife.bind(this);
        setupViewModel();
    }

    private void setupViewModel() {
        storeUpdateViewModel = ViewModelProviders.of(this, viewModelFactory).get(StoreUpdateViewModel.class);
        storeUpdateViewModel.getStoreUpdateResponseLiveData()
                .observe(this, storeUpdateResponse -> {

                });

    }


    @OnClick({R.id.updatebutton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.updatebutton:
                StoreUpdateInfo storeUpdateInfo = StoreUpdateInfo.builder()
                        .setLogo("logo")
                        .build();
                storeUpdateViewModel.updateStoreWith(storeUpdateInfo);
                break;
        }
    }
}
