package com.github.conanchen.gedit.di.common;

import android.app.Application;

import com.github.conanchen.gedit.GeditApplication;
import com.github.conanchen.gedit.di.ViewModule;
import com.github.conanchen.gedit.repository.di.RepositoryModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {

        AppModule.class,
        AndroidInjectionModule.class,

        ViewModule.class,
        RepositoryModule.class
})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(GeditApplication geditApplication);
}


