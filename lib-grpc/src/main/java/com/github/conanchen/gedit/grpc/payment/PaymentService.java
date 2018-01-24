package com.github.conanchen.gedit.grpc.payment;

import android.util.Log;

import com.github.conanchen.gedit.common.grpc.PaymentChannel;
import com.github.conanchen.gedit.common.grpc.Status;
import com.github.conanchen.gedit.grpc.store.MyIntroducedStoreService;
import com.github.conanchen.gedit.hello.grpc.BuildConfig;
import com.github.conanchen.gedit.payment.common.grpc.PaymentResponse;
import com.github.conanchen.gedit.payment.inapp.grpc.CreateInappPaymentRequest;
import com.github.conanchen.gedit.payment.inapp.grpc.GetMyPayeeCodeRequest;
import com.github.conanchen.gedit.payment.inapp.grpc.GetMyPayeeCodeResponse;
import com.github.conanchen.gedit.payment.inapp.grpc.GetPayeeCodeRequest;
import com.github.conanchen.gedit.payment.inapp.grpc.GetPayeeCodeResponse;
import com.github.conanchen.gedit.payment.inapp.grpc.PaymentInappApiGrpc;
import com.github.conanchen.gedit.payment.inapp.grpc.PrepareInappPaymentRequest;
import com.github.conanchen.gedit.payment.inapp.grpc.PrepareInappPaymentResponse;
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
        void onGetQRCodeUrlCallback(GetMyPayeeCodeResponse response);
    }

    public interface GetPayeeStoreDetailsCallback {
        void onGetPayeeStoreDetailsCallback(GetPayeeCodeResponse response);
    }

    public interface GetPaymentCallback {
        void onGetPaymentCallback(PrepareInappPaymentResponse response);
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
     * getMyPayeeCode
     *
     * @param url
     * @param callback
     */
    public void getQRCode(String url, GetQRCodeUrlCallback callback) {

        ManagedChannel channel = getManagedChannel();
        PaymentInappApiGrpc.PaymentInappApiStub paymentInappApiStub = PaymentInappApiGrpc.newStub(channel);
        paymentInappApiStub
                .withDeadlineAfter(60, TimeUnit.SECONDS)
                .getMyPayeeCode(GetMyPayeeCodeRequest.newBuilder()
                        .setPayeeStoreUuid(url)
                        .build(), new StreamObserver<GetMyPayeeCodeResponse>() {
                    @Override
                    public void onNext(GetMyPayeeCodeResponse value) {
                        Log.i("-=-=-=-=--", "进了onNext()方法" + gson.toJson(value));
                        callback.onGetQRCodeUrlCallback(value);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i("-=-=-=-=--", "onError()方法" + t.getMessage());
                        callback.onGetQRCodeUrlCallback(GetMyPayeeCodeResponse.newBuilder()
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
     * getPayeeCode
     *
     * @param request
     * @param callback
     */
    public void getPaymentStoreDetails(GetPayeeCodeRequest request, GetPayeeStoreDetailsCallback callback) {

        ManagedChannel channel = getManagedChannel();
        PaymentInappApiGrpc.PaymentInappApiStub paymentInappApiStub = PaymentInappApiGrpc.newStub(channel);
        paymentInappApiStub
                .withDeadlineAfter(60, TimeUnit.SECONDS)
                .getPayeeCode(request, new StreamObserver<GetPayeeCodeResponse>() {
                    @Override
                    public void onNext(GetPayeeCodeResponse value) {
                        Log.i("-=-=-=-=--", "进了onNext()方法" + gson.toJson(value));
                        callback.onGetPayeeStoreDetailsCallback(value);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i("-=-=-=-=--", "onError()方法" + t.getMessage());
                        callback.onGetPayeeStoreDetailsCallback(GetPayeeCodeResponse.newBuilder()
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
     * prepare
     *
     * @param url
     * @param callback
     */
    public void getPayment(String url, GetPaymentCallback callback) {

        ManagedChannel channel = getManagedChannel();
        PaymentInappApiGrpc.PaymentInappApiStub paymentInappApiStub = PaymentInappApiGrpc.newStub(channel);
        paymentInappApiStub
                .withDeadlineAfter(60, TimeUnit.SECONDS)
                .prepare(PrepareInappPaymentRequest.newBuilder()
                        .setPayeePayeeCode("ceshi")
                        .build(), new StreamObserver<PrepareInappPaymentResponse>() {
                    @Override
                    public void onNext(PrepareInappPaymentResponse value) {
                        Log.i("-=-=-=-=--", "进了onNext()方法" + gson.toJson(value));
                        callback.onGetPaymentCallback(value);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i("-=-=-=-=--", "onError()方法" + t.getMessage());
                        callback.onGetPaymentCallback(PrepareInappPaymentResponse.newBuilder()
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

        CreateInappPaymentRequest createPaymentRequest = null;
        if ("1".equals(paymentInfo.payType)) {
            createPaymentRequest = CreateInappPaymentRequest.newBuilder()
                    .setActualPay(paymentInfo.actualPay)
                    .setChannel(PaymentChannel.ALIPAY)
                    .setPointsPay(paymentInfo.pointsPay)
                    .setShouldPay(paymentInfo.shouldPay)
                    .setPayeePayeeCode(paymentInfo.payeeReceiptCode)
                    .setPayerIp(paymentInfo.payerIp)
                    .build();
        } else if ("2".equals(paymentInfo.payType)) {
            createPaymentRequest = CreateInappPaymentRequest.newBuilder()
                    .setActualPay(paymentInfo.actualPay)
                    .setChannel(PaymentChannel.WECHAT)
                    .setPointsPay(paymentInfo.pointsPay)
                    .setShouldPay(paymentInfo.shouldPay)
                    .setPayeePayeeCode(paymentInfo.payeeReceiptCode)
                    .setPayerIp(paymentInfo.payerIp)
                    .build();
        }

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
