package com.github.conanchen.gedit.room.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.github.conanchen.gedit.room.MyRoomDatabase;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.gedit.room.hello.DaoHello;
import com.github.conanchen.gedit.room.kv.DaoKeyValue;
import com.github.conanchen.gedit.room.store.DaoMyIntroducedStore;
import com.github.conanchen.gedit.room.store.DaoMyMemberStore;
import com.github.conanchen.gedit.room.store.DaoMyStore;
import com.github.conanchen.gedit.room.store.DaoMyWorkinStore;
import com.github.conanchen.gedit.room.store.DaoStore;

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
        return Room.databaseBuilder(context, MyRoomDatabase.class, "gedit.db").build();
    }

    @Singleton
    @Provides
    DaoHello provideDaoHello(MyRoomDatabase db) {
        return db.daoHello();
    }


    @Singleton
    @Provides
    DaoStore provideDaoStore(MyRoomDatabase db) {
        return db.daoStore();
    }

    @Singleton
    @Provides
    DaoKeyValue provideKeyValueDao(MyRoomDatabase db) {
        return db.daoKeyValue();
    }

    @Singleton
    @Provides
    DaoMyStore provideDaoMyStore(MyRoomDatabase db) {
        return db.daoMyStore();
    }


    @Singleton
    @Provides
    DaoMyWorkinStore provideDaoMyWorkinStore(MyRoomDatabase db) {
        return db.daoMyWorkinStore();
    }


    @Singleton
    @Provides
    DaoMyIntroducedStore provideDaoMyIntroducedStore(MyRoomDatabase db) {
        return db.daoMyIntroducedStore();
    }

    @Singleton
    @Provides
    DaoMyMemberStore provideDaoMyMemberStore(MyRoomDatabase db) {
        return db.daoMyMemberStore();
    }


    @Singleton
    @Provides
    public RoomFascade provideRoomFascade(DaoHello daoHello, DaoStore daoStore, DaoKeyValue daoKeyValue,
                                          DaoMyStore daoMyStore) {
        return new RoomFascade(daoHello, daoStore, daoKeyValue, daoMyStore);
    }
}