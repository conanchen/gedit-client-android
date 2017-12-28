package com.github.conanchen.yeamore.hello.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.TypeConverters;


@Database(entities =
        {
                Hello.class
        }, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MyRoomDatabase extends android.arch.persistence.room.RoomDatabase {
    public abstract DaoHello daoHello();
}