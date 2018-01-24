package com.github.conanchen.gedit.grpc.payment;

import android.util.Log;

import com.github.conanchen.gedit.common.grpc.PaymentChannel;
import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.grpc.store.MyIntroducedStoreService;
import com.github.conanchen.gedit.hello.grpc.BuildConfig;
import com.github.conanchen.gedit.payment.common.grpc.PaymentResponse;
import com.github.conanchen.gedit.payment.inapp.grpc.CreatePaymentRequest;
import com.github.conanchen.gedit.payment.inapp.grpc.GetMyReceiptCodeRequest;
import com.github.conanchen.gedit.payment.inapp.grpc.GetMyReceiptCodeResponse;
import com.github.conanchen.gedit.payment.inapp.grpc.GetReceiptCodeRequest;
import com.github.conanchen.gedit.payment.inapp.grpc.GetReceiptCodeResponse;
import com.github.conanchen.gedit.payment.inapp.grpc.PaymentInappApiGrpc;
import com.github.conanchen.gedit.payment.inapp.grpc.PreparMyPaymentRequest;
import com.github.conanchen.gedit.payment.inapp.grpc.PrepareMyPaymentResponse;
import com.github.conanchen.gedit.utils.JcaUtils;
import com.github.conanchen.utils.vo.PaymentInfo;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import io.grpc.CallCredentials;
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

    public interface GetPayeeStoreDetailsCallback {
        void onGetPayeeStoreDetailsCallback(GetReceiptCodeResponse response);
    }

    public interface GetPaymentCallback {
        void onGetPaymentCallback(PrepareMyPaymentResponse response);
    }


    public interface CreatePaymentCallback {
        void onCreatePaymentCallback(PaymentResponse response);
    }

    private ManagedChannel getManagedChannel() {
        return OkHttpChannelBuilder
                .forAddress(BuildConfig.GRPC_SERVER_HOST, BuildConfig.GRPC_SERVER_PORT_PAYMENT)
                .usePlaintext(true)
                //                .keepAliveTime(60, TimeUnit.SECONDS)
                .build();
    }

    /**
     * getMyReceiptCode
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
                        Log.i("-=-=-=-=--", "onError()方法" + t.getMessage());
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
     * getReceiptCode
     *
     * @param request
     * @param callback
     */
    public void getPaymentStoreDetails(GetReceiptCodeRequest request, GetPayeeStoreDetailsCallback callback) {

        ManagedChannel channel = getManagedChannel();
        PaymentInappApiGrpc.PaymentInappApiStub paymentInappApiStub = PaymentInappApiGrpc.newStub(channel);
        paymentInappApiStub
                .withDeadlineAfter(60, TimeUnit.SECONDS)
                .getReceiptCode(request, new StreamObserver<GetReceiptCodeResponse>() {
                    @Override
                    public void onNext(GetReceiptCodeResponse value) {
                        Log.i("-=-=-=-=--", "进了onNext()方法" + gson.toJson(value));
                        callback.onGetPayeeStoreDetailsCallback(value);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i("-=-=-=-=--", "onError()方法" + t.getMessage());
                        callback.onGetPayeeStoreDetailsCallback(GetReceiptCodeResponse.newBuilder()
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
     * prepareMyPayment
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
                        Log.i("-=-=-=-=--", "onError()方法" + t.getMessage());
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

    /**
     * create
     *
     * @param callback
     */
    public void getCreatePayment(PaymentInfo paymentInfo, CreatePaymentCallback callback) {

        CallCredentials callCredentials = JcaUtils
                .getCallCredentials(paymentInfo.voAccessToken.accessToken,
                        Long.valueOf(paymentInfo.voAccessToken.expiresIn));

        CreatePaymentRequest createPaymentRequest = null;
        if ("1".equals(paymentInfo.payType)) {
            createPaymentRequest = CreatePaymentRequest.newBuilder()
                    .setActualPay(paymentInfo.actualPay)
                    .setChannel(PaymentChannel.ALIPAY)
                    .setPointsPay(paymentInfo.pointsPay)
                    .setShouldPay(paymentInfo.shouldPay)
                    .setPayeeReceiptCode(paymentInfo.payeeReceiptCode)
                    .setPayerIp(paymentInfo.payerIp)
                    .build();
        } else if ("2".equals(paymentInfo.payType)) {
            createPaymentRequest = CreatePaymentRequest.newBuilder()
                    .setActualPay(paymentInfo.actualPay)
                    .setChannel(PaymentChannel.WECHAT)
                    .setPointsPay(paymentInfo.pointsPay)
                    .setShouldPay(paymentInfo.shouldPay)
                    .setPayeeReceiptCode(paymentInfo.payeeReceiptCode)
                    .setPayerIp(paymentInfo.payerIp)
                    .build();
        }

        Log.i("-=-=-=-=-", "Token:" + paymentInfo.voAccessToken.accessToken + "----------expiresIn:" + paymentInfo.voAccessToken.expiresIn);


        ManagedChannel channel = getManagedChannel();
        PaymentInappApiGrpc.PaymentInappApiStub paymentInappApiStub = PaymentInappApiGrpc.newStub(channel);
        paymentInappApiStub
                .withDeadlineAfter(60, TimeUnit.SECONDS)
                .withCallCredentials(callCredentials)
                .create(createPaymentRequest, new StreamObserver<PaymentResponse>() {
                    @Override
                    public void onNext(PaymentResponse value) {
                        Log.i("-=-=-=-=--", "进了onNext()方法" + gson.toJson(value));
                        callback.onCreatePaymentCallback(value);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i("-=-=-=-=--", "onError()方法" + gson.toJson(t));
                        callback.onCreatePaymentCallback(PaymentResponse.newBuilder()
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
