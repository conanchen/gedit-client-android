package com.github.conanchen.gedit.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.github.conanchen.gedit.di.common.ViewModelFactory;
import com.github.conanchen.gedit.di.common.ViewModelKey;
import com.github.conanchen.gedit.ui.auth.SigninViewModel;
import com.github.conanchen.gedit.ui.hello.HelloViewModel;
import com.github.conanchen.gedit.ui.my.MyViewModel;
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
    @ViewModelKey(MyViewModel.class)
    abstract ViewModel bindMyViewModel(MyViewModel myViewModel);


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
    @ViewModelKey(StoreUpdateHeadPortraitViewModel.class)
    abstract ViewModel bindStoreUpdateHeadPortraitViewModel(StoreUpdateHeadPortraitViewModel signinViewModel);


}
