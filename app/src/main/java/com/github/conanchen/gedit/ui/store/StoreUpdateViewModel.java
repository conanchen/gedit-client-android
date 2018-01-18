package com.github.conanchen.gedit.ui.store;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.utils.vo.StoreUpdateInfo;
import com.github.conanchen.gedit.repository.StoreRepository;
import com.github.conanchen.gedit.util.AbsentLiveData;
import com.github.conanchen.gedit.vo.StoreUpdateResponse;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/1/10.
 */

public class StoreUpdateViewModel extends ViewModel {
    @VisibleForTesting
    final MutableLiveData<StoreUpdateInfo> storeUpdateInfoMutableLiveData = new MutableLiveData<>();
    private final LiveData<StoreUpdateResponse> storeUpdateResponseLiveData;

    @SuppressWarnings("unchecked")
    @Inject
    public StoreUpdateViewModel(StoreRepository storeRepository) {
        storeUpdateResponseLiveData = Transformations.switchMap(storeUpdateInfoMutableLiveData, storeUpdateInfo -> {
            if (storeUpdateInfo == null) {
                return AbsentLiveData.create();
            } else {
                return storeRepository.updateStore(storeUpdateInfo);
            }
        });


    }


    @VisibleForTesting
    public LiveData<StoreUpdateResponse> getStoreUpdateResponseLiveData() {
        return storeUpdateResponseLiveData;
    }

    public void updateStoreWith(StoreUpdateInfo storeUpdateInfo) {
        storeUpdateInfoMutableLiveData.setValue(storeUpdateInfo);
    }


}
