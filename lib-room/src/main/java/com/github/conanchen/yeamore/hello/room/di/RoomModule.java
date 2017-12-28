package com.github.conanchen.yeamore.hello.room.di;

import android.arch.persistence.room.Room;
import android.content.Context;


import com.github.conanchen.yeamore.hello.room.DaoHello;
import com.github.conanchen.yeamore.hello.room.MyRoomDatabase;
import com.github.conanchen.yeamore.hello.room.RoomFascade;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by amirziarati on 10/4/16.
 */
@Singleton
@Module
public class RoomModule {

    @Singleton
    @Provides
    public String provideAmir() {
        return "Amir Was Injected";
    }

    @Singleton
    @Provides
    MyRoomDatabase provideRoomDatabase(Context context) {
        return Room.databaseBuilder(context, MyRoomDatabase.class, "yeamorehello.db").build();
    }

    @Singleton
    @Provides
    DaoHello provideDaoHello(MyRoomDatabase db) {
        return db.daoHello();
    }


    @Singleton
    @Provides
    public RoomFascade provideRoomFascade(DaoHello daoHello) {
        return new RoomFascade(daoHello);
    }
}