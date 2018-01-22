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
public interface DaoPosting {
    @Insert(onConflict = REPLACE)
    Long save(Posting posting);

    @Insert(onConflict = REPLACE)
    Long[] saveAll(Posting... postings);

    @Delete
    void delete(Posting posting);

    @Query("SELECT * FROM Posting WHERE accountUuid = :accountUuid LIMIT 1")
    LiveData<Posting> findLive(String accountUuid);

    @Query("SELECT * FROM Posting WHERE accountUuid = :accountUuid LIMIT 1")
    Maybe<Posting> findMaybe(String accountUuid);

    @Query("SELECT * FROM Posting WHERE accountUuid = :accountUuid LIMIT 1")
    Posting findOne(String accountUuid);


    @Query("SELECT * FROM Posting WHERE accountUuid = :accountUuid AND :start < created AND created < :end ORDER by created DESC ")
    public abstract DataSource.Factory<Integer, Posting> listLivePagedMyPostingByDate(String accountUuid, Long start,Long end);

    @Query("SELECT * FROM Posting WHERE accountUuid = :accountUuid ORDER by created DESC ")
    public abstract DataSource.Factory<Integer, Posting> listLivePagedMyPosting(String accountUuid);

}
