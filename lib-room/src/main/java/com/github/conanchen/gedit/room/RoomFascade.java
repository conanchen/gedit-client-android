package com.github.conanchen.gedit.room;


import com.github.conanchen.gedit.room.hello.DaoHello;

import javax.inject.Inject;

/**
 * Created by conanchen on 10/4/16.
 */
public class RoomFascade {

    public final DaoHello daoHello;

    @Inject
    String strAmir;


    @Inject
    public RoomFascade(DaoHello daoHello) {
        this.daoHello = daoHello;
        System.out.println(strAmir);

    }

    public String getConvertedStrAmir() {
        return "Convert " + strAmir;
    }


}