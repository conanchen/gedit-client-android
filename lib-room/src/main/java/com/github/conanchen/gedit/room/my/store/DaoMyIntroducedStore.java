package com.github.conanchen.gedit.room.my.store;

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
public interface DaoMyIntroducedStore {
    @Insert(onConflict = REPLACE)
    Long save(MyIntroducedStore store);

    @Insert(onConflict = REPLACE)
    Long[] saveAll(MyIntroducedStore... stores);

    @Delete
    void delete(MyIntroducedStore store);

    @Query("SELECT * FROM MyIntroducedStore WHERE storeUuid = :storeUuid LIMIT 1")
    LiveData<MyIntroducedStore> findLive(String storeUuid);

    @Query("SELECT * FROM MyIntroducedStore WHERE storeUuid = :storeUuid LIMIT 1")
    Maybe<MyIntroducedStore> findMaybe(String storeUuid);

    @Query("SELECT * FROM MyIntroducedStore WHERE storeUuid = :storeUuid LIMIT 1")
    MyIntroducedStore findOne(String storeUuid);


    @Query("SELECT * FROM MyIntroducedStore ORDER by lastUpdated DESC ")
    public abstract DataSource.Factory<Integer, MyIntroducedStore> listLivePagedMyStore();

}
