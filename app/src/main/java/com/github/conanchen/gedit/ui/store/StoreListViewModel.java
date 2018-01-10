package com.github.conanchen.gedit.ui.store;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.repository.hello.HelloRepository;
import com.github.conanchen.gedit.repository.hello.StoreRepository;
import com.github.conanchen.gedit.room.hello.Store;
import com.github.conanchen.gedit.util.AbsentLiveData;
import com.github.conanchen.gedit.vo.Location;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Conan Chen on 2018/1/8.
 */

public class StoreListViewModel extends ViewModel {

    @VisibleForTesting
    final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
    private final LiveData<List<Store>> liveStores;

    @SuppressWarnings("unchecked")
    @Inject
    public StoreListViewModel(StoreRepository storeRepository) {
        liveStores = Transformations.switchMap(locationMutableLiveData, location -> {
            if (location == null) {
                return AbsentLiveData.create();
            } else {
                return storeRepository.loadStoresNearAt(location);
            }
        });

    }

    @VisibleForTesting
    public void updateLocation(Location location) {
        locationMutableLiveData.setValue(location);
    }

    @VisibleForTesting
    public LiveData<List<Store>> getLiveStores() {
        return liveStores;
    }


}
