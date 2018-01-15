package com.github.conanchen.gedit.ui.store;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.hello.grpc.store.StoreCreateInfo;
import com.github.conanchen.gedit.room.kv.VoAccessToken;
import com.github.conanchen.gedit.ui.auth.CurrentSigninViewModel;
import com.github.conanchen.gedit.ui.auth.LoginActivity;
import com.google.common.base.Strings;
import com.google.gson.Gson;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = "/app/StoreCreateActivity")
public class StoreCreateActivity extends BaseActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private StoreCreateViewModel storeCreateViewModel;
    private CurrentSigninViewModel currentSigninViewModel;

    @BindView(R.id.title)
    AppCompatTextView title;
    @BindView(R.id.left_back)
    AppCompatImageView leftBack;
    @BindView(R.id.create)
    AppCompatButton create;
    @BindView(R.id.name)
    AppCompatEditText name;
    @BindView(R.id.tel)
    AppCompatEditText tel;
    @BindView(R.id.address)
    AppCompatEditText address;


    public static String TAG = StoreCreateActivity.class.getSimpleName();
    private static final Gson gson = new Gson();


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_store);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        title.setText("创建商铺");

        setupViewModel();
        setupEditText();
    }

    private void setupEditText() {


    }

    private void setupViewModel() {
        storeCreateViewModel = ViewModelProviders.of(this, viewModelFactory).get(StoreCreateViewModel.class);
        currentSigninViewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrentSigninViewModel.class);
        storeCreateViewModel.getStoreCreateResponseLiveData()
                .observe(this, storeCreateResponse -> {
                    String message = String.format("storeCreateResponse=%s", gson.toJson(storeCreateResponse));
                    Log.i(TAG, message);
                    if (storeCreateResponse != null) {
                        startActivity(new Intent(StoreCreateActivity.this, MyStoreDetailsActivity.class));
                    }
                });
    }

    @OnClick({R.id.left_back, R.id.create})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.left_back:
                finish();
                break;
            case R.id.create:

                currentSigninViewModel.getCurrentSigninResponse().observe(this, signinResponse -> {
                    if (io.grpc.Status.Code.OK.name().compareToIgnoreCase(signinResponse.getStatus().getCode()) == 0) {
                        //创建商铺
                        String storeName = name.getText().toString().trim();
                        String storeTel = tel.getText().toString().trim();
                        String storeAddress = address.getText().toString().trim();
                        if (Strings.isNullOrEmpty(storeName) || Strings.isNullOrEmpty(storeTel) || Strings.isNullOrEmpty(storeAddress)) {
                            return;
                        }

                        StoreCreateInfo storeCreateInfo = StoreCreateInfo.builder()
                                .setAddress(storeAddress)
                                .setName(storeName)
                                .setMobile(storeTel)
                                .setVoAccessToken(VoAccessToken.builder().setAccessToken(signinResponse.getAccessToken()).setExpiresIn(signinResponse.getExpiresIn()).build())
                                .build();
                        storeCreateViewModel.createStoreWith(storeCreateInfo);
                    } else {
                        startActivity(new Intent(StoreCreateActivity.this, LoginActivity.class));
                    }
                });

                break;
        }
    }
}
