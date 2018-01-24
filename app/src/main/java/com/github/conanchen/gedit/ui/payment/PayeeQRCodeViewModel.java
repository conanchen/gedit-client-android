package com.github.conanchen.gedit.ui.payment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.payment.inapp.grpc.GetMyPayeeCodeResponse;
import com.github.conanchen.gedit.repository.PaymentRepository;
import com.github.conanchen.gedit.util.AbsentLiveData;
import com.github.conanchen.utils.vo.PaymentInfo;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/1/22.
 */

public class PayeeQRCodeViewModel extends ViewModel {
    @VisibleForTesting
    final MutableLiveData<PaymentInfo> paymentMutableLiveData = new MutableLiveData<>();
    private final LiveData<GetMyPayeeCodeResponse> paymentQRCodeLiveData;

    @SuppressWarnings("unchecked")
    @Inject
    public PayeeQRCodeViewModel(PaymentRepository paymentRepository) {
        paymentQRCodeLiveData = Transformations.switchMap(paymentMutableLiveData, paymentInfo -> {
            if (paymentInfo == null) {
                return AbsentLiveData.create();
            } else {
                return paymentRepository.getQRCode(paymentInfo);
            }
        });
    }

    @VisibleForTesting
    public LiveData<GetMyPayeeCodeResponse> getStoreQRCodeLiveData() {
        return paymentQRCodeLiveData;
    }

    public void getQRCode(PaymentInfo paymentInfo) {
        paymentMutableLiveData.setValue(paymentInfo);
    }
}
