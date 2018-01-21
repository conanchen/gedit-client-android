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
public interface DaoAccount {
    @Insert(onConflict = REPLACE)
    Long save(Account account);

    @Query("SELECT * FROM Account ORDER by lastUpdated DESC ")
    public abstract DataSource.Factory<Integer, Account> listLivePagedMyAccounts();

}
