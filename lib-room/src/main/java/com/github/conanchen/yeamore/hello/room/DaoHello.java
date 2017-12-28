package com.github.conanchen.yeamore.hello.room;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.LivePagedListProvider;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface DaoHello {

    @Insert(onConflict = REPLACE)
    Long save(Hello hello);

    @Insert(onConflict = REPLACE)
    Long[] saveAll(Hello... hellos);

    @Delete
    void delete(Hello hello);

    @Query("SELECT * FROM Hello WHERE id = :id LIMIT 1")
    LiveData<Hello> findLive(int id);

    @Query("SELECT * FROM Hello WHERE id = :id LIMIT 1")
    Maybe<Hello> findMaybe(int id);

    @Query("SELECT * FROM Hello WHERE id = :id LIMIT 1")
    Hello findOne(int id);

    @Query("SELECT * FROM Hello ORDER by lastUpdated DESC ")
    public abstract DataSource.Factory<Integer, Hello> listLivePagedHellos();

    @Query("SELECT * FROM Hello ORDER BY lastUpdated DESC LIMIT 1")
    Maybe<Hello> findMaybeLatestHello();

    @Query("SELECT * FROM Hello ORDER by lastUpdated DESC LIMIT :size")
    Flowable<List<Hello>> getFlowableHellos(Integer size);

    @Query("SELECT * FROM Hello ORDER by lastUpdated DESC LIMIT :size")
    LiveData<List<Hello>> getLiveHellos(Integer size);
}