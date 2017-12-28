package com.github.conanchen.yeamore.hello.repository.di;


import com.github.conanchen.yeamore.hello.grpc.GrpcFascade;
import com.github.conanchen.yeamore.hello.grpc.di.GrpcModule;
import com.github.conanchen.yeamore.hello.repository.HelloRepository;
import com.github.conanchen.yeamore.hello.repository.RepositoryFascade;
import com.github.conanchen.yeamore.hello.room.Hello;
import com.github.conanchen.yeamore.hello.room.RoomFascade;
import com.github.conanchen.yeamore.hello.room.di.RoomModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by conanchen on 10/4/16.
 */
@Singleton
@Module(includes = {
        GrpcModule.class,
        RoomModule.class
})
public class RepositoryModule {

    @Singleton
    @Provides
    public RepositoryFascade provideRepositoryFascade(
            HelloRepository helloRepository
    ) {
        return new RepositoryFascade(
                helloRepository);
    }


    @Singleton
    @Provides
    public HelloRepository provideHelloRepository(
            RoomFascade roomFascade, GrpcFascade grpcFascade) {
        return new HelloRepository(roomFascade,grpcFascade);
    }
}