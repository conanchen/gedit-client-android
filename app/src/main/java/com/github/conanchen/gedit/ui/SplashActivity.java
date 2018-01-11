package com.github.conanchen.gedit.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;

import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.ui.auth.LoginActivity;
import com.github.conanchen.gedit.ui.auth.SigninViewModel;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity {
    public static final String TAG = SplashActivity.class.getSimpleName();
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    SigninViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewModel();
    }

    private void setupViewModel() {
        loginViewModel = ViewModelProviders.of(this, viewModelFactory).get(SigninViewModel.class);
        loginViewModel.getCurrentSigninResponse().observe(this, signinResponse -> {
            if (io.grpc.Status.Code.OK.name().compareToIgnoreCase(signinResponse.getStatus().getCode()) == 0) {
                // Start home activity
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            // close splash activity
            SplashActivity.this.finish();
        });
    }


}