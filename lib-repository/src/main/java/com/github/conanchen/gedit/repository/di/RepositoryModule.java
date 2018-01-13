package com.github.conanchen.gedit.repository.di;


import com.github.conanchen.gedit.hello.grpc.di.GrpcFascade;
import com.github.conanchen.gedit.hello.grpc.di.GrpcModule;
import com.github.conanchen.gedit.repository.RepositoryFascade;
import com.github.conanchen.gedit.repository.hello.HelloRepository;
import com.github.conanchen.gedit.repository.hello.RegisterRepository;
import com.github.conanchen.gedit.repository.hello.SigninRepository;
import com.github.conanchen.gedit.repository.hello.StoreRepository;
import com.github.conanchen.gedit.repository.kv.KeyValueRepository;
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
        RoomModule.class,
})
public class RepositoryModule {

    @Singleton
    @Provides
    public RepositoryFascade provideRepositoryFascade(
            HelloRepository helloRepository,
            StoreRepository storeRepository,
            SigninRepository signinRepository,
            KeyValueRepository keyValueRepository,
            RegisterRepository registerRepository
    ) {
        return new RepositoryFascade(
                helloRepository,
                storeRepository,
                signinRepository,
                keyValueRepository,
                registerRepository);
    }


    @Singleton
    @Provides
    public HelloRepository provideHelloRepository(
            RoomFascade roomFascade, GrpcFascade grpcFascade) {
        return new HelloRepository(roomFascade, grpcFascade);
    }

    @Singleton
    @Provides
    public StoreRepository provideStoreRepository(
            RoomFascade roomFascade, GrpcFascade grpcFascade) {
        return new StoreRepository(roomFascade, grpcFascade);
    }


    @Singleton
    @Provides
    public SigninRepository provideSigninRepository(
            RoomFascade roomFascade, GrpcFascade grpcFascade) {
        return new SigninRepository(roomFascade, grpcFascade);
    }


    @Singleton
    @Provides
    public KeyValueRepository provideKeyValueRepository(
            RoomFascade roomFascade, GrpcFascade grpcFascade) {
        return new KeyValueRepository(roomFascade, grpcFascade);
    }


    @Singleton
    @Provides
    public RegisterRepository provideRegisterRepository(
            RoomFascade roomFascade, GrpcFascade grpcFascade) {
        return new RegisterRepository(roomFascade, grpcFascade);
    }


}