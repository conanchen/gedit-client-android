package com.github.conanchen.gedit.ui.store;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.repository.hello.StoreRepository;
import com.github.conanchen.gedit.room.hello.Store;
import com.github.conanchen.gedit.util.AbsentLiveData;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/1/8.
 */

public class StoreViewModel extends ViewModel {
    @VisibleForTesting
    final MutableLiveData<Long> storeTime = new MutableLiveData<>();
    private final LiveData<List<Store>> store;
    private StoreRepository storeRepository;

    @SuppressWarnings("unchecked")
    @Inject
    public StoreViewModel(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
        store = Transformations.switchMap(storeTime, time -> {
            if (time == null) {
                return AbsentLiveData.create();
            } else {
                return storeRepository.createTime(time);
            }
        });

    }


    @VisibleForTesting
    public void setStoreInfo(Store store) {
        storeRepository.storeCreate(store);
    }

    @VisibleForTesting
    public LiveData<List<Store>> getStore() {
        return store;
    }

    @VisibleForTesting
    public void reloadHellos() {
        this.storeTime.setValue(System.currentTimeMillis());
    }

}
