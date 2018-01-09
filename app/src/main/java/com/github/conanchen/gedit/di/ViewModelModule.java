package com.github.conanchen.gedit.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.github.conanchen.gedit.di.common.ViewModelFactory;
import com.github.conanchen.gedit.di.common.ViewModelKey;
import com.github.conanchen.gedit.ui.hello.HelloViewModel;
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
    abstract ViewModel bindViewModel(HelloViewModel helloViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(StoreViewModel.class)
    abstract ViewModel bindStoreViewModel(StoreViewModel storeViewModel);

}
