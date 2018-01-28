package com.github.conanchen.gedit.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.github.conanchen.gedit.di.common.ViewModelFactory;
import com.github.conanchen.gedit.di.common.ViewModelKey;
import com.github.conanchen.gedit.ui.MainViewModel;
import com.github.conanchen.gedit.ui.auth.CurrentSigninViewModel;
import com.github.conanchen.gedit.ui.auth.RegisterViewModel;
import com.github.conanchen.gedit.ui.auth.SigninViewModel;
import com.github.conanchen.gedit.ui.hello.HelloViewModel;
import com.github.conanchen.gedit.ui.my.MySummaryViewModel;
import com.github.conanchen.gedit.ui.my.myfans.MyFansViewModel;
import com.github.conanchen.gedit.ui.my.myintroducedstore.MyIntroducedStoresViewModel;
import com.github.conanchen.gedit.ui.my.mypoints.CanConsumptionPointsFragmentViewModel;
import com.github.conanchen.gedit.ui.my.mypoints.CanExchangePointsFragmentViewModel;
import com.github.conanchen.gedit.ui.my.mypoints.MyPointsViewModel;
import com.github.conanchen.gedit.ui.my.mypoints.TodayAddedPointsFragmentViewModel;
import com.github.conanchen.gedit.ui.my.mystore.MyStoreEmployeesViewModel;
import com.github.conanchen.gedit.ui.my.mystore.MyStoresViewModel;
import com.github.conanchen.gedit.ui.my.myworkinstore.MyWorkStoresViewModel;
import com.github.conanchen.gedit.ui.payment.PayeeQRCodeViewModel;
import com.github.conanchen.gedit.ui.payment.PointsPayViewModel;
import com.github.conanchen.gedit.ui.store.StoreCreateViewModel;
import com.github.conanchen.gedit.ui.store.StoreListViewModel;
import com.github.conanchen.gedit.ui.store.StoreUpdateHeadPortraitViewModel;
import com.github.conanchen.gedit.ui.store.StoreUpdateViewModel;
import com.github.conanchen.gedit.ui.store.StoreViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

    @Binds
    @IntoMap
    @ViewModelKey(HelloViewModel.class)
    abstract ViewModel bindHelloViewModel(HelloViewModel helloViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(StoreViewModel.class)
    abstract ViewModel bindStoreViewModel(StoreViewModel storeViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(StoreCreateViewModel.class)
    abstract ViewModel bindStoreCreateViewModel(StoreCreateViewModel storeCreateViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(StoreListViewModel.class)
    abstract ViewModel bindStoreListViewModel(StoreListViewModel storeListViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MySummaryViewModel.class)
    abstract ViewModel bindMyViewModel(MySummaryViewModel mySummaryViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(StoreUpdateViewModel.class)
    abstract ViewModel bindStoreUpdateViewModel(StoreUpdateViewModel storeUpdateViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(SigninViewModel.class)
    abstract ViewModel bindSigninViewModel(SigninViewModel signinViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CurrentSigninViewModel.class)
    abstract ViewModel bindCurrentSigninViewModel(CurrentSigninViewModel currentSigninViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(StoreUpdateHeadPortraitViewModel.class)
    abstract ViewModel bindStoreUpdateHeadPortraitViewModel(StoreUpdateHeadPortraitViewModel signinViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel.class)
    abstract ViewModel bindRegisterViewModel(RegisterViewModel registerViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MyStoresViewModel.class)
    abstract ViewModel bindMyStoresViewModel(MyStoresViewModel myStoresViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MyWorkStoresViewModel.class)
    abstract ViewModel bindMyWorkStoresViewModel(MyWorkStoresViewModel myWorkStoresViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MyIntroducedStoresViewModel.class)
    abstract ViewModel bindMyIntroducedStoresViewModel(MyIntroducedStoresViewModel myIntroducedStoresViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MyStoreEmployeesViewModel.class)
    abstract ViewModel bindMyStoreEmployeesViewModel(MyStoreEmployeesViewModel myIntroducedStoresViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MyFansViewModel.class)
    abstract ViewModel bindMyFansViewModel(MyFansViewModel myFansViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MyPointsViewModel.class)
    abstract ViewModel bindMyPointsViewModel(MyPointsViewModel myPointsViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TodayAddedPointsFragmentViewModel.class)
    abstract ViewModel bindTodayAddPointsFragmentViewModel(TodayAddedPointsFragmentViewModel todayAddedPointsFragmentViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(CanExchangePointsFragmentViewModel.class)
    abstract ViewModel bindCanExchangePointsFragmentViewModel(CanExchangePointsFragmentViewModel todayAddPointsFragmentViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(CanConsumptionPointsFragmentViewModel.class)
    abstract ViewModel bindCanConsumptionPointsFragmentViewModel(CanConsumptionPointsFragmentViewModel todayAddPointsFragmentViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(PayeeQRCodeViewModel.class)
    abstract ViewModel bindPayeeQRCodeViewModel(PayeeQRCodeViewModel payeeQRCodeViewModel);


    @Binds
    @IntoMap
    @ViewModelKey(PointsPayViewModel.class)
    abstract ViewModel bindPointsPayViewModel(PointsPayViewModel pointsPayViewModel);



    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindMainViewModel(MainViewModel mainViewModel);

}
