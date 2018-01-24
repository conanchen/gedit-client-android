package com.github.conanchen.gedit.ui.payment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.payment.common.grpc.PaymentResponse;
import com.github.conanchen.gedit.payment.inapp.grpc.GetPayeeCodeResponse;
import com.github.conanchen.gedit.payment.inapp.grpc.PrepareInappPaymentResponse;
import com.github.conanchen.gedit.repository.PaymentRepository;
import com.github.conanchen.gedit.util.AbsentLiveData;
import com.github.conanchen.utils.vo.PaymentInfo;

import javax.inject.Inject;

/**
 * Created by hutao on 2018/1/22.
 */

public class PointsPayViewModel extends ViewModel {
    @VisibleForTesting
    final MutableLiveData<PaymentInfo> paymentStoreDetailsMutableLiveData = new MutableLiveData<>();
    private final LiveData<GetPayeeCodeResponse> paymentStoreDetailsLiveData;

    @VisibleForTesting
    final MutableLiveData<PaymentInfo> paymentMutableLiveData = new MutableLiveData<>();
    private final LiveData<PrepareInappPaymentResponse> paymentLiveData;

    @VisibleForTesting
    final MutableLiveData<PaymentInfo> createPaymentMutableLiveData = new MutableLiveData<>();
    private final LiveData<PaymentResponse> createPaymentLiveData;

    @SuppressWarnings("unchecked")
    @Inject
    public PointsPayViewModel(PaymentRepository paymentRepository) {
        paymentStoreDetailsLiveData = Transformations.switchMap(paymentStoreDetailsMutableLiveData, paymentInfo -> {
            if (paymentInfo == null) {
                return AbsentLiveData.create();
            } else {
                return paymentRepository.getPayeeStoreDetails(paymentInfo);
            }
        });

        paymentLiveData = Transformations.switchMap(paymentMutableLiveData, paymentInfo -> {
            if (paymentInfo == null) {
                return AbsentLiveData.create();
            } else {
                return paymentRepository.getPayment(paymentInfo);
            }
        });


        createPaymentLiveData = Transformations.switchMap(createPaymentMutableLiveData, paymentInfo -> {
            if (paymentInfo == null) {
                return AbsentLiveData.create();
            } else {
                return paymentRepository.createPayment(paymentInfo);
            }
        });


    }

    @VisibleForTesting
    public LiveData<GetPayeeCodeResponse> getPaymentStoreDetailsLiveData() {
        return paymentStoreDetailsLiveData;
    }

    public void getPaymentStoreDetails(PaymentInfo paymentInfo) {
        paymentStoreDetailsMutableLiveData.setValue(paymentInfo);
    }

    @VisibleForTesting
    public LiveData<PrepareInappPaymentResponse> getPaymentLiveData() {
        return paymentLiveData;
    }

    public void getPayment(PaymentInfo paymentInfo) {
        paymentMutableLiveData.setValue(paymentInfo);
    }


    @VisibleForTesting
    public LiveData<PaymentResponse> getCreatePaymentLiveData() {
        return createPaymentLiveData;
    }

    public void createPayment(PaymentInfo paymentInfo) {
        createPaymentMutableLiveData.setValue(paymentInfo);
    }
}
