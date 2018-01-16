package com.github.conanchen.gedit.ui.auth;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.hello.grpc.auth.SigninInfo;
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
    @BindView(R.id.show)
    AppCompatTextView mResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setupViewModel();
        setupInputChecker();
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
                    mResultTextView.setText("登陆结果：" + signinResponse.accessToken + "===" + signinResponse.expiresIn);
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
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.forget:
                //忘记密码
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                break;
        }
    }
}
