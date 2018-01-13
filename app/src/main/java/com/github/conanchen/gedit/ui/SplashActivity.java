package com.github.conanchen.gedit.ui;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;

import com.github.conanchen.gedit.di.common.BaseActivity;
import com.github.conanchen.gedit.ui.auth.CurrentSigninViewModel;
import com.github.conanchen.gedit.ui.auth.LoginActivity;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity {
    public static final String TAG = SplashActivity.class.getSimpleName();
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    CurrentSigninViewModel currentSigninViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewModel();
    }

    private void setupViewModel() {
        currentSigninViewModel = ViewModelProviders.of(this, viewModelFactory).get(CurrentSigninViewModel.class);
        currentSigninViewModel.getCurrentSigninResponse().observe(this, signinResponse -> {
            if (io.grpc.Status.Code.OK.name().compareToIgnoreCase(signinResponse.getStatus().getCode()) == 0) {
                // Start home activity
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
            // close splash activity
            SplashActivity.this.finish();
        });
    }


}