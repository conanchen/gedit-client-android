package com.github.conanchen.gedit.di.common;

import android.arch.lifecycle.LifecycleRegistry;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {


    private final LifecycleRegistry registry = new LifecycleRegistry(this);

    @Override
    public LifecycleRegistry getLifecycle() {
        return registry;
    }
}