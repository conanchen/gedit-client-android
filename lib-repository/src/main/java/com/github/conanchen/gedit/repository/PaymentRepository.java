package com.github.conanchen.gedit.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.di.GrpcFascade;
import com.github.conanchen.gedit.grpc.payment.PaymentService;
import com.github.conanchen.gedit.payment.inapp.grpc.GetMyReceiptCodeResponse;
import com.github.conanchen.gedit.payment.inapp.grpc.GetReceiptCodeResponse;
import com.github.conanchen.gedit.payment.inapp.grpc.ReceiptCode;
import com.github.conanchen.gedit.room.RoomFascade;
import com.google.gson.Gson;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hutao on 2018/1/22.
 */

public class PaymentRepository {
    private final static Gson gson = new Gson();
    private RoomFascade roomFascade;
    private GrpcFascade grpcFascade;
    private RepositoryFascade repositoryFascade;

    @Inject
    public PaymentRepository(RoomFascade roomFascade, GrpcFascade grpcFascade) {
        this.roomFascade = roomFascade;
        this.grpcFascade = grpcFascade;
    }

    /**
     * 联网获取生成二维码的字符串
     *
     * @param url
     * @return
     */
    public LiveData<GetMyReceiptCodeResponse> getQRCode(String url) {
        return new LiveData<GetMyReceiptCodeResponse>() {
            @Override
            protected void onActive() {
                grpcFascade.paymentService.getQRCode(url, new PaymentService.GetQRCodeUrlCallback() {
                    @Override
                    public void onGetQRCodeUrlCallback(GetMyReceiptCodeResponse response) {
                        Observable.fromCallable(() -> {

                            if ("OK".compareToIgnoreCase(response.getStatus().getCode()) == 0) {
                                return response;
                            } else {
                                GetMyReceiptCodeResponse getMyReceiptCodeResponse = GetMyReceiptCodeResponse.newBuilder()
                                        .setReceiptCode(ReceiptCode.newBuilder()
                                                .setCode("fail")
                                                .build())
                                        .build();
                                return getMyReceiptCodeResponse;
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<GetMyReceiptCodeResponse>() {
                                    @Override
                                    public void accept(@NonNull GetMyReceiptCodeResponse getMyReceiptCodeResponse) throws Exception {
                                        if (!"fail".equals(getMyReceiptCodeResponse.getReceiptCode().getCode())) {
                                            setValue(GetMyReceiptCodeResponse.newBuilder()
                                                    .setStatus(Status.newBuilder().setCode("OK")
                                                            .setDetails("update Store successfully")
                                                            .build())
                                                    .build());
                                        } else {
                                            setValue(GetMyReceiptCodeResponse.newBuilder()
                                                    .setStatus(Status.newBuilder()
                                                            .setCode(response.getStatus().getCode())
                                                            .setDetails(response.getStatus().getDetails())
                                                            .build())
                                                    .build());
                                        }
                                    }
                                });
                        ;
                    }
                });
            }
        };
    }


    /**
     * 联网获取生成二维码的字符串
     *
     * @param url
     * @return
     */
    public LiveData<GetReceiptCodeResponse> getPaymentStoreDetails(String url) {
        return new LiveData<GetReceiptCodeResponse>() {
            @Override
            protected void onActive() {
                grpcFascade.paymentService.getPaymentStoreDetails(url, new PaymentService.GetPaymentStoreDetailsCallback() {
                    @Override
                    public void onGetPaymentStoreDetailsCallback(GetReceiptCodeResponse response) {
                        Observable.fromCallable(() -> {

                            if ("OK".compareToIgnoreCase(response.getStatus().getCode()) == 0) {
                                return response;
                            } else {
                                GetReceiptCodeResponse getReceiptCodeResponse = GetReceiptCodeResponse.newBuilder()
                                        .setReceiptCode(ReceiptCode.newBuilder()
                                                .setCode("fail")
                                                .build())
                                        .build();
                                return getReceiptCodeResponse;
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<GetReceiptCodeResponse>() {
                                    @Override
                                    public void accept(@NonNull GetReceiptCodeResponse getReceiptCodeResponse) throws Exception {
                                        if (!"fail".equals(getReceiptCodeResponse.getReceiptCode().getCode())) {
                                            setValue(GetReceiptCodeResponse.newBuilder()
                                                    .setStatus(Status.newBuilder().setCode("OK")
                                                            .setDetails("update Store successfully")
                                                            .build())
                                                    .build());
                                        } else {
                                            setValue(GetReceiptCodeResponse.newBuilder()
                                                    .setStatus(Status.newBuilder()
                                                            .setCode(response.getStatus().getCode())
                                                            .setDetails(response.getStatus().getDetails())
                                                            .build())
                                                    .build());
                                        }
                                    }
                                });
                        ;
                    }
                });
            }
        };
    }
}
