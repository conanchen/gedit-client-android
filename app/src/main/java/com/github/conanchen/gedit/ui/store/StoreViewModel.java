package com.github.conanchen.gedit.ui.store;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.repository.StoreProfileRepository;
import com.github.conanchen.gedit.room.store.Store;
import com.github.conanchen.gedit.util.AbsentLiveData;

import javax.inject.Inject;

/**
 * Created by Conan Chen on 2018/1/8.
 */

public class StoreViewModel extends ViewModel {
    @VisibleForTesting
    final MutableLiveData<String> uuidMutableLiveData = new MutableLiveData<>();
    private final LiveData<Store> storeLiveData;

    @SuppressWarnings("unchecked")
    @Inject
    public StoreViewModel(StoreProfileRepository storeProfileRepository) {
        storeLiveData = Transformations.switchMap(uuidMutableLiveData, uuid -> {
            if (uuid == null) {
                return AbsentLiveData.create();
            } else {
                return storeProfileRepository.findStore(uuid);
            }
        });

    }


    @VisibleForTesting
    public void setUuid(String uuid) {
        uuidMutableLiveData.setValue(uuid);
    }

    @VisibleForTesting
    public LiveData<Store> getStoreLiveData() {
        return storeLiveData;
    }

}
