package com.github.conanchen.gedit.hello.grpc.di;

import com.github.conanchen.gedit.hello.grpc.HelloService;
import com.github.conanchen.gedit.hello.grpc.auth.RegisterService;
import com.github.conanchen.gedit.hello.grpc.auth.SigninService;
import com.github.conanchen.gedit.hello.grpc.store.MyIntroducedStoreService;
import com.github.conanchen.gedit.hello.grpc.store.MyMemberStoreService;
import com.github.conanchen.gedit.hello.grpc.store.MyStoreService;
import com.github.conanchen.gedit.hello.grpc.store.MyWorkinStoreService;
import com.github.conanchen.gedit.hello.grpc.store.StoreService;

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
    public SigninService signinService;
    public RegisterService registerService;


    @Inject
    public GrpcFascade(HelloService helloService,
                       StoreService storeService,
                       MyStoreService myStoreService,
                       MyWorkinStoreService myWorkinStoreService,
                       MyIntroducedStoreService myIntroducedStoreService,
                       MyMemberStoreService myMemberStoreService,
                       SigninService signinService,
                       RegisterService registerService) {
        this.helloService = helloService;
        this.storeService = storeService;
        this.myStoreService = myStoreService;
        this.myWorkinStoreService = myWorkinStoreService;
        this.myIntroducedStoreService = myIntroducedStoreService;
        this.myMemberStoreService = myMemberStoreService;
        this.signinService = signinService;
        this.registerService = registerService;
        System.out.println(strAmir);
    }

    public String getConvertedStrAmir() {
        return "Convert " + strAmir;
    }

}