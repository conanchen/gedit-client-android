package com.github.conanchen.gedit.ui.payment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.github.conanchen.gedit.payment.common.grpc.PaymentResponse;
import com.github.conanchen.gedit.payment.inapp.grpc.GetReceiptCodeResponse;
import com.github.conanchen.gedit.repository.PaymentRepository;
import com.github.conanchen.gedit.util.AbsentLiveData;
import com.google.common.base.Strings;

import javax.inject.Inject;

/**
 * Created by hutao on 2018/1/22.
 */

public class PointsPayViewModel extends ViewModel {
    @VisibleForTesting
    final MutableLiveData<String> paymentStoreDetailsMutableLiveData = new MutableLiveData<>();
    private final LiveData<GetReceiptCodeResponse> paymentStoreDetailsLiveData;

//    @VisibleForTesting
//    final MutableLiveData<String> paymentMutableLiveData = new MutableLiveData<>();
//    private final LiveData<PaymentResponse> paymentLiveData;

    @SuppressWarnings("unchecked")
    @Inject
    public PointsPayViewModel(PaymentRepository paymentRepository) {
        paymentStoreDetailsLiveData = Transformations.switchMap(paymentStoreDetailsMutableLiveData, string -> {
            if (Strings.isNullOrEmpty(string)) {
                return AbsentLiveData.create();
            } else {
                return paymentRepository.getPaymentStoreDetails(string);
            }
        });

//        paymentLiveData = Transformations.switchMap(paymentMutableLiveData, string -> {
//            if (Strings.isNullOrEmpty(string)) {
//                return AbsentLiveData.create();
//            } else {
//                return paymentRepository.getPayment(string);
//            }
//        });


    }

    @VisibleForTesting
    public LiveData<GetReceiptCodeResponse> getPaymentStoreDetailsLiveData() {
        return paymentStoreDetailsLiveData;
    }

    public void getPaymentStoreDetails(String url) {
        paymentStoreDetailsMutableLiveData.setValue(url);
    }

//    @VisibleForTesting
//    public LiveData<PaymentResponse> getPaymentLiveData() {
//        return paymentLiveData;
//    }
//
//    public void getPayment(String url) {
//        paymentMutableLiveData.setValue(url);
//    }
}
