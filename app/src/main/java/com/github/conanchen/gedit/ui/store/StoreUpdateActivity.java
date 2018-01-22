package com.github.conanchen.gedit.ui.store;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.InputType;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.ui.auth.CurrentSigninViewModel;
import com.github.conanchen.gedit.util.JudgeISMobileNo;
import com.github.conanchen.utils.vo.StoreUpdateInfo;
import com.github.conanchen.utils.vo.VoAccessToken;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.grpc.Status;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

@Route(path = "/app/StoreUpdateActivity")
public class StoreUpdateActivity extends BaseActivity {

    private String TAG = StoreUpdateActivity.class.getSimpleName();
    private static final Gson gson = new Gson();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private StoreUpdateViewModel storeUpdateViewModel;
    private CurrentSigninViewModel currentSigninViewModel;

    @BindView(R.id.edit)
    AppCompatEditText mEdit;
    @BindView(R.id.edit_tel2)
    AppCompatEditText mEditTel2;
    @BindView(R.id.edit_tel3)
    AppCompatEditText mEditTel3;
    @BindView(R.id.desc)
    AppCompatTextView mTvDesc;
    @BindView(R.id.title)
    AppCompatTextView mTvTitle;
    @BindView(R.id.update)
    AppCompatButton mBtUpdate;

    @Autowired
    public String MODIFY_TYPE;//从我的店铺详情传递过来

    @Autowired
    public String uuid;//从我的店铺详情传递过来

    private boolean isLogin = false;//是否登录
    private String accessToken;
    private String expiresIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_update);
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);

        setupTitleOrDesc();
        setupViewModel();
        setupInputChecker();
    }

    private void setupTitleOrDesc() {
        //设置标题 及描述
        switch (MODIFY_TYPE) {
            case "PHONE":
                mTvTitle.setText("修改电话号码");
                mEdit.setMaxLines(11);
                mEdit.setHint("请输入电话号码");
                mEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
//                mEditTel2.setVisibility(View.VISIBLE);
//                mEditTel3.setVisibility(View.VISIBLE);
                break;
            case "DESC":
                mTvTitle.setText("修改描述");
                mEdit.setHint("商铺描述");
                break;
            case "DETAIL_ADDRESS":
                mTvTitle.setText("修改详细地址");
                mEdit.setHint("商铺详细地址");
                break;
            case "POINTS_RATE":
                mTvTitle.setText("修改优惠额度");
                mEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
                mEdit.setHint("消费可获得百分之几的积分");
                break;
            default:

                break;
        }
    }

    private void setupViewModel() {
        storeUpdateViewModel = ViewModelProviders.of(this, viewModelFactory).get(StoreUpdateViewModel.class);
        currentSigninViewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrentSigninViewModel.class);
        storeUpdateViewModel.getStoreUpdateResponseLiveData()
                .observe(this, storeUpdateResponse -> {
                    String message = String.format("storeUpdateResponse=%s", gson.toJson(storeUpdateResponse));
                    Log.i(TAG, message);
                    if (storeUpdateResponse != null) {
                        mTvDesc.setText(message);
                    }
                });

        currentSigninViewModel.getCurrentSigninResponse().observe(this, signinResponse -> {
            if (Status.Code.OK.name().compareToIgnoreCase(signinResponse.getStatus().getCode()) == 0) {
                isLogin = true;
                accessToken = signinResponse.getAccessToken();
                expiresIn = signinResponse.getExpiresIn();
            } else {
                isLogin = false;

            }
        });
    }

    private void setupInputChecker() {
        Observable<CharSequence> observableEdit = RxTextView.textChanges(mEdit);
        Observable.combineLatest(observableEdit, observableEdit,
                (name, other) -> isEditValid(name.toString()) && true)
                .subscribe(aBoolean -> RxView.enabled(mBtUpdate).accept(aBoolean));

        RxView.clicks(mBtUpdate)
                .throttleFirst(3, TimeUnit.SECONDS) //防止3秒内连续点击,或者只使用doOnNext部分
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (isLogin) {
                        Toast.makeText(StoreUpdateActivity.this, "修改中....", Toast.LENGTH_SHORT).show();
                        String value = mEdit.getText().toString().trim();

                        StoreUpdateInfo storeUpdateInfo = getStoreUpdateInfo(value);

                        Log.i("-=-=-=-", "uuid" + storeUpdateInfo.uuid);
                        storeUpdateViewModel.updateStoreWith(storeUpdateInfo);
                    } else {
                        ARouter.getInstance().build("/app/LoginActivity").navigation();
                    }
                });
    }

    /**
     * 联网时候的数据封装
     *
     * @param value
     * @return
     */
    @Nullable
    private StoreUpdateInfo getStoreUpdateInfo(String value) {

        VoAccessToken voAccessToken = VoAccessToken.builder()
                .setAccessToken(Strings.isNullOrEmpty(accessToken) ? System.currentTimeMillis() + "" : accessToken)
                .setExpiresIn(Strings.isNullOrEmpty(expiresIn) ? System.currentTimeMillis() + "" : expiresIn)
                .build();
        StoreUpdateInfo storeUpdateInfo = null;
        switch (MODIFY_TYPE) {
            case "PHONE":
                List<String> listTel = new ArrayList<>();
                listTel.add(value);
                storeUpdateInfo = StoreUpdateInfo.builder()
                        .setVoAccessToken(voAccessToken)
                        .setName(StoreUpdateInfo.Field.PHONE)
                        .setValue(listTel)
                        .setUuid(uuid)
                        .build();
                break;
            case "DESC":
                storeUpdateInfo = StoreUpdateInfo.builder()
                        .setVoAccessToken(voAccessToken)
                        .setName(StoreUpdateInfo.Field.DESC)
                        .setValue(value)
                        .setUuid(uuid)
                        .build();
                break;
            case "DETAIL_ADDRESS":
                storeUpdateInfo = StoreUpdateInfo.builder()
                        .setVoAccessToken(voAccessToken)
                        .setName(StoreUpdateInfo.Field.DETAIL_ADDRESS)
                        .setValue(value)
                        .setUuid(uuid)
                        .build();
                break;
            case "POINTS_RATE":
                storeUpdateInfo = StoreUpdateInfo.builder()
                        .setVoAccessToken(voAccessToken)
                        .setName(StoreUpdateInfo.Field.POINTS_RATE)
                        .setValue(value)
                        .setUuid(uuid)
                        .build();
                break;
        }
        return storeUpdateInfo;
    }

    /**
     * 对输入框的监听
     *
     * @param s 输入框输入的文字
     * @return
     */
    private boolean isEditValid(String s) {
        switch (MODIFY_TYPE) {
            case "PHONE":

                if (JudgeISMobileNo.isMobileNO(s)) {
                    return true;
                }
                break;
            case "DESC":
                if (!Strings.isNullOrEmpty(s)) {
                    return true;
                }
                break;
            case "DETAIL_ADDRESS":
                if (!Strings.isNullOrEmpty(s)) {
                    return true;
                }
                break;
            case "POINTS_RATE":
                if (!Strings.isNullOrEmpty(s)) {
                    return true;
                }
                break;
        }
        return false;
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
