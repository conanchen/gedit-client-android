package com.github.conanchen.gedit.ui.my.mystore;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.repository.MyStoreRepository;
import com.github.conanchen.gedit.room.my.store.MyStore;
import com.github.conanchen.gedit.util.AbsentLiveData;
import com.github.conanchen.utils.vo.StoreCreateInfo;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/1/17.
 */

public class MyStoresViewModel extends ViewModel {

    @VisibleForTesting
    final MutableLiveData<StoreCreateInfo> lastUpdatedMutableLiveData = new MutableLiveData<>();
    private final LiveData<PagedList<MyStore>> myStorePagedListLiveData;

    @SuppressWarnings("unchecked")
    @Inject
    public MyStoresViewModel(MyStoreRepository myStoreRepository) {
        myStorePagedListLiveData = Transformations.switchMap(lastUpdatedMutableLiveData, storeCreateInfo -> {
            if (storeCreateInfo == null) {
                return AbsentLiveData.create();
            } else {
                return myStoreRepository.loadMyStores(storeCreateInfo);
            }
        });
    }

    @VisibleForTesting
    public void loadMyStores(StoreCreateInfo storeCreateInfo) {
        lastUpdatedMutableLiveData.setValue(storeCreateInfo);
    }

    @VisibleForTesting
    public LiveData<PagedList<MyStore>> getMyStorePagedListLiveData() {
        return myStorePagedListLiveData;
    }
}
