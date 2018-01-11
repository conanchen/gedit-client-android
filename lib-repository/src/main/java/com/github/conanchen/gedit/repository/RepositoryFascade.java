package com.github.conanchen.gedit.repository;

import com.github.conanchen.gedit.repository.hello.HelloRepository;
import com.github.conanchen.gedit.repository.hello.SigninRepository;
import com.github.conanchen.gedit.repository.hello.StoreRepository;
import com.github.conanchen.gedit.repository.kv.KeyValueRepository;

import javax.inject.Inject;

/**
 * Created by conanchen on 10/4/16.
 */
public class RepositoryFascade {

    @Inject
    String strAmir;

    public HelloRepository helloRepository;
    public StoreRepository storeRepository;
    public KeyValueRepository keyValueRepository;
    public SigninRepository signinRepository;

    @Inject
    public RepositoryFascade(HelloRepository helloRepository,
                             StoreRepository storeRepository,
                             SigninRepository signinRepository,
                             KeyValueRepository keyValueRepository) {
        this.helloRepository = helloRepository;
        this.storeRepository = storeRepository;
        this.signinRepository = signinRepository;
        this.keyValueRepository = keyValueRepository;
        System.out.println(strAmir);
    }

    public String getConvertedStrAmir() {
        return "Convert " + strAmir;
    }
}