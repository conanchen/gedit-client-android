package com.github.conanchen.yeamore.hello.grpc;

import javax.inject.Inject;

/**
 * Created by conanchen on 10/4/16.
 */
public class GrpcFascade {


    @Inject
    String strAmir;

    public HelloService helloService;


    @Inject
    public GrpcFascade(HelloService helloService) {
        this.helloService = helloService;
        System.out.println(strAmir);
    }

    public String getConvertedStrAmir() {
        return "Convert " + strAmir;
    }

}