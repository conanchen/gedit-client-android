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
 * Created by hutao on 2018/1/17.
 */
@Dao
public interface DaoMyStore {
    @Insert(onConflict = REPLACE)
    Long save(MyStore store);

    @Insert(onConflict = REPLACE)
    Long[] saveAll(MyStore... stores);

    @Delete
    void delete(MyStore store);

    @Query("SELECT * FROM MyStore WHERE storeUuid = :storeUuid LIMIT 1")
    LiveData<MyStore> findLive(String storeUuid);

    @Query("SELECT * FROM MyStore WHERE storeUuid = :storeUuid LIMIT 1")
    Maybe<MyStore> findMaybe(String storeUuid);

    @Query("SELECT * FROM MyStore WHERE storeUuid = :storeUuid LIMIT 1")
    MyStore findOne(String storeUuid);


    @Query("SELECT * FROM MyStore ORDER by lastUpdated DESC ")
    public abstract DataSource.Factory<Integer, MyStore> listLivePagedMyStore();

}
