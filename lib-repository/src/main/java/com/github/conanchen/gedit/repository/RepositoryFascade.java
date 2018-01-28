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
    public StoreProfileRepository storeProfileRepository;
    public MyStoreRepository myStoreRepository;
    public KeyValueRepository keyValueRepository;
    public SigninRepository signinRepository;
    public RegisterRepository registerRepository;
    public AccountingRepository accountingRepository;
    public StoreWorkerRepository storeWorkerRepository;
    public MySummaryRepository mySummaryRepository;

    @Inject
    public RepositoryFascade(HelloRepository helloRepository,
                             StoreProfileRepository storeProfileRepository,
                             MyStoreRepository myStoreRepository,
                             SigninRepository signinRepository,
                             KeyValueRepository keyValueRepository,
                             RegisterRepository registerRepository,
                             AccountingRepository accountingRepository,
                             StoreWorkerRepository storeWorkerRepository,
                             MySummaryRepository mySummaryRepository
                             ) {
        this.helloRepository = helloRepository;
        this.storeProfileRepository = storeProfileRepository;
        this.myStoreRepository = myStoreRepository;
        this.signinRepository = signinRepository;
        this.keyValueRepository = keyValueRepository;
        this.registerRepository = registerRepository;
        this.accountingRepository = accountingRepository;
        this.storeWorkerRepository = storeWorkerRepository;
        this.mySummaryRepository = mySummaryRepository;
        System.out.println(strAmir);
    }

    public String getConvertedStrAmir() {
        return "Convert " + strAmir;
    }
}