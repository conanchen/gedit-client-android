package com.github.conanchen.gedit.di;


import com.github.conanchen.gedit.ui.MainActivity;
import com.github.conanchen.gedit.ui.hello.HelloActivity;
import com.github.conanchen.gedit.ui.my.MyFragment;
import com.github.conanchen.gedit.ui.store.StoreCreateActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module()
public abstract class ViewModule {
    @ContributesAndroidInjector
    abstract MyFragment contributeMyFragment();


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
//
//    @ContributesAndroidInjector
//    abstract WordExamActivity contributeWordExamActivity();
//
//    @ContributesAndroidInjector
//    abstract WordCrawlerActivity contributeWordCrawlerActivity();
//
//    @ContributesAndroidInjector
//    abstract LoginActivity contributeLoginActivity();
//
//    @ContributesAndroidInjector
//    abstract AppServiceKeepliveTraceImpl contributeAppServiceKeepliveTraceImpl();
//
//    @ContributesAndroidInjector
//    abstract AppServiceCommandSenderImpl contributeAppServiceCommandSenderImpl();
}