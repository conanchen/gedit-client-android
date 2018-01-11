package com.github.conanchen.gedit.ui.auth;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.github.conanchen.gedit.GeditApplication;
import com.github.conanchen.gedit.R;
import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.hello.grpc.auth.SigninInfo;
import com.google.common.base.Strings;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    AppCompatEditText name;
    @BindView(R.id.pass)
    AppCompatEditText pass;
    @BindView(R.id.login)
    AppCompatButton login;
    @BindView(R.id.show)
    AppCompatTextView show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setupViewModel();
    }

    private void setupViewModel() {
        loginViewModel = ViewModelProviders.of(this, viewModelFactory).get(SigninViewModel.class);
        loginViewModel.getSigninResponseLiveData()
                .observe(this, signinResponse -> {
                    show.setText("登陆结果：" + signinResponse.accessToken + "===" + signinResponse.expiresIn);
                });
    }

    @OnClick(R.id.login)
    public void onViewClicked() {
        String userName = name.getText().toString().trim();
        String password = pass.getText().toString().trim();
        if (Strings.isNullOrEmpty(userName) || Strings.isNullOrEmpty(password)) {
            Toast.makeText(this, "输入账号密码", Toast.LENGTH_LONG).show();
            return;
        }
        SigninInfo loginInfo = SigninInfo.builder()
                .setMobile(userName)
                .setPassword(password)
                .build();

        loginViewModel.login(loginInfo);
    }
}
