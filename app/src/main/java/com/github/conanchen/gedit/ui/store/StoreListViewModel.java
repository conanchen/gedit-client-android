package com.github.conanchen.gedit.ui.store;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.common.grpc.Location;
import com.github.conanchen.gedit.repository.StoreProfileRepository;
import com.github.conanchen.gedit.room.store.Store;
import com.github.conanchen.gedit.util.AbsentLiveData;

import javax.inject.Inject;

/**
 * Created by Conan Chen on 2018/1/8.
 */

public class StoreListViewModel extends ViewModel {

    @VisibleForTesting
    final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
    private final LiveData<PagedList<Store>> storePagedListLiveData;

    @SuppressWarnings("unchecked")
    @Inject
    public StoreListViewModel(StoreProfileRepository storeProfileRepository) {
        storePagedListLiveData = Transformations.switchMap(locationMutableLiveData, location -> {
            if (location == null) {
                return AbsentLiveData.create();
            } else {
                return storeProfileRepository.loadStoresNearAt(location);
            }
        });
    }

    @VisibleForTesting
    public void updateLocation(Location location) {
        locationMutableLiveData.setValue(location);
    }

    @VisibleForTesting
    public LiveData<PagedList<Store>> getStorePagedListLiveData() {
        return storePagedListLiveData;
    }


}
