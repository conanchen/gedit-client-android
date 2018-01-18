package com.github.conanchen.gedit.room.store;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import io.reactivex.Maybe;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Conan Chen on 2018/1/17.
 */
@Dao
public interface DaoMyWorkinStore {
    @Insert(onConflict = REPLACE)
    Long save(MyWorkinStore store);

    @Insert(onConflict = REPLACE)
    Long[] saveAll(MyWorkinStore... stores);

    @Delete
    void delete(MyWorkinStore store);

    @Query("SELECT * FROM MyWorkinStore WHERE storeUuid = :storeUuid LIMIT 1")
    LiveData<MyWorkinStore> findLive(String storeUuid);

    @Query("SELECT * FROM MyWorkinStore WHERE storeUuid = :storeUuid LIMIT 1")
    Maybe<MyWorkinStore> findMaybe(String storeUuid);

    @Query("SELECT * FROM MyWorkinStore WHERE storeUuid = :storeUuid LIMIT 1")
    MyWorkinStore findOne(String storeUuid);


    @Query("SELECT * FROM MyWorkinStore ORDER by lastUpdated DESC ")
    public abstract DataSource.Factory<Integer, MyWorkinStore> listLivePagedMyStore();

}
