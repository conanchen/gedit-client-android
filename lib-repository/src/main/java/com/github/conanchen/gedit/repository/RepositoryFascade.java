package com.github.conanchen.gedit.repository;

import com.github.conanchen.gedit.repository.hello.HelloRepository;
import com.github.conanchen.gedit.repository.hello.StoreRepository;

import javax.inject.Inject;

/**
 * Created by conanchen on 10/4/16.
 */
public class RepositoryFascade {


    @Inject
    String strAmir;

    public HelloRepository helloRepository;
    public StoreRepository storeRepository;

    @Inject
    public RepositoryFascade(HelloRepository helloRepository,StoreRepository storeRepository) {
        this.helloRepository = helloRepository;
        this.storeRepository = storeRepository;
        System.out.println(strAmir);
    }

    public String getConvertedStrAmir() {
        return "Convert " + strAmir;
    }
}