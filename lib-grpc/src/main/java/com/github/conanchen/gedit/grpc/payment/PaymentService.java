package com.github.conanchen.gedit.grpc.payment;

import android.util.Log;

import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.grpc.store.MyIntroducedStoreService;
import com.github.conanchen.gedit.hello.grpc.BuildConfig;
import com.github.conanchen.gedit.payment.inapp.grpc.GetMyReceiptCodeRequest;
import com.github.conanchen.gedit.payment.inapp.grpc.GetMyReceiptCodeResponse;
import com.github.conanchen.gedit.payment.inapp.grpc.GetReceiptCodeRequest;
import com.github.conanchen.gedit.payment.inapp.grpc.GetReceiptCodeResponse;
import com.github.conanchen.gedit.payment.inapp.grpc.PaymentInappApiGrpc;
import com.github.conanchen.gedit.payment.inapp.grpc.PreparMyPaymentRequest;
import com.github.conanchen.gedit.payment.inapp.grpc.PrepareMyPaymentResponse;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import io.grpc.ManagedChannel;
import io.grpc.okhttp.OkHttpChannelBuilder;
import io.grpc.stub.StreamObserver;

/**
 * Created by hutao on 2018/1/22.
 */

public class PaymentService {
    private final static String TAG = MyIntroducedStoreService.class.getSimpleName();
    private Gson gson = new Gson();

    public interface GetQRCodeUrlCallback {
        void onGetQRCodeUrlCallback(GetMyReceiptCodeResponse response);
    }

    public interface GetPaymentStoreDetailsCallback {
        void onGetPaymentStoreDetailsCallback(GetReceiptCodeResponse response);
    }

    public interface GetPaymentCallback {
        void onGetPaymentCallback(PrepareMyPaymentResponse response);
    }

    private ManagedChannel getManagedChannel() {
        return OkHttpChannelBuilder
                .forAddress(BuildConfig.GRPC_SERVER_HOST, BuildConfig.GRPC_SERVER_PORT_PAYMENT)
                .usePlaintext(true)
                //                .keepAliveTime(60, TimeUnit.SECONDS)
                .build();
    }

    /**
     * 获取二维码的字符串
     *
     * @param url
     * @param callback
     */
    public void getQRCode(String url, GetQRCodeUrlCallback callback) {

        ManagedChannel channel = getManagedChannel();
        PaymentInappApiGrpc.PaymentInappApiStub paymentInappApiStub = PaymentInappApiGrpc.newStub(channel);
        paymentInappApiStub
                .withDeadlineAfter(60, TimeUnit.SECONDS)
                .getMyReceiptCode(GetMyReceiptCodeRequest.newBuilder()
                        .setPayeeStoreUuid(url)
                        .build(), new StreamObserver<GetMyReceiptCodeResponse>() {
                    @Override
                    public void onNext(GetMyReceiptCodeResponse value) {
                        Log.i("-=-=-=-=--", "进了onNext()方法" + gson.toJson(value));
                        callback.onGetQRCodeUrlCallback(value);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i("-=-=-=-=--", "onError()方法" + gson.toJson(t));
                        callback.onGetQRCodeUrlCallback(GetMyReceiptCodeResponse.newBuilder()
                                .setStatus(Status.newBuilder()
                                        .setCode("Fail")
                                        .setDetails("enter onError() method"))
                                .build());
                    }

                    @Override
                    public void onCompleted() {

                    }
                });
    }


    /**
     * 获取二维码的字符串
     *
     * @param url
     * @param callback
     */
    public void getPaymentStoreDetails(String url, GetPaymentStoreDetailsCallback callback) {

        ManagedChannel channel = getManagedChannel();
        PaymentInappApiGrpc.PaymentInappApiStub paymentInappApiStub = PaymentInappApiGrpc.newStub(channel);
        paymentInappApiStub
                .withDeadlineAfter(60, TimeUnit.SECONDS)
                .getReceiptCode(GetReceiptCodeRequest.newBuilder()
                        .setCode(url)
                        .build(), new StreamObserver<GetReceiptCodeResponse>() {
                    @Override
                    public void onNext(GetReceiptCodeResponse value) {
                        Log.i("-=-=-=-=--", "进了onNext()方法" + gson.toJson(value));
                        callback.onGetPaymentStoreDetailsCallback(value);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i("-=-=-=-=--", "onError()方法" + gson.toJson(t));
                        callback.onGetPaymentStoreDetailsCallback(GetReceiptCodeResponse.newBuilder()
                                .setStatus(Status.newBuilder()
                                        .setCode("Fail")
                                        .setDetails("enter onError() method"))
                                .build());
                    }

                    @Override
                    public void onCompleted() {

                    }
                });
    }

    /**
     * 获取二维码的字符串
     *
     * @param url
     * @param callback
     */
    public void getPayment(String url, GetPaymentCallback callback) {

        ManagedChannel channel = getManagedChannel();
        PaymentInappApiGrpc.PaymentInappApiStub paymentInappApiStub = PaymentInappApiGrpc.newStub(channel);
        paymentInappApiStub
                .withDeadlineAfter(60, TimeUnit.SECONDS)
                .prepareMyPayment(PreparMyPaymentRequest.newBuilder()
                        .setPayeeReceiptCode("ceshi")
                        .build(), new StreamObserver<PrepareMyPaymentResponse>() {
                    @Override
                    public void onNext(PrepareMyPaymentResponse value) {
                        Log.i("-=-=-=-=--", "进了onNext()方法" + gson.toJson(value));
                        callback.onGetPaymentCallback(value);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i("-=-=-=-=--", "onError()方法" + gson.toJson(t));
                        callback.onGetPaymentCallback(PrepareMyPaymentResponse.newBuilder()
                                .setStatus(Status.newBuilder()
                                        .setCode("Fail")
                                        .setDetails("enter onError() method"))
                                .build());
                    }

                    @Override
                    public void onCompleted() {

                    }
                });
    }


}
