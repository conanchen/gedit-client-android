package com.github.conanchen.gedit.room;


import com.github.conanchen.gedit.room.hello.DaoHello;
import com.github.conanchen.gedit.room.store.DaoStore;

import javax.inject.Inject;

/**
 * Created by conanchen on 10/4/16.
 */
public class RoomFascade {

    public final DaoHello daoHello;
    public final DaoStore daoStore;

    @Inject
    String strAmir;


    @Inject
    public RoomFascade(DaoHello daoHello, DaoStore daoStore) {
        this.daoStore = daoStore;
        this.daoHello = daoHello;
        System.out.println(strAmir);

    }

    public String getConvertedStrAmir() {
        return "Convert " + strAmir;
    }


}