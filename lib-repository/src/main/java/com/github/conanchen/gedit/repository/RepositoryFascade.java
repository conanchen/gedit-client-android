package com.github.conanchen.gedit.repository;

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
    public MyStoreRepository myStoreRepository;
    public KeyValueRepository keyValueRepository;
    public SigninRepository signinRepository;
    public RegisterRepository registerRepository;
    public AccountingRepository accountingRepository;

    @Inject
    public RepositoryFascade(HelloRepository helloRepository,
                             StoreRepository storeRepository,
                             MyStoreRepository myStoreRepository,
                             SigninRepository signinRepository,
                             KeyValueRepository keyValueRepository,
                             RegisterRepository registerRepository,
                             AccountingRepository accountingRepository
                             ) {
        this.helloRepository = helloRepository;
        this.storeRepository = storeRepository;
        this.myStoreRepository = myStoreRepository;
        this.signinRepository = signinRepository;
        this.keyValueRepository = keyValueRepository;
        this.registerRepository = registerRepository;
        this.accountingRepository = accountingRepository;
        System.out.println(strAmir);
    }

    public String getConvertedStrAmir() {
        return "Convert " + strAmir;
    }
}