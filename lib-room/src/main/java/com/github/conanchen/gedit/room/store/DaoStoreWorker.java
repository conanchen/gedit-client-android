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
 * Created by Conan Chen on 2018/1/8.
 */
@Dao
public interface DaoStoreWorker {
    @Insert(onConflict = REPLACE)
    Long save(StoreWorker storeWorker);

    @Insert(onConflict = REPLACE)
    Long[] saveAll(StoreWorker... storeWorker);

    @Delete
    void delete(StoreWorker storeWorker);

    @Query("SELECT * FROM StoreWorker WHERE uuid = :uuid LIMIT 1")
    LiveData<StoreWorker> findLive(String uuid);

    @Query("SELECT * FROM StoreWorker WHERE uuid = :uuid LIMIT 1")
    Maybe<StoreWorker> findMaybe(String uuid);

    @Query("SELECT * FROM StoreWorker WHERE uuid = :uuid LIMIT 1")
    StoreWorker findOne(String uuid);

    @Query("SELECT * FROM StoreWorker ORDER by lastUpdated DESC ")
    public abstract DataSource.Factory<Integer, StoreWorker> listLivePagedStore();
}
