package com.github.conanchen.gedit.hello.grpc.di;

import com.github.conanchen.gedit.hello.grpc.HelloService;
import com.github.conanchen.gedit.hello.grpc.StoreService;

import javax.inject.Inject;

/**
 * Created by conanchen on 10/4/16.
 */
public class GrpcFascade {


    @Inject
    String strAmir;

    public HelloService helloService;
    public StoreService storeService;


    @Inject
    public GrpcFascade(HelloService helloService,StoreService storeService) {
        this.helloService = helloService;
        this.storeService = storeService;
        System.out.println(strAmir);
    }

    public String getConvertedStrAmir() {
        return "Convert " + strAmir;
    }

}