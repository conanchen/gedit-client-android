package com.github.conanchen.gedit.repository.di;


import com.github.conanchen.gedit.hello.grpc.di.GrpcFascade;
import com.github.conanchen.gedit.hello.grpc.di.GrpcModule;
import com.github.conanchen.gedit.repository.hello.HelloRepository;
import com.github.conanchen.gedit.repository.RepositoryFascade;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.gedit.room.di.RoomModule;

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