package com.github.conanchen.gedit.ui.payment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.payment.inapp.grpc.GetMyPayeeCodeResponse;
import com.github.conanchen.gedit.repository.PaymentRepository;
import com.github.conanchen.gedit.util.AbsentLiveData;
import com.google.common.base.Strings;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/1/22.
 */

public class PayeeQRCodeViewModel extends ViewModel {
    @VisibleForTesting
    final MutableLiveData<String> paymentMutableLiveData = new MutableLiveData<>();
    private final LiveData<GetMyPayeeCodeResponse> paymentQRCodeLiveData;

    @SuppressWarnings("unchecked")
    @Inject
    public PayeeQRCodeViewModel(PaymentRepository paymentRepository) {
        paymentQRCodeLiveData = Transformations.switchMap(paymentMutableLiveData, string -> {
            if (Strings.isNullOrEmpty(string)) {
                return AbsentLiveData.create();
            } else {
                return paymentRepository.getQRCode(string);
            }
        });
    }

    @VisibleForTesting
    public LiveData<GetMyPayeeCodeResponse> getStoreQRCodeLiveData() {
        return paymentQRCodeLiveData;
    }

    public void getQRCode(String url) {
        paymentMutableLiveData.setValue(url);
    }
}
