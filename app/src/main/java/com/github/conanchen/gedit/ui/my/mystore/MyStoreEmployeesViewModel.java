package com.github.conanchen.gedit.ui.my.mystore;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.repository.StoreWorkerRepository;
import com.github.conanchen.gedit.room.store.StoreWorker;
import com.github.conanchen.gedit.store.worker.grpc.WorkshipResponse;
import com.github.conanchen.gedit.util.AbsentLiveData;
import com.github.conanchen.utils.vo.PaymentInfo;
import com.github.conanchen.utils.vo.StoreUpdateInfo;
import com.github.conanchen.utils.vo.VoAccessToken;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/1/17.
 */

public class MyStoreEmployeesViewModel extends ViewModel {
    @VisibleForTesting
    final MutableLiveData<PaymentInfo> locationMutableLiveData = new MutableLiveData<>();
    private final LiveData<PagedList<StoreWorker>> liveStores;

    @VisibleForTesting
    final MutableLiveData<PaymentInfo> addWorkerMutableLiveData = new MutableLiveData<>();
    private final LiveData<WorkshipResponse> addWorkerLivaData;

    @SuppressWarnings("unchecked")
    @Inject
    public MyStoreEmployeesViewModel(StoreWorkerRepository storeWorkerRepository) {
        liveStores = Transformations.switchMap(locationMutableLiveData, paymentInfo -> {
            if (paymentInfo == null) {
                return AbsentLiveData.create();
            } else {
                return storeWorkerRepository.loadAllEmployees(paymentInfo);
            }
        });

        addWorkerLivaData = Transformations.switchMap(addWorkerMutableLiveData, paymentInfo -> {
            if (paymentInfo == null) {
                return AbsentLiveData.create();
            } else {
                return storeWorkerRepository.addWorker(paymentInfo);
            }
        });
    }

    @VisibleForTesting
    public void getAllEmployees(PaymentInfo paymentInfo) {
        locationMutableLiveData.setValue(paymentInfo);
    }

    @VisibleForTesting
    public LiveData<PagedList<StoreWorker>> getLiveStores() {
        return liveStores;
    }

    @VisibleForTesting
    public void addWorker(PaymentInfo paymentInfo) {
        addWorkerMutableLiveData.setValue(paymentInfo);
    }

    @VisibleForTesting
    public LiveData<WorkshipResponse> getAddWorkerLiveData() {
        return addWorkerLivaData;
    }
}
