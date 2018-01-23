package com.github.conanchen.gedit.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.TypeConverters;

import com.github.conanchen.gedit.room.hello.DaoHello;
import com.github.conanchen.gedit.room.hello.Hello;
import com.github.conanchen.gedit.room.kv.DaoKeyValue;
import com.github.conanchen.gedit.room.kv.KeyValue;
import com.github.conanchen.gedit.room.kv.ValueConverters;
import com.github.conanchen.gedit.room.my.accounting.Account;
import com.github.conanchen.gedit.room.my.accounting.Balance;
import com.github.conanchen.gedit.room.my.accounting.DaoAccount;
import com.github.conanchen.gedit.room.my.accounting.DaoBalance;
import com.github.conanchen.gedit.room.my.accounting.DaoJournal;
import com.github.conanchen.gedit.room.my.accounting.DaoPosting;
import com.github.conanchen.gedit.room.my.accounting.Journal;
import com.github.conanchen.gedit.room.my.accounting.Posting;
import com.github.conanchen.gedit.room.my.fan.DaoFanship;
import com.github.conanchen.gedit.room.my.fan.Fanship;
import com.github.conanchen.gedit.room.my.payment.DaoPayment;
import com.github.conanchen.gedit.room.my.payment.Payment;
import com.github.conanchen.gedit.room.my.store.DaoMyIntroducedStore;
import com.github.conanchen.gedit.room.my.store.DaoMyMemberStore;
import com.github.conanchen.gedit.room.my.store.DaoMyStore;
import com.github.conanchen.gedit.room.my.store.DaoMyWorkinStore;
import com.github.conanchen.gedit.room.my.store.MyIntroducedStore;
import com.github.conanchen.gedit.room.my.store.MyMemberStore;
import com.github.conanchen.gedit.room.my.store.MyStore;
import com.github.conanchen.gedit.room.my.store.MyWorkinStore;
import com.github.conanchen.gedit.room.store.DaoStore;
import com.github.conanchen.gedit.room.store.DaoStoreWorker;
import com.github.conanchen.gedit.room.store.Store;
import com.github.conanchen.gedit.room.store.StoreWorker;


@Database(entities =
        {
                Hello.class,
                Store.class,
                MyWorkinStore.class,
                MyIntroducedStore.class,
                MyMemberStore.class,
                KeyValue.class,
                MyStore.class,
                Account.class,
                Balance.class,
                Journal.class,
                Posting.class,
                Payment.class,
                Fanship.class,
                StoreWorker.class
        },
        version = 1,
        exportSchema = false)
@TypeConverters({ValueConverters.class, Converters.class})
public abstract class MyRoomDatabase extends android.arch.persistence.room.RoomDatabase {
    public abstract DaoHello daoHello();

    public abstract DaoStore daoStore();

    public abstract DaoKeyValue daoKeyValue();

    public abstract DaoMyStore daoMyStore();

    public abstract DaoMyWorkinStore daoMyWorkinStore();

    public abstract DaoMyIntroducedStore daoMyIntroducedStore();

    public abstract DaoMyMemberStore daoMyMemberStore();

    public abstract DaoFanship daoFanship();

    public abstract DaoPayment daoDaoPayment();

    public abstract DaoAccount daoDaoAccount();

    public abstract DaoBalance daoDaoBalance();

    public abstract DaoJournal daoDaoJournal();

    public abstract DaoPosting daoDaoPosting();

    public abstract DaoStoreWorker daoStoreWorker();
}