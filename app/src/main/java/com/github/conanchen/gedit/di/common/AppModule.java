package com.github.conanchen.gedit.di.common;

import android.app.Application;
import android.content.Context;

import com.github.conanchen.gedit.di.ViewModelModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = ViewModelModule.class)
public class AppModule {


    @Provides
    @Singleton
    Context providesApplicationContext(Application application) {
        return application.getApplicationContext();
    }

}