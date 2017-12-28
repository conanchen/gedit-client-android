package com.github.conanchen.yeamore.hello.grpc.di;


import com.github.conanchen.yeamore.hello.grpc.GrpcFascade;
import com.github.conanchen.yeamore.hello.grpc.HelloService;


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
            HelloService helloService
    ) {
        return new GrpcFascade(
                helloService);
    }


    @Singleton
    @Provides
    public HelloService provideHelloService() {
        return new HelloService();
    }
}