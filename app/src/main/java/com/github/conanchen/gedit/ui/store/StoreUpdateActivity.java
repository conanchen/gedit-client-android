package com.github.conanchen.gedit.ui.store;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.hello.grpc.store.StoreUpdateInfo;
import com.github.conanchen.gedit.room.kv.VoAccessToken;
import com.github.conanchen.gedit.ui.auth.CurrentSigninViewModel;
import com.github.conanchen.gedit.ui.auth.LoginActivity;
import com.google.gson.Gson;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/app/StoreUpdateActivity")
public class StoreUpdateActivity extends BaseActivity {

    private String TAG = StoreUpdateActivity.class.getSimpleName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private StoreUpdateViewModel storeUpdateViewModel;
    private CurrentSigninViewModel currentSigninViewModel;

    @BindView(R.id.name_edit)
    AppCompatEditText nameEdit;
    @BindView(R.id.address_text)
    AppCompatTextView address_text;
    private static final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_update);
        ButterKnife.bind(this);
        setupViewModel();
    }

    private void setupViewModel() {
        storeUpdateViewModel = ViewModelProviders.of(this, viewModelFactory).get(StoreUpdateViewModel.class);
        currentSigninViewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrentSigninViewModel.class);
        storeUpdateViewModel.getStoreUpdateResponseLiveData()
                .observe(this, storeUpdateResponse -> {
                    String message = String.format("storeUpdateResponse=%s", gson.toJson(storeUpdateResponse));
                    Log.i(TAG, message);
                    if (storeUpdateResponse != null) {
                        address_text.setText(message);
                    }
                });
    }

    @OnClick(R.id.updatebutton)
    public void setUpdatebuttonClick() {
        currentSigninViewModel.getCurrentSigninResponse().observe(this, signinResponse -> {
            if (io.grpc.Status.Code.OK.name().compareToIgnoreCase(signinResponse.getStatus().getCode()) == 0) {
                String name = nameEdit.getText().toString().trim();
                //获取uuid   传入数据库查询 ?? 为什么要到数据库查询，
                String uuid = "1";
                StoreUpdateInfo storeUpdateInfo = StoreUpdateInfo.builder()
                        .setVoAccessToken(VoAccessToken.builder().setAccessToken(signinResponse.getAccessToken()).setExpiresIn(signinResponse.getExpiresIn()).build())
                        .setName(StoreUpdateInfo.Field.NAME)
                        .setValue(name)
                        .setUuid(uuid)
                        .build();
                storeUpdateViewModel.updateStoreWith(storeUpdateInfo);
            } else {
                startActivity(new Intent(StoreUpdateActivity.this, LoginActivity.class));
            }

        });
    }
}
