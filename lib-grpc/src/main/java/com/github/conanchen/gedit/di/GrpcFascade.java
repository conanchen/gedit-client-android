package com.github.conanchen.gedit.di;

import com.github.conanchen.gedit.grpc.accounting.PostingService;
import com.github.conanchen.gedit.grpc.auth.RegisterService;
import com.github.conanchen.gedit.grpc.auth.SigninService;
import com.github.conanchen.gedit.grpc.fan.MyFansService;
import com.github.conanchen.gedit.grpc.accounting.AccountService;
import com.github.conanchen.gedit.grpc.hello.HelloService;
import com.github.conanchen.gedit.grpc.payment.PaymentService;
import com.github.conanchen.gedit.grpc.store.MyIntroducedStoreService;
import com.github.conanchen.gedit.grpc.store.MyMemberStoreService;
import com.github.conanchen.gedit.grpc.store.MyStoreService;
import com.github.conanchen.gedit.grpc.store.MyWorkinStoreService;
import com.github.conanchen.gedit.grpc.store.StoreService;

import javax.inject.Inject;

/**
 * Created by conanchen on 10/4/16.
 */
public class GrpcFascade {


    @Inject
    String strAmir;

    public HelloService helloService;
    public StoreService storeService;
    public MyStoreService myStoreService;
    public MyWorkinStoreService myWorkinStoreService;
    public MyIntroducedStoreService myIntroducedStoreService;
    public MyMemberStoreService myMemberStoreService;
    public MyFansService myFansService;
    public SigninService signinService;
    public RegisterService registerService;
    public AccountService accountService;
    public PostingService postingService;
    public PaymentService paymentService;


    @Inject
    public GrpcFascade(HelloService helloService,
                       StoreService storeService,
                       MyStoreService myStoreService,
                       MyWorkinStoreService myWorkinStoreService,
                       MyIntroducedStoreService myIntroducedStoreService,
                       MyMemberStoreService myMemberStoreService,
                       MyFansService myFansService,
                       SigninService signinService,
                       RegisterService registerService,
                       AccountService accountService,
                       PostingService postingService,
                       PaymentService paymentService
                       ) {
        this.helloService = helloService;
        this.storeService = storeService;
        this.myStoreService = myStoreService;
        this.myWorkinStoreService = myWorkinStoreService;
        this.myIntroducedStoreService = myIntroducedStoreService;
        this.myMemberStoreService = myMemberStoreService;
        this.myFansService = myFansService;
        this.signinService = signinService;
        this.registerService = registerService;
        this.accountService = accountService;
        this.postingService = postingService;
        this.paymentService = paymentService;
        System.out.println(strAmir);
    }

    public String getConvertedStrAmir() {
        return "Convert " + strAmir;
    }

}