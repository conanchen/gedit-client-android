package com.github.conanchen.gedit.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.TypeConverters;

import com.github.conanchen.gedit.room.hello.DaoHello;
import com.github.conanchen.gedit.room.hello.Hello;


@Database(entities =
        {
                Hello.class
        }, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MyRoomDatabase extends android.arch.persistence.room.RoomDatabase {
    public abstract DaoHello daoHello();
}