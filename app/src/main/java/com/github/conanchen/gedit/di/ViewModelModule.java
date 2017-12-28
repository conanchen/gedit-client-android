package com.github.conanchen.gedit.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.github.conanchen.gedit.ui.hello.HelloViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(HelloViewModel.class)
    abstract ViewModel bindUserViewModel(HelloViewModel userViewModel);


    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(HelloViewModelFactory factory);
}
