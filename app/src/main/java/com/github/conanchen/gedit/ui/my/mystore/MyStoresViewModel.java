package com.github.conanchen.gedit.ui.my.mystore;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.repository.MyStoreRepository;
import com.github.conanchen.gedit.room.store.MyStore;
import com.github.conanchen.gedit.util.AbsentLiveData;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/1/17.
 */

public class MyStoresViewModel extends ViewModel {

    @VisibleForTesting
    final MutableLiveData<Long> lastUpdatedMutableLiveData = new MutableLiveData<>();
    private final LiveData<PagedList<MyStore>> myStorePagedListLiveData;

    @SuppressWarnings("unchecked")
    @Inject
    public MyStoresViewModel(MyStoreRepository myStoreRepository) {
        myStorePagedListLiveData = Transformations.switchMap(lastUpdatedMutableLiveData, times -> {
            if (times == null) {
                return AbsentLiveData.create();
            } else {
                return myStoreRepository.loadMyStores(times);
            }
        });
    }

    @VisibleForTesting
    public void loadMyStores(Long times) {
        lastUpdatedMutableLiveData.setValue(times);
    }

    @VisibleForTesting
    public LiveData<PagedList<MyStore>> getMyStorePagedListLiveData() {
        return myStorePagedListLiveData;
    }
}
