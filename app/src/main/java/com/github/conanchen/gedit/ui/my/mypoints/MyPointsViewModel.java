package com.github.conanchen.gedit.ui.my.mypoints;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.repository.MyStoreRepository;
import com.github.conanchen.gedit.room.my.store.MyStore;
import com.github.conanchen.gedit.util.AbsentLiveData;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/1/20.
 */

public class MyPointsViewModel extends ViewModel{
    @VisibleForTesting
    final MutableLiveData<Long> lastUpdatedMutableLiveData = new MutableLiveData<>();
    private final LiveData<PagedList<MyStore>> myStorePagedListLiveData;

    @SuppressWarnings("unchecked")
    @Inject
    public MyPointsViewModel(MyStoreRepository myStoreRepository) {
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
