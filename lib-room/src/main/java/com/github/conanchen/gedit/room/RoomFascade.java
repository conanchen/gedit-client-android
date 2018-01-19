package com.github.conanchen.gedit.room;


import com.github.conanchen.gedit.room.hello.DaoHello;
import com.github.conanchen.gedit.room.kv.DaoKeyValue;
import com.github.conanchen.gedit.room.my.fan.DaoFanship;
import com.github.conanchen.gedit.room.my.store.DaoMyIntroducedStore;
import com.github.conanchen.gedit.room.my.store.DaoMyStore;
import com.github.conanchen.gedit.room.store.DaoStore;

import javax.inject.Inject;

/**
 * Created by conanchen on 10/4/16.
 */
public class RoomFascade {

    public final DaoHello daoHello;
    public final DaoStore daoStore;
    public final DaoKeyValue daoKeyValue;
    public final DaoMyStore daoMyStore;
    public final DaoMyIntroducedStore daoMyIntroducedStore;
    public final DaoFanship daoFanship;

    @Inject
    String strAmir;


    @Inject
    public RoomFascade(DaoHello daoHello,
                       DaoStore daoStore,
                       DaoKeyValue daoKeyValue,
                       DaoMyStore daoMyStore,
                       DaoMyIntroducedStore daoMyIntroducedStore,
                       DaoFanship daoFanship
    ) {
        this.daoStore = daoStore;
        this.daoHello = daoHello;
        this.daoKeyValue = daoKeyValue;
        this.daoMyStore = daoMyStore;
        this.daoMyIntroducedStore = daoMyIntroducedStore;
        this.daoFanship = daoFanship;
        System.out.println(strAmir);

    }

    public String getConvertedStrAmir() {
        return "Convert " + strAmir;
    }


}