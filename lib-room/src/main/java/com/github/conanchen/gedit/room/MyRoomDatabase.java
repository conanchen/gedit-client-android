package com.github.conanchen.gedit.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.TypeConverters;

import com.github.conanchen.gedit.room.hello.DaoHello;
import com.github.conanchen.gedit.room.hello.Hello;
import com.github.conanchen.gedit.room.kv.DaoKeyValue;
import com.github.conanchen.gedit.room.kv.KeyValue;
import com.github.conanchen.gedit.room.kv.ValueConverters;
import com.github.conanchen.gedit.room.store.DaoMyStore;
import com.github.conanchen.gedit.room.store.DaoStore;
import com.github.conanchen.gedit.room.store.MyStore;
import com.github.conanchen.gedit.room.store.Store;


@Database(entities =
        {
                Hello.class,
                Store.class,
                KeyValue.class,
                MyStore.class
        },
        version = 1,
        exportSchema = false)
@TypeConverters({ValueConverters.class, Converters.class})
public abstract class MyRoomDatabase extends android.arch.persistence.room.RoomDatabase {
    public abstract DaoHello daoHello();

    public abstract DaoStore daoStore();

    public abstract DaoKeyValue daoKeyValue();

    public abstract DaoMyStore daoMyStores();

}