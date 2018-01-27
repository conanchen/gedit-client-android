package com.github.conanchen.gedit.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.repository.MyWorkinStoreRepository;
import com.github.conanchen.gedit.store.worker.grpc.WorkshipResponse;
import com.github.conanchen.gedit.util.AbsentLiveData;
import com.github.conanchen.utils.vo.VoAccessToken;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/1/27.
 */

public class MainViewModel extends ViewModel {
    @VisibleForTesting
    final MutableLiveData<VoAccessToken> workingStoreMutableLiveData = new MutableLiveData<>();
    private final LiveData<WorkshipResponse> workingStoreLiveData;

    @SuppressWarnings("unchecked")
    @Inject
    public MainViewModel(MyWorkinStoreRepository myWorkinStoreRepository) {
        workingStoreLiveData = Transformations.switchMap(workingStoreMutableLiveData, voAccessToken -> {
            if (voAccessToken == null) {
                return AbsentLiveData.create();
            } else {
                return myWorkinStoreRepository.getMyCurrentWorkinStore(voAccessToken);
            }
        });
    }

    @VisibleForTesting
    public LiveData<WorkshipResponse> getMyWorkingStoreLiveData() {
        return workingStoreLiveData;
    }

    public void getMyCurrentWorkinStore(VoAccessToken voAccessToken) {
        workingStoreMutableLiveData.setValue(voAccessToken);
    }

}
