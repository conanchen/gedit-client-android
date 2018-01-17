package com.github.conanchen.gedit.ui.my.mystore;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.repository.hello.StoreRepository;
import com.github.conanchen.gedit.room.store.MyStores;
import com.github.conanchen.gedit.room.store.Store;
import com.github.conanchen.gedit.store.owner.grpc.OwnershipResponse;
import com.github.conanchen.gedit.util.AbsentLiveData;
import com.github.conanchen.gedit.vo.Location;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/1/17.
 */

public class MyStoresViewModel extends ViewModel {

    @VisibleForTesting
    final MutableLiveData<Long> locationMutableLiveData = new MutableLiveData<>();
    private final LiveData<PagedList<MyStores>> liveStores;

    @SuppressWarnings("unchecked")
    @Inject
    public MyStoresViewModel(StoreRepository storeRepository) {
        liveStores = Transformations.switchMap(locationMutableLiveData, times -> {
            if (times == null) {
                return AbsentLiveData.create();
            } else {
                return storeRepository.loadMyStores(times);
            }
        });
    }

    @VisibleForTesting
    public void loadMyStores(Long times) {
        locationMutableLiveData.setValue(times);
    }

    @VisibleForTesting
    public LiveData<PagedList<MyStores>> getLiveStores() {
        return liveStores;
    }
}
