package com.github.conanchen.gedit.ui.auth;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.grpc.auth.SigninInfo;
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

/**
 * 登录界面
 */
@Route(path = "/app/LoginActivity")
public class LoginActivity extends BaseActivity {
    public static final String TAG = LoginActivity.class.getSimpleName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    SigninViewModel loginViewModel;

    @BindView(R.id.name)
    AppCompatEditText mPhoneEditText;
    @BindView(R.id.pass)
    AppCompatEditText mPasswordEditText;
    @BindView(R.id.login)
    AppCompatButton mLoginButton;
    @BindView(R.id.pass_icon)
    AppCompatImageView mIvPassIcon;

    private static final Gson gson = new Gson();
    private boolean isShowPwd = false;//是否显示密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setupViewModel();
        setupInputChecker();

        setupOnClick();
    }

    private void setupOnClick() {
        mIvPassIcon.setOnClickListener((view) -> {
            if (isShowPwd) {
                //当前密码为显示状态  现在需要隐藏掉
                mIvPassIcon.setImageDrawable(getResources().getDrawable(R.mipmap.eyes_1));
                //设置输入文本为密码
                mPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                //设置密码为用户不可见
                mPasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                //设置光标位置到文本最后
                mPasswordEditText.setSelection(mPasswordEditText.length());
                isShowPwd = false;
            } else {
                //当前密码为隐藏状态  现在需要显示
                mIvPassIcon.setImageDrawable(getResources().getDrawable(R.mipmap.eyes));
                //设置输入文本为密码
                mPasswordEditText.setTransformationMethod(null);
                //设置密码为用户可见
                mPasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                //设置光标位置到文本最后
                mPasswordEditText.setSelection(mPasswordEditText.length());
                isShowPwd = true;
            }
        });
    }

    private void setupInputChecker() {
        Observable<CharSequence> observablePhone = RxTextView.textChanges(mPhoneEditText);
        Observable<CharSequence> observablePassword = RxTextView.textChanges(mPasswordEditText);

        Observable.combineLatest(observablePhone, observablePassword,
                (phone, password) -> isPhoneValid(phone.toString()) && isPasswordValid(password.toString()))
                .subscribe(aBoolean -> RxView.enabled(mLoginButton).accept(aBoolean));

        RxView.clicks(mLoginButton)
                .throttleFirst(3, TimeUnit.SECONDS) //防止3秒内连续点击,或者只使用doOnNext部分
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    Toast.makeText(LoginActivity.this, "登录中....", Toast.LENGTH_SHORT).show();
                    String userName = mPhoneEditText.getText().toString().trim();
                    String password = mPasswordEditText.getText().toString().trim();
                    SigninInfo loginInfo = SigninInfo.builder()
                            .setMobile(userName)
                            .setPassword(password)
                            .build();

                    loginViewModel.login(loginInfo);
                });
    }

    private boolean isPhoneValid(String phone) {
        return phone.length() == 11;
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    private void setupViewModel() {
        loginViewModel = ViewModelProviders.of(this, viewModelFactory).get(SigninViewModel.class);
        loginViewModel.getSigninResponseLiveData()
                .observe(this, signinResponse -> {
                    if (Status.Code.OK.getNumber() == signinResponse.getStatus().getCode().getNumber()) {
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                    }

                });
    }


    @OnClick({R.id.back, R.id.register, R.id.forget})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.register:
                //快速注册
                ARouter.getInstance().build("/app/RegisterActivity").withString("TYPE", "Register").navigation();
                break;
            case R.id.forget:
                //忘记密码
                ARouter.getInstance().build("/app/RegisterActivity").withString("TYPE", "ForgetPass").navigation();
                break;
        }
    }
}
