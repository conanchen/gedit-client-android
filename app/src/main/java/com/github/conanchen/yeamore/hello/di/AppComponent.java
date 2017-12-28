package com.github.conanchen.yeamore.hello.di;

import android.app.Application;


import com.github.conanchen.yeamore.hello.HelloApplication;
import com.github.conanchen.yeamore.hello.repository.di.RepositoryModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {

        AppModule.class,
        AndroidInjectionModule.class,

        MainActivityModule.class,
        RepositoryModule.class,
})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(HelloApplication helloApplication);
}


