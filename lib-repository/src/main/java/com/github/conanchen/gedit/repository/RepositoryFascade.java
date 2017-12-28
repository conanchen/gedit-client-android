package com.github.conanchen.gedit.repository;

import com.github.conanchen.gedit.repository.hello.HelloRepository;

import javax.inject.Inject;

/**
 * Created by conanchen on 10/4/16.
 */
public class RepositoryFascade {


    @Inject
    String strAmir;

    public HelloRepository helloRepository;


    @Inject
    public RepositoryFascade(HelloRepository helloRepository) {
        this.helloRepository = helloRepository;
        System.out.println(strAmir);
    }

    public String getConvertedStrAmir() {
        return "Convert " + strAmir;
    }

}