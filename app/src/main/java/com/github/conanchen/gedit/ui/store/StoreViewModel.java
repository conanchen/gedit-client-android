package com.github.conanchen.gedit.ui.store;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.repository.StoreRepository;
import com.github.conanchen.gedit.room.store.Store;
import com.github.conanchen.gedit.util.AbsentLiveData;

import javax.inject.Inject;

/**
 * Created by Conan Chen on 2018/1/8.
 */

public class StoreViewModel extends ViewModel {
    @VisibleForTesting
    final MutableLiveData<String> uuidMutableLiveData = new MutableLiveData<>();
    private final LiveData<Store> store;

    @SuppressWarnings("unchecked")
    @Inject
    public StoreViewModel(StoreRepository storeRepository) {
        store = Transformations.switchMap(uuidMutableLiveData, uuid -> {
            if (uuid == null) {
                return AbsentLiveData.create();
            } else {
                return storeRepository.findStore(uuid);
            }
        });

    }


    @VisibleForTesting
    public void setUuid(String uuid) {
        uuidMutableLiveData.setValue(uuid);
    }

    @VisibleForTesting
    public LiveData<Store> getStore() {
        return store;
    }

}
