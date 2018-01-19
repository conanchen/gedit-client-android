package com.github.conanchen.gedit.room.my.payment;

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
public interface DaoPayment {
    @Insert(onConflict = REPLACE)
    Long save(Payment payment);

    @Insert(onConflict = REPLACE)
    Long[] saveAll(Payment... payments);

    @Delete
    void delete(Payment payment);

    @Query("SELECT * FROM Payment WHERE uuid = :uuid LIMIT 1")
    LiveData<Payment> findLive(String uuid);

    @Query("SELECT * FROM Payment WHERE uuid = :uuid LIMIT 1")
    Maybe<Payment> findMaybe(String uuid);

    @Query("SELECT * FROM Payment WHERE uuid = :uuid LIMIT 1")
    Payment findOne(String uuid);


    @Query("SELECT * FROM Payment WHERE payerUuid = :payerUuid ORDER by created DESC ")
    public abstract DataSource.Factory<Integer, Payment> listLivePagedMyPayment(String payerUuid);

    @Query("SELECT * FROM Payment WHERE payeeStoreUuid = :payeeStoreUuid ORDER by created DESC ")
    public abstract DataSource.Factory<Integer, Payment> listLivePagedStoreReceipt(String payeeStoreUuid);

}
