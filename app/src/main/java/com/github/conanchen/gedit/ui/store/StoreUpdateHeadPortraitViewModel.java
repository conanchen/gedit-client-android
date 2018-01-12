package com.github.conanchen.gedit.ui.store;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.hello.grpc.store.StoreUpdateInfo;
import com.github.conanchen.gedit.repository.hello.StoreRepository;
import com.github.conanchen.gedit.util.AbsentLiveData;
import com.github.conanchen.gedit.vo.StoreUpdateResponse;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/1/11.
 */

public class StoreUpdateHeadPortraitViewModel extends ViewModel {
    @VisibleForTesting
    final MutableLiveData<StoreUpdateInfo> storeUpdateInfoMutableLiveData = new MutableLiveData<>();
    private final LiveData<StoreUpdateResponse> updateResponseLiveData;

    @SuppressWarnings("unchecked")
    @Inject
    public StoreUpdateHeadPortraitViewModel(StoreRepository storeRepository) {
        updateResponseLiveData = Transformations.switchMap(storeUpdateInfoMutableLiveData, storeUpdateInfo -> {
            if (storeUpdateInfo == null) {
                return AbsentLiveData.create();
            } else {
                return storeRepository.updateHeadPortrait(storeUpdateInfo);
            }
        });
    }

    @VisibleForTesting
    public LiveData<StoreUpdateResponse> getStoreUpdateResponseLiveData() {
        return updateResponseLiveData;
    }


    public void updateHeadPortrait(StoreUpdateInfo storeUpdateInfo) {
        storeUpdateInfoMutableLiveData.setValue(storeUpdateInfo);
    }

}
