package com.github.conanchen.gedit.repository.di;


import com.github.conanchen.gedit.di.GrpcFascade;
import com.github.conanchen.gedit.di.GrpcModule;
import com.github.conanchen.gedit.repository.AccountingRepository;
import com.github.conanchen.gedit.repository.MyIntroducedStoreRepository;
import com.github.conanchen.gedit.repository.MyMemberStoreRepository;
import com.github.conanchen.gedit.repository.MyWorkinStoreRepository;
import com.github.conanchen.gedit.repository.RepositoryFascade;
import com.github.conanchen.gedit.repository.HelloRepository;
import com.github.conanchen.gedit.repository.MyStoreRepository;
import com.github.conanchen.gedit.repository.RegisterRepository;
import com.github.conanchen.gedit.repository.SigninRepository;
import com.github.conanchen.gedit.repository.StoreProfileRepository;
import com.github.conanchen.gedit.repository.StoreWorkerRepository;
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
            StoreProfileRepository storeProfileRepository,
            MyStoreRepository myStoreRepository,
            SigninRepository signinRepository,
            KeyValueRepository keyValueRepository,
            RegisterRepository registerRepository,
            AccountingRepository accountingRepository,
            StoreWorkerRepository storeWorkerRepository
    ) {
        return new RepositoryFascade(
                helloRepository,
                storeProfileRepository,
                myStoreRepository,
                signinRepository,
                keyValueRepository,
                registerRepository,
                accountingRepository,
                storeWorkerRepository);
    }


    @Singleton
    @Provides
    public HelloRepository provideHelloRepository(
            RoomFascade roomFascade, GrpcFascade grpcFascade) {
        return new HelloRepository(roomFascade, grpcFascade);
    }

    @Singleton
    @Provides
    public StoreProfileRepository provideStoreRepository(
            RoomFascade roomFascade, GrpcFascade grpcFascade) {
        return new StoreProfileRepository(roomFascade, grpcFascade);
    }



    @Singleton
    @Provides
    public MyStoreRepository provideMyStoreRepository(
            RoomFascade roomFascade, GrpcFascade grpcFascade) {
        return new MyStoreRepository(roomFascade, grpcFascade);
    }


    @Singleton
    @Provides
    public MyIntroducedStoreRepository provideMyIntroducedStoreRepository(
            RoomFascade roomFascade, GrpcFascade grpcFascade) {
        return new MyIntroducedStoreRepository(roomFascade, grpcFascade);
    }


    @Singleton
    @Provides
    public MyWorkinStoreRepository provideMyWorkinStoreRepository(
            RoomFascade roomFascade, GrpcFascade grpcFascade) {
        return new MyWorkinStoreRepository(roomFascade, grpcFascade);
    }



    @Singleton
    @Provides
    public MyMemberStoreRepository provideMyMemberStoreRepository(
            RoomFascade roomFascade, GrpcFascade grpcFascade) {
        return new MyMemberStoreRepository(roomFascade, grpcFascade);
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


    @Singleton
    @Provides
    public AccountingRepository provideAccountingRepository(
            RoomFascade roomFascade, GrpcFascade grpcFascade) {
        return new AccountingRepository(roomFascade, grpcFascade);
    }



    @Singleton
    @Provides
    public StoreWorkerRepository provideStoreWorkerRepository(
            RoomFascade roomFascade, GrpcFascade grpcFascade) {
        return new StoreWorkerRepository(roomFascade, grpcFascade);
    }


}