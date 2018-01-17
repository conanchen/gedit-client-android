package com.github.conanchen.gedit.ui.store;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.hello.grpc.store.StoreCreateInfo;
import com.github.conanchen.gedit.room.kv.VoAccessToken;
import com.github.conanchen.gedit.ui.auth.CurrentSigninViewModel;
import com.github.conanchen.gedit.ui.auth.LoginActivity;
import com.github.conanchen.gedit.ui.my.mystore.MyStoreActivity;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

@Route(path = "/app/StoreCreateActivity")
public class StoreCreateActivity extends BaseActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private StoreCreateViewModel storeCreateViewModel;
    private CurrentSigninViewModel currentSigninViewModel;


    @BindView(R.id.create)
    AppCompatButton mCreateButton;

    @BindView(R.id.name)
    AppCompatEditText mNameEditText;

    @BindView(R.id.introducerPhone)
    AppCompatEditText introducerPhone;


    public static String TAG = StoreCreateActivity.class.getSimpleName();
    private static final Gson gson = new Gson();
    private String accessToken;
    private String expiresIn;


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_store);
        ButterKnife.bind(this);

        setupViewModel();
        setupInputChecker();
    }

    private void setupInputChecker() {
        Observable<CharSequence> observableName = RxTextView.textChanges(mNameEditText);
        Observable<CharSequence> observableAddress = RxTextView.textChanges(mNameEditText);
        Observable.combineLatest(observableName, observableAddress,
                (name, address) -> isNameValid(name.toString()) && isAddressValid(address.toString()))
                .subscribe(aBoolean -> RxView.enabled(mCreateButton).accept(aBoolean));

        RxView.clicks(mCreateButton)
                .throttleFirst(3, TimeUnit.SECONDS) //防止3秒内连续点击,或者只使用doOnNext部分
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    Toast.makeText(StoreCreateActivity.this, "创建中....", Toast.LENGTH_SHORT).show();
//                    if (isLogin) {
                        //创建商铺
                        String storeName = mNameEditText.getText().toString().trim();
                        String storeTel = introducerPhone.getText().toString().trim();

                        StoreCreateInfo storeCreateInfo = StoreCreateInfo.builder()
                                .setName(storeName)
                                .setMobile(storeTel)
                                .setVoAccessToken(VoAccessToken.builder().setAccessToken("accToken").setExpiresIn("ExpiresIn").build())
                                .build();
                        storeCreateViewModel.createStoreWith(storeCreateInfo);
//                    } else {
//                        startActivity(new Intent(StoreCreateActivity.this, LoginActivity.class));
//                    }

                });

    }

    private boolean isNameValid(String name) {
        return !Strings.isNullOrEmpty(name);
    }

    private boolean isAddressValid(String password) {
        return password.length() >= 6;
    }

    boolean isLogin = false;

    private void setupViewModel() {
        storeCreateViewModel = ViewModelProviders.of(this, viewModelFactory).get(StoreCreateViewModel.class);
        currentSigninViewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrentSigninViewModel.class);
        storeCreateViewModel.getStoreCreateResponseLiveData().observe(this, storeCreateResponse -> {
                    String message = String.format("storeCreateResponse=%s", gson.toJson(storeCreateResponse));
                    Log.i(TAG, message);
                    if (storeCreateResponse != null) {
                        startActivity(new Intent(StoreCreateActivity.this, MyStoreActivity.class));
                    }
                });


        currentSigninViewModel.getCurrentSigninResponse().observe(this, signinResponse -> {
            if (io.grpc.Status.Code.OK.name().compareToIgnoreCase(signinResponse.getStatus().getCode()) == 0) {
                isLogin = true;
                accessToken = signinResponse.getAccessToken();
                expiresIn = signinResponse.getExpiresIn();
            } else {
                isLogin = false;
            }
        });

    }

    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        finish();
    }
}
