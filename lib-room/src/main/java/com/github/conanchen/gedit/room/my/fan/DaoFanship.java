package com.github.conanchen.gedit.room.my.fan;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.github.conanchen.gedit.room.my.fan.Fanship;

import io.reactivex.Maybe;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Conan Chen on 2018/1/17.
 */
@Dao
public interface DaoFanship {
    @Insert(onConflict = REPLACE)
    Long save(Fanship fan);

    @Delete
    void delete(Fanship fan);

    @Query("SELECT * FROM Fanship ORDER by lastUpdated DESC ")
    public abstract DataSource.Factory<Integer, Fanship> listLivePagedFanship();

}
