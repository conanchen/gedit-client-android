package com.github.conanchen.gedit.di;



import com.github.conanchen.gedit.ui.hello.HelloActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module()
public abstract class MainActivityModule {

//    @ContributesAndroidInjector(modules = {
//            FragmentLoginBuildersModule.class,
//            FragmentWordsBuildersModule.class,
//            FragmentMyBuildersModule.class
//    })

    @ContributesAndroidInjector
    abstract HelloActivity contributeHelloActivity();
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