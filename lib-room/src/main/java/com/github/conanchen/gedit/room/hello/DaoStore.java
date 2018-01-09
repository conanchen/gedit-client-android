package com.github.conanchen.gedit.room.hello;

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
 * Created by Administrator on 2018/1/8.
 */
@Dao
public interface DaoStore {
    @Insert(onConflict = REPLACE)
    long save(Store store);

    @Insert(onConflict = REPLACE)
    Long[] saveAll(Store... stores);

    @Delete
    void delete(Store store);

    @Query("SELECT * FROM Store WHERE uuid = :uuid LIMIT 1")
    LiveData<Store> findLive(String uuid);

    @Query("SELECT * FROM Store WHERE uuid = :uuid LIMIT 1")
    Maybe<Store> findMaybe(String uuid);

    @Query("SELECT * FROM Store WHERE uuid = :uuid LIMIT 1")
    Store findOne(String uuid);

    @Query("SELECT * FROM Store ORDER by address DESC ")
    public abstract DataSource.Factory<Integer, Store> listLivePagedStores();

    @Query("SELECT * FROM Store ORDER BY address DESC LIMIT 1")
    Maybe<Store> findMaybeLatestStore();

    @Query("SELECT * FROM Store ORDER by address DESC LIMIT :size")
    Flowable<List<Store>> getFlowableStores(Integer size);

    @Query("SELECT * FROM Store ORDER by address DESC LIMIT :size")
    LiveData<List<Store>> getLiveStores(Integer size);
}
