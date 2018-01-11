package com.github.conanchen.gedit.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.TypeConverters;

import com.github.conanchen.gedit.room.hello.DaoHello;
import com.github.conanchen.gedit.room.store.DaoStore;
import com.github.conanchen.gedit.room.hello.Hello;
import com.github.conanchen.gedit.room.store.Store;


@Database(entities = {
        Hello.class,
        Store.class},
        version = 1,
        exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MyRoomDatabase extends android.arch.persistence.room.RoomDatabase {
    public abstract DaoHello daoHello();

    public abstract DaoStore daoStore();
}