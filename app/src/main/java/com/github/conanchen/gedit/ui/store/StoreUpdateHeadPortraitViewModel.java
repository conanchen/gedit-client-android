package com.github.conanchen.gedit.ui.store;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.repository.StoreProfileRepository;
import com.github.conanchen.gedit.store.profile.grpc.UpdateStoreResponse;
import com.github.conanchen.utils.vo.StoreUpdateInfo;
import com.github.conanchen.gedit.util.AbsentLiveData;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/1/11.
 */

public class StoreUpdateHeadPortraitViewModel extends ViewModel {
    @VisibleForTesting
    final MutableLiveData<StoreUpdateInfo> storeUpdateInfoMutableLiveData = new MutableLiveData<>();
    private final LiveData<UpdateStoreResponse> updateResponseLiveData;

    @SuppressWarnings("unchecked")
    @Inject
    public StoreUpdateHeadPortraitViewModel(StoreProfileRepository storeProfileRepository) {
        updateResponseLiveData = Transformations.switchMap(storeUpdateInfoMutableLiveData, storeUpdateInfo -> {
            if (storeUpdateInfo == null) {
                return AbsentLiveData.create();
            } else {
                return storeProfileRepository.updateHeadPortrait(storeUpdateInfo);
            }
        });
    }

    @VisibleForTesting
    public LiveData<UpdateStoreResponse> getStoreUpdateResponseLiveData() {
        return updateResponseLiveData;
    }


    public void updateHeadPortrait(StoreUpdateInfo storeUpdateInfo) {
        storeUpdateInfoMutableLiveData.setValue(storeUpdateInfo);
    }

}
