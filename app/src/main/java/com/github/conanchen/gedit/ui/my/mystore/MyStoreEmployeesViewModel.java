package com.github.conanchen.gedit.ui.my.mystore;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.common.grpc.Location;
import com.github.conanchen.gedit.repository.StoreProfileRepository;
import com.github.conanchen.gedit.repository.StoreWorkerRepository;
import com.github.conanchen.gedit.room.my.store.MyStore;
import com.github.conanchen.gedit.room.store.Store;
import com.github.conanchen.gedit.room.store.StoreWorker;
import com.github.conanchen.gedit.store.worker.grpc.WorkshipResponse;
import com.github.conanchen.gedit.util.AbsentLiveData;
import com.github.conanchen.utils.vo.VoAccessToken;
import com.google.common.base.Strings;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/1/17.
 */

public class MyStoreEmployeesViewModel extends ViewModel{
    @VisibleForTesting
    final MutableLiveData<VoAccessToken> locationMutableLiveData = new MutableLiveData<>();
    private final LiveData<PagedList<StoreWorker>> liveStores;

    @VisibleForTesting
    final MutableLiveData<String> addWorkerMutableLiveData = new MutableLiveData<>();
    private final LiveData<WorkshipResponse> addWorkerLivaData;

    @SuppressWarnings("unchecked")
    @Inject
    public MyStoreEmployeesViewModel(StoreWorkerRepository storeWorkerRepository) {
        liveStores = Transformations.switchMap(locationMutableLiveData, voAccessToken -> {
            if (voAccessToken == null) {
                return AbsentLiveData.create();
            } else {
                return storeWorkerRepository.loadAllEmployees(voAccessToken);
            }
        });

        addWorkerLivaData = Transformations.switchMap(addWorkerMutableLiveData, string -> {
            if (Strings.isNullOrEmpty(string)) {
                return AbsentLiveData.create();
            } else {
                return storeWorkerRepository.addWorker(string);
            }
        });
    }

    @VisibleForTesting
    public void getAllEmployees(VoAccessToken voAccessToken) {
        locationMutableLiveData.setValue(voAccessToken);
    }

    @VisibleForTesting
    public LiveData<PagedList<StoreWorker>> getLiveStores() {
        return liveStores;
    }

    @VisibleForTesting
    public void addWorker(String workerUuid) {
        addWorkerMutableLiveData.setValue(workerUuid);
    }

    @VisibleForTesting
    public LiveData<WorkshipResponse> getAddWorkerLiveData() {
        return addWorkerLivaData;
    }
}
