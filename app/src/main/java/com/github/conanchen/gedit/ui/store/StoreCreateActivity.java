package com.github.conanchen.gedit.ui.store;

import android.Manifest;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.github.conanchen.amap.livelocation.AmapLiveLocation;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.ui.auth.CurrentSigninViewModel;
import com.github.conanchen.gedit.util.JudgeISMobileNo;
import com.github.conanchen.utils.vo.StoreCreateInfo;
import com.github.conanchen.utils.vo.VoAccessToken;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
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
    @BindView(R.id.districtUuid)
    AppCompatEditText mEtDistrictPhone;
    @BindView(R.id.detailAddress)
    AppCompatEditText mEtDetailAddress;

    private double lon = 0;
    private double lat = 0;

    public static String TAG = StoreCreateActivity.class.getSimpleName();
    private static final Gson gson = new Gson();
    private VoAccessToken voAccessToken;


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_store);
        ButterKnife.bind(this);

        setupLocation();
        setupViewModel();
        setupInputChecker();

    }

    private void setupLocation() {
        AmapLiveLocation amapLiveLocation = AmapLiveLocation.builder().setContext(this).build();
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.READ_PHONE_STATE
                        , Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        amapLiveLocation.locate().observe(StoreCreateActivity.this, aMapLocation -> {
                            if (aMapLocation.getLatitude() > 0 && aMapLocation.getLongitude() > 0) {
                                lat = aMapLocation.getLatitude();
                                lon = aMapLocation.getLongitude();
                                String city = aMapLocation.getCity();
                                String address = aMapLocation.getAddress();
                                mEtDetailAddress.setText(city + address);
                            }
                        });
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                    }
                }).onSameThread()
                .check();
    }

    private void setupInputChecker() {
        Observable<CharSequence> observableName = RxTextView.textChanges(mNameEditText);
        Observable<CharSequence> observablePhone = RxTextView.textChanges(mEtDistrictPhone);
        Observable<CharSequence> observableDetailAddress = RxTextView.textChanges(mEtDetailAddress);
        Observable.combineLatest(observableName, observablePhone, observableDetailAddress,
                (name, phone, address) -> isNameValid(name.toString()) && isPhone(phone.toString()) && isAddressValid(address.toString()))
                .subscribe(aBoolean -> RxView.enabled(mCreateButton).accept(aBoolean));

        RxView.clicks(mCreateButton)
                .throttleFirst(3, TimeUnit.SECONDS) //防止3秒内连续点击,或者只使用doOnNext部分
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    Toast.makeText(StoreCreateActivity.this, "创建中....", Toast.LENGTH_SHORT).show();

                    if (isLogin) {
                        //创建商铺
                        String name = mNameEditText.getText().toString().trim();
                        String districtMobil = mEtDistrictPhone.getText().toString().trim();
                        String address = mEtDetailAddress.getText().toString().trim();

                        StoreCreateInfo storeCreateInfo = StoreCreateInfo.builder()
                                .setVoAccessToken(voAccessToken)
                                .setName(name)
                                .setMobile(districtMobil)
                                .setDetailAddress(address)
                                .setLon(lon)
                                .setLat(lat)
                                .build();
                        storeCreateViewModel.createStoreWith(storeCreateInfo);
                    } else {
                        ARouter.getInstance().build("/app/LoginActivity").navigation();
                    }

                });

    }

    private boolean isNameValid(String name) {
        return !Strings.isNullOrEmpty(name);
    }

    private boolean isAddressValid(String password) {
        return password.length() >= 2;
    }

    private boolean isPhone(String phone) {
        return JudgeISMobileNo.isMobileNO(phone);
    }

    boolean isLogin = false;

    private void setupViewModel() {
        storeCreateViewModel = ViewModelProviders.of(this, viewModelFactory).get(StoreCreateViewModel.class);
        currentSigninViewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrentSigninViewModel.class);

        storeCreateViewModel.getStoreCreateResponseLiveData().observe(this, storeCreateResponse -> {
            if (Status.Code.OK == storeCreateResponse.getStatus().getCode()) {
                ARouter.getInstance().build("/app/MyStoreActivity").withString("uuid", storeCreateResponse.getUuid()).navigation();
                finish();
            }
        });

        currentSigninViewModel.getCurrentSigninResponse().observe(this, signinResponse -> {
            if (Status.Code.OK.getNumber() == signinResponse.getStatus().getCode().getNumber()) {
                isLogin = true;
                voAccessToken = VoAccessToken.builder()
                        .setAccessToken(signinResponse.getAccessToken())
                        .setExpiresIn(signinResponse.getExpiresIn())
                        .build();
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
