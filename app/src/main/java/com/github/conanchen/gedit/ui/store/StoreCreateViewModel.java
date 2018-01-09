package com.github.conanchen.gedit.ui.store;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.repository.hello.StoreRepository;
import com.github.conanchen.gedit.room.hello.Store;
import com.github.conanchen.gedit.vo.StoreCreateInfo;
import com.github.conanchen.gedit.util.AbsentLiveData;

import javax.inject.Inject;

/**
 * Created by Conan Chen on 2018/1/8.
 */

public class StoreCreateViewModel extends ViewModel {
    @VisibleForTesting
    final MutableLiveData<StoreCreateInfo> storeCreateInfoMutableLiveData = new MutableLiveData<>();
    private final LiveData<Store> store;

    @SuppressWarnings("unchecked")
    @Inject
    public StoreCreateViewModel(StoreRepository storeRepository) {
        store = Transformations.switchMap(storeCreateInfoMutableLiveData, storeCreateInfo -> {
            if (storeCreateInfo == null) {
                return AbsentLiveData.create();
            } else {
                return storeRepository.createStore(storeCreateInfo);
            }
        });

    }


    @VisibleForTesting
    public LiveData<Store> getStore() {
        return store;
    }

    public void createStoreWith(StoreCreateInfo storeCreateInfo) {
        storeCreateInfoMutableLiveData.setValue(storeCreateInfo);
    }
}
