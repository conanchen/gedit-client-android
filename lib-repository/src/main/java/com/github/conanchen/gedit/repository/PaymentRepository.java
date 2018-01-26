package com.github.conanchen.gedit.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.di.GrpcFascade;
import com.github.conanchen.gedit.grpc.payment.PaymentService;
import com.github.conanchen.gedit.payment.common.grpc.Payment;
import com.github.conanchen.gedit.payment.common.grpc.PaymentResponse;
import com.github.conanchen.gedit.payment.inapp.grpc.GetMyPayeeCodeResponse;
import com.github.conanchen.gedit.payment.inapp.grpc.GetPayeeCodeResponse;
import com.github.conanchen.gedit.payment.inapp.grpc.PayeeCode;
import com.github.conanchen.gedit.payment.inapp.grpc.PrepareInappPaymentResponse;
import com.github.conanchen.gedit.room.RoomFascade;
import com.github.conanchen.utils.vo.PaymentInfo;
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
     * @return
     */
    public LiveData<GetMyPayeeCodeResponse> getQRCode(PaymentInfo paymentInfo) {
        return new LiveData<GetMyPayeeCodeResponse>() {
            @Override
            protected void onActive() {
                grpcFascade.paymentService.getQRCode(paymentInfo, new PaymentService.GetQRCodeUrlCallback() {
                    @Override
                    public void onGetQRCodeUrlCallback(GetMyPayeeCodeResponse response) {
                        Observable.fromCallable(() -> {

                            if (Status.Code.OK.getNumber() == response.getStatus().getCode().getNumber()) {
                                return response;
                            } else {
                                GetMyPayeeCodeResponse getMyReceiptCodeResponse = GetMyPayeeCodeResponse.newBuilder()
                                        .setStatus(Status.newBuilder()
                                                .setCode(response.getStatus().getCode())
                                                .setDetails(response.getStatus().getDetails())
                                                .build())
                                        .setPayeeCode(PayeeCode.newBuilder()
                                                .setPayeeCode("fail")
                                                .build())
                                        .build();
                                return getMyReceiptCodeResponse;
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<GetMyPayeeCodeResponse>() {
                                    @Override
                                    public void accept(@NonNull GetMyPayeeCodeResponse getMyPayeeCodeResponse) throws Exception {
                                        if (Status.Code.OK.getNumber() == getMyPayeeCodeResponse.getStatus().getCode().getNumber()) {
                                            setValue(GetMyPayeeCodeResponse.newBuilder()
                                                    .setPayeeCode(getMyPayeeCodeResponse.getPayeeCode())
                                                    .setStatus(Status.newBuilder()
                                                            .setCode(Status.Code.OK)
                                                            .setDetails("get QR code successfully")
                                                            .build())
                                                    .build());
                                        } else {
                                            setValue(GetMyPayeeCodeResponse.newBuilder()
                                                    .setPayeeCode(getMyPayeeCodeResponse.getPayeeCode())
                                                    .setStatus(Status.newBuilder()
                                                            .setCode(getMyPayeeCodeResponse.getStatus().getCode())
                                                            .setDetails(getMyPayeeCodeResponse.getStatus().getDetails())
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
     * 获取商家信息
     *
     * @return
     */
    public LiveData<GetPayeeCodeResponse> getPayeeStoreDetails(PaymentInfo paymentInfo) {
        return new LiveData<GetPayeeCodeResponse>() {
            @Override
            protected void onActive() {
                grpcFascade.paymentService.getPaymentStoreDetails(paymentInfo, new PaymentService.GetPayeeStoreDetailsCallback() {
                    @Override
                    public void onGetPayeeStoreDetailsCallback(GetPayeeCodeResponse response) {
                        Observable.fromCallable(() -> {

                            if (Status.Code.OK.getNumber() == response.getStatus().getCode().getNumber()) {
                                return response;
                            } else {
                                GetPayeeCodeResponse getPayeeCodeResponse = GetPayeeCodeResponse.newBuilder()
                                        .setStatus(Status.newBuilder()
                                                .setCode(response.getStatus().getCode())
                                                .setDetails(response.getStatus().getDetails())
                                                .build())
                                        .setPayeeCode(PayeeCode.newBuilder()
                                                .setPayeeCode("fail")
                                                .build())
                                        .build();
                                return getPayeeCodeResponse;
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<GetPayeeCodeResponse>() {
                                    @Override
                                    public void accept(@NonNull GetPayeeCodeResponse getReceiptCodeResponse) throws Exception {
                                        if (Status.Code.OK.getNumber() == getReceiptCodeResponse.getStatus().getCode().getNumber()) {
                                            setValue(GetPayeeCodeResponse.newBuilder()
                                                    .setPayeeCode(getReceiptCodeResponse.getPayeeCode())
                                                    .setStatus(Status.newBuilder()
                                                            .setCode(Status.Code.OK)
                                                            .setDetails("sao miao successfully")
                                                            .build())
                                                    .build());
                                        } else {
                                            setValue(GetPayeeCodeResponse.newBuilder()
                                                    .setPayeeCode(getReceiptCodeResponse.getPayeeCode())
                                                    .setStatus(Status.newBuilder()
                                                            .setCode(getReceiptCodeResponse.getStatus().getCode())
                                                            .setDetails(getReceiptCodeResponse.getStatus().getDetails())
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
     * 顾客扫码店员/收银员的收款码后获取如果支付一定金额
     *
     * @return
     */
    public LiveData<PrepareInappPaymentResponse> getPayment(PaymentInfo paymentInfo) {
        return new LiveData<PrepareInappPaymentResponse>() {
            @Override
            protected void onActive() {
                grpcFascade.paymentService.getPayment(paymentInfo, new PaymentService.GetPaymentCallback() {
                    @Override
                    public void onGetPaymentCallback(PrepareInappPaymentResponse response) {
                        Observable.fromCallable(() -> {
                            if (Status.Code.OK.getNumber() == response.getStatus().getCode().getNumber()) {
                                return response;
                            } else {
                                PrepareInappPaymentResponse prepareMyPaymentResponse = PrepareInappPaymentResponse.newBuilder()
                                        .setPreparePayment(response.getPreparePayment())
                                        .setStatus(response.getStatus())
                                        .build();
                                return prepareMyPaymentResponse;
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<PrepareInappPaymentResponse>() {
                                    @Override
                                    public void accept(@NonNull PrepareInappPaymentResponse getReceiptCodeResponse) throws Exception {
                                        if (Status.Code.OK.getNumber() == getReceiptCodeResponse.getStatus().getCode().getNumber()) {
                                            setValue(PrepareInappPaymentResponse.newBuilder()
                                                    .setPreparePayment(getReceiptCodeResponse.getPreparePayment())
                                                    .setStatus(Status.newBuilder()
                                                            .setCode(getReceiptCodeResponse.getStatus().getCode())
                                                            .setDetails("prepare successfully")
                                                            .build())
                                                    .build());
                                        } else {
                                            setValue(PrepareInappPaymentResponse.newBuilder()
                                                    .setPreparePayment(getReceiptCodeResponse.getPreparePayment())
                                                    .setStatus(Status.newBuilder()
                                                            .setCode(getReceiptCodeResponse.getStatus().getCode())
                                                            .setDetails(getReceiptCodeResponse.getStatus().getDetails())
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
     * 生成订单号
     *
     * @param paymentInfo
     * @return
     */
    public LiveData<PaymentResponse> createPayment(PaymentInfo paymentInfo) {
        return new LiveData<PaymentResponse>() {
            @Override
            protected void onActive() {
                grpcFascade.paymentService.createPayment(paymentInfo, new PaymentService.CreatePaymentCallback() {
                    @Override
                    public void onCreatePaymentCallback(PaymentResponse response) {

                        Observable.fromCallable(() -> {
                            if (Status.Code.OK.getNumber() == response.getStatus().getCode().getNumber()) {
                                return response;
                            } else {
                                PaymentResponse paymentResponse = PaymentResponse.newBuilder()
                                        .setStatus(response.getStatus())
                                        .setPayment(Payment.newBuilder().build())
                                        .build();
                                return paymentResponse;
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<PaymentResponse>() {
                                    @Override
                                    public void accept(@NonNull PaymentResponse paymentResponse) throws Exception {
                                        if (Status.Code.OK.getNumber() == paymentResponse.getStatus().getCode().getNumber()) {
                                            setValue(PaymentResponse.newBuilder()
                                                    .setStatus(Status.newBuilder()
                                                            .setCode(paymentResponse.getStatus().getCode())
                                                            .setDetails("pay success")
                                                            .build())
                                                    .setPayment(paymentResponse.getPayment())
                                                    .build());
                                        } else {
                                            setValue(PaymentResponse.newBuilder()
                                                    .setStatus(paymentResponse.getStatus())
                                                    .setPayment(paymentResponse.getPayment())
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
