package com.github.conanchen.gedit.di;


import com.github.conanchen.gedit.ui.MainActivity;
import com.github.conanchen.gedit.ui.SplashActivity;
import com.github.conanchen.gedit.ui.auth.ForgetPasswordActivity;
import com.github.conanchen.gedit.ui.auth.LoginActivity;
import com.github.conanchen.gedit.ui.auth.RegisterActivity;
import com.github.conanchen.gedit.ui.hello.HelloActivity;
import com.github.conanchen.gedit.ui.my.myintroducedstore.MyIntroducedStoresActivity;
import com.github.conanchen.gedit.ui.my.MyStoreEmployeesActivity;
import com.github.conanchen.gedit.ui.my.MySummaryFragment;
import com.github.conanchen.gedit.ui.my.MyWorkStoresActivity;
import com.github.conanchen.gedit.ui.my.mystore.MyStoresActivity;
import com.github.conanchen.gedit.ui.payment.GaptureActivity;
import com.github.conanchen.gedit.ui.store.StoreCreateActivity;
import com.github.conanchen.gedit.ui.store.StoreDetailActivity;
import com.github.conanchen.gedit.ui.store.StoreListFragment;
import com.github.conanchen.gedit.ui.store.StoreUpdateActivity;
import com.github.conanchen.gedit.ui.store.StoreUpdateHeadPortraitActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module()
public abstract class ViewModule {

    @ContributesAndroidInjector
    abstract MySummaryFragment contributeMyFragment();

    @ContributesAndroidInjector
    abstract StoreListFragment contributeStoreListFragment();

    @ContributesAndroidInjector
    abstract StoreDetailActivity contributeStoreDetailActivity();


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

    @ContributesAndroidInjector
    abstract GaptureActivity contributeGaptureActivity();

    @ContributesAndroidInjector
    abstract MyStoresActivity contributeMyStoresActivity();

    @ContributesAndroidInjector
    abstract MyWorkStoresActivity contributeMyWorkStoresActivity();

    @ContributesAndroidInjector
    abstract MyIntroducedStoresActivity contributeMyIntroducedStoresActivity();

    @ContributesAndroidInjector
    abstract MyStoreEmployeesActivity contributeMyStoreEmployeesActivity();

    @ContributesAndroidInjector
    abstract ForgetPasswordActivity contributeForgetPasswordActivity();


    //
//    @ContributesAndroidInjector
//    abstract AppServiceKeepliveTraceImpl contributeAppServiceKeepliveTraceImpl();
//
//    @ContributesAndroidInjector
//    abstract AppServiceCommandSenderImpl contributeAppServiceCommandSenderImpl();
}