package com.github.conanchen.gedit.ui.payment;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.payment.inapp.grpc.GetMyReceiptCodeResponse;
import com.github.conanchen.gedit.repository.StoreRepository;
import com.github.conanchen.gedit.store.profile.grpc.UpdateStoreResponse;
import com.github.conanchen.gedit.util.AbsentLiveData;
import com.github.conanchen.utils.vo.StoreUpdateInfo;
import com.google.common.base.Strings;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/1/22.
 */

public class PayeeQRCodeViewModel extends ViewModel {
    @VisibleForTesting
    final MutableLiveData<String> storeUpdateInfoMutableLiveData = new MutableLiveData<>();
    private final LiveData<GetMyReceiptCodeResponse> storeQRCodeLiveData;

    @SuppressWarnings("unchecked")
    @Inject
    public PayeeQRCodeViewModel(StoreRepository storeRepository) {
        storeQRCodeLiveData = Transformations.switchMap(storeUpdateInfoMutableLiveData, string->{
            if(Strings.isNullOrEmpty(string)){
                return AbsentLiveData.create();
            }else{
                return  storeRepository.getQRCode(string);
            }
        });
    }

    @VisibleForTesting
    public LiveData<GetMyReceiptCodeResponse> getStoreQRCodeLiveData() {
        return storeQRCodeLiveData;
    }

    public void getQRCode(String url) {
        storeUpdateInfoMutableLiveData.setValue(url);
    }
}
