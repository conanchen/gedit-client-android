package com.github.conanchen.gedit.di;


import com.github.conanchen.gedit.grpc.accounting.AccountService;
import com.github.conanchen.gedit.grpc.accounting.PostingService;
import com.github.conanchen.gedit.grpc.auth.MySummaryService;
import com.github.conanchen.gedit.grpc.auth.RegisterService;
import com.github.conanchen.gedit.grpc.auth.SigninService;
import com.github.conanchen.gedit.grpc.fan.MyFansService;
import com.github.conanchen.gedit.grpc.hello.HelloService;
import com.github.conanchen.gedit.grpc.payment.PaymentService;
import com.github.conanchen.gedit.grpc.store.MyIntroducedStoreService;
import com.github.conanchen.gedit.grpc.store.MyMemberStoreService;
import com.github.conanchen.gedit.grpc.store.MyStoreService;
import com.github.conanchen.gedit.grpc.store.MyWorkinStoreService;
import com.github.conanchen.gedit.grpc.store.StoreProfileService;
import com.github.conanchen.gedit.grpc.store.StoreSearchService;
import com.github.conanchen.gedit.grpc.store.StoreWorkerService;

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
            StoreProfileService storeProfileService,
            StoreSearchService storeSearchService,
            MyStoreService myStoreService,
            MyWorkinStoreService myWorkinStoreService,
            MyIntroducedStoreService myIntroducedStoreService,
            MyMemberStoreService myMemberStoreService,
            MyFansService myFansService,
            SigninService signinService,
            RegisterService registerService,
            AccountService accountService,
            PostingService postingService,
            PaymentService paymentService,
            StoreWorkerService storeWorkerService,
            MySummaryService mySummaryService

    ) {
        return new GrpcFascade(
                helloService,
                storeProfileService,
                storeSearchService,
                myStoreService,
                myWorkinStoreService,
                myIntroducedStoreService,
                myMemberStoreService,
                myFansService,
                signinService,
                registerService,
                accountService,
                postingService,
                paymentService,
                storeWorkerService,
                mySummaryService);
    }

    @Singleton
    @Provides
    public HelloService provideHelloService() {
        return new HelloService();
    }


    @Singleton
    @Provides
    public StoreProfileService provideStoreProfileService() {
        return new StoreProfileService();
    }

    @Singleton
    @Provides
    public StoreSearchService provideStoreSearchService() {
        return new StoreSearchService();
    }

    @Singleton
    @Provides
    public MyStoreService provideMyStoreService() {
        return new MyStoreService();
    }

    @Singleton
    @Provides
    public MyIntroducedStoreService provideMyIntroducedStoreService() {
        return new MyIntroducedStoreService();
    }

    @Singleton
    @Provides
    public MyFansService provideMyFansService() {
        return new MyFansService();
    }

    @Singleton
    @Provides
    public MyWorkinStoreService provideMyWorkinStoreService() {
        return new MyWorkinStoreService();
    }

    @Singleton
    @Provides
    public MyMemberStoreService provideMyMemberStoreService() {
        return new MyMemberStoreService();
    }

    @Singleton
    @Provides
    public SigninService provideSigninService() {
        return new SigninService();
    }

    @Singleton
    @Provides
    public RegisterService provideRegisterService() {
        return new RegisterService();
    }

    @Singleton
    @Provides
    public AccountService provideAccountingService() {
        return new AccountService();
    }

    @Singleton
    @Provides
    public PostingService providePostingService() {
        return new PostingService();
    }

    @Singleton
    @Provides
    public PaymentService providePaymentService() {
        return new PaymentService();
    }

    @Singleton
    @Provides
    public StoreWorkerService provideStoreWorkerService() {
        return new StoreWorkerService();
    }

    @Singleton
    @Provides
    public MySummaryService provideMySummaryService() {
        return new MySummaryService();
    }
}