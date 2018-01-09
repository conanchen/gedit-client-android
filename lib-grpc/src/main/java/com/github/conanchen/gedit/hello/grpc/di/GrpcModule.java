package com.github.conanchen.gedit.hello.grpc.di;


import com.github.conanchen.gedit.hello.grpc.HelloService;
import com.github.conanchen.gedit.hello.grpc.StoreService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by conanchen on 10/4/16.
 */
@Singleton
@Module()
public class GrpcModule {

    @Singleton
    @Provides
    public GrpcFascade provideGrpcFascade(
            HelloService helloService,
            StoreService storeService

    ) {
        return new GrpcFascade(
                helloService,storeService);
    }


    @Singleton
    @Provides
    public HelloService provideHelloService() {
        return new HelloService();
    }


    @Singleton
    @Provides
    public StoreService provideStoreService() {
        return new StoreService();
    }
}