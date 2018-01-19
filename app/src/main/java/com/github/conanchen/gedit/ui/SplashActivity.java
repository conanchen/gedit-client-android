package com.github.conanchen.gedit.ui;

import android.content.Intent;
import android.os.Bundle;

import com.github.conanchen.gedit.di.common.BaseActivity;

public class SplashActivity extends BaseActivity {
    public static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(SplashActivity.this, MainActivity.class));

        // close splash activity
        SplashActivity.this.finish();
    }
}