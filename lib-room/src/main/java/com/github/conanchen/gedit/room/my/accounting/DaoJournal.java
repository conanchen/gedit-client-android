package com.github.conanchen.gedit.room.my.accounting;

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
public interface DaoJournal {
    @Insert(onConflict = REPLACE)
    Long save(Balance store);

    @Insert(onConflict = REPLACE)
    Long[] saveAll(Balance... stores);

    @Delete
    void delete(Balance store);

    @Query("SELECT * FROM Balance WHERE accountUuid = :accountUuid LIMIT 1")
    LiveData<Balance> findLive(String accountUuid);

    @Query("SELECT * FROM Balance WHERE accountUuid = :accountUuid LIMIT 1")
    Maybe<Balance> findMaybe(String accountUuid);

    @Query("SELECT * FROM Balance WHERE accountUuid = :accountUuid LIMIT 1")
    Balance findOne(String accountUuid);


    @Query("SELECT * FROM Balance ORDER by lastUpdated DESC ")
    public abstract DataSource.Factory<Integer, Balance> listLivePagedMyStore();

}
