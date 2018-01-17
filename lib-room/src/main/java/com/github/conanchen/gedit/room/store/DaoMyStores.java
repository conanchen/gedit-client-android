package com.github.conanchen.gedit.room.store;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by hutao on 2018/1/17.
 */
@Dao
public interface DaoMyStores {
    @Insert(onConflict = REPLACE)
    Long save(MyStores store);

    @Insert(onConflict = REPLACE)
    Long[] saveAll(MyStores... stores);

    @Delete
    void delete(MyStores store);

    @Query("SELECT * FROM MyStores WHERE storeUuid = :storeUuid LIMIT 1")
    LiveData<MyStores> findLive(String storeUuid);

    @Query("SELECT * FROM MyStores WHERE storeUuid = :storeUuid LIMIT 1")
    Maybe<MyStores> findMaybe(String storeUuid);

    @Query("SELECT * FROM MyStores WHERE storeUuid = :storeUuid LIMIT 1")
    MyStores findOne(String storeUuid);


    @Query("SELECT * FROM MyStores ORDER by lastUpdated DESC ")
    public abstract DataSource.Factory<Integer, MyStores> listLivePagedMyStore();

}
