package com.github.conanchen.gedit.di;


import com.github.conanchen.gedit.ui.MainActivity;
import com.github.conanchen.gedit.ui.SplashActivity;
import com.github.conanchen.gedit.ui.auth.LoginActivity;
import com.github.conanchen.gedit.ui.auth.RegisterActivity;
import com.github.conanchen.gedit.ui.hello.HelloActivity;
import com.github.conanchen.gedit.ui.my.MySummaryFragment;
import com.github.conanchen.gedit.ui.my.myfans.MyFansActivity;
import com.github.conanchen.gedit.ui.my.myintroducedstore.MyIntroducedStoresActivity;
import com.github.conanchen.gedit.ui.my.mypoints.CanConsumptionPointsFragment;
import com.github.conanchen.gedit.ui.my.mypoints.CanExchangePointsFragment;
import com.github.conanchen.gedit.ui.my.mypoints.MyPointsActivity;
import com.github.conanchen.gedit.ui.my.mypoints.TodayAddedPointsFragment;
import com.github.conanchen.gedit.ui.my.mystore.MyStoreEmployeesActivity;
import com.github.conanchen.gedit.ui.my.mystore.MyStoresActivity;
import com.github.conanchen.gedit.ui.my.myworkinstore.MyWorkStoresActivity;
import com.github.conanchen.gedit.ui.payment.GaptureActivity;
import com.github.conanchen.gedit.ui.payment.PayeeQRCodeActivity;
import com.github.conanchen.gedit.ui.payment.PointsPayActivity;
import com.github.conanchen.gedit.ui.store.StoreCreateActivity;
import com.github.conanchen.gedit.ui.store.StoreDetailActivity;
import com.github.conanchen.gedit.ui.store.StoreListFragment;
import com.github.conanchen.gedit.ui.store.StoreUpdateActivity;
import com.github.conanchen.gedit.ui.store.StoreUpdateStoreDisplayActivity;

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

    @ContributesAndroidInjector
    abstract TodayAddedPointsFragment contributeTodayAddPointsFragment();

    @ContributesAndroidInjector
    abstract CanConsumptionPointsFragment contributeCanConsumptionPointsFragment();

    @ContributesAndroidInjector
    abstract CanExchangePointsFragment contributeCanExchangePointsFragment();

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
    abstract StoreUpdateStoreDisplayActivity contributeStoreUpdateStoreDisplayActivity();

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
    abstract MyFansActivity contributeMyFansActivity();

    @ContributesAndroidInjector
    abstract MyPointsActivity contributeMyPointsActivity();

    @ContributesAndroidInjector
    abstract PayeeQRCodeActivity contributePayeeQRCodeActivity();

    @ContributesAndroidInjector
    abstract PointsPayActivity contributePointsPayActivity();


    //
//    @ContributesAndroidInjector
//    abstract AppServiceKeepliveTraceImpl contributeAppServiceKeepliveTraceImpl();
//
//    @ContributesAndroidInjector
//    abstract AppServiceCommandSenderImpl contributeAppServiceCommandSenderImpl();
}