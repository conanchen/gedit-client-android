package com.github.conanchen.gedit.di;


import com.github.conanchen.gedit.ui.MainActivity;
import com.github.conanchen.gedit.ui.SplashActivity;
import com.github.conanchen.gedit.ui.auth.LoginActivity;
import com.github.conanchen.gedit.ui.auth.RegisterActivity;
import com.github.conanchen.gedit.ui.hello.HelloActivity;
import com.github.conanchen.gedit.ui.my.MyFragment;
import com.github.conanchen.gedit.ui.store.StoreCreateActivity;
import com.github.conanchen.gedit.ui.store.StoreListFragment;
import com.github.conanchen.gedit.ui.store.StoreUpdateActivity;
import com.github.conanchen.gedit.ui.store.StoreUpdateHeadPortraitActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module()
public abstract class ViewModule {

    @ContributesAndroidInjector
    abstract MyFragment contributeMyFragment();

    @ContributesAndroidInjector
    abstract StoreListFragment contributeStoreListFragment();


    @ContributesAndroidInjector(modules = {
//            FragmentLoginBuildersModule.class,
//            FragmentWordsBuildersModule.class,
//            FragmentMyBuildersModule.class
    })
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector
    abstract HelloActivity contributeHelloActivity();

    //
    @ContributesAndroidInjector
    abstract StoreCreateActivity contributeCreateStoreActivity();

    @ContributesAndroidInjector
    abstract StoreUpdateActivity contributeStoreUpdateActivity();

    @ContributesAndroidInjector
    abstract StoreUpdateHeadPortraitActivity contributeStoreUpdateHeadPortraitActivity();

    @ContributesAndroidInjector
    abstract LoginActivity contributeLoginActivity();

    @ContributesAndroidInjector
    abstract SplashActivity contributeSplashActivity();

    @ContributesAndroidInjector
    abstract RegisterActivity contributeRegisterActivity();


    //
//    @ContributesAndroidInjector
//    abstract AppServiceKeepliveTraceImpl contributeAppServiceKeepliveTraceImpl();
//
//    @ContributesAndroidInjector
//    abstract AppServiceCommandSenderImpl contributeAppServiceCommandSenderImpl();
}