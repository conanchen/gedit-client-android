package com.github.conanchen.gedit.hello.grpc.di;

import com.github.conanchen.gedit.hello.grpc.HelloService;
import com.github.conanchen.gedit.hello.grpc.auth.RegisterService;
import com.github.conanchen.gedit.hello.grpc.auth.SigninService;
import com.github.conanchen.gedit.hello.grpc.store.MyStoreService;
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
    public SigninService signinService;
    public RegisterService registerService;


    @Inject
    public GrpcFascade(HelloService helloService,
                       StoreService storeService,
                       MyStoreService myStoreService,
                       SigninService signinService,
                       RegisterService registerService) {
        this.helloService = helloService;
        this.storeService = storeService;
        this.myStoreService = myStoreService;
        this.signinService = signinService;
        this.registerService = registerService;
        System.out.println(strAmir);
    }

    public String getConvertedStrAmir() {
        return "Convert " + strAmir;
    }

}